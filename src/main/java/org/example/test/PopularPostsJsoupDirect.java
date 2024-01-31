package org.example.test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PopularPostsJsoupDirect {
    public static void main(String[] args) {
        Map<String, JSONObject> postsMap = null;
        try {
            postsMap = trackPopularPosts();
            savePostsToJsonFile(postsMap, "popular_posts.json");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static Map<String, JSONObject> trackPopularPosts() throws IOException {
        String url = "http://gall.dcinside.com/board/lists/?id=football_new8";
        Map<String, JSONObject> postsMap = new HashMap<>();
        Document postDocs = Jsoup.connect(url).get();
        Elements posts = postDocs.select(".concept_txtlist li a");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = formatter.format(new Date());

        for (Element post : posts) {
            String postLink = "";
            try {
                String postTitle = post.text();
                postLink = post.attr("href").replace("https", "http"); // 'https'를 'http'로 변경
                if (postLink.contains("amp;")) {
                    postLink = postLink.replace("amp;", "");
                }
                Document postDoc = Jsoup.connect(postLink).get();
                Element view = postDoc.selectFirst(".view_content_wrap .gall_count");
                int count = Integer.parseInt(view.text().replaceAll("[^\\d]", "")); // 숫자만 추출

                JSONObject postJSON = new JSONObject();
                postJSON.put("title", postTitle);
                postJSON.put("link", postLink);
                postJSON.put("total_views", count);
                postJSON.put("last_updated", currentDate);

                postsMap.put(postLink, postJSON);
            } catch (NumberFormatException e) {
                System.err.println("Parsing error for post: " + postLink + " - " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error processing post: " + postLink);
            }
        }

        return postsMap;
    }

    public static void savePostsToJsonFile(Map<String, JSONObject> postsMap, String fileName) throws IOException {
        JSONArray postsArray = new JSONArray();
        for (String key : postsMap.keySet()) {
            postsArray.put(postsMap.get(key));
        }

        Files.write(Paths.get(fileName), postsArray.toString().getBytes());
        System.out.println("Saved posts to " + fileName);
    }
}
