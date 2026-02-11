package com.aaboo.svelteSpringbootStarter.jobCalendar;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aaboo.svelteSpringbootStarter.common.JtMap;

public interface JobCalendarMapper {	
	List<JtMap> selectJobCalendar(@Param("param") Map<String, Object> param);//JOB 조회
	List<JtMap> selectJobCalendarDetail(@Param("param") Map<String, Object> param);//PROC 조회
	int insertJobCalendar(@Param("param") Map<String, Object> param);//배치등록
}
