package com.github.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EnvConfig {
    
    @Bean
    public WebClient webClient(@Value("${GITHUB_TOKEN}") String githubToken) {
        if (githubToken == null || githubToken.isEmpty()) {
            throw new IllegalStateException("GITHUB_TOKEN environment variable is required");
        }
        return WebClient.builder()
            .baseUrl("https://api.github.com")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + githubToken)
            .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
            .build();
    }
}
