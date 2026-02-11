package com.aaboo.svelteSpringbootStarter.batchResult;

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
@RequestMapping(value="/batchResult")
public class BatchResultController {
	
	@Autowired BatchResultService batchResultService;
	
	@RequestMapping(value="/{reqPath}", method= {RequestMethod.POST})
	@ResponseBody
	public ResponseEntity<String> ResultControllerEngine(HttpServletRequest req, HttpServletResponse res, @PathVariable("reqPath") String reqPath) throws Exception {
		//logger.info(reqPath+"={}", body);		
		switch(reqPath){
			case "selectBatchResult": return batchResultService.selectBatchResult(req); //배치처리결과, 프로세스처리결과 조회
			default: return Response.error(ResponseCode.ERR_NONE_URL);
		}
	}

}