package com.durga.OneToOne.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="employee_profiles")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeProfile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="phone_number")
	private String phoneNumber;
	
	private String address;
	
	@Column(name="date_of_birth")
	private LocalDate dateOfBirth;
	
	@Column(name="emergency_contact")
	private String emergencyContact;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="employee_id", nullable = false, unique = true)
	private Employee employee;

}

