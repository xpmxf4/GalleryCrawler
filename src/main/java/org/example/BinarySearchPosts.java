package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BinarySearchPosts {
    public static void main(String[] args) {
        System.setProperty("jsse.enableSNIExtension", "false");
        int left = 1;
        int right = 47622; // 전체 페이지 수
        String targetDate = "23.10.07";

        int firstPage = findFirstPage(left, right, targetDate);
        int lastPage = findLastPage(left, right, targetDate);

        // 게시물의 수 추정
        int estCount = (lastPage - firstPage + 1) * 50;

        System.out.println("firstPage = " + firstPage);
        System.out.println("lastPage = " + lastPage);
        System.out.println("estCount = " + estCount);
    }
    private static int findFirstPage(int left, int right, String targetDate) {
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




    private static int findLastPage(int left, int right, String targetDate) {
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
            String url = "http://gall.dcinside.com/board/lists/?id=football_new8&list_num=50&sort_type=N&search_head=&page=" + page;
            Document doc = Jsoup.connect(url).get();
            Elements usPosts = doc.select(".us-post"); // Select .us_post elements

            // 가장 이른 날짜와 가장 늦은 날짜를 초기화합니다.
            String earliestDate = "99.99.99"; // 임의의 늦은 날짜로 초기화
            String latestDate = "00.00.00"; // 임의의 이른 날짜로 초기화

            for (Element usPost : usPosts) {
                Element date = usPost.selectFirst(".gall_date");
                String postDate = date.text();

                // 가장 이른 날짜와 가장 늦은 날짜를 업데이트합니다.
                if (postDate.compareTo(earliestDate) < 0) {
                    earliestDate = postDate;
                }
                if (postDate.compareTo(latestDate) > 0) {
                    latestDate = postDate;
                }
            }

            // 날짜 범위와 targetDate를 비교합니다.
            if (earliestDate.compareTo(targetDate) <= 0 && latestDate.compareTo(targetDate) >= 0) {
                return 2; // 임계점: targetDate를 포함하는 페이지
            } else if (latestDate.compareTo(targetDate) < 0) {
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
