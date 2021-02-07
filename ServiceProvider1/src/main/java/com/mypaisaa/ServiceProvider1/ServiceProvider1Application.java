package com.mypaisaa.ServiceProvider1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.mypaisaa")
public class ServiceProvider1Application {

	public static void main(String[] args) {
		SpringApplication.run(ServiceProvider1Application.class, args);
	}

}
