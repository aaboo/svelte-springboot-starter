package com.aaboo.svelteSpringbootStarter.purchase;

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
public class PurchaseService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired PurchaseMapper purchaseMapper;
	
	//매입처리 조회
	public ResponseEntity<String> selectPurchase(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("makedate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		List<JtMap> list = purchaseMapper.selectPurchase(param);		
		return Response.ok(list);
	}
	
}
