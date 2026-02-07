package com.ems.exception;

public class AuthorizationException extends Exception{
	/**
	 * Thrown when a user lacks permission to perform an action
	 */
	private static final long serialVersionUID = 1L;

	public AuthorizationException(String s){
		super(s);
	}
}
