package com.deploy.demo.exceptions;
/**
 * An exception indicating that the object that is required or actioned on
 * does not exist.
 * 
 **/
public class ObjectNotFoundException extends GenericException {
	
	private static final long serialVersionUID = 1L;
	
	public ObjectNotFoundException(String message) {
		super(message);
	}
	
	public ObjectNotFoundException(String message, Class<?> objectClass) {
		this(message, objectClass, null);
	}
	
	public ObjectNotFoundException(Class<?> objectClass) {
		this(null, objectClass, null);
	}
	
	public ObjectNotFoundException(String message, Class<?> objectClass, Throwable cause) {
		super(message, cause);
		this.objectClass = objectClass;
	}
}
