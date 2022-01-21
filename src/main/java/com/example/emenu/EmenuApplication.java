package com.example.emenu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class EmenuApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmenuApplication.class, args);
	}

}
