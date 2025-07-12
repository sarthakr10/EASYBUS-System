package com.login;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableDiscoveryClient
@EnableJpaAuditing
@SpringBootApplication
public class LoginRegistrationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginRegistrationServiceApplication.class, args);
	}

}
