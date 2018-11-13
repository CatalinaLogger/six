package com.maybe;

import org.flowable.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan
@ComponentScan(basePackages = "org.flowable.app")
@EnableScheduling
@SpringBootApplication(
		exclude = {
				SecurityAutoConfiguration.class,
				org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
				UserDetailsServiceAutoConfiguration.class
		}
)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
