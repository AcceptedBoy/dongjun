package com.gdut.dongjun;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Application {

	public ServletRegistrationBean cxfServlet() {
		return new ServletRegistrationBean(new CXFServlet(), "/dongjun_service/ws/*");
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

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
