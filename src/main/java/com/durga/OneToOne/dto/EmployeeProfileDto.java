package com.durga.OneToOne.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeProfileDto {


	@JsonAlias("phoneNumebr")
	private String phoneNumber;
	
	private String address;
	
	@JsonAlias("dataOfBirth")
	private LocalDate dateOfBirth;
	

	private String emergencyContact;
}

