package com.filmdata.api.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Repository {
    private String id;
    private String name;
    private String description;
    private int starCount;
}
