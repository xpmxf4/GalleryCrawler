package org.example.crawler.all;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.model.Post;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DirectJsoupPopularPostsTracker {
    public static void main(String[] args) {
        try {
            List<Post> postsList = trackPopularPosts();
            savePostsToJsonFile(postsList, "popular_posts.json");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static List<Post> trackPopularPosts() throws IOException {
        String url = "http://gall.dcinside.com/board/lists/?id=football_new8";
        List<Post> postsList = new ArrayList<>();
        Document postDocs = Jsoup.connect(url).get();
        Elements posts = postDocs.select(".concept_txtlist li a");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = formatter.format(new Date());

        for (Element post : posts) {
            String postLink = "";
            try {
                String postTitle = post.text();
                postLink = post.attr("href").replace("https", "http");
                if (postLink.contains("amp;")) {
                    postLink = postLink.replace("amp;", "");
                }
                Document postDoc = Jsoup.connect(postLink).get();
                Element view = postDoc.selectFirst(".view_content_wrap .gall_count");
                int count = Integer.parseInt(view.text().replaceAll("[^\\d]", ""));

                Post postObject = Post.builder()
                        .title(postTitle)
                        .link(postLink)
                        .totalViews(count)
                        .lastUpdated(currentDate)
                        .build();

                postsList.add(postObject);
            } catch (NumberFormatException e) {
                System.err.println("Parsing error for post: " + postLink + " - " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error processing post: " + postLink);
            }
        }

        return postsList;
    }

    public static void savePostsToJsonFile(List<Post> postsList, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        String json = mapper.writeValueAsString(postsList);
        Files.write(Paths.get(fileName), json.getBytes());
        System.out.println("Saved posts to " + fileName);
    }
}
