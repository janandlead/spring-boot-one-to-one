package com.durga.OneToOne;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.durga.OneToOne.dto.*;
import com.durga.OneToOne.exception.*;
import com.durga.OneToOne.repository.*;
import com.durga.OneToOne.service.EmployeeService;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:employees;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa", "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop", "spring.sql.init.mode=never",
    "spring.jpa.open-in-view=false"
})
class OneToOneApplicationTests {
    @Autowired EmployeeService service;
    @Autowired EmployeeRepository employees;
    @Autowired EmployeeProfileRepository profiles;

    /**
     * Removes employees and their cascaded profiles before each test.
     */
    @BeforeEach
    void cleanDatabase() {
        employees.deleteAll();
    }

    /**
     * Builds reusable profile input with a test-specific address.
     *
     * @param address address to include
     * @return profile request with fixed contact and birth-date values
     */
    private EmployeeProfileDto profile(String address) {
        return new EmployeeProfileDto("1234567890", address, LocalDate.of(1995, 4, 12), "9876543210");
    }

    /**
     * Verifies relationship persistence, in-place updates, orphan removal,
     * profile recreation, and cascade deletion across service transactions.
     */
    @Test
    void profileLifecyclePersistsBothSidesAndRemovesOrphans() {
        EmployeeResponseDto created = service.create(new EmployeeRequestDto(
                "Alex", "alex@example.com", "Engineering", profile("First address")));
        Long id = created.getId();
        Long profileId = created.getEmployeeProfileResponse().getId();
        assertThat(id).isNotNull();
        assertThat(profileId).isNotNull();
        assertThat(created.getName()).isEqualTo("Alex");
        assertThat(created.getDepartment()).isEqualTo("Engineering");
        assertThat(profiles.findById(profileId).orElseThrow().getEmployee().getId()).isEqualTo(id);
        assertThat(service.findById(id).orElseThrow().getEmployeeProfileResponse().getDateOfBirth())
                .isEqualTo(LocalDate.of(1995, 4, 12));
        assertThat(service.findAll()).hasSize(1);

        EmployeeResponseDto updated = service.updateProfile(id, profile("Second address"));
        assertThat(updated.getEmployeeProfileResponse().getId()).isEqualTo(profileId);
        assertThat(service.findById(id).orElseThrow().getEmployeeProfileResponse().getAddress())
                .isEqualTo("Second address");
        assertThat(profiles.count()).isEqualTo(1);

        service.deleteProfile(id);
        assertThat(profiles.count()).isZero();
        assertThat(service.findById(id).orElseThrow().getEmployeeProfileResponse()).isNull();
        service.updateProfile(id, profile("Third address"));
        assertThat(profiles.count()).isEqualTo(1);
        service.delete(id);
        assertThat(employees.count()).isZero();
        assertThat(profiles.count()).isZero();
    }

    /**
     * Verifies that profiles are optional and duplicate emails do not create employees.
     */
    @Test
    void supportsEmployeeWithoutProfileAndRejectsDuplicateEmail() {
        EmployeeRequestDto request = new EmployeeRequestDto("Alex", "alex@example.com", "Engineering", null);
        assertThat(service.create(request).getEmployeeProfileResponse()).isNull();
        assertThatThrownBy(() -> service.create(request)).isInstanceOf(DuplicateEmployeeException.class);
        assertThat(employees.count()).isEqualTo(1);
    }

    /**
     * Verifies that read, update, and delete operations reject missing employee IDs.
     */
    @Test
    void missingEmployeeOperationsReportNotFound() {
        assertThatThrownBy(() -> service.findById(-1L)).isInstanceOf(EmployeeNotFoundException.class);
        assertThatThrownBy(() -> service.updateProfile(-1L, profile("Address")))
                .isInstanceOf(EmployeeNotFoundException.class);
        assertThatThrownBy(() -> service.deleteProfile(-1L)).isInstanceOf(EmployeeNotFoundException.class);
        assertThatThrownBy(() -> service.delete(-1L)).isInstanceOf(EmployeeNotFoundException.class);
    }
}
