package com.gdut.dongjun.exception;

public class DaoException extends BaseException {

	private static final long serialVersionUID = -8120911161526165185L;

	public DaoException(String errorMsg) {
		super(errorMsg);
	}

	public DaoException(int errorCode, String errorMsg) {
		super(errorCode, errorMsg);
	}

	public DaoException(Throwable cause, int errorCode, String errorMsg, String traceId) {
		super(cause, errorCode, errorMsg, traceId);
	}

	public DaoException(Throwable cause, int errorCode, String errorMsg) {
		super(cause, errorCode, errorMsg);
	}

	public DaoException(Throwable cause, String errorMsg) {
		super(cause, errorMsg);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

}
