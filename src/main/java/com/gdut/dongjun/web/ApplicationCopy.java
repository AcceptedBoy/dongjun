package com.gdut.dongjun.web;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.DispatcherType;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;

/*@SpringBootApplication
@EnableAspectJAutoProxy*/
public class ApplicationCopy /*extends SpringBootServletInitializer*/ {



	@Bean
	public DataSource dataSource() {

		ComboPooledDataSource ds = new ComboPooledDataSource();
		ds.setJdbcUrl("jdbc:mysql://localhost:3306/elecon_service?useUnicode=true&amp;charaterEncoding=utf-8&" +
				"zeroDateTimeBehavior=convertToNull");
		ds.setUser("root");
		ds.setPassword("root");//elecon
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

	@Bean
	public SqlSessionFactoryBean sessionFactory() {

		SqlSessionFactoryBean sFactoryBean = new SqlSessionFactoryBean();
		sFactoryBean.setDataSource(dataSource());

		String packageSearchPath = "classpath*:com/gdut/dongjun/domain/dao/**/*.xml";
		Resource[] resources = null;
		try {
			resources = new PathMatchingResourcePatternResolver()
					.getResources(packageSearchPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sFactoryBean.setMapperLocations(resources);
		sFactoryBean.setTypeAliasesPackage("com.gdut.dongjun.domain.po");

		return sFactoryBean;
	}

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

	public DataSourceTransactionManager txManager() {
		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(dataSource());
		return txManager;
	}

	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {

		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		return filter;
	}

	@Bean
	public FilterRegistrationBean registerCharacterEncodingFilter() {

		FilterRegistrationBean chafil = new FilterRegistrationBean();
		chafil.setFilter(characterEncodingFilter());
		chafil.setDispatcherTypes(DispatcherType.ASYNC);
		chafil.addUrlPatterns("/*");
		return chafil;
	}
	
	/*--------------------------------------------------------
	 * 					cxf 配置
	 * -------------------------------------------------------
	 */
	public ServletRegistrationBean cxfServlet() {
		return new ServletRegistrationBean(new CXFServlet(), "/dongjun_server/ws/*");
	}

	@Bean(name = Bus.DEFAULT_BUS_ID)
	public SpringBus springBus() {
		return new SpringBus();
	}

	/*@Bean
	public CommonService commonService() {
		return new CommonServiceImpl();
	}

	@Bean
	public Endpoint endpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), commonService());
		endpoint.publish("/common");
		endpoint.setWsdlLocation("common.wsdl");
		return endpoint;
	}*/

	/*public static void main(String[] args) throws Exception {
		SpringApplication.run(ApplicationCopy.class, args);
	}*/
}
