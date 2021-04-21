package com.nirvana.springreactorexample.service;

import com.nirvana.springreactorexample.model.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EmployeeService {
    Mono<Employee> getById(String id);
    Flux<Employee> getAll(List<String> ids);
}
