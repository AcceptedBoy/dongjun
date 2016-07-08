package com.gdut.dongjun.annotation;

import org.hibernate.validator.constraints.Length;

public class TestFor {

	@Length(max = 5, message = "heh")
	private String age;
	
	public TestFor(String age) {
		this.age = age;
	}
	
}
