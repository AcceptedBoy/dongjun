package com.gdut.dongjun.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

public class SpringInitCase implements BeanNameAware, BeanClassLoaderAware, BeanFactoryAware, InitializingBean {

	@Override
	public void setBeanName(String name) {
		
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
}
