package com.aaboo.svelteSpringbootStarter.jobproc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.aaboo.svelteSpringbootStarter.common.JtMap;
import com.aaboo.svelteSpringbootStarter.common.Response;
import com.aaboo.svelteSpringbootStarter.common.ResponseCode;
import com.aaboo.svelteSpringbootStarter.common.Utils;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JobprocService {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired Gson gson;
	@Autowired JobprocMapper jobprocMapper;
	@Autowired PlatformTransactionManager platformTransactionManager; //트랜잭션
		
	//INIT
	public ResponseEntity<String> selectInit(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("workdate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		List<JtMap> jobgroup = jobprocMapper.selectJobgroup(param);		
		return Response.ok(jobgroup);
	}	
	//JOB 조회
	public ResponseEntity<String> selectJob(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("workdate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		
		//로그 출력용 플래그 추가(LogbackFilter.java 참조)
		param.put("log", true);
		
		//조회
		List<JtMap> jobList = jobprocMapper.selectJob(param);		
		List<JtMap> procList = jobprocMapper.selectProc(param);
		
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
	//PROC 조회
	public ResponseEntity<String> selectProc(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		
		if(param.get("workdate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		else if(param.get("jobcode")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		
		List<JtMap> list = jobprocMapper.selectProc(param);		
		return Response.ok(list);
	}
	//실행중인 JOB, PROC 조회 - JobprocSocketController.java에서 사용
	public Map<String, Object> selectRunning(){
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String workdate = sdf.format(new Date());
		param.put("workdate", workdate);
		//param.put("jobstatus", "R");
		
		//조회
		List<JtMap> jobList = jobprocMapper.selectJob(param); //오늘 전체 JOB
		List<JtMap> procList = jobprocMapper.selectProc(param);	//오늘 전체 PROC
		
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
		result.put("jobList", jobList);
		result.put("jobProcListObj", jobProcListObj);
		
		return result;
	}
	//JOB 작업실행
	public ResponseEntity<String> saveJobProcQ(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("workdate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("jobcode")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		//트랜잭션
		TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
		int result = 0;
		try {
			result += jobprocMapper.updateJobProcQ_abProcTran(param);
			result += jobprocMapper.updateJobProcQ_abJobTran(param);
			platformTransactionManager.commit(status);//커밋
		} catch (Exception e) {
			e.printStackTrace();
			platformTransactionManager.rollback(status);//롤백
			return Response.error(ResponseCode.ERR_ROLLBACK);
		}
		return Response.ok(result);
	}
	//JOB 결과확인
	public ResponseEntity<String> saveJobProcResultConfirm(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("workdate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("jobcode")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("workmemo")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		//트랜잭션
		TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
		int result = 0;
		try {
			//수작업 여부 확인
			JtMap job = jobprocMapper.selectJob(param).get(0);
			String jobstatus = job.get("jobstatus").toString();
			if(jobstatus.equals("H")) {//수작업
				result += jobprocMapper.updateJobProcResultConfirm_abJobTran_H(param);
				result += jobprocMapper.updateJobProcResultConfirm_abProcTran_H(param);
			}else {
				result += jobprocMapper.updateJobProcResultConfirm_abJobTran(param);
			}
			platformTransactionManager.commit(status);//커밋
		} catch (Exception e) {
			e.printStackTrace();
			platformTransactionManager.rollback(status);//롤백
			return Response.error(ResponseCode.ERR_ROLLBACK);
		}
		return Response.ok(result);
	}
	//JOB 작업스킵
	public ResponseEntity<String> saveJobProcSkip(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("workdate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("jobcode")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("workmemo")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		//트랜잭션
		TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
		int result = 0;
		try {
			result += jobprocMapper.updateJobProcSkip_abJobTran(param);
			result += jobprocMapper.updateJobProcSkip_abProcTran(param);
			platformTransactionManager.commit(status);//커밋
		} catch (Exception e) {
			e.printStackTrace();
			platformTransactionManager.rollback(status);//롤백
			return Response.error(ResponseCode.ERR_ROLLBACK);
		}
		return Response.ok(result);
	}
	//PROC 스킵처리(C-기존procstatus)
	public ResponseEntity<String> updateProcC(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("workdate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("proccode")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("comment")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("createid")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		int result = jobprocMapper.updateProcC(param);
		jobprocMapper.callProcAbLastflag(param);//PROC 처리 후 프로시저호출 : ab_proc_tran.lastflag 자동업데이트
		return Response.ok(result);
	}
	//PROC 자료없음(C-N)
	public ResponseEntity<String> updateProcN(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("workdate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("proccode")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("createid")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("comment")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		int result = jobprocMapper.updateProcN(param);
		return Response.ok(result);
	}
	//PROC 재실행(Q-'')
	public ResponseEntity<String> insertProcQ(HttpServletRequest req) throws Exception{
		Map<String, Object> param = Utils.getRequestData(req);
		logger.info("param: {}", param);
		if(param.get("workdate")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("proccode")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		if(param.get("createid")==null) return Response.error(ResponseCode.ERR_NONE_PARAM);
		int result = jobprocMapper.insertProcQ(param);
		jobprocMapper.callProcAbLastflag(param);//PROC 처리 후 프로시저호출 : ab_proc_tran.lastflag 자동업데이트
		return Response.ok(result);
	}
}
