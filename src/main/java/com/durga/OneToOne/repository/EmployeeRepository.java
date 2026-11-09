package com.durga.OneToOne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.durga.OneToOne.entity.Employee;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	/**
	 * Checks whether an employee has the supplied email.
	 *
	 * @param email email to look up
	 * @return true if a matching employee exists, otherwise false
	 */
	boolean existsByEmail(String email);

}
