package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository){
        return args -> {
            Student etienne = new Student(
                    "Etienne",
                    "etienne1800503@gmail.com",
                    LocalDate.of(2003, Month.MAY,18),
                    20);

            Student alex = new Student(
                "Alex",
                    "alex@gmail.com",
                    LocalDate.of(2003,
                            Month.MAY,18),
                    20
            );

            studentRepository.saveAll(List.of(etienne,alex));
        };
    }
}
