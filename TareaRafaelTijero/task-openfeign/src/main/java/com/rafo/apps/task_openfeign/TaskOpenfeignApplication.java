package com.rafo.apps.task_openfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TaskOpenfeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskOpenfeignApplication.class, args);
	}

}
