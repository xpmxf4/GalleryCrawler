package org.example.crawler.views;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DateRangeViewCountSummarizer {
    public static void main(String[] args) throws IOException {
        String url = "http://gall.dcinside.com/board/lists/?id=football_new8&list_num=100&sort_type=N&list_num=100&search_head=&page=";
        int firstPage = 9472;
        int lastPage = 9761;
        String targetDate = "23.10.07";
        int viewCounts = 0;

        viewCounts = getViewCounts(url, firstPage, lastPage, targetDate);

        System.out.println(viewCounts);
    }

    private static int getViewCounts(String url, int firstPage, int lastPage, String targetDate) throws IOException {
        int result = 0;

        for (int currPage = firstPage; currPage <= lastPage; currPage++) {
            Document doc = Jsoup.connect(url + currPage).get();
            Elements posts = doc.select(".us-post");

            for (Element post : posts) {
                Element dateEl = post.selectFirst(".gall_date");
                String date = dateEl.text();

                if (date.equals(targetDate)) {
                    Element countEl = post.selectFirst(".gall_count");
                    int view = Integer.parseInt(countEl.text());
                    result += view;
                }
            }
        }
        return result;
    }
}
