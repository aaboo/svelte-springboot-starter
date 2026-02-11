package com.aaboo.svelteSpringbootStarter.filetran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aaboo.svelteSpringbootStarter.common.CommonMapper;
import com.aaboo.svelteSpringbootStarter.common.JtMap;
import com.aaboo.svelteSpringbootStarter.common.Response;
import com.aaboo.svelteSpringbootStarter.common.ResponseCode;
import com.aaboo.svelteSpringbootStarter.common.Utils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class FiletranService {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired FiletranMapper filetranMapper;
	@Autowired CommonMapper commonMapper;
	
	//파일송수신 INIT
	public ResponseEntity<String> init(HttpServletRequest req) throws Exception{
		//결과형태
		HashMap<String, Object> result = new HashMap<String, Object>();
		//파일송수신 기관 불러오기(TOAST-UI SELECT-BOX)
		List<JtMap> svrnameList = commonMapper.selectFiletxserver();
		//결과조합
		result.put("svrnameList", svrnameList);
		//출력
		return Response.ok(result);
	}
	
	//파일송수신 조회
	public ResponseEntity<String> selectFiletran(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("workdate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		List<JtMap> list = filetranMapper.selectFiletran(param);		
		return Response.ok(list);
	}
}
