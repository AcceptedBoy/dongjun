package com.gdut.dongjun.util;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
public class SpringBeanUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	@Autowired
	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		applicationContext = ctx;
	}
	
	/**
	 * 得到当前项目的ApplicationContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	/**
	 * 按照名字，得到当前容器下的bean
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
	
	/**
	 * 返回ServletContext
	 * @return
	 */
	public static ServletContext getServletContext() {
		return ((WebApplicationContext)applicationContext).getServletContext();
	}
	

}
