package com.aaboo.svelteSpringbootStarter.common;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CommonService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired CommonMapper commonMapper;

	//작업일 구분
	public ResponseEntity<String> selectDategubun(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("workdate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		String result = commonMapper.selectDategubun(param);		
		return Response.ok(result);
	}
}
