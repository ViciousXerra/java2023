package edu.hw11;

import java.lang.reflect.InvocationTargetException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class Task1Test {

    @Test
    @DisplayName("Test ByteBuddy \"hello world\"")
    void testByteBuddy() {
        //Given
        String expected = "Hello, ByteBuddy!";
        //When
        try (
            DynamicType.Unloaded<Object> unloaded = new ByteBuddy()
            .subclass(Object.class)
            .method(ElementMatchers.isToString())
            .intercept(FixedValue.value(expected))
            .make()
        ) {
            Class<?> dynamicTypeClass = unloaded.load(unloaded.getClass().getClassLoader()).getLoaded();
            String actual = dynamicTypeClass.getDeclaredConstructor().newInstance().toString();
            //Then
            assertThat(actual).isEqualTo(expected);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Unable to instantiate dynamic type.", e);
        }
    }

}
