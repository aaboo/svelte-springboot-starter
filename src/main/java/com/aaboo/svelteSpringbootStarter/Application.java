package com.aaboo.svelteSpringbootStarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@Configuration
@PropertySource({"classpath:application.properties", "classpath:application-${spring.profiles.active:local}.properties"})
//public class Application extends SpringBootServletInitializer { //2번 로딩되어 extends 삭제
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}