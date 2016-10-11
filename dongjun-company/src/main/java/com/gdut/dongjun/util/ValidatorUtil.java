package com.gdut.dongjun.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.validation.ObjectError;


/**
 * @author AcceptedBoy
 */
/**
 * @author USER
 *
 */
public abstract class ValidatorUtil {

	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	
	public static Validator getValidator() {
		return factory.getValidator();
	}
	
	public static Set<ConstraintViolation<Object>> validate(Object obj) {
		return getValidator().validate(obj);
	}
	
	/**
	 * @return 验证结果的集合
	 */
	public static List<String> getValidationMessage(Object obj) {
		Set<ConstraintViolation<Object>> msgs = validate(obj);
		if(msgs.size() == 0) {
			return null;
		} else {
			List<String> result = new LinkedList<>();
			for(ConstraintViolation<Object> msg : msgs) {
				result.add(msg.getMessage());
			}
			return result;
		}
	}
	
	public static List<String> getValidationMessage(Object ... objs) {
		List<String> result = new LinkedList<>(); 
		for(Object obj : objs) {
			List<String> sonResult = getValidationMessage(obj);
			if(sonResult != null) {
				result.addAll(sonResult);
			}
		}
		return result;
	}
	
	public static List<String> getValidationMessage(List<ObjectError> objErrors) {
		List<String> result = new LinkedList<>(); 
		for(ObjectError error : objErrors) {
			result.add(error.getDefaultMessage());
		}
		return result;
	}
	
	public abstract Object responseValidationMessage(Object ... obj);
	
	/**
	 * 这个方法为各个系统提供特定验证返回机制
	 * @param <T>
	 */
	public abstract Object responseValidationMessage(Object obj);
}
