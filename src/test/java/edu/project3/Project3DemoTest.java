package edu.project3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;

class Project3DemoTest {

    @Test
    @DisplayName("Demo url run.")
    void testUrl() {
        //When
        String[] args = new String[] {
            "--path",
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
            "--from",
            "2015-06-01",
            "--to",
            "2015-06-03",
            "--format",
            "adoc"
        };
        //Then
        assertThatCode(() -> Driver.execute(args))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Demo url run with simple date range.")
    void testUrlWithSingleTimeRange() {
        //When
        String[] args = new String[] {
            "--path",
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
            "--from",
            "2015-06-01",
            "--format",
            "markdown"
        };
        //Then
        assertThatCode(() -> Driver.execute(args))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Demo local file run.")
    void testLocal() {
        //When
        String[] args = new String[] {
            "--path",
            "src/test/resources/project3resources/testinputs/*.txt",
        };
        //Then
        assertThatCode(() -> Driver.execute(args))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Demo local file run with single date range.")
    void testLocalWithDateRange() {
        //When
        String[] args = new String[] {
            "--path",
            "src/test/resources/project3resources/testinputs/*.txt",
            "--to",
            "2015-07-01",
            "--format",
            "markdown"
        };
        //Then
        assertThatCode(() -> Driver.execute(args))
            .doesNotThrowAnyException();
    }

}
