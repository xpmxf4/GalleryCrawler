package org.example.crawler.all;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpPopularPostsCrawler {
    private static final String OUTPUT_FILE = "posts_output.json";
    private static final String ERROR_LOG_FILE = "error_log.txt";

    public static void main(String[] args) {
        Map<String, JSONObject> postsMap = trackPopularPosts();
        writeJSONToFile(postsMap, OUTPUT_FILE);
    }

    public static Map<String, JSONObject> trackPopularPosts() {
        String url = "http://gall.dcinside.com/board/lists/?id=football_new8";
        Map<String, JSONObject> postsMap = new HashMap<>();
        try {
            String html = fetchHtml(url);
            Document postDocs = Jsoup.parse(html);
            Elements posts = postDocs.select(".concept_txtlist li a");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate = formatter.format(new Date());

            loopForMapping(postsMap, posts, currentDate);
        } catch (UnknownHostException e) {
            writeErrorToFile("UnknownHostException: " + e.getMessage());
        } catch (SocketTimeoutException e) {
            writeErrorToFile("SocketTimeoutException: " + e.getMessage());
        } catch (IOException e) {
            writeErrorToFile("IOException: " + e.getMessage());
        }
        return postsMap;
    }

    private static void loopForMapping(Map<String, JSONObject> postsMap, Elements posts, String currentDate) throws IOException {
        for (Element post : posts) {
            String postTitle = post.text();
            String postLink = post.attr("href").replace("https", "http");
            if (postLink.contains("amp;")) {
                postLink = postLink.replace("amp;", "");
            }
            System.out.println("postLink = " + postLink);
            String postPageHtml = fetchHtml(postLink);
            Document postDoc = Jsoup.parse(postPageHtml);
            Element view = postDoc.selectFirst(".view_content_wrap .gall_count");
            int count = Integer.parseInt(view.text().replaceAll("[^\\d]", ""));

            JSONObject postJSON = new JSONObject();
            postJSON.put("title", postTitle);
            postJSON.put("link", postLink);
            postJSON.put("total_views", count);
            postJSON.put("last_updated", currentDate);

            postsMap.put(postLink, postJSON);
        }
    }

    private static void writeJSONToFile(Map<String, JSONObject> postsMap, String fileName) {
        JSONObject finalResult = new JSONObject();
        JSONArray resultsArray = new JSONArray();

        for (JSONObject post : postsMap.values()) {
            resultsArray.put(post);
        }

        finalResult.put("results", resultsArray);

        try (FileWriter file = new FileWriter(fileName)) {
            file.write(finalResult.toString());
        } catch (IOException e) {
            writeErrorToFile("IOException in writeJSONToFile: " + e.getMessage());
        }
    }

    private static void writeErrorToFile(String errorMessage) {
        try (FileWriter fw = new FileWriter(ERROR_LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(errorMessage);
        } catch (IOException e) {
            System.err.println("Error writing to error log file: " + e.getMessage());
        }
    }

    private static String fetchHtml(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }
}
