package com.aaboo.svelteSpringbootStarter.batchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aaboo.svelteSpringbootStarter.common.JtMap;
import com.aaboo.svelteSpringbootStarter.common.Response;
import com.aaboo.svelteSpringbootStarter.common.ResponseCode;
import com.aaboo.svelteSpringbootStarter.common.Utils;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class BatchResultService {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired Gson gson;	
	@Autowired BatchResultMapper batchResultMapper;
	
	//배치처리결과
	public ResponseEntity<String> selectBatchResult(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("workdate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
				
		List<JtMap> jobList = batchResultMapper.selectJob(param);		
		List<JtMap> procList = batchResultMapper.selectProc(param);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("jobList", jobList);
		result.put("procList", procList);
		
		return Response.ok(result);
	}
}
