package com.durga.OneToOne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.durga.OneToOne.entity.EmployeeProfile;
@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

}
