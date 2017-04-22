package com.gdut.dongjun.exception;

public class ServiceException extends BaseException {

	private static final long serialVersionUID = -2909470567715968962L;

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String errorMsg) {
		super(errorMsg);
	}

	public ServiceException(Throwable cause, String errorMsg) {
		super(cause, errorMsg);
	}

	public ServiceException(int errorCode, String errorMsg) {
		super(errorCode, errorMsg);
	}

	public ServiceException(Throwable cause, int errorCode, String errorMsg) {
		super(cause, errorCode, errorMsg);
	}

	public ServiceException(Throwable cause, int errorCode, String errorMsg, String traceId) {
		super(cause, errorCode, errorMsg, traceId);
	}
	
	

}
