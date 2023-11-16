package edu.hw6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HackerNewsTest {

    @Test
    @DisplayName("Test \"get top stories ids from response body\" method")
    void testTopStoriesIdMethod() {
        //When
        long[] ids = HackerNews.hackerNewsTopStories();
        //Then
        assertThat(ids).isNotEmpty();
    }

    @Test
    @DisplayName("Test \"get story title by it's id from response body\" method")
    void testTitleByGivenId() {
        //Given
        String expected = "Placemark is going open source and shutting down";
        //When
        String actual = HackerNews.newsTitle(38250459);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

}
