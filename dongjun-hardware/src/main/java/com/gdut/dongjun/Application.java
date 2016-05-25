package com.gdut.dongjun;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.rmi.RemoteException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.gdut.dongjun.service.rmi.HardwareService;
import com.gdut.dongjun.service.rmi.impl.HardwareServiceImpl;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Application extends SpringBootServletInitializer {
	
	@Autowired
    private ApplicationContext applicationContext;
	
	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	
	/**
	 * 
	 * @Title: characterEncodingFilter
	 * @Description: TODO
	 * @param @return
	 * @return CharacterEncodingFilter
	 * @throws
	 */
	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {

		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		return filter;
	}

	/**
	 * 
	 * @Title: registerCharacterEncodingFilter
	 * @Description: TODO
	 * @param @return
	 * @return FilterRegistrationBean
	 * @throws
	 */
	@Bean
	public FilterRegistrationBean registerCharacterEncodingFilter() {

		FilterRegistrationBean chafil = new FilterRegistrationBean();
		chafil.setFilter(characterEncodingFilter());
		chafil.addUrlPatterns("/*");
		return chafil;
	}
	
	@Bean
	public DataSource dataSource() {

		com.mchange.v2.c3p0.ComboPooledDataSource ds = new ComboPooledDataSource();
		ds.setJdbcUrl("jdbc:mysql://localhost:3306/elecon?useUnicode=true&amp;charaterEncoding=utf-8&" +
				"zeroDateTimeBehavior=convertToNull");
		ds.setUser("root");
		ds.setPassword("root");//elecon
		try {
			ds.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
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
	 * 
	 * @Title: sessionFactory
	 * @Description: TODO
	 * @param @return
	 * @return SqlSessionFactoryBean
	 * @throws
	 */
	@Bean
	public SqlSessionFactoryBean sessionFactory() {

		SqlSessionFactoryBean sFactoryBean = new SqlSessionFactoryBean();
		sFactoryBean.setDataSource(dataSource());

		String packageSearchPath = "classpath*:com/gdut/dongjun/domain/dao/*.xml";
		Resource[] resources = null;
		try {
			resources = new PathMatchingResourcePatternResolver()
					.getResources(packageSearchPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sFactoryBean.setMapperLocations(resources);
		sFactoryBean.setTypeAliasesPackage("com.gdut.dongjun.domain.po");

		return sFactoryBean;
	}

	/**
	 * 
	 * @Title: sessionTemplate
	 * @Description: TODO
	 * @param @return
	 * @return SqlSessionTemplate
	 * @throws
	 */
	@Bean(name = "msg_sqlSessionTemplate")
	public SqlSessionTemplate sessionTemplate() {

		SqlSessionTemplate sst = null;
		try {
			sst = new SqlSessionTemplate(sessionFactory().getObject());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sst;
	}
	
    // Configure the embedded tomcat to use same settings as default standalone tomcat deploy
    @Bean
    public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
        // Made to match the context path when deploying to standalone tomcat- can easily be kept in sync w/ properties
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory("/ws-server-1.0", 8080);
        return factory;
    }
    
    /**
     * rmi 配置
     * @throws RemoteException 
     */
    @Bean
    public HardwareService hardwareService() throws RemoteException {
    	return new HardwareServiceImpl();
    }
    
    @Bean
    public RmiServiceExporter rmiServiceExporter() throws RemoteException {
    	RmiServiceExporter exporter = new RmiServiceExporter();
    	exporter.setServiceName("HardwareService");
    	exporter.setRegistryPort(9998);
    	exporter.setServicePort(9999);
    	exporter.setServiceInterface(HardwareService.class);
    	exporter.setService(hardwareService());
    	return exporter;
    }

	public static void main(String[] args) throws Exception {

		SpringApplication.run(Application.class, args);
	}

}
