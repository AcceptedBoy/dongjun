package com.gdut.dongjun.exception;

public class ValidatorException extends ServiceException {

	private static final long serialVersionUID = 1L;
	
	public ValidatorException(String errorMsg) {
		super(errorMsg);
	}

	public ValidatorException(Throwable cause, int errorCode, String errorMsg, String traceId) {
		super(cause, errorCode, errorMsg, traceId);
	}

	public ValidatorException(Throwable cause, int errorCode, String errorMsg) {
		super(cause, errorCode, errorMsg);
	}

	public ValidatorException(Throwable cause, String errorMsg) {
		super(cause, errorMsg);
	}

	public ValidatorException(Throwable cause) {
		super(cause);
	}

	public ValidatorException(int errorCode, String errorMsg) {
		super(errorCode, errorMsg);
	}

}
