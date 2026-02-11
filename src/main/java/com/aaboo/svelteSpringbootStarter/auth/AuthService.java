package com.aaboo.svelteSpringbootStarter.auth;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aaboo.svelteSpringbootStarter.common.JtMap;
import com.aaboo.svelteSpringbootStarter.common.Response;
import com.aaboo.svelteSpringbootStarter.common.ResponseCode;
import com.aaboo.svelteSpringbootStarter.common.SessionUtils;
import com.aaboo.svelteSpringbootStarter.common.Utils;
import com.google.gson.Gson;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired Gson gson;
	@Autowired AuthMapper authMapper;
	
	//로그인 하기
	//public ResponseEntity<String> login(HashMap<String,Object> body, HttpServletRequest req, HttpServletResponse res) throws Exception{
	public ResponseEntity<String> login(HttpServletRequest req, HttpServletResponse res) throws Exception{
		String serviceName = Utils.property("server.name");
		//req 정보
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("login.param: {}", param);

		//유효성 검사
		if(param.get("userid")==null) return Response.error(ResponseCode.MESSAGE, "아이디를 입력해 주세요");
		else if(param.get("password")==null) return Response.error(ResponseCode.MESSAGE, "비밀번호를 입력해 주세요");
				
		//로그인 기본 정보
		String userip = Utils.getUserIP(req);
		logger.info("로그인[{}]({}: {}) = {}, {}",  param.get("userid"), userip, req.getHeader("User-Agent"));
		
		
		//DB정보
		JtMap userDbInfo = authMapper.selectUserCheck(param);
		logger.info("login.userDbInfo: {}", gson.toJson(userDbInfo));
		
		//req:DB 정보비교
		//실패
		if(
				userDbInfo==null
				|| (!userDbInfo.get("userpw").equals(Utils.sha256(param.get("password").toString())))
		){
			return Response.error(ResponseCode.MESSAGE, "아이디 또는 패스워드가 일치하지 않습니다.", "/login");
		}
		//성공
		else {
			String userid = userDbInfo.get("userid").toString();
			String grade = userDbInfo.get("grade").toString();
			String auth = userDbInfo.get("auth").toString();
			String useragent = req.getHeader("User-Agent");
			String sessionid = req.getSession().getId();//서버 재시작하면 사용자 다시 로그인 해야함
			//로그인 성공 DB정보 새로 업데이트;
			String loginAuthKey = SessionUtils.getLoginAuthkey(userid, grade, auth, userip, sessionid, useragent);			
			
			
			param.put("loginAuthKey", loginAuthKey);
			param.put("userip", userip);			
			authMapper.updateUserLoginInfo(param);
			
			//기존 쿠키 삭제
			SessionUtils.removeAllCookies(req, res);
			
			//SysCookie 쿠키정보 갱신
			String sysCookie = SessionUtils.getAppCookieEnc(req, userDbInfo);
			SessionUtils.setCookie(res, sysCookie);
			//쿠키 아이디 저장
			if(param.get("isSaveID")!=null){
				//로그인
				if(String.valueOf(param.get("isSaveID")).equals("true")) {
					Utils.setCookie(res, serviceName+"_savedID", userid, false, 60*60*24*30); //30일
				}else {
					Utils.deleteCookie(req, res, serviceName+"_savedID");
					Utils.deleteCookie(req, res, serviceName+"_isSaveID");
				}
			}else{
				//로그인재시도
				Boolean isSaveID = false;
				Cookie[] cookies = req.getCookie();
				if(cookies!=null)){
					for(Cookie cookie : cookies){
						String key = cookie.getName();
						if(key.equals(serviceName) || key.startsWith(serviceName+"_isSaveID")){
							isSaveID = true;
							break;
						}
					}
					if(isSaveID){
						int limitUpdate = 60*60*24*30; //30일
						Utils.setCookie(res, serviceName+"_isSaveID", "true", limitUpdate);
						Utils.setCookie(res, serviceName+"_savedID", userid, limitUpdate);
					}
				}
			}
			//아이디_토큰 저장
			String useridToknCookieName = serviceName+"_"+Utils.aes256.encode(userid).replaceAll("[^a-zA-Z]", "");
			Utils.setCookie(res, useridToknCookieName, Utils.getTokn(userid + Utils.getUserIP(req) + grade + auth), true);
			//Utils.setCookie(res, useridToknCookieName, Utils.getTokn(userid + Utils.getUserIP() + grade + auth), true);
		}	
		
		//String result = "Login";
		
		return Response.ok();
	}
	//로그아웃
	public ResponseEntity<String> logout(HttpServletRequest req, HttpServletResponse res) throws Exception{	

		SessionUtils.logout(req, res);		
		return Response.ok();
	}	
	//로그인 여부 확인
	public ResponseEntity<String> check(HttpServletRequest req, HttpServletResponse res) throws Exception{
		return Response.ok(String.valueOf(SessionUtils.check(req, res)));
	}	
	//로그인 사용자 정보 조회
	public ResponseEntity<String> userInfo(HttpServletRequest req) throws Exception{
		return Response.ok(SessionUtils.getAppCookieDec(req));
	}
	//로그인 시간제한 초기화(60분)
	public ResponseEntity<String> reset(HttpServletRequest req, HttpServletResponse res) throws Exception{
		SessionUtils.resetCookie(req, res);
		return Response.ok();
	}
	//로그인 재시도 팝업 나타날 때 쿠키 삭제 처리
	public ResponseEntity<String> logoff(HttpServletRequest req, HttpServletResponse res) throws Exception{
		SessionUtils.removeAllCookies(req, res);
		return Response.ok();
	}
}
