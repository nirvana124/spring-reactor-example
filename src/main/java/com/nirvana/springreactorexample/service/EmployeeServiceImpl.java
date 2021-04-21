package com.nirvana.springreactorexample.service;

import com.nirvana.springreactorexample.entity.Salary;
import com.nirvana.springreactorexample.exception.SalaryNotFoundException;
import com.nirvana.springreactorexample.model.Employee;
import com.nirvana.springreactorexample.model.Profile;
import com.nirvana.springreactorexample.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final SalaryRepository salaryRepository;
    private final ProfileService profileService;

    @Override
    public Mono<Employee> getById(String id) {
        return Mono.zip(profileService.getById(id), getSalaryById(id))
                .map(data -> buildEmployee(data.getT1(), data.getT2()));
    }

    @Override
    public Flux<Employee> getAll(List<String> ids) {
        var salaries = getSalaries(ids)
                .collect(Collectors.toMap(Salary::getId, it -> it)).block();
        return profileService.profiles(ids)
                .map(profile -> buildEmployee(profile, salaries.get(profile.getId())));
    }

    private Mono<Salary> getSalaryById(String id) {
        return salaryRepository.findById(id)
                .switchIfEmpty(Mono.error(new SalaryNotFoundException()));
    }

    private Flux<Salary> getSalaries(List<String> ids) {
        return salaryRepository.findAllById(ids)
                .switchIfEmpty(Mono.error(new SalaryNotFoundException()));
    }

    private Employee buildEmployee(Profile profile, Salary salary) {
        return Employee.builder().id(profile.getId()).name(profile.getName()).salary(salary.getAmount()).build();
    }
}
