package com.ems.exception;

public class InvalidPasswordFormatException extends Exception {
	/**
	 * Thrown when password does not meet required format or policy
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPasswordFormatException(String s){
		super(s);
	}
}
