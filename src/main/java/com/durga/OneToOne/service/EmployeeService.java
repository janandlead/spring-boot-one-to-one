package com.durga.OneToOne.service;

import java.util.List;
import java.util.Optional;

import com.durga.OneToOne.dto.EmployeeProfileDto;
import com.durga.OneToOne.dto.EmployeeRequestDto;
import com.durga.OneToOne.dto.EmployeeResponseDto;

public interface EmployeeService {
	
	
	/**
	 * Creates an employee and its optional profile.
	 *
	 * @param employeeRequestDto employee details and optional profile
	 * @return saved employee with generated IDs and profile details
	 * @throws com.durga.OneToOne.exception.DuplicateEmployeeException if the email is already registered
	 */
	EmployeeResponseDto create(EmployeeRequestDto employeeRequestDto);
	
	/**
	 * Retrieves an employee with its optional profile.
	 *
	 * @param id employee ID
	 * @return a populated optional; a missing employee raises an exception
	 * @throws com.durga.OneToOne.exception.EmployeeNotFoundException if the employee does not exist
	 */
	Optional<EmployeeResponseDto> findById(Long id);
	
	/**
	 * Retrieves all employees and their optional profiles.
	 *
	 * @return employee responses, or an empty list
	 */
	List<EmployeeResponseDto> findAll();
	
	/**
	 * Deletes an employee and cascades deletion to its profile.
	 *
	 * @param id employee ID
	 * @throws com.durga.OneToOne.exception.EmployeeNotFoundException if the employee does not exist
	 */
	void delete(Long id);
	
	/**
	 * Replaces all profile fields, creating a profile if none exists.
	 * Null field values clear the corresponding stored values.
	 *
	 * @param id employee ID
	 * @param employeeProfileDto replacement profile details
	 * @return employee with its saved profile
	 * @throws com.durga.OneToOne.exception.EmployeeNotFoundException if the employee does not exist
	 */
	EmployeeResponseDto updateProfile(Long id, EmployeeProfileDto employeeProfileDto);
	
	/**
	 * Removes the profile while retaining the employee.
	 * Does nothing if the employee already has no profile.
	 *
	 * @param id employee ID
	 * @throws com.durga.OneToOne.exception.EmployeeNotFoundException if the employee does not exist
	 */
	void deleteProfile(Long id);

}
