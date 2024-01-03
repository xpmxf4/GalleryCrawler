package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BinarySearchPosts {
    public static void main(String[] args) {
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
            if (result == 2) {  // 임계점 발견
                return mid;  // 해당 페이지 번호를 반환
            } else if (result == 1) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left; // or right, since left == right here
    }


    private static int findLastPage(int left, int right, String targetDate) {
        while (left < right) {
            int mid = left + (right - left + 1) / 2;
            int result = containsDate(mid, targetDate);
            if (result == 2) {  // 임계점 발견
                return mid;  // 해당 페이지 번호를 반환
            } else if (result == 1) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return right; // or left, since left == right here
    }


    private static int containsDate(int page, String targetDate) {
        try {
            String url = "https://gall.dcinside.com/board/lists/?id=football_new8&page=" + page;
            Document doc = Jsoup.connect(url).get();
            Elements dates = doc.select(".gall_date");

            boolean hasTargetDate = false;
            boolean hasOtherDate = false;

            for (Element date : dates) {
                if (date.text().contains(targetDate))
                    hasTargetDate = true;
                else
                    hasOtherDate = true;
            }

            // 임계점 처리
            if (hasTargetDate && !hasOtherDate) // targetDate 만 존재
                return 1;
            else if (hasTargetDate && hasOtherDate) // 임계점
                return 2;
            else // targetDate 가 하나도 없는 경우
                return 0;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

}
