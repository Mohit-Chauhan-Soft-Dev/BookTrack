package com.booktrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.booktrack.security.jwt.JwtProperties;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class BooktrackApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooktrackApiApplication.class, args);
	}

}
