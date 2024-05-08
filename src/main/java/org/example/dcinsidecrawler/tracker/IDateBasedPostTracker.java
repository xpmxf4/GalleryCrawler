package org.example.dcinsidecrawler.tracker;

public interface IDateBasedPostTracker {
    int getViewCountOnDate(String date);

    int getViewCountsBetweenDates(String startDate, String endDate);

//    int estimatePostCountForDate(String targetDate);

    int getFirstPage(String targetDate);

    int getLastPage(String targetDate);
}
