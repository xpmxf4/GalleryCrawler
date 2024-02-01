package org.example.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Post {
    private String title;
    private String link;
    private int totalViews;
    private String lastUpdated;

}

