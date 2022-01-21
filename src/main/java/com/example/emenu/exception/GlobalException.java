package com.example.emenu.exception;

public class GlobalException extends RuntimeException{
	private static final long serialVersionUID = 1399255957635867243L;

	final String DEFAULT_ERROR_MSG = "An error has occurred";
	private String errorName;

	GlobalException(String errorName)
	{
		super();
		this.errorName = errorName;
	}

	GlobalException(String message, String errorName) {
		super(message);
		this.errorName = errorName;
	}

	public String getErrorName () {
		return this.errorName;
	}
}
