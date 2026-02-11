package com.aaboo.svelteSpringbootStarter.daemon;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aaboo.svelteSpringbootStarter.common.JtMap;

public interface DaemonMapper {

	List<JtMap> selectDaemonList(@Param("param") Map<String, Object> param);//데몬리스트 조회
	List<JtMap> selectDaemonHistory(@Param("param") Map<String, Object> param);//데몬리스트별 프로세스 히스토리 조회
	int updateDaemonErr(@Param("param") Map<String, Object> param);//데몬 프로세스 에러 중 담당자 에러확인버튼 클릭 처리
	
}
