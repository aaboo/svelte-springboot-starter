package com.aaboo.svelteSpringbootStarter.batchResult;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aaboo.svelteSpringbootStarter.common.JtMap;

public interface BatchResultMapper {
	List<JtMap> selectJob(@Param("param") Map<String, Object> param);//JOB 조회
	List<JtMap> selectProc(@Param("param") Map<String, Object> param);//PROC 조회	

}
