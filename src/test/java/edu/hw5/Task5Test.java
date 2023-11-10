package edu.hw5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Task5Test {

    private final static String NULL_ID_MESSAGE = "Id can't be null.";
    private final static String EMPTY_ID_MESSAGE = "Vehicle id can't be empty.";

    private static Object[][] provideTestingMethodWithValidData() {
        return new Object[][] {
            {
                true,
                "А001АА777"
            },
            {
                true,
                "В123ВВ78"
            },
            {
                true,
                "Е567ЕЕ116"
            },
            {
                true,
                "К890КК45"
            },
            {
                true,
                "М999ММ178"
            },
            {
                false,
                "А01АА777"
            },
            {
                false,
                "Г001АА777"
            },
            {
                false,
                "А001БА777"
            },
            {
                false,
                "А001А077"
            },
            {
                false,
                "А001АА00"
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test vehicle id check method with valid data.")
    @MethodSource("provideTestingMethodWithValidData")
    void testPasswordCheckWithValidData(boolean expected, String vehicleId){
        //When
        boolean actual = Task5.isValidVehicleId(vehicleId);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Check passing null.")
    void testNullArg() {
        assertThatThrownBy(() -> Task5.isValidVehicleId(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_ID_MESSAGE);
    }

    @Test
    @DisplayName("Check passing empty string vehicleId.")
    void testEmptyStringPassword() {
        assertThatThrownBy(() -> Task5.isValidVehicleId(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(EMPTY_ID_MESSAGE);
    }

}
