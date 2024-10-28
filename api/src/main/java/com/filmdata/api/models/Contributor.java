package com.filmdata.api.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contributor {
    private String login;
    private int contributions;
    private String avatarUrl;
}
