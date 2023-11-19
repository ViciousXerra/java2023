package edu.project3;

import edu.project3.logstreamextractors.LocalFileLogStreamExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LocalFileUnusualBehaviourTest {

    @Test
    @DisplayName("Unexisting file test.")
    void testUnexistingFile() {
        assertThatThrownBy(() -> new LocalFileLogStreamExtractor("src/test/resources/project3resources/testinputs"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Unable to resolve path. Please, specify your path arguments.");
    }

}
