package com.aaboo.svelteSpringbootStarter.jobproc;

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
@RequestMapping(value="/jobproc")
public class JobprocController {
	
	@Autowired JobprocService jobprocService;
	
	@RequestMapping(value="/{reqPath}", method= {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public ResponseEntity<String> JobprocControllerEngine(HttpServletRequest req, HttpServletResponse res, @PathVariable("reqPath") String reqPath) throws Exception {
		//logger.info(reqPath+"={}", body);		
		switch(reqPath){
			case "selectInit": return jobprocService.selectInit(req); //INIT
			case "selectJob": return jobprocService.selectJob(req); //JOB 조회
			case "selectProc": return jobprocService.selectProc(req); //PROC 조회
			case "saveJobProcQ": return jobprocService.saveJobProcQ(req); //JOB 작업실행
			case "saveJobProcResultConfirm": return jobprocService.saveJobProcResultConfirm(req); //JOB 결과확인
			case "saveJobProcSkip": return jobprocService.saveJobProcSkip(req); //JOB 작업스킵
			case "updateProcC": return jobprocService.updateProcC(req); //PROC 스킵처리(C-기존procstatus)
			case "updateProcN": return jobprocService.updateProcN(req); //PROC 스킵처리(C-N)
			case "insertProcQ": return jobprocService.insertProcQ(req); //PROC 재실행(C-N)
			default: return Response.error(ResponseCode.ERR_NONE_URL);
		}
	}
}
