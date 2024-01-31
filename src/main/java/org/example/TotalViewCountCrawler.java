package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class TotalViewCountCrawler {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the target date (Format: yyyy.MM.dd): ");
        String targetDate = scanner.nextLine();

        try {
            int totalViewCount = getTotalViewCount(targetDate);
            System.out.println("Total view count for " + targetDate + " is: " + totalViewCount);
        } catch (IOException e) {
            System.err.println("An error occurred during crawling: " + e.getMessage());
        }
    }

    private static int getTotalViewCount(String targetDate) throws IOException {
        int totalViewCount = 0;
        int page = 1;
        boolean isDone = false;

        while (!isDone) {
            String url = "http://gall.dcinside.com/board/lists/?id=football_new8&page=" + page;
            Document doc = Jsoup.connect(url).get();

            Elements posts = doc.select(".gall_list .us-post");

            for (Element post : posts) {
                String postDate = post.select(".gall_date").attr("title");
                String formattedPostDate = postDate.split(" ")[0].replace("-", ".");

                if (formattedPostDate.equals(targetDate)) {
                    String viewCountStr = post.select(".gall_count").text().replaceAll("[^\\d]", "");
                    totalViewCount += Integer.parseInt(viewCountStr);
                } else if (formattedPostDate.compareTo(targetDate) < 0) {
                    isDone = true; // 입력된 날짜보다 이전 날짜의 게시물을 만나면 종료
                    break;
                }
            }

            page++;
        }

        return totalViewCount;
    }
}
