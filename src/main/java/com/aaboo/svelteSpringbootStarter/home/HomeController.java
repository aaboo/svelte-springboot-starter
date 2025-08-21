package com.aaboo.svelteSpringbootStarter.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

//@CrossOrigin(origins = "http://localhost:8082")
@Controller
@RequestMapping(value="/")
public class HomeController {

	@Autowired HomeService homeService;
	
	/*
	//thyemeleaf 템플릿 테스트
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String HomeController1(final HttpServletRequest req, final Model model) {
		model.addAttribute("message", "World");
		return "index"; // /src/main/resoureces/template/index.html
		//return "login/login"; // /src/main/resoureces/template/login/login.html
	}
	*/

	//sveltekit redirection
	@GetMapping("/")
	public String HomeController1(final HttpServletRequest req, final Model model) {
		return "forward:index.html";
	}

	
	
	//ResponseBody 테스트
	@RequestMapping(value="/home", method={RequestMethod.POST})
	@ResponseBody
	public String HomeController2(HttpServletRequest req) {
		return "Welcome Home!";
	}
	
	//DB접속 테스트
	@RequestMapping(value="/test", method= {RequestMethod.POST})
	@ResponseBody
	public String test(HttpServletRequest req) {		
		return homeService.test();
	}
	//DB접속 테스트
	@RequestMapping(value="/heartBeat", method={RequestMethod.POST})
	@ResponseBody
	public String heartBeat(HttpServletRequest req) {		
		return homeService.heartBeat();
	}
	//DB접속 테스트
	@RequestMapping(value="/testMysql", method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String testMysql(HttpServletRequest req) {		
		return homeService.testMysql();
	}
	//application.properties 파일 정보 읽기 테스트
	@RequestMapping(value="/testEnv", method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String testEnv(HttpServletRequest req) {		
		return homeService.testEnv();
	}
	//rollback 테스트 1
	@RequestMapping(value="/rollbackTest1", method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String rollbackTest1(HttpServletRequest req) {		
		return homeService.rollbackTest1();
	}
	//rollback 테스트 2
	@RequestMapping(value="/rollbackTest2", method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String rollbackTest2(HttpServletRequest req) {		
		return homeService.rollbackTest2();
	}
	//rollback 테스트 3
	@RequestMapping(value="/rollbackTest3", method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String rollbackTest3(HttpServletRequest req) {		
		return homeService.rollbackTest3();
	}
	//rollback 테스트 4 - MySQL 테스트
	@RequestMapping(value="/rollbackTest4", method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String rollbackTest4(HttpServletRequest req) {		
		return homeService.rollbackTest4();
	}
	//rollback 테스트 5 - MySQL 테스트
	@RequestMapping(value="/rollbackTest5", method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String rollbackTest5(HttpServletRequest req) {		
		return homeService.rollbackTest5();
	}
}
