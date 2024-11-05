package com.example.springthymeleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableScheduling
@SpringBootApplication
public class SpringthymeleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringthymeleafApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("manager123"));
		
	}

}
