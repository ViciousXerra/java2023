package edu.hw10.task1;

import edu.hw10.task1.testSamples.TestClass1;
import edu.hw10.task1.testSamples.TestClass2;
import edu.hw10.task1.testSamples.TestClass3;
import edu.hw10.task1.testSamples.TestRecord;
import edu.hw10.task1.utils.RandomValueUtils;
import java.lang.reflect.Parameter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RandomValueUtilsTest {

    private final static Pattern PATTERN = Pattern.compile("[a-z]+");

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, 0})
    @DisplayName("Test non positive random string length.")
    void testNonPositiveRandomStringTargetInput(int targetInput) {
        assertThatThrownBy(() -> {
            RandomValueUtils.generateRandomString(targetInput);
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("String length must be a positive num.");
    }

    @ParameterizedTest
    @MethodSource("provideRandomStringTestMethod")
    @DisplayName("Test random string generation.")
    void testStringGeneration(int expectedLength, int targetInput) {
        //When
        String actual = RandomValueUtils.generateRandomString(targetInput);
        //Then
        Assertions.assertAll(
            () -> assertThat(actual.length()).isEqualTo(expectedLength),
            () -> {
                Matcher matcher = PATTERN.matcher(actual);
                assertThat(matcher.find()).isTrue();
            }
        );
    }

    @Test
    @DisplayName("Test passing null parameter.")
    void testNullParameter() {
        assertThatThrownBy(() -> RandomValueUtils.generateRandomInt(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Parameter instance can't be null.");
    }

    @RepeatedTest(10)
    @DisplayName("Test @MinValue and @MaxValue annotations on class.")
    void testRandomIntegerGenerationOnClass() {
        Assertions.assertAll(
            () -> assertThat((int) RandomValueUtils.generateRandomInt(TestClass1.class.getDeclaredConstructor(int.class)
                .getParameters()[0])).isBetween(0, Integer.MAX_VALUE),
            () -> assertThat((int) RandomValueUtils.generateRandomInt(TestClass2.class.getDeclaredConstructor(int.class)
                .getParameters()[0])).isBetween(Integer.MIN_VALUE, 0),
            () -> assertThat((int) RandomValueUtils.generateRandomInt(TestClass3.class.getDeclaredConstructor(int.class)
                .getParameters()[0])).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE)
            );
    }

    @RepeatedTest(10)
    @DisplayName("Test @MinValue and @MaxValue annotations on record.")
    void testRandomIntegerGenerationOnRecord() throws NoSuchMethodException {
        Parameter[] recParam =
            TestRecord.class.getDeclaredConstructor(int.class, int.class, int.class).getParameters();
        Assertions.assertAll(
            () -> assertThat((int) RandomValueUtils.generateRandomInt(recParam[0])).isBetween(
                Integer.MIN_VALUE,
                Integer.MAX_VALUE
            ),
            () -> assertThat((int) RandomValueUtils.generateRandomInt(recParam[1])).isBetween(
                0,
                Integer.MAX_VALUE
            ),
            () -> assertThat((int) RandomValueUtils.generateRandomInt(recParam[2])).isBetween(
                Integer.MIN_VALUE,
                0
            )
        );

    }

    private static Object[][] provideRandomStringTestMethod() {
        return new Object[][] {
            {5, 5},
            {10, 10},
            {4, 4}
        };
    }

}
