package com.aaboo.svelteSpringbootStarter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.aaboo.svelteSpringbootStarter.common.SessionUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//WebConfig.java 설정도 함께 사용
public class ProxyProcessor implements HandlerInterceptor  {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());	

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		
		//System.out.println("==============================start=================================");
		logger.info("req.getRequestURI() : {}", req.getRequestURI());
		
		Cookie cookie = SessionUtils.getCookie(req);
		String sysCookie = cookie!=null?cookie.getValue():null;
		Boolean check = SessionUtils.check(req, res);
		if(cookie==null || !check) {
			logger.info(sysCookie);
			logger.info(String.valueOf(check));
			SessionUtils.logout(req, res);
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(req, res, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView modelAndView) throws Exception {
		//System.out.println("===============================end==================================");
		//HandlerInterceptor.super.postHandle(req,  res,  handler,  modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception exception) throws Exception {
		//System.out.println("===============================end==================================");
		//HandlerInterceptor.super.afterCompletion(req,  res,  handler,  exception);
	}
}
