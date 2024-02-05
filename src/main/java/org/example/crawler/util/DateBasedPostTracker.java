package org.example.crawler.util;

import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class DateBasedPostTracker implements IDateBasedPostTracker{
    private static final int MIN_PAGE = 1;
    private static final int MAX_PAGE = 47622;
    private static final int POSTS_PER_PAGE = 100; // 페이지 당 게시물 수

    @Override
    public int estimatePostCountForDate(String targetDate) {
        int firstPage = findFirstPage(targetDate);
        int lastPage = findLastPage(targetDate);

        if (firstPage > lastPage) return -1;

        return (lastPage - firstPage + 1) * POSTS_PER_PAGE;
    }

    @Override
    public int findFirstPage(String targetDate) {
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
    public int findLastPage(String targetDate) {
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
        try {
            String url = "http://gall.dcinside.com/board/lists/?id=football_new8&sort_type=N&list_num=100&search_head=&page=" + page;
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }


}
