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

public class EmployeeProfileResponse {

	private Long id;
	private String phoneNumber;
	private String address;
	private LocalDate dateOfBirth;
	private String emergencyContact;
	
}
