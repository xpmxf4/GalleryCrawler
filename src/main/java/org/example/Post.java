package org.example;

import lombok.Builder;

@Builder
public class Post {
    private String title;
    private String link;
    private int totalViews;
    private String lastUpdated;

}

