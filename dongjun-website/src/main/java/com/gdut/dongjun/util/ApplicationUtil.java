package com.gdut.dongjun.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationUtil implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;
	/**
	 * 实现该接口的setApplicationContext(ApplicationContext context)方法，
	 * 并保存ApplicationContext 对象。Spring初始化时，
	 * 会通过该方法将ApplicationContext对象注入。
	 * 
	 * 如果只是在类中使用autowired对使用对象进行注入，这一步骤在spring的执行过程中
	 * 完成bean初始化后还没有执行，则得出的值为null
	 */
	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext; 
	}
	
	public static ApplicationContext getApplicationContext() {  
        return applicationContext;  
    } 
	
	public static Object getBean(String name) throws BeansException {  
        return applicationContext.getBean(name);  
    }
}
