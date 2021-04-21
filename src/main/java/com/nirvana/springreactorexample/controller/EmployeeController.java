package com.nirvana.springreactorexample.controller;

import com.nirvana.springreactorexample.exception.ProfileNotFoundException;
import com.nirvana.springreactorexample.exception.SalaryNotFoundException;
import com.nirvana.springreactorexample.model.Employee;
import com.nirvana.springreactorexample.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public Mono<Employee> getEmployee(@PathVariable String id) {
        return employeeService.getById(id);
    }

    @GetMapping
    public Flux<Employee> employees(@RequestParam("id") List<String> ids) {
        return employeeService.getAll(ids);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Profile not found.")
    @ExceptionHandler(ProfileNotFoundException.class)
    public void profileNotFound() {
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Salary not found.")
    @ExceptionHandler(SalaryNotFoundException.class)
    public void salaryNotFound() {
    }
}
