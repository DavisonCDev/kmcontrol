package com.oksmart.kmcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.oksmart.kmcontrol.repository")
public class KmcontrolApplication {

	public static void main(String[] args) {
		SpringApplication.run(KmcontrolApplication.class, args);
	}
}
