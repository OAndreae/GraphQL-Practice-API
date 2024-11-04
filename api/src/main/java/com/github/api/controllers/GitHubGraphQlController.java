package com.github.api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.api.models.Contributor;
import com.github.api.models.Issue;
import com.github.api.models.Repository;

import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class GitHubGraphQlController {
    private final WebClient webClient;

    @QueryMapping
    public Mono<Repository> repository(@Argument String owner, @Argument String name) {
        return webClient.get()
            .uri("/repos/{owner}/{name}", owner, name)
            .retrieve()
            .bodyToMono(JsonNode.class)
            .map(json -> Repository.builder()
                .id(json.get("id").asText())
                .name(json.get("name").asText())
                .description(json.get("description").asText())
                .starCount(json.get("stargazers_count").asInt())
                .build());
    }

    @SchemaMapping(typeName = "Repository")
    public Flux<Issue> issues(Repository repository, @Argument Integer first, DataFetchingEnvironment env) {
        Map<String, Object> arguments = env.getExecutionStepInfo().getParent().getArguments();
        String owner = (String) arguments.get("owner");
        String name = (String) arguments.get("name");
        
        return webClient.get()
            .uri("/repos/{owner}/{name}/issues?per_page={first}", owner, name, first)
            .retrieve()
            .bodyToFlux(JsonNode.class)
            .map(json -> Issue.builder()
                .number(json.get("number").asInt())
                .title(json.get("title").asText())
                .state(json.get("state").asText())
                .createdAt(json.get("created_at").asText())
                .build());
    }

    @SchemaMapping(typeName = "Repository")
    public Flux<Contributor> contributors(Repository repository, @Argument Integer first, DataFetchingEnvironment env) {
        Map<String, Object> arguments = env.getExecutionStepInfo().getParent().getArguments();
        String owner = (String) arguments.get("owner");
        String name = (String) arguments.get("name");
        
        return webClient.get()
            .uri("/repos/{owner}/{name}/contributors?per_page={first}", owner, name, first)
            .retrieve()
            .bodyToFlux(JsonNode.class)
            .map(json -> Contributor.builder()
                .login(json.get("login").asText())
                .contributions(json.get("contributions").asInt())
                .avatarUrl(json.get("avatar_url").asText())
                .build());
    }
}
