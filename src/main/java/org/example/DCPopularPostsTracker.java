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
import java.util.*;

public class DCPopularPostsTracker {
    private static final String JSON_FILE = "dc_popular_posts2.json";
    private static Map<String, JSONObject> postsMap = new HashMap<>();
    private static final String URL = "http://gall.dcinside.com/board/lists/?id=football_new8";

    private static final long ONE_HOUR = 1000 * 60 * 60;

    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                trackPopularPosts();
            }
        };

        long delay = calculateInitialDelay();
        timer.scheduleAtFixedRate(hourlyTask, delay, ONE_HOUR);
    }

    private static long calculateInitialDelay() {
        Calendar now = Calendar.getInstance();
        Calendar nextHour = (Calendar) now.clone();
        nextHour.set(Calendar.MINUTE, 0);
        nextHour.set(Calendar.SECOND, 0);
        nextHour.set(Calendar.MILLISECOND, 0);
        nextHour.add(Calendar.HOUR, 1);

        return nextHour.getTimeInMillis() - now.getTimeInMillis();
    }

    public static void trackPopularPosts() {
        try {
            Elements posts = fetchPopularPosts();
            String currentDate = getCurrentDate();

            for (Element post : posts) {
                processPost(post, currentDate);
            }

            writeJSONToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Elements fetchPopularPosts() throws IOException {
        Document postDocs = Jsoup.connect(URL).get();
        return postDocs.select(".concept_txtlist li a");
    }

    private static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date());
    }

    private static void processPost(Element post, String currentDate) throws IOException {
        String postTitle = post.text();
        String postLink = post.attr("href").replace("https", "http");
        int count = fetchPostViewCount(postLink);

        JSONObject postJSON = postsMap.getOrDefault(postLink, new JSONObject());
        postJSON.put("title", postTitle);
        postJSON.put("link", postLink);
        postJSON.put("total_views", count);
        postJSON.put("last_updated", currentDate);

        postsMap.put(postLink, postJSON);
    }

    private static int fetchPostViewCount(String postLink) throws IOException {
        Document postDoc = Jsoup.connect(postLink).get();
        Element view = postDoc.selectFirst(".view_content_wrap .gall_count");
        return Integer.parseInt(view.text().replaceAll("[^\\d]", ""));
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
