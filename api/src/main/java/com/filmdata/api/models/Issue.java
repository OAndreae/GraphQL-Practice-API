package com.filmdata.api.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Issue {
    private int number;
    private String title;
    private String state;
    private String createdAt;
}
