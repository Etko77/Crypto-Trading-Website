package com.example.cryptoprojectapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoProjectAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoProjectAppApplication.class, args);
	}

}
