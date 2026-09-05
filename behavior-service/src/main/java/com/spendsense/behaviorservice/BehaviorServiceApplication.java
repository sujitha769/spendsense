package com.spendsense.behaviorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BehaviorServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				BehaviorServiceApplication.class,
				args
		);
	}
}