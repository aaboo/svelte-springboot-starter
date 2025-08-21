package com.aaboo.svelteSpringbootStarter;

import java.util.Map;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.aaboo.svelteSpringbootStarter.user.UserService;

import jakarta.servlet.http.HttpServletRequest;

/*
 * @Authoer aaboo (20240405) 
 * @Description 
 * 	- USER_SVC_HISTORY 테이블에 접속 로그를 남기기 위해 만들어진 모듈
 * 	- !within(..)을 제외한 모든 *.Controller.java 의 method를 실행하기 전 userService.log(req)가 먼저 선처리됨
 * @Setting 방법
 *  - pom.xml 파일에 AspectJ dependency 작성
 *  - WebCopnfig.java에 @EnableAspectJAutoProxy 작성
 *  - ControllerAspect @Before 작성 및 전처리 메쏘드 실행
 */

@Aspect
@Component
public class ControllerAspect {
	
	private final UserService userService;
	
	public ControllerAspect(UserService userService) {
		this.userService = userService;
	}
	@Before(
		"execution(* com.aaboo.svelteSpringbootStarter.*.*Controller.*(..))" //모든 *Controller.java의 method 
		+" && args(req,..)" //파라미터 조건: 첫 파라미터 req
		+" && !within(com.aaboo.svelteSpringbootStarter.auth.*Controller)" //제외할 컨트롤러
		+" && !within(com.aaboo.svelteSpringbootStarter.user.*Controller)"
	)
	public void beforeControllerMethodExecution1(HttpServletRequest req) throws Exception {
		userService.log(req);
	}
}