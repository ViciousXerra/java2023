package edu.hw11.task2;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.assertj.core.api.Assertions.assertThat;

class Task2Test {

    private static Class<?> clazz;
    private static Object instance;

    static class ArithmeticUtilsInterceptor {

        public int sum(int a, int b) {
            return a * b;
        }

    }

    @BeforeAll
    public static void setup() {
        try (
            DynamicType.Unloaded<?> unloaded = new ByteBuddy()
                .subclass(ArithmeticUtils.class)
                .method(named("sum"))
                .intercept(MethodDelegation.to(new ArithmeticUtilsInterceptor()))
                .make()
        ) {
            clazz = unloaded.load(
                unloaded.getClass().getClassLoader(),
                ClassLoadingStrategy.UsingLookup.of(
                    MethodHandles.privateLookupIn(ArithmeticUtils.class, MethodHandles.lookup())
                )
            ).getLoaded();
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
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
    void testOriginalMethod(int value1, int value2, int expectedMultiply)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //When
        int actual = (int) clazz.getMethod("sum", int.class, int.class).invoke(instance, value1, value2);
        //Then
        assertThat(actual).isEqualTo(expectedMultiply);
    }

}
