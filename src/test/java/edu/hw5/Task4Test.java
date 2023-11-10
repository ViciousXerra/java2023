package edu.hw5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task4Test {

    private final static String NULL_PASSWORD_MESSAGE = "Password can't be null.";
    private final static String EMPTY_PASSWORD_MESSAGE = "Password can't be empty.";

    private static Object[][] provideTestingMethodWithValidData() {
        return new Object[][] {
            {
                false,
                "password"
            },
            {
                true,
                "pass~word"
            },
            {
                true,
                "pass!word"
            },
            {
                true,
                "pass@word"
            },
            {
                true,
                "pass#word"
            },
            {
                true,
                "pass$word"
            },
            {
                true,
                "pass%word"
            },
            {
                true,
                "pass^word"
            },
            {
                true,
                "pass&word"
            },
            {
                true,
                "pass*word"
            },
            {
                true,
                "pass|word"
            },
            {
                true,
                "~!@#$%^&*|"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test password check method with valid data.")
    @MethodSource("provideTestingMethodWithValidData")
    void testPasswordCheckWithValidData(boolean expected, String password){
        //When
        boolean actual = Task4.isPasswordValid(password);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Check passing null.")
    void testNullArg() {
        assertThatThrownBy(() -> Task4.isPasswordValid(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_PASSWORD_MESSAGE);
    }

    @Test
    @DisplayName("Check passing empty string password.")
    void testEmptyStringPassword() {
        assertThatThrownBy(() -> Task4.isPasswordValid(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(EMPTY_PASSWORD_MESSAGE);
    }
}
