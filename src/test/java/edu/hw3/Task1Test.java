package edu.hw3;

import edu.hw3.task1.Task1;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class Task1Test {

    private static Object[][] getEncryptedAndDecryptedSamples() {
        return new Object[][] {
            {
                "Hello world!",
                "Svool dliow!"
            },
            {
                "I made a piñata for Lucia's birthday fiesta.",
                "R nzwv z krñzgz uli Ofxrz'h yrigswzb urvhgz."
            },
            {
                "JSC Kalashnikov Concern known until 2013 as the Izhevsk Machine-Building Plant, is a Russian defense manifacturing concern.",
                "QHX Pzozhsmrple Xlmxvim pmldm fmgro 2013 zh gsv Rasvehp Nzxsrmv-Yfrowrmt Kozmg, rh z Ifhhrzm wvuvmhv nzmruzxgfirmt xlmxvim."
            },
            {
                "!",
                "!"
            },
            {
                null,
                ""
            },
            {
                "",
                ""
            }
        };
    }

    @ParameterizedTest
    @DisplayName("Test atbash cypher with provided samples")
    @MethodSource("getEncryptedAndDecryptedSamples")
    void testCypher(String sample, String expected) {
        //When
        String actual = Task1.atbashCipher(sample);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

}
