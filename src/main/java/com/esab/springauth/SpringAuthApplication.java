package com.esab.springauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.PropertySource;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@SpringBootApplication
// @PropertySource("file:${user.dir}/.env")
@EnableAdminServer
public class SpringAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAuthApplication.class, args);
	}

}
