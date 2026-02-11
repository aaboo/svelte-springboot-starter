package com.aaboo.svelteSpringbootStarter.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aaboo.svelteSpringbootStarter.common.Response;
import com.aaboo.svelteSpringbootStarter.common.ResponseCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="/auth")
public class AuthController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired AuthService authService;
	@Value("${server.name}") protected String serviceName;
	
	@RequestMapping(value="/{reqPath}", method= {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public ResponseEntity<String> AuthControllerEngine(HttpServletRequest req, HttpServletResponse res, @PathVariable("reqPath") String reqPath) throws Exception {
		//logger.info(reqPath+"={}", body);
		
		switch(reqPath){
			case "login": return authService.login(req, res); //로그인
			case "logout": return authService.logout(req, res); //로그아웃
			case "check": return authService.check(req, res); //로그인 여부 확인
			case "userInfo": return authService.userInfo(req); //로그인된 사용자 정보 조회
			case "reset": return authService.reset(req, res); //시간 추가
			case "logoff": return authService.logoff(req, res); //로그인 재시도 팝업 나타날 때 처리
			default: return Response.error(ResponseCode.ERR_NONE_URL);
		}
	}
}
