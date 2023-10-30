package edu.hw4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidationErrorTest {

    private final static String TYPE_FIELD_DESCRIPTION = "\"type\" field is null.\n";
    private final static String SEX_FIELD_DESCRIPTION = "\"sex\" field is null.\n";
    private final static String AGE_FIELD_DESCRIPTION = "\"age\" field is less or equal to 0.\n";
    private final static String HEIGHT_FIELD_DESCRIPTION = "\"height\" field is less or equal to 0.\n";
    private final static String WEIGHT_FIELD_DESCRIPTION = "\"weight\" field is less or equal to 0.\n";

    @Test
    @DisplayName("Test validation error class.")
    void testValidationErrorSet() {
        List<Animal> input = List.of(
            new Animal("Stella", null, Animal.Sex.F, 10, 10, 10, true),
            new Animal("Kitty", Animal.Type.CAT, null, 10, 10, 10, true),
            new Animal("Crunchy", Animal.Type.CAT, Animal.Sex.F, -1, 10, 10, true),
            new Animal("Luna", Animal.Type.CAT, Animal.Sex.F, 10, 0, 10, true),
            new Animal("Paula", Animal.Type.CAT, Animal.Sex.F, 10, 10, 0, true)
        );
        //Given
        Map<String, Set<ValidationError>> expected = new HashMap<>() {
            {
                put("Stella", ValidationError.getErrors(input.get(0)));
                put("Kitty", ValidationError.getErrors(input.get(1)));
                put("Crunchy", ValidationError.getErrors(input.get(2)));
                put("Luna", ValidationError.getErrors(input.get(3)));
                put("Paula", ValidationError.getErrors(input.get(4)));
            }
        };
        //When
        Map<String, Set<ValidationError>> actual = HomeWork4Util.getInstanceErrors(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Test sample with several errors occurs.")
    void testSeveralErrors() {
        //Given
        Map<String, String> expected = new HashMap<>() {
            {
                put(
                    "Sample1",
                    TYPE_FIELD_DESCRIPTION +
                        HEIGHT_FIELD_DESCRIPTION +
                        WEIGHT_FIELD_DESCRIPTION +
                        SEX_FIELD_DESCRIPTION +
                        AGE_FIELD_DESCRIPTION

                );
            }
        };
        //When
        Map<String, String> actual = HomeWork4Util.getInstanceErrorsDescription(List.of(
            new Animal("Sample1", null, null, -1, 0, 0, true)
        ));
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Passing null value to util method.")
    void testPassingNull() {
        assertThatThrownBy(() -> ValidationError.getErrors(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Animal value can't be null.\n");
    }

}
