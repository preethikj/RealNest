package com.capstone.realNest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RealNestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealNestApplication.class, args);
	}

}
