package com.gdut.dongjun.web.intercept;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdut.dongjun.domain.model.ErrorInfo;
import com.gdut.dongjun.domain.model.ResponseMessage;
import com.gdut.dongjun.domain.model.ResponseMessage.Type;
import com.gdut.dongjun.service.common.CommonSwitch;
import com.gdut.dongjun.util.DefaultValidUtil;

/**
 * 通过controller切面织入，使发生异常的时候直接返回json格式数据
 * @author link xiaoMian <972192420@qq.com>
 *
 * 如果是上线的版本，则会直接返回500字符串，TODO 讲道理跳到500页面才对，也没有这种界面
 *
 * <p>如果是开发的版本，则会返回一些信息在界面上，主要是在开发时期或者维护时期看的
 */
@ControllerAdvice
public class HandleExceptionController {

	@Autowired
	private CommonSwitch commonSwitch;

	/**
	 * 当验证权限失败的时候返回json格式
	 */
//	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseBody Object handleAnthorized(HttpServletRequest request,
			UnauthorizedException ex) {

		if(commonSwitch.isProduction()) {
			return new ResponseEntity<String>("500", HttpStatus.valueOf(500));
		}
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
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody ResponseEntity<?> handleMissingParameter(HttpServletRequest request,
			MissingServletRequestParameterException ex) {

		if(commonSwitch.isProduction()) {
			return new ResponseEntity<String>("500", HttpStatus.valueOf(500));
		}

	    return new ResponseEntity<>(ResponseMessage.addException(
	    		new ErrorInfo(request.getRequestURL().toString(), 
	    						overrideExceptionToString(ex.toString()), 
	    						HttpStatus.BAD_REQUEST.value(), 
	    						ex.getMessage())), 
	    		HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * 当字段验证不合格抛出异常返回
	 */
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	@ResponseBody ResponseEntity<?> handleValidator(HttpServletRequest request,
			BindException ex) {

		if(commonSwitch.isProduction()) {
			return new ResponseEntity<String>("500", HttpStatus.valueOf(500));
		}

		return new ResponseEntity<>(new ResponseMessage(
										Type.WARNING, 
										DefaultValidUtil.getValidationMessage(ex.getAllErrors()), 
										false,
										new ErrorInfo(
												request.getRequestURL().toString(), 
												overrideExceptionToString(ex.toString()), 
												HttpStatus.BAD_REQUEST.value(), 
												ex.getMessage()
										)),
								HttpStatus.BAD_REQUEST);

	}
	
	/**
	 * 所有未处理异常的捕捉类，TODO
	 * 以后会转到系统异常页面
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody ResponseEntity<?> handleException(HttpServletRequest request,
			Exception ex) {

		if(commonSwitch.isProduction()) {
			return new ResponseEntity<String>("500", HttpStatus.valueOf(500));
		}

		return new ResponseEntity<>(new ResponseMessage(
										Type.WARNING, 
										DefaultValidUtil.getValidationMessage(ex.toString()), 
										false,
										new ErrorInfo(
												request.getRequestURL().toString(), 
												overrideExceptionToString(ex.toString()), 
												HttpStatus.INTERNAL_SERVER_ERROR.value(), 
												ex.getMessage()
										)),
								HttpStatus.INTERNAL_SERVER_ERROR);

	}
		
	
	private String overrideExceptionToString(String toString) {
		String ex = null;
		try {
			/*
			 * 防止{@code NullPointerException}这类异常的toString没有”:”
			 */
			ex = toString.substring(0, toString.indexOf(":"));			
		} catch (Exception e) {
			return toString;
		}
		return ex;
	}
}
