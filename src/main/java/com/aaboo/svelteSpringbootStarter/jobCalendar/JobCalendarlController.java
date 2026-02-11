package com.aaboo.svelteSpringbootStarter.jobCalendar;

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
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="/jobCalendar")
public class JobCalendarlController {
	
	@Autowired JobCalendarService jobCalendarService;
	
	@RequestMapping(value="/{reqPath}", method= {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public ResponseEntity<String> AuthControllerEngine(HttpServletRequest req, HttpServletResponse res, @PathVariable("reqPath") String reqPath) throws Exception {
		//logger.info(reqPath+"={}", body);		
		switch(reqPath){
			case "selectJobCalendar": return jobCalendarService.selectJobCalendar(req); //조회
			case "selectJobCalendarDetail": return jobCalendarService.selectJobCalendarDetail(req); //상세조회
			case "insertJobCalendar": return jobCalendarService.insertJobCalendar(req); //배치등록
			default: return Response.error(ResponseCode.ERR_NONE_URL);
		}
	}
}
