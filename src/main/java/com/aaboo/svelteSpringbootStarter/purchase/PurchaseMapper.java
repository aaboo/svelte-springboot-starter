package com.aaboo.svelteSpringbootStarter.purchase;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aaboo.svelteSpringbootStarter.common.JtMap;

public interface PurchaseMapper {
	
	List<JtMap> selectPurchase(@Param("param") Map<String, Object> param);//매입처리 조회
	
}
