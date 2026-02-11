package com.aaboo.svelteSpringbootStarter.daemon;

import java.util.ArrayList;
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

import jakarta.servlet.http.HttpServletRequest;

@Service
public class DaemonService {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired DaemonMapper daemonMapper;
	
	//DAEMON 조회
	public ResponseEntity<String> selectDaemon(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		//유효성검사
		if(param.get("historyCnt")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		//historyCnt: 1~40
		int historyCnt = Integer.parseInt(String.valueOf(param.get("historyCnt")));
		if(historyCnt<1 || historyCnt>40) return Response.error(ResponseCode.ERR_INVALID_PARAM);
		
		//조회
		List<JtMap> jobList = daemonMapper.selectDaemonList(param);		
		List<JtMap> procList = daemonMapper.selectDaemonHistory(param);
		
		//PROC 리스트 {jobcode: procList} 형태로 정리
		HashMap<String, List<JtMap>> jobProcListObj = new HashMap<String, List<JtMap>>();
		List<JtMap> procListTemp = null;
		String jobcodeTmp = null;
		for(JtMap item : procList) {
			String jobcode = item.get("jobcode").toString();
			if(!jobcode.equals(jobcodeTmp)) {
				if(jobcodeTmp!=null) jobProcListObj.put(jobcodeTmp, procListTemp);
				jobcodeTmp = jobcode;
				procListTemp = new ArrayList<JtMap>();
			}
			procListTemp.add(item);
		}
		jobProcListObj.put(jobcodeTmp, procListTemp);

		//결과 정리
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("jobList", jobList);
		result.put("jobProcListObj", jobProcListObj);
		
		return Response.ok(result);
	}
	
	//정상 Error로 표기 전환
	public ResponseEntity<String> updateDaemonErrStart(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("daemoncode")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		
		//파라미터 추가
		param.put("alertonoff", "C");
		
		//업데이트
		int result = daemonMapper.updateDaemonErr(param);
		
		return Response.ok(result);
	}
	
	//비정상 Error로 표기 전환
	public ResponseEntity<String> updateDaemonErrFinish(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("daemoncode")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		
		//파라미터 추가
		param.put("alertonoff", "A");
		
		//업데이트
		int result = daemonMapper.updateDaemonErr(param);
		
		return Response.ok(result);
	}
	
}
