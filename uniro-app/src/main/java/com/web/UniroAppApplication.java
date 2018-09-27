package com.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"com"} ,exclude = { SecurityAutoConfiguration.class })
public class UniroAppApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		//System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(UniroAppApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(UniroAppApplication.class);
	}
}
