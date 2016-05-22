package com.gdut.dongjun.web.intercept;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gdut.dongjun.domain.model.ErrorInfo;
import com.gdut.dongjun.domain.model.ResponseMessage;

@ControllerAdvice
public class HandleExceptionController {

	/**
	 * 当验证权限失败的时候返回json格式
	 */
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseBody ResponseEntity<ResponseMessage> handleAnthorized(HttpServletRequest request, 
			UnauthorizedException ex) {
			
	    return new ResponseEntity<>(ResponseMessage.addException(
	    		new ErrorInfo(request.getRequestURL().toString(), 
	    						overrideExceptionToString(ex.toString()), 
	    						HttpStatus.UNAUTHORIZED.value(), 
	    						ex.getMessage())), 
	    		HttpStatus.UNAUTHORIZED);
	}
	
	/**
	 * 当请求缺少参数时返回json数据
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody ResponseEntity<ResponseMessage> handleBadRequest(HttpServletRequest request, 
			MissingServletRequestParameterException ex) {
		
	    return new ResponseEntity<>(ResponseMessage.addException(
	    		new ErrorInfo(request.getRequestURL().toString(), 
	    						overrideExceptionToString(ex.toString()), 
	    						HttpStatus.BAD_REQUEST.value(), 
	    						ex.getMessage())), 
	    		HttpStatus.BAD_REQUEST);
	}
	
	private String overrideExceptionToString(String toString) {
		return toString.substring(0, toString.indexOf(":"));
	}
}
