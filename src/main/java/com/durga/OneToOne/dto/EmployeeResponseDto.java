package com.durga.OneToOne.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeResponseDto {
	
	
	private Long id;
	private String name;
	private String email;
	private String department;
	private EmployeeProfileResponse employeeProfileResponse;
	

}
