package org.example.crawler.util;

public interface IDateBasedPostTracker {
    int estimatePostCountForDate(String targetDate);
    int findFirstPage(String targetDate);
    int findLastPage(String targetDate);
}
