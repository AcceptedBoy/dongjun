package com.gdut.dongjun.annotation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Test;

public class RR {

	@Test
	public void testOne() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		TestFor t = new TestFor("111111111111111");
		Set<ConstraintViolation<TestFor>> tet = validator.validate(t);
		for(ConstraintViolation<TestFor> tt : tet) {
			System.out.println(tt.getMessage());
		}
	}
}
