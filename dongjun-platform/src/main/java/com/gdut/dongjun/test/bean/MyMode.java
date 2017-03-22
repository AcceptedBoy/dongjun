package com.gdut.dongjun.test.bean;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class MyMode implements IMode, InitializingBean, DisposableBean {

	private static AtomicInteger i = new AtomicInteger(0);

	private int value;

	public MyMode() {
		System.out.println("MyMode constructor call");
		this.value = i.incrementAndGet();
	}

	@Override
	public void print() {
		System.out.println("MyMode print called");
		System.out.println(value);
	}

	public void destroy() throws Exception {
		System.out.println("Mode destroy called");
	}

	public void afterPropertiesSet() throws Exception {
		System.out.println("Mode afterPropertiesSet called");

	}

	public void beanInit() {
		System.out.println("Mode @Bean anno Init called");
	}

	public void beanDestory() {
		System.out.println("Mode @Bean anno destory called");
	}

	@PostConstruct
	public void annotationInit() {
		System.out.println("Mode @PostConstruct anno init called");
	}

	@PreDestroy
	public void annotationDestory() {
		System.out.println("Mode @PreDestroy anno destory called");
	}
}