package com.aaboo.svelteSpringbootStarter.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CommonMapper {
	String selectDategubun(@Param("param") Map<String, Object> param);//작업일구분
	List<JtMap> selectFiletxserver();//파일송수신 기관 불러오기(TOAST-UI SELECT-BOX)
}
