package edu.project3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;

class Project3DemoTest {

    @Test
    @DisplayName("Demo run.")
    void test() {
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

}
