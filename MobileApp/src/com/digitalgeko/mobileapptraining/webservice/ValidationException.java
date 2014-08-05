package com.digitalgeko.mobileapptraining.webservice;
public class ValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4938295506904957841L;

	public ValidationException() {
		super();
	}

	public ValidationException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ValidationException(String detailMessage) {
		super(detailMessage);
	}

	public ValidationException(Throwable throwable) {
		super(throwable);
	}

	
}
