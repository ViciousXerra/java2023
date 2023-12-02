package edu.hw8.task3;

import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MessageDigestPasswordHashBruteForceTest {

    private final static Path INPUT_FILE_PATH = Path.of("src/test/resources/hw8testresources/hashedpasswords");

    @Test
    @DisplayName("Test pre-prepared MD5 hash with single thread based algorithm.")
    void testSingleThread() {
        //Given
        Map<String, String> expected = Map.of(
            "a.v.petrov", "0Ax6t",
            "v.v.belov", "5a2Iz",
            "i.i.ivanov", "0xjAp",
            "k.p.maslov", "1fGtT"
        );
        //When
        PasswordBruteForce pbf = new SingleThreadMdHashPasswordBruteForce(INPUT_FILE_PATH, 5);
        Map<String, String> actual = pbf.extractPasswords();
        //Then
        assertThat(actual).containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    @DisplayName("Test pre-prepared MD5 hash with multi thread based algorithm.")
    void testMultiThread() {
        //Given
        Map<String, String> expected = Map.of(
            "a.v.petrov", "0Ax6t",
            "v.v.belov", "5a2Iz",
            "i.i.ivanov", "0xjAp",
            "k.p.maslov", "1fGtT"
        );
        //When
        PasswordBruteForce pbf = new MultiThreadMdHashPasswordBruteForce(INPUT_FILE_PATH, 5);
        Map<String, String> actual = pbf.extractPasswords();
        //Then
        assertThat(actual).containsExactlyInAnyOrderEntriesOf(expected);
    }

}
