package com.durga.OneToOne.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="employees")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique=true)
	private String email;
	
	private String department;
	
	@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval =true,fetch = FetchType.LAZY )
	private EmployeeProfile employeeProfile;

	/**
	 * Detaches the old profile and synchronizes both sides of the relationship.
	 * Removing a managed employee's profile allows JPA orphan removal at flush.
	 *
	 * @param profile profile to associate, or null to remove the association
	 */
	public void setEmployeeProfile(EmployeeProfile profile) {
		if (this.employeeProfile != null) {
			this.employeeProfile.setEmployee(null);
		}
		this.employeeProfile = profile;
		if (profile != null) {
			profile.setEmployee(this);
		}
	}

}
