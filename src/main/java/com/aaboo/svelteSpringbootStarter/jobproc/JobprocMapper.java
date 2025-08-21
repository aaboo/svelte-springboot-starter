package com.aaboo.svelteSpringbootStarter.jobproc;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aaboo.svelteSpringbootStarter.common.JtMap;

public interface JobprocMapper {
	//JOB:PROC = 1:N
	List<JtMap> selectJobgroup(@Param("param") Map<String, Object> param);//JOB 조회
	List<JtMap> selectJob(@Param("param") Map<String, Object> param);//JOB 조회
	List<JtMap> selectProc(@Param("param") Map<String, Object> param);//PROC 조회	
	//PROC 상세팝업레서 처리
	int updateJobProcQ_abProcTran(@Param("param") Map<String, Object> param);//JOB 작업실행: AB_PROC_TRAN
	int updateJobProcQ_abJobTran(@Param("param") Map<String, Object> param);//JOB 작업실행: AB_JOB_TRAN
	int updateJobProcResultConfirm_abJobTran_H(@Param("param") Map<String, Object> param);//JOB 결과확인: AB_JOB_TRAN(수작업)
	int updateJobProcResultConfirm_abProcTran_H(@Param("param") Map<String, Object> param);//JOB 결과확인: AB_PROC_TRAN(수작업)
	int updateJobProcResultConfirm_abJobTran(@Param("param") Map<String, Object> param);//JOB 결과확인: AB_JOB_TRAN(수작업외)
	int updateJobProcSkip_abJobTran(@Param("param") Map<String, Object> param);//JOB 작업스킵: AB_JOB_TRAN
	int updateJobProcSkip_abProcTran(@Param("param") Map<String, Object> param);//JOB 작업스킵: AB_PROC_TRAN
	int updateProcC(@Param("param") Map<String, Object> param);//PROC 스킵처리(C-기존procstatus)
	int updateProcN(@Param("param") Map<String, Object> param);//PROC 스킵처리(C-N)
	int insertProcQ(@Param("param") Map<String, Object> param);//PROC 재실행(Q-'')
	int callProcAbLastflag(@Param("param") Map<String, Object> param);//PROC 처리 후 프로시저호출 : ab_proc_tran.lastflag 자동업데이트
}
