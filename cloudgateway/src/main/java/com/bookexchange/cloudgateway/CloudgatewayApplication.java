package com.bookexchange.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class CloudgatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudgatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route(r -> r.path("/api/v1/usuarios/**").uri("lb://ms-usuarios"))
				.route(r -> r.path("/api/v1/login/**").uri("lb://ms-usuarios"))
				.route(r -> r.path("/api/v1/cadastro/**").uri("lb://ms-usuarios"))
				.route(r -> r.path("/api/v1/livros/**").uri("lb://ms-livros"))
				.route(r -> r.path("/api/v1/livros/colecoes/**").uri("lb://ms-livros"))
				.route(r -> r.path("/api/v1/avaliacao/**").uri("lb://ms-usuarios"))
				.route(r -> r.path("/api/v1/gerenciamento/**").uri("lb://ms-gerenciamento-livros"))
				.route(r -> r.path("/api/v1/notificacoes/**").uri("lb://ms-notificacoes"))
				.build();
	}
}
