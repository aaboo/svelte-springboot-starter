package com.aaboo.svelteSpringbootStarter.db;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.aaboo.svelteSpringbootStarter.common.AES256;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan("com.aaboo.svelteSpringbootStarter")
public class MysqlConfig {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${dataSource.tokn}") protected String tokn;
	
	protected CommonDbConfig commonDbConfig = new CommonDbConfig();
	
	/*****************************************************************
	 * Mysql (Not primary - sub)
	 * ***************************************************************/	
	//application.properties	 
	@Value("${datasource.mysql.url}") protected String mysqlUrl;
	@Value("${datasource.mysql.username}") protected String mysqlUsername;
	@Value("${datasource.mysql.password}") protected String mysqlPassword;
	

	@Bean
	public DataSource dataSourceMysql() throws Exception {
		AES256 aes256 = new AES256(tokn);
		HikariDataSource dataSource = new HikariDataSource();
		//dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		dataSource.setJdbcUrl(mysqlUrl);
		dataSource.setUsername(aes256.decode(mysqlUsername));
		dataSource.setPassword(aes256.decode(mysqlPassword));
		dataSource.setAutoCommit(false); //autocommit: false > 이것이 있어야 rollback 기능이 먹힌다.
		//dataSource.setConnectionTestQuery("SELECT 1");
		dataSource.setMaximumPoolSize(50);
		return dataSource;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactoryMysql(@Autowired @Qualifier("dataSourceMysql") DataSource dataSourceMysql) throws Exception {
		logger.info("mysqlSqlSessionFactory() Start");
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSourceMysql);
		sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
		//sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		sqlSessionFactoryBean.setConfiguration(commonDbConfig.getConfig());
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:${server.packagedir}/**/*MapperMysql.xml"));
		
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public SqlSession sqlSessionMysql(@Autowired @Qualifier("sqlSessionFactoryMysql") SqlSessionFactory sqlSessionFactoryMysql) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactoryMysql);
	}	
	
	@Bean
	public DataSourceTransactionManager transactionManagerMysql(@Autowired @Qualifier("dataSourceMysql") DataSource dataSourceMysql) {
		return new DataSourceTransactionManager(dataSourceMysql);
	}
	
	
}