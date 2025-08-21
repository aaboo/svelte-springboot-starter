package com.aaboo.svelteSpringbootStarter.db;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.type.JdbcType;

public class CommonDbConfig {
	/*****************************************************************
	 * 기본 설정: Null값 처리
	 * ***************************************************************/
	public Configuration getConfig() {
		Configuration cfg = new Configuration();
		cfg.setCacheEnabled(true);
		cfg.setLazyLoadingEnabled(false);
		cfg.setAggressiveLazyLoading(false);
		cfg.setMultipleResultSetsEnabled(true);
		cfg.setUseColumnLabel(true);
		cfg.setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
		cfg.setDefaultExecutorType(ExecutorType.SIMPLE);
		cfg.setDefaultStatementTimeout(60);
		cfg.setJdbcTypeForNull(JdbcType.VARCHAR);
		cfg.setCallSettersOnNulls(true);
		cfg.setMapUnderscoreToCamelCase(false);
		cfg.setUseGeneratedKeys(false);
		cfg.setLogPrefix("[SQL]");
		return cfg;
	}
}
