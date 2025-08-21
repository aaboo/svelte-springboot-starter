package com.aaboo.svelteSpringbootStarter.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="/common")
public class CommonController {
	
	@Autowired CommonService commonService;
	
	@RequestMapping(value="/{reqPath}", method= {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public ResponseEntity<String> AuthControllerEngine(HttpServletRequest req, HttpServletResponse res, @PathVariable("reqPath") String reqPath) throws Exception {
		//logger.info(reqPath+"={}", body);		
		switch(reqPath){
			case "selectDategubun": return commonService.selectDategubun(req); //작업일구분
			default: return Response.error(ResponseCode.ERR_NONE_URL);
		}
	}

}
