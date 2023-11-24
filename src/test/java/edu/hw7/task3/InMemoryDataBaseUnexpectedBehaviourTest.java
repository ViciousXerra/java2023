package edu.hw7.task3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryDataBaseUnexpectedBehaviourTest {

    private final static String NULL_PERSON_MESSAGE = "Unable to add invalid Person arg.";
    private final static String NULL_KEY_MESSAGE = "Key must not to be null.";

    @Test
    @DisplayName("Test passing null person.")
    void testNullPerson() {
        assertThatThrownBy(() -> SyncInMemoryDataBase.getDataBaseInstance().add(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_PERSON_MESSAGE);

        assertThatThrownBy(() -> LockMemoryDataBase.getDataBaseInstance().add(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_PERSON_MESSAGE);
    }

    @Test
    @DisplayName("Test passing null fields of person record.")
    void testNullFields() {
        assertThatThrownBy(() ->
            SyncInMemoryDataBase
                .getDataBaseInstance()
                .add(new Person(1, null, "address", "")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_PERSON_MESSAGE);

        assertThatThrownBy(() ->
            LockMemoryDataBase
                .getDataBaseInstance()
                .add(new Person(1, "Vasya", "in a middle of nowhere", null)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_PERSON_MESSAGE);
    }

    @Test
    @DisplayName("Test passing null keys.")
    void testNullKeys() {
        assertThatThrownBy(() ->
            SyncInMemoryDataBase
                .getDataBaseInstance()
                .findByAddress(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_KEY_MESSAGE);

        assertThatThrownBy(() ->
            LockMemoryDataBase
                .getDataBaseInstance()
                .findByName(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NULL_KEY_MESSAGE);
    }

}
