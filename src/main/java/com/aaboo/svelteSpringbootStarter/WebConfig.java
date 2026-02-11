package com.aaboo.svelteSpringbootStarter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAspectJAutoProxy
public class WebConfig implements WebMvcConfigurer {
	
	//ProxyProcess를 생성하고 예외 Path(excludePathPatterns) 을 지정해 준다.
	private static final String[] INTERCEPTOR_WHITE_LIST = {
		"/_app/**",
		"/js/**",
		"/*.png",
		"/*.ico",
		"/img/**",
		"/*.html",
		"/login",
		"/auth/**"
	};
	@Bean
	public ProxyProcessor proxyProcessor() { //Property 정보를 가져오기 위해 Bean을 등록
		return new ProxyProcessor();
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(proxyProcessor())
			.addPathPatterns("/**")
			.excludePathPatterns(INTERCEPTOR_WHITE_LIST);
	}
	
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**").allowedOrigins("http:localhost:8082");
//	}

}
