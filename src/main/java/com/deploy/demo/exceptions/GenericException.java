package com.deploy.demo.exceptions;

public class GenericException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	protected Class<?> objectClass;
	
	public GenericException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public GenericException(String message) {
	    super(message);
	}
	
	/**
	 * The class of the object that was not found. Contains the
	 * interface-class of the flowable-object that was not found.
	 **/
	public Class<?> getObjectClass() {
		return objectClass;
	}
}
