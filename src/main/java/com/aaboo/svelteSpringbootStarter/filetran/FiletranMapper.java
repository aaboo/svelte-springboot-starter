package com.aaboo.svelteSpringbootStarter.filetran;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aaboo.svelteSpringbootStarter.common.JtMap;

public interface FiletranMapper {

	List<JtMap> selectFiletran(@Param("param") Map<String, Object> param);//파일송수신 조회
	
}
