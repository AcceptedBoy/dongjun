package com.gdut.dongjun.service.manager;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 为没有注入到spring容器的类提供获取bean功能
 * @author Gordan_Deng
 * @date 2017年3月29日
 */
@Component
public class SpringApplicationContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}
	
	public static Object getBean(String name) {
		if (null == name) {
			return null;
		}
		return applicationContext.getBean(name);
	}
	
	public static <T> T getBean(Class<T> clazz) {
		if (null == clazz) {
			return null;
		}
		return applicationContext.getBean(clazz);
	}

}
