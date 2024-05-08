package org.example.dcinsidecrawler.tracker;

import lombok.NoArgsConstructor;
import org.example.dcinsidecrawler.provider.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class DateBasedPostTracker implements IDateBasedPostTracker {
    private static final int MIN_PAGE = 1;
    private static final int MAX_PAGE = 47622;
    private static final int POSTS_PER_PAGE = 100; // 페이지 당 게시물 수

    @Override
    public int getViewCountsBetweenDates(String startDate, String endDate) {
        int result = 0;
        LocalDate currentDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate lastDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        while (!currentDate.isAfter(lastDate)) {
            String targetDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int firstPage = getFirstPage(targetDate);
            int lastPage = getLastPage(targetDate);

            if (firstPage != -1 && lastPage != -1) {
                result += getViewCountsForDate(targetDate, firstPage, lastPage);
            }

            currentDate = currentDate.plusDays(1);
        }

        return result;
    }

    @Override
    public int getViewCountOnDate(String date) {
        int firstPage = getFirstPage(date);
        int lastPage = getLastPage(date);

        if (firstPage == -1 || lastPage == -1) {
            return 0; // 그 어떤 게시글도 찾아지지 않음
        }

        return getViewCountsForDate(date, firstPage, lastPage);
    }

    private int getViewCountsForDate(String targetDate, int firstPage, int lastPage) {
        int viewCount = 0;
        for (int page = firstPage; page <= lastPage; page++) {
            String url = GalleryUrlProvider.getGalleryUrl(targetDate) + page;
            try {
                Document doc = Jsoup.connect(url).get();
                Elements posts = doc.select(".us-post");

                for (Element post : posts) {
                    Element dateElement = post.selectFirst(".gall_date");
                    String postDate = dateElement.attr("title").split(" ")[0];

                    if (postDate.equals(targetDate)) {
                        Element viewCountElement = post.selectFirst(".gall_count");
                        int postViewCount = Integer.parseInt(viewCountElement.text());
                        viewCount += postViewCount;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to fetch data from URL: " + url, e);
            }
        }
        return viewCount;
    }

    @Override
    public int getFirstPage(String targetDate) {
        int left = MIN_PAGE;
        int right = MAX_PAGE;

        while (left < right) {
            int mid = left + (right - left) / 2;
            int result = containsDate(mid, targetDate);
            if (result == 2) { // 임계점: targetDate를 포함하는 페이지
                right = mid; // 임계점을 찾으면 오른쪽 경계를 당깁니다.
            } else if (result == 1) {
                right = mid - 1; // 페이지의 모든 게시물의 날짜가 targetDate보다 이릅니다.
            } else {
                left = mid + 1; // 페이지의 모든 게시물의 날짜가 targetDate보다 늦습니다.
            }
        }
        return left; // 이진 탐색이 완료된 후 left는 첫 번째 targetDate가 있는 페이지를 가리킵니다.
    }

    @Override
    public int getLastPage(String targetDate) {
        int left = MIN_PAGE;
        int right = MAX_PAGE;

        while (left < right) {
            int mid = left + (right - left + 1) / 2;  // Adjust for upper bound
            int result = containsDate(mid, targetDate);

            if (result == 2) { // 임계점: targetDate를 포함하는 페이지
                left = mid; // 임계점을 찾으면 왼쪽 경계를 당깁니다.
            } else if (result == 0) { // 페이지의 모든 게시물의 날짜가 targetDate보다 늦습니다.
                left = mid + 1;
            } else { // 페이지의 모든 게시물의 날짜가 targetDate보다 이릅니다.
                right = mid - 1;
            }
        }
        return right; // 이진 탐색이 완료된 후 right는 마지막 targetDate가 있는 페이지를 가리킵니다.
    }

    private static int containsDate(int page, String targetDate) {
        String url = GalleryUrlProvider.getGalleryUrl(targetDate) + page;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements usPosts = doc.select(".us-post"); // Select .us_post elements

            LocalDate earliestDate = LocalDate.MAX;
            LocalDate latestDate = LocalDate.MIN;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Element usPost : usPosts) {
                String postDateTime = usPost.selectFirst(".gall_date").attr("title");
                LocalDate postDate = LocalDate.parse(postDateTime.split(" ")[0], formatter);

                if (postDate.isBefore(earliestDate)) {
                    earliestDate = postDate;
                }
                if (postDate.isAfter(latestDate)) {
                    latestDate = postDate;
                }
            }

            LocalDate target = LocalDate.parse(targetDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!earliestDate.isAfter(target) && !latestDate.isBefore(target)) {
                return 2; // 임계점: targetDate를 포함하는 페이지
            } else if (latestDate.isBefore(target)) {
                return 1; // 페이지의 모든 게시물의 날짜가 targetDate보다 이릅니다.
            } else {
                return 0; // 페이지의 모든 게시물의 날짜가 targetDate보다 늦습니다.
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch data from URL: " + url, e);
        }
    }
}