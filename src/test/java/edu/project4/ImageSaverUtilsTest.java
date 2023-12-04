package edu.project4;

import edu.project4.plotters.PixelsImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImageSaverUtilsTest {

    @Test
    @DisplayName("Test unresolving file exstension.")
    void testUnresolvingExtension() {
        assertThatThrownBy(() -> {
            Path path = Path.of("src/test/resources/project4testresources/testsample.idk");
            try {
                ImageSaverUtils.saveImage(
                    PixelsImage.create(1980, 1020),
                    path
                );
            } finally {
                Files.delete(path);
            }
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("This file extension is not supported.");
    }

    @Test
    @DisplayName("Test unspesified file exstension.")
    void testUnspesifiedExtension() {
        assertThatThrownBy(() -> {
            Path path = Path.of("src/test/resources/project4testresources/testsample");
            try {
                ImageSaverUtils.saveImage(
                    PixelsImage.create(1980, 1020),
                    path
                );
            } finally {
                Files.delete(path);
            }
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("File extension is not specified.");
    }

}
