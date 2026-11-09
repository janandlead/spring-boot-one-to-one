package com.durga.OneToOne.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.durga.OneToOne.dto.EmployeeProfileDto;
import com.durga.OneToOne.dto.EmployeeProfileResponse;
import com.durga.OneToOne.dto.EmployeeRequestDto;
import com.durga.OneToOne.dto.EmployeeResponseDto;
import com.durga.OneToOne.entity.Employee;
import com.durga.OneToOne.entity.EmployeeProfile;
import com.durga.OneToOne.exception.DuplicateEmployeeException;
import com.durga.OneToOne.exception.EmployeeNotFoundException;
import com.durga.OneToOne.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeResponseDto create(EmployeeRequestDto request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmployeeException("Employee already exists with email: " + request.getEmail());
        }
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        if (request.getEmployeeProfileDto() != null) {
            EmployeeProfile profile = new EmployeeProfile();
            copyProfile(request.getEmployeeProfileDto(), profile);
            employee.setEmployeeProfile(profile);
        }
        return toResponse(employeeRepository.saveAndFlush(employee));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeResponseDto> findById(Long id) {
        return Optional.of(toResponse(requireEmployee(id)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> findAll() {
        return employeeRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        employeeRepository.delete(requireEmployee(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeResponseDto updateProfile(Long id, EmployeeProfileDto request) {
        Employee employee = requireEmployee(id);
        EmployeeProfile profile = employee.getEmployeeProfile();
        if (profile == null) {
            profile = new EmployeeProfile();
            employee.setEmployeeProfile(profile);
        }
        copyProfile(request, profile);
        return toResponse(employeeRepository.saveAndFlush(employee));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteProfile(Long id) {
        requireEmployee(id).setEmployeeProfile(null);
    }

    /**
     * Loads an employee within the current service transaction.
     *
     * @param id employee ID
     * @return the managed employee entity
     * @throws EmployeeNotFoundException if no employee has the supplied ID
     */
    private Employee requireEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    /**
     * Copies all profile fields, including nulls, without changing the ID or association.
     *
     * @param request source profile details
     * @param profile target entity to modify
     */
    private void copyProfile(EmployeeProfileDto request, EmployeeProfile profile) {
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setAddress(request.getAddress());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setEmergencyContact(request.getEmergencyContact());
    }

    /**
     * Maps an employee and its profile while the service transaction is open.
     *
     * @param employee entity to map
     * @return employee details with a null profile response when no profile exists
     */
    private EmployeeResponseDto toResponse(Employee employee) {
        EmployeeProfile profile = employee.getEmployeeProfile();
        EmployeeProfileResponse profileResponse = profile == null ? null : new EmployeeProfileResponse(
                profile.getId(), profile.getPhoneNumber(), profile.getAddress(),
                profile.getDateOfBirth(), profile.getEmergencyContact());
        return new EmployeeResponseDto(employee.getId(), employee.getName(), employee.getEmail(),
                employee.getDepartment(), profileResponse);
    }
}
