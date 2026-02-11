package com.aaboo.svelteSpringbootStarter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SveltekitController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//Sveltekit은 /src/main/webapp/index.html 파일 하나만 생성되어 routing 된다.
	//브라우저 새로고침이나 WAS redirect 할 경우 index.html로 보내주어야 404 not found가 나지 않는다.
	@RequestMapping(value="/{path:[^\\.]*}", method= {RequestMethod.GET})
	public String forward(@PathVariable String path) {
		logger.info(path);
		return "forward:index.html";
	}
	
}
