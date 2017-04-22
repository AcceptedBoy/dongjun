package com.gdut.dongjun.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 这个类是为了多例的类也能顺利注入spring管理的bean
 * @author Gordan_Deng
 * @date 2017年3月8日
 */
@Component
public class SpringApplicationContextHolder implements ApplicationContextAware {
	
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringApplicationContextHolder.context = context;
	}

	public static Object getSpringBean(String beanName) {
		if (null == beanName) {
			return null;
		}
		return context == null ? null : context.getBean(beanName);
	}

	public static String[] getBeanDefinitionNames() {
		return context.getBeanDefinitionNames();
	}
}
