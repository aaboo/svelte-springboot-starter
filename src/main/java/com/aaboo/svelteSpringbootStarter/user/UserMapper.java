package com.aaboo.svelteSpringbootStarter.user;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aaboo.svelteSpringbootStarter.common.JtMap;

public interface UserMapper {
	
	int insertUserHistory(@Param("param") Map<String,Object> param); //로그기록
	JtMap selectLoginUser(@Param("param") Map<String,Object> param); //로그인시도 사용자 정보 불러오기
	JtMap getAgentinfoByOagent(@Param("agent") String agent); //총판의 총판정보 가져오기
	JtMap getAgentinfoByCagent(@Param("agent") String agent); //대리점의 총판정보 가져오기
	JtMap getAgentinfoByDealer(@Param("agent") String agent); //딜러의 총판정보 가져오기

	int insertSiteHistory(@Param("param") Map<String, Object> param);//단말기 변경이력 저장(공통)
	
	int insertUserSvcHistory(@Param("param") Map<String, Object> param); //USER_SVC_HISTORY 로그저장
	
}
