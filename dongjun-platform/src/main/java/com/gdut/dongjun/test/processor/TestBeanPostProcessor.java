package com.gdut.dongjun.test.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.test.bean.MyMode;

@Component
public class TestBeanPostProcessor implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof MyMode) {
			System.out.println("BeanPostProcessor postProcessBeforeInitialization called");
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof MyMode) {
			System.out.println("BeanPostProcessor postProcessAfterInitialization called");
		}
		return bean;
	}
}
