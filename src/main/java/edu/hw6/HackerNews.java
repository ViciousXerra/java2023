package edu.hw6;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class HackerNews {

    private final static Logger LOGGER = LogManager.getLogger();

    private final static String STORY_BY_ID_ENDPOINT_TEMPLATE =
        "https://hacker-news.firebaseio.com/v0/item/%d.json?print=pretty";
    private final static String TOP_STORIES_ENDPOINT = "https://hacker-news.firebaseio.com/v0/topstories.json";

    private HackerNews() {

    }

    public static long[] hackerNewsTopStories() {
        HttpRequest request = getRequestInstance(TOP_STORIES_ENDPOINT);
        HttpResponse<String> response;
        try (
            HttpClient client = getHttpClientInstance()
        ) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            LOGGER.info("Input/Output error occurs.");
            return new long[0];
        } catch (InterruptedException e) {
            LOGGER.info("Thread has been interrupted.");
            return new long[0];
        }
        return Arrays
            .stream(response.body().split("[\\[\\],]"))
            .skip(1)
            .mapToLong(Long::parseLong)
            .toArray();
    }

    public static String newsTitle(long id) {
        HttpRequest request = getRequestInstance(String.format(STORY_BY_ID_ENDPOINT_TEMPLATE, id));
        HttpResponse<String> response;
        try (
            HttpClient client = getHttpClientInstance()
        ) {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            LOGGER.info("Input/Output error occurs.");
            return "";
        } catch (InterruptedException e) {
            LOGGER.info("Thread has been interrupted.");
            return "";
        }
        Pattern titlePattern = Pattern.compile("\"title\" : \"(.+)\"");
        Matcher matcher = titlePattern.matcher(response.body());
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    private static HttpRequest getRequestInstance(String URIString) {
        return HttpRequest
            .newBuilder()
            .uri(URI.create(URIString))
            .version(HttpClient.Version.HTTP_2)
            .GET()
            .build();
    }

    private static HttpClient getHttpClientInstance() {
        return HttpClient
            .newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    }

}
