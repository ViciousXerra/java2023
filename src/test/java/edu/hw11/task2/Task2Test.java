package edu.hw11.task2;


import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.assertj.core.api.Assertions.assertThat;

class Task2Test {

    static class ArithmeticUtilsInterceptor {

        public static int multiply(int a, int b) {
            return a * b;
        }

    }

    @BeforeAll
    public static void setup() {
        ByteBuddyAgent.install();
        try (
            DynamicType.Unloaded<?> unloaded = new ByteBuddy()
                .redefine(ArithmeticUtils.class)
                .method(named("sum"))
                .intercept(MethodDelegation.to(ArithmeticUtilsInterceptor.class))
                .make()
        ) {
            unloaded.load(unloaded.getClass().getClassLoader(), ClassReloadingStrategy.fromInstalledAgent()).getLoaded();
        }
    }

    private static Object[][] provideTestMethod() {
        return new Object[][] {
            {-10, 10, -100},
            {5, 4, 20},
            {1, 1, 1},
            {15, 3, 45},
            {-50, -10, 500},
            {250, -5, -1250},
        };
    }

    @ParameterizedTest
    @MethodSource("provideTestMethod")
    @DisplayName("Test method original call and interceptor calls.")
    void testOriginalMethod(int value1, int value2, int expectedMultiply) {
        //Then
        assertThat(ArithmeticUtils.sum(value1, value2)).isEqualTo(expectedMultiply);
    }

}
