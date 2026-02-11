package com.aaboo.svelteSpringbootStarter.filetran;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aaboo.svelteSpringbootStarter.common.Response;
import com.aaboo.svelteSpringbootStarter.common.ResponseCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="/filetran")
public class FiletranController {
	
	@Autowired FiletranService filetranService;

	@RequestMapping(value="/{reqPath}", method= {RequestMethod.POST})
	public ResponseEntity<String> FiletranControllerEngine(HttpServletRequest req, HttpServletResponse res, @PathVariable("reqPath") String reqPath) throws Exception {
		//logger.info(reqPath+"={}", body);		
		switch(reqPath){
			case "init": return filetranService.init(req); //파일송수신 INIT			
			case "selectFiletran": return filetranService.selectFiletran(req); //파일송수신 조회			
			default: return Response.error(ResponseCode.ERR_NONE_URL);
		}
	}
}
