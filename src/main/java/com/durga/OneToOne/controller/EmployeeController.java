package com.durga.OneToOne.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.durga.OneToOne.dto.EmployeeProfileDto;
import com.durga.OneToOne.dto.EmployeeRequestDto;
import com.durga.OneToOne.dto.EmployeeResponseDto;
import com.durga.OneToOne.service.EmployeeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    /**
     * Creates an employee with an optional profile.
     *
     * @param request employee details from the request body
     * @return HTTP 201 with the saved employee
     * @throws com.durga.OneToOne.exception.DuplicateEmployeeException if the email exists; mapped to HTTP 409
     */
    @PostMapping
    public ResponseEntity<EmployeeResponseDto> create(@RequestBody EmployeeRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(request));
    }

    /**
     * Retrieves one employee by ID.
     *
     * @param id employee ID from the path
     * @return HTTP 200 with the employee, or HTTP 404 for an empty service result
     * @throws com.durga.OneToOne.exception.EmployeeNotFoundException if the employee does not exist; mapped to HTTP 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.of(employeeService.findById(id));
    }

    /**
     * Lists employees with their optional profiles.
     *
     * @return all employee responses, or an empty list
     */
    @GetMapping
    public List<EmployeeResponseDto> findAll() {
        return employeeService.findAll();
    }

    /**
     * Deletes the employee and its profile, returning HTTP 204 on success.
     *
     * @param id employee ID from the path
     * @throws com.durga.OneToOne.exception.EmployeeNotFoundException if the employee does not exist; mapped to HTTP 404
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }

    /**
     * Replaces profile fields or creates a profile for an employee without one.
     *
     * @param id employee ID from the path
     * @param request complete replacement profile details
     * @return HTTP 200 with the employee and saved profile
     * @throws com.durga.OneToOne.exception.EmployeeNotFoundException if the employee does not exist; mapped to HTTP 404
     */
    @PutMapping("/{id}/profile")
    public EmployeeResponseDto updateProfile(@PathVariable Long id, @RequestBody EmployeeProfileDto request) {
        return employeeService.updateProfile(id, request);
    }

    /**
     * Removes only the profile, returning HTTP 204 even if it was already absent.
     *
     * @param id employee ID from the path
     * @throws com.durga.OneToOne.exception.EmployeeNotFoundException if the employee does not exist; mapped to HTTP 404
     */
    @DeleteMapping("/{id}/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@PathVariable Long id) {
        employeeService.deleteProfile(id);
    }
}
