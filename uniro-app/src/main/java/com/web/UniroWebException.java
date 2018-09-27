package com.web;

/**
 * @author RITESH SINGH
 * @since JDK 1.8
 * @version 1.0
 *
 */
public class UniroWebException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public int code;
	
	public UniroWebException() {
		super();
	}
	public UniroWebException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public UniroWebException(String message, Throwable cause) {
		super(message, cause);
	}
	public UniroWebException(String message) {
		super(message);
	}
	public UniroWebException(String message, int code) {
		super(message);
		this.code = code;
	}
	public UniroWebException(Throwable cause) {
		super(cause);
	}
}
