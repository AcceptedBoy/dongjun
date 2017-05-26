package com.gdut.dongjun;

import java.beans.PropertyVetoException;
import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
@ImportResource("classpath:website-service.xml")
public class Application extends SpringBootServletInitializer {

	/*--------------------------------------------------------
	 * 					数据库配置
	 * -------------------------------------------------------
	 */

	/**
	 * 数据库c3p0连接池
	 */
	@Bean
	public DataSource dataSource() {

		ComboPooledDataSource ds = new ComboPooledDataSource();
		ds.setJdbcUrl(
				"jdbc:mysql://127.0.0.1:3306/elecon_platform?useUnicode=true&amp;charaterEncoding=utf-8&zeroDateTimeBehavior=convertToNull");
		ds.setUser("root");
		ds.setPassword("759486");// elecon
		try {
			ds.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		ds.setAcquireIncrement(5);
		ds.setInitialPoolSize(10);
		ds.setMinPoolSize(5);
		ds.setMaxPoolSize(60);
		ds.setMaxIdleTime(120);
		return ds;
	}

	/**
	 * SqlSessionFactoryBean管理mapper文件
	 */
	@Bean
	public SqlSessionFactoryBean sessionFactory() {

		SqlSessionFactoryBean sFactoryBean = new SqlSessionFactoryBean();
		sFactoryBean.setDataSource(dataSource());

		String packageSearchPath = "classpath*:com/gdut/dongjun/domain/dao/**/*.xml";
		Resource[] resources = null;
		try {
			resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sFactoryBean.setMapperLocations(resources);
		sFactoryBean.setTypeAliasesPackage("com.gdut.dongjun.domain.po");

		return sFactoryBean;
	}

	/**
	 * 使用SqlSessionTemplate，dao层用该对象进行数据库操作
	 */
	@Bean(name = "msg_sqlSessionTemplate")
	public SqlSessionTemplate sessionTemplate() {

		SqlSessionTemplate sst = null;
		try {
			sst = new SqlSessionTemplate(sessionFactory().getObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sst;
	}
	
	/*--------------------------------------------------------
	 * 					编码过滤器 配置
	 * -------------------------------------------------------
	 */

	/**
	 * 配置UTF-8过滤器
	 */
	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {

		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		return filter;
	}

	/**
	 * 过滤器进行编码配置
	 */
	@Bean
	public FilterRegistrationBean registerCharacterEncodingFilter() {

		FilterRegistrationBean chafil = new FilterRegistrationBean();
		chafil.setFilter(characterEncodingFilter());
		chafil.setDispatcherTypes(DispatcherType.ASYNC);
		chafil.addUrlPatterns("/*");
		return chafil;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
