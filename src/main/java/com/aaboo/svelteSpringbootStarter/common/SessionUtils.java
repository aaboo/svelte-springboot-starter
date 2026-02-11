package com.aaboo.svelteSpringbootStarter.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aaboo.svelteSpringbootStarter.auth.AuthMapper;
import com.google.gson.Gson;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class SessionUtils {

	protected static final Logger logger = LoggerFactory.getLogger(SessionUtils.class);

	protected static final Gson gson = new Gson();
	private static AuthMapper authMapper;
	
	private static String serviceName;
	private static int maxCookieAge;
	private static int maxSessionAge;

	//static의 properties값 가져오기는 아래와 같이 
	@Autowired private void setAuthMapper(AuthMapper authMapper) { SessionUtils.authMapper = authMapper; }
	@Value("${server.name}") private void setServiceName(String serviceName){ SessionUtils.serviceName = serviceName; }
	@Value("${cookie.maxAge}") private void setMaxCookieAge(int maxCookieAge){ SessionUtils.maxCookieAge = maxCookieAge; } 
	@Value("${session.maxAge}") private void setMaxSessionAge(int maxSessionAge){ SessionUtils.maxSessionAge = maxSessionAge; }
	
	protected static final String SavedCookieID = serviceName +"_savedID";

	//쿠키 유효성 검사
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public static Boolean check(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		Boolean result = true;
		
		//request
		Map<String,Object> param = Utils.getRequestData(req);
		
		//쿠키 복호화
		Cookie cookie = Utils.getCookie(req, serviceName);
		if(cookie!=null) {
			String sysCookie = cookie.getValue();		
			//logger.info("cookie check = {}", sysCookie);		
			if(sysCookie==null) {
				logger.info("--01");
				SessionUtils.logout(req,  res);
				return false;
			}
			Map<String,String> sysCookieObj = gson.fromJson(Utils.aes256.decode(sysCookie), HashMap.class);//쿠키 복호화
			
			//사용자정보
			String userid = sysCookieObj.get("userid").toString();
			String grade = sysCookieObj.get("grade").toString();
			String auth = sysCookieObj.get("auth").toString();
			
			//req
			String userip = Utils.getUserIP(req);
			String useragent = req.getHeader("User-Agent");
			String sessionid = req.getSession().getId();
			
			//userid
			Cookie cookieSavedID = Utils.getCookie(req, SavedCookieID);
			String savedID = cookieSavedID!=null?cookieSavedID.getValue():"";
			Cookie useridTokn = Utils.getCookie(req, serviceName+"_"+Utils.aes256.encode(userid));
			String useridToknTest = Utils.getTokn(userid + userip + grade + auth);
			
			//로그인때 발급받은 Key
			String loginAuthKey = authMapper.selectLoginAuthKey(param);
			

			//아이디_토큰(${server.name}_4T09R...)
			String useridToknCookieName = serviceName+"_"+Utils.aes256.encode(userid).replaceAll("[^a-zA-Z]", "");
			Cookie useridToknCookie = Utils.getCookie(req, useridToknCookieName);
		

			if(useridToknCookie==null) {
				logger.info("--02");
				SessionUtils.logout(req,  res);
				return false;
			}else {			
				String useridToknCookieValue = useridToknCookie.getValue();
				String useridToknCookieTest = Utils.getTokn(userid + userip + grade + auth);
				if(!useridToknCookieValue.equals(useridToknCookieTest)) {
					logger.info("--03");
					SessionUtils.logout(req,  res);
					return false;
				}				
			}
			
			//기타 여러가지		
			if(!userid.equals(savedID)) {
				logger.info("--11");
				//result = false;
			}
			else if(useridTokn!=null && !useridTokn.getValue().equals(useridToknTest)) {
				logger.info("--12 = {}", !useridTokn.equals(useridToknTest));
				logger.info("--useridTokn:{} / useridToknTest:{}", useridTokn, useridToknTest);
				result = false;
			}
			else if(!sysCookieObj.get("userip").equals(userip)) {
				logger.info("--13");
				result = false;
			}
			else if(!sysCookieObj.get("useragent").equals(useragent)) {
				logger.info("--14");
				logger.info("--{} / {}", sysCookieObj.get("useragent"), useragent);
				result = false;
			}
			else if(!sysCookieObj.get("sessionid").equals(sessionid)) {
				logger.info("--15");
				result = false;
			}
			else if(loginAuthKey==null) {
				logger.info("--17");
				logger.info("--USER_LOGIN_INFO.loginAuthKey = {}", loginAuthKey);
				result = false;
			}
			else if(!sysCookieObj.get("loginAuthKey").toString().equals(loginAuthKey)) {
				logger.info("--18");
				logger.info("--{} / {}", sysCookieObj.get("loginAuthKey"), loginAuthKey);
				result = false;	
			}
		}
		
		if(!result) SessionUtils.removeAllCookies(req, res);//실패시 모든 쿠키 삭제
		
		return result;
	}
	
	
	//쿠키정보 암호화
	public static String getAppCookieEnc(HttpServletRequest req, JtMap userDbInfo) throws Exception {
		
		if(userDbInfo==null) return null;
		
		//정보준비
		String userid = userDbInfo.get("userid").toString();
		String username = userDbInfo.get("username").toString();
		String grade = userDbInfo.get("grade").toString();
		String auth = userDbInfo.get("auth").toString();
		String userip = Utils.getUserIP(req);
		String useragent = req.getHeader("User-Agent");
		String sessionid = req.getSession().getId();
		String loginAuthkey = getLoginAuthkey(userid, grade, auth, userip, sessionid, useragent);
		
		//정보정리 및 처리
		HashMap<String,Object> ck = new HashMap<String,Object>();
		ck.put("userid", userid);
		ck.put("username", username);
		ck.put("grade", grade);
		ck.put("auth", auth);
		ck.put("userip", userip);
		ck.put("useragent", useragent);
		ck.put("sessionid", sessionid);
		ck.put("loginAuthKey", loginAuthkey);
		String ck_stringify = gson.toJson(ck);
		String ck_enc = Utils.aes256.encode(ck_stringify);
		
		//정보확인
		logger.info("cookie.encode = {}", ck_enc);
		logger.info("cookie.decode = {}", Utils.aes256.decode(ck_enc));
		
		return ck_enc;
		
	}
	//쿠키정보 복호화 
	@SuppressWarnings("unchecked")
	public static Map<String,String> getAppCookieDec(HttpServletRequest req) throws Exception {
		Cookie cookie = SessionUtils.getCookie(req);
		if(cookie==null) return null;
		String sysCookie = cookie.getValue();
		if(sysCookie==null) return null;
		return gson.fromJson(Utils.aes256.decode(sysCookie), HashMap.class);
	}
	//쿠키정보 중에서 userid 찾기 
	public static String getUserid(HttpServletRequest req) throws Exception {
		String userid = null;
		try {
			Map<String,String> vop = SessionUtils.getAppCookieDec(req);
			userid = vop.get("userid");
		}catch(Exception e) {
			//nothing happen
		}
		return userid;
	}
	
	//USER_LOGIN_INFO.LOGINAUTHKEY 만들기
	public static String getLoginAuthkey(String userid, String grade, String auth, String userip, String sessionid, String useragent) {		
		return Utils.sha256(
			userid
			+ grade
			+ auth
			+ userip
			+ sessionid
			+ useragent
		).substring(0,50).toUpperCase();
	}
	
	//로그인 세션 제한시간 구하기
	public static String getSessionLimit() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, maxCookieAge);
		String maxAge =sdf.format(cal.getTime());		
		return maxAge;
	}
	
	//쿠키정보 쓰기
	public static void setCookie(HttpServletResponse res, String value) {
		Cookie cookie = new Cookie(serviceName, value);
		cookie.setMaxAge(60*maxCookieAge); //60분
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		//cookie.setSecure(true);//https에서만 쿠키 전송(hosts 접속시 작동되지 않았다.)		
		res.addCookie(cookie);
	}

	//쿠키정보 읽기
	public static Cookie getCookie(HttpServletRequest req){
		//System.out.println("SessionUtils.serviceName = "+ serviceName);
		Cookie[] cookies = req.getCookies();
		if(cookies!=null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(serviceName)) return cookie;				
			}
		}
		return null;
	}
	
	//쿠키 제한시간 연장
	public static void resetCookie(HttpServletRequest req, HttpServletResponse res) {		
		Cookie[] cookies = req.getCookies();
		if(cookies!=null) {
			for(Cookie cookie : cookies) {
				String key = cookie.getName();
				if(key.equals(serviceName) || key.startsWith(serviceName+"_")) {
					//logger.info(key);
					cookie.setPath("/");
					if(key.equals(serviceName+"_isSaveID") || key.equals(serviceName+"_savedID")) {
						cookie.setMaxAge(60*60*24*maxSessionAge); //30일
					}else {						
						cookie.setHttpOnly(true);
						//cookie.setSecure(true); //안씀
						cookie.setMaxAge(60*maxCookieAge); //60분
					}
					res.addCookie(cookie);
				}
			}
		}
	}

	//쿠키 삭제(_savedID, _isSavedID 제외)
	public static void removeAllCookies(HttpServletRequest req, HttpServletResponse res) throws Exception{		
		Cookie[] cookies = req.getCookies();
		if(cookies!=null) {
			for(Cookie cookie : cookies) {
				String key = cookie.getName();
				if(key.equals(serviceName) || key.startsWith(serviceName+"_")) {
					if(!key.equals(serviceName+"_savedID") && !key.equals(serviceName+"_isSaveID")) {
						Cookie sysCookie = new Cookie(key, null);
						sysCookie.setMaxAge(0); //0일
						sysCookie.setHttpOnly(true);
						sysCookie.setPath("/");
						//sysCookie.setSecure(true);//https에서만 쿠키 전송(hosts 접속시 작동되지 않았다.)	
						res.addCookie(sysCookie);
					}
				}
			}
		}
	}
	
	//강제 로그아웃
	public static void logout(HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		SessionUtils.removeAllCookies(req, res);
		
		res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		res.setHeader("Prama", "no-cache");
		
		res.sendRedirect("/login");
	}
}
