package edu.hw10.task1;

import edu.hw10.task1.classes.AnotherClass;
import edu.hw10.task1.classes.Randomizable;
import edu.hw10.task1.classes.SomeClass;
import edu.hw10.task1.classes.SomeRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RandomObjectGeneratorTest {

    private final static ObjectGenerator OBJECT_GENERATOR = new RandomObjectGenerator();

    @Test
    @DisplayName("Test passing null Class instance.")
    void testNullClassInstance() {
        assertThatThrownBy(() -> OBJECT_GENERATOR.nextObject(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid length of method arguments.");
    }

    @Test
    @DisplayName("Test interface.")
    void testUnsafeCast() {
        assertThatThrownBy(() -> OBJECT_GENERATOR.nextObject(Randomizable.class))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Generator can't handle this type.");
    }

    @Test
    @DisplayName("Test creating SomeClass instance.")
    void testSomeClassInstance() {
        SomeClass object = (SomeClass) OBJECT_GENERATOR.nextObject(SomeClass.class);
        Assertions.assertAll(
            () -> assertThat(object.getStringValue()).isNotNull(),
            () -> assertThat(object.getIntValue()).isNotEqualTo(0),
            () -> assertThat(object.getIntValue()).isBetween(100, Integer.MAX_VALUE)
        );
    }

    @Test
    @DisplayName("Test creating AnotherClass instance.")
    void testAnotherClassInstance() {
        AnotherClass object = (AnotherClass) OBJECT_GENERATOR.nextObject(AnotherClass.class);
        assertThat(object.getValue()).isEqualTo(15);
    }

    @Test
    @DisplayName("Test creating SomeRecord instance.")
    void testSomeRecordInstance() {
        SomeRecord object = (SomeRecord) OBJECT_GENERATOR.nextObject(SomeRecord.class);
        Assertions.assertAll(
            () -> assertThat(object.value()).isNull(),
            () -> assertThat(object.value1()).isBetween(Integer.MIN_VALUE, 65535),
            () -> assertThat(object.value2()).isBetween(Integer.MIN_VALUE, Integer.MAX_VALUE)
        );
    }

}
