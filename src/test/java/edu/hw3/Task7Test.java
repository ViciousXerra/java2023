package edu.hw3;

import edu.hw3.task7.Task7;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

class Task7Test {

    @Test
    @DisplayName("Test null key value existion in TreeMap type with custom comparator.")
    void testNullKeyExistion() {
        //Given
        Comparator<Integer> nullAllowedComparator = Task7.getNullableTreeMapComparator(Integer::compareTo);
        Map<Integer, String> map = new TreeMap<>(nullAllowedComparator);
        //When
        map.put(null, "testing");
        //Then
        assertThat(map.containsKey(null)).isTrue();
    }

}
