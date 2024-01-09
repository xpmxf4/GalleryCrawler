package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DCPopularPostsTracker {
    private static final String JSON_FILE = "dc_popular_posts.json";
    private static Map<String, JSONObject> postsMap = new HashMap<>();

    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                trackPopularPosts();
            }
        };

        timer.scheduleAtFixedRate(hourlyTask, 0L, 1000 * 60 * 60); // 매시간 크롤링
    }

    public static void trackPopularPosts() {
        String url = "http://gall.dcinside.com/board/lists/?id=football_new8";
        try {
            Document postDocs = Jsoup.connect(url).get();
            Elements posts = postDocs.select(".concept_txtlist li:not([style=display:none;]) a");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate = formatter.format(new Date());

            for (Element post : posts) {
                String postTitle = post.text();
                String postLink = post.attr("href");
                Document postDoc = Jsoup.connect(postLink).get();
                Element view = postDoc.selectFirst(".view_content_wrap .gall_count");
                int count = Integer.parseInt(view.text().replaceAll("[^\\d]", "")); // 숫자만 추출

                JSONObject postJSON;
                if (postsMap.containsKey(postLink)) {
                    postJSON = postsMap.get(postLink);
                    int totalViews = postJSON.getInt("total_views");
                    postJSON.put("total_views", totalViews + count);
                } else {
                    postJSON = new JSONObject();
                    postJSON.put("title", postTitle);
                    postJSON.put("link", postLink);
                    postJSON.put("total_views", count);
                    postsMap.put(postLink, postJSON);
                }
                postJSON.put("last_updated", currentDate);
            }

            writeJSONToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeJSONToFile() {
        try (FileWriter file = new FileWriter(JSON_FILE)) {
            JSONArray postsList = new JSONArray();
            postsMap.values().forEach(postsList::put);
            file.write(postsList.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
