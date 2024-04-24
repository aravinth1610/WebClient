package com.example.demo.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	
	@Bean
     WebClient webClient() {
        return WebClient.builder().baseUrl("http://localhost:8082").build();
    }
}
