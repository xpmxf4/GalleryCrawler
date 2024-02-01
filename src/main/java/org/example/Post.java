package org.example;

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

