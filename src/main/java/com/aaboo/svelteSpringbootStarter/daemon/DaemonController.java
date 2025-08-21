package com.aaboo.svelteSpringbootStarter.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping(value="/daemon")
public class DaemonController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired DaemonService daemonService;

	@RequestMapping(value="/{reqPath}", method= {RequestMethod.POST})
	public ResponseEntity<String> DaemonControllerEngine(HttpServletRequest req, HttpServletResponse res, @PathVariable("reqPath") String reqPath) throws Exception {
		logger.info("reqPath={}", reqPath);		
		switch(reqPath){
			case "selectDaemon": return daemonService.selectDaemon(req); //파일송수신 조회
			//데몬 프로세스 에러 중 담당자 에러확인버튼 클릭 처리
			case "updateDaemonErrStart": return daemonService.updateDaemonErrStart(req);//정상 Error로 표기 전환		
			case "updateDaemonErrFinish": return daemonService.updateDaemonErrFinish(req);//비정상 Error로 표기 전환		
			default: return Response.error(ResponseCode.ERR_NONE_URL);
		}
	}
}

