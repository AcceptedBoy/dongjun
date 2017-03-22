package com.gdut.dongjun.test.processor;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;

import com.gdut.dongjun.test.bean.MyMode;

@Component
public class TestInstantiationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		if (beanClass.isAssignableFrom(MyMode.class)) {
			System.out.println("InstantiationBeanPostProcessor postProcessBeforeInstantiation called");
		}
		return null;
	}

	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		if (bean instanceof MyMode) {
			System.out.println("InstantiationBeanPostProcessor postProcessAfterInstantiation called");
		}
		return true;
	}

	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean,
			String beanName) throws BeansException {
		if (bean instanceof MyMode) {
			System.out.println("InstantiationBeanPostProcessor postProcessPropertyValues called");
		}
		return pvs;
	}

}
