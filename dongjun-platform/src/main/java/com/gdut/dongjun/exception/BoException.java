package com.gdut.dongjun.exception;

public class BoException extends BaseException {

	private static final long serialVersionUID = 3992871629378687900L;

	public BoException(int errorCode, String errorMsg) {
		super(errorCode, errorMsg);
	}

	public BoException(String errorMsg) {
		super(errorMsg);
	}

	public BoException(Throwable cause, int errorCode, String errorMsg, String traceId) {
		super(cause, errorCode, errorMsg, traceId);
	}

	public BoException(Throwable cause, int errorCode, String errorMsg) {
		super(cause, errorCode, errorMsg);
	}

	public BoException(Throwable cause, String errorMsg) {
		super(cause, errorMsg);
	}

	public BoException(Throwable cause) {
		super(cause);
	}
}
