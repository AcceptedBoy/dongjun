package com.gdut.dongjun;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.sql.DataSource;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm.SaltStyle;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.gdut.dongjun.service.common.CommonSwitch;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import net.sf.ehcache.CacheManager;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
@ImportResource("classpath:website-service.xml")
public class Application extends SpringBootServletInitializer {

	@Bean
	public CommonSwitch projectConstant() {
		return new CommonSwitch();
	}
	
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
		ds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/elecon_platform?useUnicode=true&amp;charaterEncoding=utf-8&zeroDateTimeBehavior=convertToNull");
		ds.setUser("root");
		ds.setPassword("759486");//elecon
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
			resources = new PathMatchingResourcePatternResolver()
					.getResources(packageSearchPath);
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
	 * 					事务管理配置
	 * -------------------------------------------------------
	 */
	public DataSourceTransactionManager txManager() {
		DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(dataSource());
		return txManager;
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
	
	/*--------------------------------------------------------
	 * 					shiro 配置
	 * -------------------------------------------------------
	 */

	/**
	 * 启用shiro注解
	 */
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {

		DefaultAdvisorAutoProxyCreator shiroAutoProxyCreator =
				new DefaultAdvisorAutoProxyCreator();
		shiroAutoProxyCreator.setProxyTargetClass(true);
		return shiroAutoProxyCreator;
	}

	/**
	 * 
	 */
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {

		return new LifecycleBeanPostProcessor();
	}

	/**
	 * shiro认证通知器
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor AuthorizationAttributeSourceAdvisor() {

		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(defaultWebSecurityManager());
		return advisor;
	}

	/**
	 * shiro filter
	 */	
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean ShiroFilterFactoryBean() {

		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(defaultWebSecurityManager());
		factoryBean.setFilterChainDefinitionMap(createFilterChainMap());
		factoryBean.setUnauthorizedUrl("/dongjun/user/unauthorized");
		factoryBean.setLoginUrl("/dongjun/login");
		return factoryBean;
	}
	
	/**
	 * <p>*：匹配零个或多个字符串
	 * <p>**：匹配路径中的零个或多个路径
	 * <p>url模式匹配顺序是按照在配置中的声明顺序匹配，即从头开始使用第一个匹配的url模式对应的拦截器链
	 */
	private Map<String, String> createFilterChainMap() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("/dongjun/elecon/*", "anon");
		map.put("/dongjun/admin/**", "roles[super_admin]");
		map.put("/dongjun/**", "authc");
		return map;
	}

	/**
	 * DefaultWebSecurityManager配置
	 */
	@Bean
	public DefaultWebSecurityManager defaultWebSecurityManager() {

		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		manager.setRealm(jdbcAuthenticationRealm());
		return manager;
	}

	/**
	 * shiro认证池
	 */
	@Bean
	public JdbcRealm jdbcAuthenticationRealm() {

		JdbcRealm realm = new JdbcRealm();

		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		SimpleCredentialsMatcher matcher = new SimpleCredentialsMatcher();
		
		credentialsMatcher.setHashAlgorithmName("SHA-256");
		realm.setDataSource(dataSource());
		realm.setCredentialsMatcher(matcher);
		realm.setAuthenticationCacheName("shiro.authorizationCache");
		realm.setAuthenticationQuery("select password from user where name = ?");
		realm.setSaltStyle(SaltStyle.NO_SALT);
		
		/**
		 * 查询用户的角色时只能通过用户的名字查，查询用户的权限时只能通过用户的角色名查
		 */
		realm.setUserRolesQuery("select role from role where id in " +
				"(select role_id from user_role where user_id in " +
				"(select id from user where name = ?))");
		realm.setPermissionsQuery("select permission from permission where id in " +
				"(select permission_id from role_permission where role_id in " +
				"(select id from role where role = ?))");
		realm.setPermissionsLookupEnabled(true);
		return realm;
	}

	/**
	 * {@code @Autowired
     * private Validator validator;}
	 */
	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}
	
	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	//注册Servlet，同样的Bean有FilterRegistrationBean和ServletListenerRegistrationBean
	@Bean//fix bug: 上次的名字是dispatcherServlet，搞得那些请求都没办法处理了
	public ServletRegistrationBean cxfServlet() {
		return new ServletRegistrationBean(new CXFServlet(), "/dongjun-website/ws/*");
	}
	
	@Bean
	public CacheManager cacheManager() {
		return new CacheManager();
	}
}
