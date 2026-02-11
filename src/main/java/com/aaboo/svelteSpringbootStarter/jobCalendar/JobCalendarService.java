package com.aaboo.svelteSpringbootStarter.jobCalendar;

import java.text.SimpleDateFormat;
import java.util.Date;
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
public class JobCalendarService {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired Gson gson;
	@Autowired JobCalendarMapper jobCalendarMapper;
	
	//조회
	public ResponseEntity<String> selectJobCalendar(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		String date1 = (String) param.get("date1");
		String date2 = (String) param.get("date2");
		if(date1==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(date2==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		try {
			Date format1 = new SimpleDateFormat("yyyyMMdd").parse(date1);
			Date format2 = new SimpleDateFormat("yyyyMMdd").parse(date2);
//			long diffSec = (format2.getTime() - format1.getTime()) / 1000; //초 차이
//			long diffMin = (format2.getTime() - format1.getTime()) / (1000*60); //분 차이
//			long diffHour = (format2.getTime() - format1.getTime()) / (1000*60*60); //시간 차이
			long diffDays = (format2.getTime() - format1.getTime()) / (1000*60*60*24); //일자수 차이
			//logger.info("diffDays = {}", diffDays);
			if(diffDays > 42) return Response.error(ResponseCode.ERR_INVALID_PARAM);
		}catch(Exception e) {
			return Response.error(ResponseCode.ERR_INVALID_PARAM);
		}
		
		
		List<JtMap> list = jobCalendarMapper.selectJobCalendar(param);		
		return Response.ok(list);
	}
	//상세조회
	public ResponseEntity<String> selectJobCalendarDetail(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);		
		List<JtMap> list = jobCalendarMapper.selectJobCalendarDetail(param);		
		return Response.ok(list);
	}
	//상세조회
	public ResponseEntity<String> insertJobCalendar(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);		
		int result = jobCalendarMapper.insertJobCalendar(param);		
		return Response.ok(result);
	}
}
