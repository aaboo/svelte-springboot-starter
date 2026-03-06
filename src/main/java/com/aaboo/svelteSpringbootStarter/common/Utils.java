package com.aaboo.svelteSpringbootStarter.common;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class Utils {
	
	protected static final Logger logger = LoggerFactory.getLogger(Utils.class);

	private static String keyForToken;
	private static String keyForAES256;
	private static String maxCookieAge;

	public static AES256 aes256; 
	private static Environment environment;
	
	//application.properties 정보 가져오기
	@Autowired private void setEnvironment(Environment environment) {
		Utils.environment = environment;
	}   
	
	//static의 properties값 가져오기는 아래와 같이 
	@Value("${key.token}") private void setKeyForToken(String keyForToken){ Utils.keyForToken = keyForToken; }
	@Value("${key.aes256}") private void setKeyForAES256(String keyForAES256){
		//System.out.println("Utils.getCookie.keyForAES256 = "+ keyForAES256);
		Utils.keyForAES256 = keyForAES256;
		aes256 = new AES256(keyForAES256);	
	}
	@Value("${cookie.maxAge}") private void setMaxCookieAge(String maxCookieAge){ Utils.maxCookieAge = maxCookieAge; } 
	
    //application.properties 정보 가져오기
    public static String property(String propertyKey) {
        String result = environment.getProperty(propertyKey);
        if(result==null) result = "";
        return result;
    }
    public static String property(String propertyKey, Object defaultValue) {
        String result = environment.getProperty(propertyKey);
        if(result==null) result = String.valueOf(defaultValue);
        return result;
    }

	/**
	 * @Description Client에서 올라온 Data 값과 세션 정보 가져오기 
	 *   - request의 파라메터 정보를 HashMap 형태로 가져옴
	 *   - 2Depth의 value가 오브젝트형일 경우 스트링을 오브젝트로 파싱처리
	 * @param request
	 * @return HashMap<String,String>
	 *  2019.01.29 aaboo
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getRequestData(HttpServletRequest request){
		Gson gson = new Gson();
		//파리미터 만들기
		HashMap<String,Object> params = new HashMap<String,Object>();
		Enumeration<?> requestParams = request.getParameterNames();//Data    = gson.toJson(request);
		while(requestParams.hasMoreElements()){
			String body = (String) requestParams.nextElement();
			//logger.info("getRequestData body: {}", body);
			params = gson.fromJson(body, HashMap.class);

		}

		try {
			//세션객체
			Cookie cookie = SessionUtils.getCookie(request);
			if(cookie!=null) {
				String encCookie = cookie.getValue().substring(8);
				String decCookie = aes256.decode(encCookie);
				//logger.info(decCookie);
				params.put("session", gson.fromJson(decCookie, HashMap.class));
			}
			//logger.info("param:"+gson.toJson(params));
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return params;
	}
	
	//사용자 로컬IP구하기
	public static String getUserIP(HttpServletRequest request) throws UnknownHostException{
		String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
        	ip = request.getHeader("X-RealIP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
        	ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        if(ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
        	InetAddress address = InetAddress.getLocalHost();
        	ip = address.getHostAddress();
        }

        return ip;
	}
	
	//6자리 랜덤숫자 생성(SMS인증번호)
	public static String getGenerateCertNumber(){
		int certNumLength = 6; //자리수
		Random random = new Random(System.currentTimeMillis());
		
    	int range = (int)Math.pow(10,  certNumLength);
    	int trim = (int)Math.pow(10,  certNumLength-1);
    	int result = random.nextInt(range)+trim;
    	
    	if(result>range){
    		result = result - trim;
    	}
    	
    	return String.valueOf(result);
	}
	
    //Hash
    public static String sha256(String input){
        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            toReturn = String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static String sha512(String input){
        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
	
    //쿠키 추가, 갱신
	public static void setCookie(HttpServletResponse res, String key, String value, Boolean httpOnly) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(60*Integer.parseInt(maxCookieAge)); //60분
		cookie.setPath("/");
		cookie.setHttpOnly(httpOnly);
		res.addCookie(cookie);
	}
	
    //쿠키 추가, 갱신 + 사용시간
	public static void setCookie(HttpServletResponse res, String key, String value, Boolean httpOnly, int liveLong) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(liveLong);
		cookie.setPath("/");
		cookie.setHttpOnly(httpOnly);
		res.addCookie(cookie);
	}
	
    //쿠키에 특정 Key, Value 삭제
	public static void deleteCookie(HttpServletRequest req, HttpServletResponse res, String key) {
		Cookie cookie = Utils.getCookie(req, key);
		if(cookie!=null) {
			cookie.setMaxAge(0); //0일
			cookie.setHttpOnly(true);
			cookie.setPath("/");	
			res.addCookie(cookie);
		}
	}
	
	//쿠키에서 특정 key값 읽기
	public static Cookie getCookie(HttpServletRequest req, String cookieName){
		Cookie[] cookies = req.getCookies();
		if(cookies!=null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(cookieName)) return cookie;				
			}
		}
		return null;
	}
	
	//토큰 생성
	public static String getTokn(String target){
		if(target==null) return "";
		return sha256(target+keyForToken+target);
	}
	
    //조회 자료 건수 제한
    public static String pageLimitCheckMsg(String mode, int totalCount) {        
        String message = "";
        String pageLimit = "0";
        if (mode.equalsIgnoreCase("EXCEL")) { // 엑셀
            pageLimit = property("excel.limit", 100);
            message = pageLimit + "건 초과 자료는 본사로 자료를 요청해 주세요.(총 " + totalCount + "건)";
        }else{//페이징 및 출력
            pageLimit = property("page.limit", 100);
            message = pageLimit + "건 초과 자료는 엑셀을 다운받아 사용해 주세요.(총 " + totalCount + "건)";
        }
        return message;
    }	
	/*
	* @Description: 자연수를 한글/중국어/위조방지 문자로 변환
	* @Param : Number(변환할숫자), String(언어선택:korean, chinese, bank, money)
	* @Return : String
	* @Add mention: converNum2StrMaker와 함께 쓰임
	*/
	public static String convertNum2Str(String _num, String _type){
		Gson gson = new Gson();
		if(_num==null || _num.equals("")) return "";
		_type = (_type == null) ? "korean" : _type;
		String num_ = _num.replaceAll("^0-9.-", "");
		String[] num = num_.split("\\.");
		String num1 = num[0];
		Boolean isMinus = num1.indexOf("-")==0;
		num1 = num1.replaceAll("^0-9", "");
		Boolean isFloat = num.length==2;
		String num2 = isFloat?num[1]:"";
		
		String result = (isMinus?"-": "")
				+ converNum2StrMaker(num1, _type, false)
				+ (isFloat?"."+ converNum2StrMaker(num2, _type, true) : "");
		return result;
	}
	private static String converNum2StrMaker(String _num, String _type, Boolean _isFloat){
		String result = "";
		String[] arrLv1 = null;
		String[] arrLv2 = null;
		String[] arrLv3 = null;
		
		//한글
		if(_type.equalsIgnoreCase("KOREAN")){
			arrLv1 = new String[]{"","일","이","삼","사","오","육","칠","팔","구"};
			arrLv2 = new String[]{"","십","백","천"};
			arrLv3 = new String[]{"","만","억","조","경","해","시","양","구","간","정","재","극","항하사","아승지","나유타","불가사의","무량대수"};
		}
		//한자
		else if(_type.equalsIgnoreCase("CHINESE")){
			arrLv1 = new String[]{"","一","二","三","四","五","六","七","八","九"};
			arrLv2 = new String[]{"","十","百","千"};
			arrLv3 = new String[]{"","萬","億","兆","京","垓","示","穰","溝","澗","正","載","極","恒河沙","阿僧祗","那由他","不可思議","無量大數"};
		}
		//위조방지
		else if(_type.equalsIgnoreCase("BANK")){
			arrLv1 = new String[]{"","壹","貳","蔘","肆","伍","陸","漆","捌","玖"};
			arrLv2 = new String[]{"","拾","伯","阡"};
			arrLv3 = new String[]{"","萬","億","兆","京","垓","示","穰","溝","澗","正","載","極","恒河沙","阿僧祗","那由他","不可思議","無量大數"};
		}

		int len = _num.length();
		if(_type.equalsIgnoreCase("MONEY")){
			for(int i=len; i>=1; i--){
				//큰단위(만억조경해...)를 구분짓는 것 4의 배수자리마다
				if((len-i) >= 3 && ((len-i)%3) == 0){    
					result = "," +result;
				}
				result = _num.substring(i-1,i) + result; 
			}
		}else{
			for(int i=len; i>=1; i--){
				if(!_isFloat){
					//큰단위(만억조경해...)를 구분짓는 것 4의 배수자리마다
					if((len-i) >= 4 && ((len-i)%4) == 0){    
						result = arrLv3[(len-i)/4] + " " +result;
					}  
					//작은단위(일십백천)을 구분짓는 것 4단어 주기
					if(!_num.substring((i-1),i).equals("0")){
						result = (len==1) ? "" : arrLv2[(len-i)%4] + result;  
					}
				}
				//통상 일 자리수 숫자를 제외한 작은단위 앞의 1은 생략
				//(처음 시작하는 1은 제외)  
				if((len-i) > 0){ 
					if(!_num.substring((i-1),i).equals("1") ||  i==1){
						result = arrLv1[Integer.parseInt(_num.substring(i-1,i))] + result; 
					}  
				}else{
					result = arrLv1[Integer.parseInt(_num.substring(i-1,i))] + result; 
				}
			}
		}
		return result;
	}

}