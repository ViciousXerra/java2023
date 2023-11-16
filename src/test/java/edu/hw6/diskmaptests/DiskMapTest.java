package edu.hw6.diskmaptests;

import edu.hw6.DiskMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiskMapTest {

    private final static String NULL_RESTRICTION_MESSAGE = "%s can't be null.";

    private static DiskMap provideFilledDiskMap() {
        DiskMap map = new DiskMap();
        for (int i = 1; i <= 10; i++) {
            map.put(String.format("key%d", i), String.format("value%d", i));
        }
        return map;
    }

    @Test
    @DisplayName("Test provided map size.")
    void testMapSize() {
        //Given
        int expected = 10;
        //When
        Map<String, String> map = provideFilledDiskMap();
        int actual = map.size();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test provided map size subtraction.")
    void testMapSizeSubtraction() {
        //Given
        int expected = 8;
        //When
        Map<String, String> map = provideFilledDiskMap();
        map.remove("key3");
        map.remove("key3");
        map.remove("key7");
        map.remove("key7");
        int actual = map.size();
        //Then
        assertThat(actual).isEqualTo(expected);
        //When
        map.clear();
        //Then
        assertThat(map.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test provided map size addition.")
    void testMapSizeAddition() {
        //Given
        int expected = 13;
        //When
        Map<String, String> map = provideFilledDiskMap();
        map.put("key5", "replaced");
        map.put("key14", "new value");
        map.put("key16", "new value");
        map.put("key808", "new value");
        int actual = map.size();
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test key contains.")
    void testKeyContains() {
        //When
        Map<String, String> map = provideFilledDiskMap();
        //Then
        assertThat(map.containsKey("key10")).isTrue();
    }

    @Test
    @DisplayName("Test value contains.")
    void testValueContains() {
        //When
        Map<String, String> map = provideFilledDiskMap();
        //Then
        assertThat(map.containsValue("value1")).isTrue();
    }

    @Test
    @DisplayName("Test keySet() method.")
    void testKeySet() {
        //Given
        Set<String> expected = Set.of(
            "key1", "key2",
            "key3", "key4",
            "key5", "key6",
            "key7", "key8",
            "key9", "key10"
        );
        Map<String, String> diskMap = provideFilledDiskMap();
        diskMap.put("key5", "replaced");
        Set<String> actual = diskMap.keySet();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test values() method.")
    void testValues() {
        //Given
        List<String> expected = List.of(
            "value1", "value2",
            "value3", "value4",
            "replaced", "value6",
            "value7", "value8",
            "value9", "value10",
            "new value"
        );
        Map<String, String> diskMap = provideFilledDiskMap();
        diskMap.put("key5", "replaced");
        diskMap.put("key11", "new value");
        Collection<String> actual = diskMap.values();
        assertThat(actual).containsExactlyInAnyOrder(expected.toArray(new String[expected.size()]));
    }

    @Test
    @DisplayName("Test passing null key.")
    void testNullKey() {
        assertThatThrownBy(() -> {
            Map<String, String> map = new DiskMap();
            map.put(null, "test");
        })
            .isInstanceOf(NullPointerException.class)
            .hasMessage(String.format(NULL_RESTRICTION_MESSAGE, "Key"));
    }

    @Test
    @DisplayName("Test passing null value.")
    void testNullValue() {
        assertThatThrownBy(() -> {
            Map<String, String> map = new DiskMap();
            map.put("test", null);
        })
            .isInstanceOf(NullPointerException.class)
            .hasMessage(String.format(NULL_RESTRICTION_MESSAGE, "Value"));
    }

    @Test
    @DisplayName("Test passing null arg to putAll method.")
    void testPassingNullToPutAll() {
        assertThatThrownBy(() -> {
            Map<String, String> map = new DiskMap();
            map.putAll(null);
        })
            .isInstanceOf(NullPointerException.class)
            .hasMessage(String.format(NULL_RESTRICTION_MESSAGE, "Map argument"));
    }

    @Test
    @DisplayName("Test passing null keys to putAll method.")
    void testPassingNullKeysTOPutAll() {
        assertThatThrownBy(() -> {
            Map<String, String> map = new DiskMap();
            map.putAll(new HashMap<>(){
                {
                    put(null, "test");
                }
            });
        })
            .isInstanceOf(NullPointerException.class)
            .hasMessage(String.format(NULL_RESTRICTION_MESSAGE, "Keys or values"));
    }

    @Test
    @DisplayName("Test putAll method.")
    void testPutAll() {
        //Given
        Map<String, String> expected = Map.of(
            "Potato", "Vegetable",
            "Peach", "Fruit",
            "Cucumber", "Vegetable",
            "Carrot", "Vegetable",
            "Banana", "Fruit"
        );
        //When
        Map<String, String> actual = new DiskMap();
        actual.putAll(expected);
        //Then
        assertThat(actual).containsExactlyInAnyOrderEntriesOf(expected);
    }
}



