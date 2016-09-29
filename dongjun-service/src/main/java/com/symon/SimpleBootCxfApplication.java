package com.symon;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.DispatcherType;
import javax.sql.DataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;

@SpringBootApplication
@ImportResource("classpath:cxf-service.xml")
public class SimpleBootCxfApplication {

    @Autowired
    private ApplicationContext ctx;

    public static void main(String[] args) {
        SpringApplication.run(SimpleBootCxfApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean dispatcherServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/dongjun_service/ws/*");
    }

    /*// Configure the embedded tomcat to use same settings as default standalone tomcat deploy
    @Bean
    public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
        // Made to match the context path when deploying to standalone tomcat- can easily be kept in sync w/ properties
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory("/ws-server-1.0", 8080);
        return factory;
    }*/

   /* private void setProviders(JAXRSServerFactoryBean factoryBean) {
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
        BinaryDataProvider<InputStream> binaryDataProvider = new BinaryDataProvider<>();
        factoryBean.setProviders(Arrays.asList(jsonProvider, binaryDataProvider));
    }

    @Bean
    public Server jaxRsServer() {
        List<ResourceProvider> resourceProviders = new LinkedList<>();
        for (String beanName : ctx.getBeanDefinitionNames()) {
            if (ctx.findAnnotationOnBean(beanName, Path.class) != null) {
                SpringResourceFactory factory = new SpringResourceFactory(beanName);
                factory.setApplicationContext(ctx);
                resourceProviders.add(factory);
            }
        }
        if (resourceProviders.size() > 0) {
            JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
            factoryBean.setBus(ctx.getBean(SpringBus.class));
            factoryBean.setResourceProviders(resourceProviders);
            setProviders(factoryBean);
            return factoryBean.create();
        } else {
            return null;
        }
    }*/

    /*@Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
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

        String packageSearchPath = "classpath*:mybatis/**/*.xml";
        Resource[] resources = null;
        try {
            resources = new PathMatchingResourcePatternResolver()
                    .getResources(packageSearchPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sFactoryBean.setMapperLocations(resources);
        sFactoryBean.setTypeAliasesPackage("com.symon.po");

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
}
