package edu.hw10.task2;

import edu.hw10.task2.cacheproxy.CacheProxy;
import edu.hw10.task2.testsamples.BasicNumberDivider;
import edu.hw10.task2.testsamples.Calculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CacheProxyTest {

    private static Object[][] provideNullTestMethod() {
        return new Object[][] {
            {
                null,
                null
            },
            {
                new BasicNumberDivider(),
                null
            },
            {
                null,
                Calculator.class
            }
        };
    }

    @ParameterizedTest
    @MethodSource("provideNullTestMethod")
    @DisplayName("test null args.")
    void testNull(Object proxyTo, Class<?> proxyType) {
        assertThatThrownBy(() -> CacheProxy.getCacheProxyInstance(proxyTo, proxyType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Arguments must be not null.");
    }

    @Test
    @DisplayName("Test fibonacci cache.")
    void testFibonacciCache() {

    }


    @Test
    @DisplayName("Test number divider cache.")
    void testNumberDividerCache() {

    }


}
