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
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@SpringBootApplication
@ImportResource("classpath:cxf-service.xml")
public class SimpleBootCxfApplication {

    @Autowired
    private ApplicationContext ctx;

    public static void main(String[] args) {
        SpringApplication.run(SimpleBootCxfApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean cxfServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/dongjun-service/ws/*");
    }

    @Bean
    public DataSource dataSource() {

        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/elecon_service?useUnicode=true&amp;charaterEncoding=utf-8&" +
                "zeroDateTimeBehavior=convertToNull&amp;useSSL=true");
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

    @Bean
    public SqlSessionFactoryBean sessionFactory() {

        SqlSessionFactoryBean sFactoryBean = new SqlSessionFactoryBean();
        sFactoryBean.setDataSource(dataSource());

        String packageSearchPath = "classpath*:mybatis/**/*.xml";
        Resource[] resources = null;
        try {
            resources = new PathMatchingResourcePatternResolver()
                    .getResources(packageSearchPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sFactoryBean.setMapperLocations(resources);
        sFactoryBean.setTypeAliasesPackage("com.gdut.dongjun.po");

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
	 * session管理器
	 * @return
	 */
	@Bean
	public DefaultWebSessionManager defaultWebSessionManager() {
		DefaultWebSessionManager manager = new DefaultWebSessionManager();
		return manager;
	}
	/**
	 * <p>*：匹配零个或多个字符串
	 * <p>**：匹配路径中的零个或多个路径
	 * <p>url模式匹配顺序是按照在配置中的声明顺序匹配，即从头开始使用第一个匹配的url模式对应的拦截器链
	 */
	private Map<String, String> createFilterChainMap() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("/dongjun/elecon/*", "anon");
		map.put("/dongjun/login_form", "anon");
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
		manager.setSessionManager(defaultWebSessionManager());
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
//		realm.setPermissionsQuery("select permission from permission where id in " +
//				"(select permission_id from role_permission where role_id in " +
//				"(select id from role where role = ?))");
		realm.setPermissionsLookupEnabled(false);
		return realm;
	}
}
