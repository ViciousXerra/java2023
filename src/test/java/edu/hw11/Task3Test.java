package edu.hw11;

import edu.hw11.task3.FibonacciImplementation;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.assertj.core.api.Assertions.assertThat;

class Task3Test {

    public abstract static class FibonacciExample {
        public abstract long calculate(int num);
    }

    @Test
    @DisplayName("Test fibonacci number ByteCodeAppender.")
    void test() {
        int expected = 1;
        try (
            DynamicType.Unloaded<?> unloaded = new ByteBuddy()
                .subclass(FibonacciExample.class)
                .method(named("calculate"))
                .intercept(FibonacciImplementation.INSTANCE).make()
        ) {
            Class<?> loaded = unloaded.load(unloaded.getClass().getClassLoader()).getLoaded();
            int actual = (int) loaded.getConstructor().newInstance().getClass().getMethod("calculate").invoke(2);
            assertThat(actual).isEqualTo(expected);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
