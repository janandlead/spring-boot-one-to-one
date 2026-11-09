package com.durga.OneToOne.exception;

public class DuplicateEmployeeException extends RuntimeException {
	
	/**
	 * Creates an exception describing a duplicate employee.
	 *
	 * @param message explanation of the conflict
	 */
	public DuplicateEmployeeException(String message) {
		super(message);
	}

}
