package org.example.dcinsidecrawler;

import org.example.dcinsidecrawler.tracker.*;

public class DCInsideCrawlerApplication {
    public static void main(String[] args) {
        IDateBasedPostTracker tracker = new DateBasedPostTracker();
        System.out.println("tracker.getViewCountOnDate(\"2024-05-05\") = " + tracker.getViewCountOnDate("2024-05-05"));
        System.out.println("tracker.getViewCountsBetweenDates(\"2024-05-04\", \"2024-05-05\") = " + tracker.getViewCountsBetweenDates("2024-05-04", "2024-05-05"));
        System.out.println("tracker.getFirstPage(\"2024-05-05\") = " + tracker.getFirstPage("2024-05-05"));
        System.out.println("tracker.getLastPage(\"2024-05-05\") = " + tracker.getLastPage("2024-05-05"));
    }
}
