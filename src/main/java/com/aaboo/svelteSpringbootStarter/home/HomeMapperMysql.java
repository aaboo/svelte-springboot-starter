package com.aaboo.svelteSpringbootStarter.home;

import java.util.List;

import com.aaboo.svelteSpringbootStarter.common.JtMap;

public interface HomeMapperMysql {
	List<JtMap> testMysql();//MYSQL 테스트
	int rollbackTestMysql1();
	int rollbackTestMysql2();
}
