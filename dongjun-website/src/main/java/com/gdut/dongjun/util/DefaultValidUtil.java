package com.gdut.dongjun.util;

import java.util.LinkedList;

import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.model.ResponseMessage.Type;

/**
 * @author AcceptedBoy
 *
 */
public class DefaultValidUtil extends ValidatorUtil {
	
	public DefaultValidUtil(){}

	@Override
	public Object responseValidationMessage(Object... objs) {
		LinkedList<String> text = (LinkedList<String>) getValidationMessage(objs);
		if(text.size() == 0) {
			return null;
		}
		return new ResponseMessage(Type.WARNING, getValidationMessage(objs), false, null);
	}

	@Override
	public Object responseValidationMessage(Object obj) {
		LinkedList<String> text = (LinkedList<String>) getValidationMessage(obj);
		if(text.size() == 0) {
			return null;
		}
		return new ResponseMessage(Type.WARNING, getValidationMessage(obj), false, null);
	}
}
