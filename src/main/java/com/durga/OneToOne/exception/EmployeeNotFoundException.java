package com.durga.OneToOne.exception;

public class EmployeeNotFoundException extends RuntimeException {
	

	/**
	 * Creates an exception describing a missing employee.
	 *
	 * @param mesasge explanation identifying the missing employee
	 */
	public EmployeeNotFoundException(String mesasge) {
		super(mesasge);
	}
}
