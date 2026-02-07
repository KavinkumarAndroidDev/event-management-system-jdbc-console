package com.ems.exception;

public class DataAccessException extends Exception{
	/**
	 * Wraps database and persistence layer errors
	 */
	private static final long serialVersionUID = 1L;
	
	public DataAccessException(String s){
		super(s);
	}
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
