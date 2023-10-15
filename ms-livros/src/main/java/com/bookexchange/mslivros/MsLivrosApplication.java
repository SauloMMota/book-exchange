package com.bookexchange.mslivros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsLivrosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsLivrosApplication.class, args);
	}

}
