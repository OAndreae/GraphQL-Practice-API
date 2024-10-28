package com.filmdata.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EnvConfig {
    
    @Bean
    public WebClient.Builder webClientBuilder(@Value("${github.token}") String githubToken) {
        return WebClient.builder()
            .baseUrl("https://api.github.com")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + githubToken)
            .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json");
    }
}
