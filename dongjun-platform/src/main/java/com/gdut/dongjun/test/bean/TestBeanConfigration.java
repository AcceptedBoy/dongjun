package com.gdut.dongjun.test.bean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class TestBeanConfigration {

	@Bean
	public IService myService() {
		return new MyService(myMode());
	}

	@Bean(initMethod = "beanInit", destroyMethod = "beanDestory")
	@Scope(value = BeanDefinition.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public IMode myMode() {
		return new MyMode();
	}
}