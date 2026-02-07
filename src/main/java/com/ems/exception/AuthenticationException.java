package com.ems.exception;

public class AuthenticationException extends Exception{
	/**
	 * Thrown when user authentication fails
	 */
	private static final long serialVersionUID = 1L;

	public AuthenticationException(String s){
		super(s);
	}
}
