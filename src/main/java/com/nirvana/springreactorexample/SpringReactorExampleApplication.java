package com.nirvana.springreactorexample;

import com.nirvana.springreactorexample.entity.Salary;
import com.nirvana.springreactorexample.repository.SalaryRepository;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import java.util.List;

@SpringBootApplication
public class SpringReactorExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringReactorExampleApplication.class, args);
    }

    // Create salary table
    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        return initializer;
    }

    // adding few entries in salary table
    @Bean
    public CommandLineRunner initialSetup(SalaryRepository repository) {
        return (args) -> {
            repository.saveAll(List.of(
                    Salary.builder().amount(20000.50).build(),
                    Salary.builder().amount(30000.50).build(),
                    Salary.builder().amount(40000.50).build(),
                    Salary.builder().amount(50000.50).build(),
                    Salary.builder().amount(60000.0).build()
            )).blockLast();
        };
    }

}
