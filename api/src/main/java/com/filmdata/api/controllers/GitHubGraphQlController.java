package com.filmdata.api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.filmdata.api.models.Repository;
import com.filmdata.api.models.Issue;
import com.filmdata.api.models.Contributor;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class GitHubGraphQlController {
    private final WebClient webClient;

    @SchemaMapping(typeName = "Query")
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
    public Flux<Issue> issues(Repository repository, @Argument Integer first) {
        return webClient.get()
            .uri("/repos/{owner}/{name}/issues?per_page={first}",
                 repository.getName().split("/")[0],
                 repository.getName().split("/")[1],
                 first)
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
    public Flux<Contributor> contributors(Repository repository, @Argument Integer first) {
        return webClient.get()
            .uri("/repos/{owner}/{name}/contributors?per_page={first}",
                 repository.getName().split("/")[0],
                 repository.getName().split("/")[1],
                 first)
            .retrieve()
            .bodyToFlux(JsonNode.class)
            .map(json -> Contributor.builder()
                .login(json.get("login").asText())
                .contributions(json.get("contributions").asInt())
                .avatarUrl(json.get("avatar_url").asText())
                .build());
    }
}