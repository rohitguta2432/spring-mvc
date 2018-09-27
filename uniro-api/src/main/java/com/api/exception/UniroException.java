package com.api.exception;

/**
 * @author RITESH SINGH
 * @since JDK 1.8
 * @version 1.0
 *
 */
public class UniroException extends RuntimeException {

	private static final long serialVersionUID = 2771174581631905388L;

	public UniroException() {
	}

	public UniroException(String message) {
		super(message);
	}

	public UniroException(Throwable cause) {
		super(cause);
	}

	public UniroException(String message, Throwable cause) {
		super(message, cause);
	}

	public UniroException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
