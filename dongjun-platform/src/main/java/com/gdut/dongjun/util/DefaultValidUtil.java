package com.gdut.dongjun.util;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.springframework.validation.ObjectError;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.model.ResponseMessage.Type;

/**
 * @author AcceptedBoy
 *
 */
public class DefaultValidUtil extends ValidatorUtil {
	
	public DefaultValidUtil(){}
	
	public List<String> responseValidationMessage(List<ObjectError> objErrors) {
		return getValidationMessage(objErrors);
	}

	/**
	 * 设计不合理，需要更加适当的方法实现
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public Object responseValidationMessage(Object... objs) {
		LinkedList<String> text = (LinkedList<String>) getValidationMessage(objs);
		if(text.size() == 0) {
			return null;
		}
		return new ResponseMessage(Type.WARNING, getValidationMessage(objs), false, ListUtils.EMPTY_LIST);
	}

	/**
	 * 设计不合理
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public Object responseValidationMessage(Object obj) {
		LinkedList<String> text = (LinkedList<String>) getValidationMessage(obj);
		if(text.size() == 0) {
			return null;
		}
		return new ResponseMessage(Type.WARNING, getValidationMessage(obj), false, ListUtils.EMPTY_LIST);
	}
}
