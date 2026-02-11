package com.aaboo.svelteSpringbootStarter.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aaboo.svelteSpringbootStarter.common.Response;
import com.aaboo.svelteSpringbootStarter.common.ResponseCode;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="/test")
public class TestController {
	
	@Autowired TestService testService;
	
	@RequestMapping(value="/{reqPath}", method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ResponseEntity<String> TestControllerGetPost(HttpServletRequest req, @PathVariable("reqPath") String reqPath) {
		System.out.println(reqPath);
		switch(reqPath){
			case "home": return testService.home(req);
			case "test1": return testService.test1(req);
			case "test2": return testService.test2(req);
			default: return Response.error(ResponseCode.ERR_NONE_URL);
		}
	}
	
}