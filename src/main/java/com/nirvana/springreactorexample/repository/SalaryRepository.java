package com.nirvana.springreactorexample.repository;

import com.nirvana.springreactorexample.entity.Salary;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepository extends ReactiveCrudRepository<Salary, String> {

}
