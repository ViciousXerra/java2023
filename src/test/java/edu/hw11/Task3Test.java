package edu.hw11;

import edu.hw11.task3.FibonacciMeth;
import java.lang.reflect.InvocationTargetException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.Opcodes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

class Task3Test {

    private static Class<?> dynamicType;
    private static Object dynamicInst;

    @BeforeAll
    public static void setup()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try (DynamicType.Unloaded<?> unloaded = new ByteBuddy().subclass(Object.class)
            .name("FibonacciSolver")
            .defineMethod("calculateFibonacciNum", long.class, Opcodes.ACC_PUBLIC)
            .withParameter(long.class)
            .intercept(
                new Implementation.Simple(new FibonacciMeth())
            ).make()) {
            dynamicType = unloaded.load(unloaded.getClass().getClassLoader()).getLoaded();
            dynamicInst = dynamicType.getDeclaredConstructor().newInstance();
        }
    }

    private static Object[][] provideTest() {
        return new Object[][] {
            {0L, 0L},
            {-1L, 0L},
            {1L, 1L},
            {2L, 1L},
            {3L, 2L},
            {4L, 3L},
            {5L, 5L},
            {6L, 8L},
            {7L, 13L},
            {8L, 21L},
            {9L, 34L},
            {10L, 55L}
        };
    }

    @ParameterizedTest
    @MethodSource("provideTest")
    @DisplayName("Test dynamic type.")
    void testDynamicType(long input, long expected)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //When
        long actual = (long) dynamicType.getMethod("calculateFibonacciNum", long.class).invoke(dynamicInst, input);
        assertThat(actual).isEqualTo(expected);
    }
}
