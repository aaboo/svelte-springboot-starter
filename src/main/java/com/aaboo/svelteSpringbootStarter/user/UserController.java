package com.aaboo.svelteSpringbootStarter.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="/user")
public class UserController {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired UserService userService;
	
	//QVAN 가맹점 신청서 연동
	@RequestMapping(value="/{reqPath}", method={RequestMethod.POST})
	public void QvanControllerEngine1(@PathVariable("reqPath") String reqPath, HttpServletRequest req, HttpServletResponse res) throws Exception {
		//logger.info("/user/==> {}", reqPath);
		switch(reqPath){
			case "hitMenu": userService.hitMenu(req); break; //메뉴접속 로그저장(front-end/src/routes/test/+layout.svelte)
			default: break;
		}
	}
}