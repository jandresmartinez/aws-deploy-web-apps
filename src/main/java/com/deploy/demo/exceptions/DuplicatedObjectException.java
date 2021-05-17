package com.deploy.demo.exceptions;
/**
 * An exception indicating that the object that is required or actioned on
 * does not exist.
 * 
 **/
public class DuplicatedObjectException extends GenericException {

	private static final long serialVersionUID = 1L;

	public DuplicatedObjectException(String message) {
		super(message);
	}

	public DuplicatedObjectException(String message, Class<?> objectClass) {
		this(message, objectClass, null);
	}

	public DuplicatedObjectException(Class<?> objectClass) {
		this(null, objectClass, null);
	}

	public DuplicatedObjectException(String message, Class<?> objectClass, Throwable cause) {
		super(message, cause);
		this.objectClass = objectClass;
	}
}
