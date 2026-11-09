package com.durga.OneToOne.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.durga.OneToOne.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Converts a duplicate employee failure into an HTTP 409 response.
	 *
	 * @param duplicateEmployeeException exception containing conflict details
	 * @param httpServletRequest request supplying the failing URI
	 * @return conflict response with timestamp, status, message, and request path
	 */
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleDuplicateEmployeeException(
			DuplicateEmployeeException duplicateEmployeeException, HttpServletRequest httpServletRequest) {

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setStatus(HttpStatus.CONFLICT.value());
		errorResponse.setError(HttpStatus.CONFLICT.getReasonPhrase());
		errorResponse.setMessage(duplicateEmployeeException.getMessage());
		errorResponse.setPath(httpServletRequest.getRequestURI());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}
	

	/**
	 * Converts a missing employee failure into an HTTP 404 response.
	 *
	 * @param employeeNotFoundException exception identifying the missing employee
	 * @param httpServletRequest request supplying the failing URI
	 * @return not-found response with timestamp, status, message, and request path
	 */
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(
			EmployeeNotFoundException employeeNotFoundException, HttpServletRequest httpServletRequest) {

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
		errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
		errorResponse.setMessage(employeeNotFoundException.getMessage());
		errorResponse.setPath(httpServletRequest.getRequestURI());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

}
