package com.example.Project.Management.bayport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:application.properties")
public class ProjectManagementIpfApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementIpfApplication.class, args);
	}
}
