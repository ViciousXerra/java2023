package edu.hw4;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CountingTest {

    private final static String EMPTY_OR_NULL_FILLED_LIST = "An empty or null-filled list was passed.";
    private final static String HEIGHT_RESTRICTION = "Height can't be less than or equal to 0.";
    private final static String NAME_FORMAT_RESTRICTION =
        "\"Name\" field must contain characters and can't be before the whitespace.";
    private final static String AGE_RESTRICTION = "Age can't be negative.";
    private final static String AGE_RANGE_RESTRICTION = "Highest age must be higher than lowest age.";

    @Test
    @Order(1)
    @DisplayName("Test animal type per each counting")
    void testEachtypeCounting() {
        //Given
        Map<Animal.Type, Integer> expected = new HashMap<>() {
            {
                put(Animal.Type.SPIDER, 2);
                put(Animal.Type.CAT, 2);
                put(Animal.Type.DOG, 2);
                put(Animal.Type.BIRD, 1);
                put(Animal.Type.FISH, 1);
            }
        };
        //When
        Map<Animal.Type, Integer> actual =
            HomeWork4Util.getAnimalTypeFrequencyMapping(DataProvider.provideTestWithData());
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(2)
    @DisplayName("Test longest name counting.")
    void testNameLengthCounting() {
        List<Animal> input = DataProvider.provideTestWithData();
        //Given
        Animal expected = input.get(7);
        //When
        Animal actual = HomeWork4Util.getLongestName(input);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(3)
    @DisplayName("Test empty list input on getLongestName() method.")
    void testEmptyListOnGetLongestNameMethod() {
        assertThatThrownBy(() -> HomeWork4Util.getLongestName(List.of()))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(EMPTY_OR_NULL_FILLED_LIST);
    }

    @Test
    @Order(4)
    @DisplayName("Test null-filled list input on getLongestName() method.")
    void testNullFilledListOnGetLongestNameMethod() {
        Animal[] animals = new Animal[4];
        Arrays.fill(animals, null);
        assertThatThrownBy(() -> HomeWork4Util.getLongestName(Arrays.asList(animals)))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(EMPTY_OR_NULL_FILLED_LIST);
    }

    @Test
    @Order(5)
    @DisplayName("Test getting predominant sex in list")
    void testPredominantSexMethod() {
        //Given
        Animal.Sex expected = Animal.Sex.M;
        //When
        Animal.Sex actual = HomeWork4Util.getPredominantSex(DataProvider.provideTestWithData());
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(6)
    @DisplayName("Test getting heaviest animal of each type.")
    void testHeaviestAnimalEachTypeMethod() {
        List<Animal> input = DataProvider.provideTestWithData();
        //Given
        Map<Animal.Type, Animal> expected = new HashMap<>() {
            {
                put(Animal.Type.SPIDER, input.get(0));
                put(Animal.Type.CAT, input.get(5));
                put(Animal.Type.DOG, input.get(3));
                put(Animal.Type.BIRD, input.get(6));
                put(Animal.Type.FISH, input.get(7));
            }
        };
        //When
        Map<Animal.Type, Animal> actual = HomeWork4Util.getHeaviestAnimalOfEachType(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(7)
    @DisplayName("Test getting oldest animal.")
    void testGettingOldestAnimal() {
        List<Animal> input = DataProvider.provideTestWithData();
        //Given
        Animal expected = input.get(6);
        //When
        Animal actual = HomeWork4Util.getOldestAnimal(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(8)
    @DisplayName("Test empty list input on getOldestAnimal() method.")
    void testEmptyListOnGetOldestAnimalMethod() {
        assertThatThrownBy(() -> HomeWork4Util.getOldestAnimal(List.of()))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(EMPTY_OR_NULL_FILLED_LIST);
    }

    @Test
    @Order(9)
    @DisplayName("Test null-filled list input on getOldestAnimal() method.")
    void testNullFilledListOnGetOldestAnimalMethod() {
        Animal[] animals = new Animal[4];
        Arrays.fill(animals, null);
        assertThatThrownBy(() -> HomeWork4Util.getOldestAnimal(Arrays.asList(animals)))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(EMPTY_OR_NULL_FILLED_LIST);
    }

    @Test
    @Order(10)
    @DisplayName("Test getting heaviest animal with height less than.")
    void testGettingHeaviestAnimalWithHeightLessThan() {
        List<Animal> input = DataProvider.provideTestWithData();
        int heightLimit = 3;
        //Given
        Animal expected = input.get(0);
        //When
        Optional<Animal> actual = HomeWork4Util.getHeaviestAnimalWithHeightLessThan(input, heightLimit);
        assertThat(actual).isPresent().get().isEqualTo(expected);
    }

    @Test
    @Order(11)
    @DisplayName("Test restricted height arg.")
    void testPassingRestrictedHeightArgToGetHeaviestAnimalMethod() {
        int heightLimit = 0;
        //Then
        assertThatThrownBy(() ->
            HomeWork4Util.getHeaviestAnimalWithHeightLessThan(DataProvider.provideTestWithData(), heightLimit))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(HEIGHT_RESTRICTION);
    }

    @Test
    @Order(12)
    @DisplayName("Test total paws counting.")
    void testTotalPawsCounting() {
        //Given
        int expected = 34;
        //When
        int actual = HomeWork4Util.totalPawsCount(DataProvider.provideTestWithData());
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(13)
    @DisplayName("Test getting animal with different age and paws count.")
    void testGettingListOfAnimalWithDifferentAgeAndPawsCount() {
        //Given
        List<Animal> expected = DataProvider.provideTestWithData().subList(0, 8);
        //When
        List<Animal> actual = HomeWork4Util.getAnimalsWithMismatchOfPawsAndAgeCount(DataProvider.provideTestWithData());
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(14)
    @DisplayName("Test getBitingAnimal() method")
    void testBitingAnimalsWithHeightOverOneMeterMethod() {
        List<Animal> input = DataProvider.provideTestWithData();
        //Given
        List<Animal> expected = input.subList(3, 4);
        //When
        List<Animal> actual = HomeWork4Util.getBitingAnimalsWithHeightOverOneMeter(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(15)
    @DisplayName("Test counting animals with weight greater than height method.")
    void testCountingAnimalsWithWeightGreaterThanHeightMethod() {
        //Given
        int expected = 1;
        //When
        int actual = HomeWork4Util.getAnimalsWhichWeightGreaterThanHeight(DataProvider.provideTestWithData());
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(16)
    @DisplayName("Test counting animals with name consisting at least of 2 words.")
    void testCountingWordsInAnimalName() {
        List<Animal> input = DataProvider.provideTestWithData();
        //Given
        List<Animal> expected = input.subList(7, 8);
        //When
        List<Animal> actual = HomeWork4Util.getAnimalsWhichNameCountAtLeastTwoWords(input);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(17)
    @DisplayName("Test counting animals with name consisting at least 2 words with invalid name.")
    void testCountingWordsInAnimalNameWithInvalidNameField() {
        List<Animal> input =
            List.of(new Animal(" James", Animal.Type.DOG, Animal.Sex.M, 1, 12, 5, false));
        //Then
        assertThatThrownBy(() -> HomeWork4Util.getAnimalsWhichNameCountAtLeastTwoWords(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NAME_FORMAT_RESTRICTION);
    }

    @Test
    @Order(18)
    @DisplayName("Test counting total weight of each type of animal in valid range of age.")
    void testTotalWeightCountOfEachTypeOfAnimalWithValidRangeOfAge() {
        int lowestAge = 5;
        int ageLimit = 10;
        //Given
        Map<Animal.Type, Integer> expected = new HashMap<>() {
            {
                put(Animal.Type.CAT, 12);
                put(Animal.Type.DOG, 15);
            }
        };
        //When
        Map<Animal.Type, Integer> actual =
            HomeWork4Util.countTotalWeightOfAnimalsOfEachTypeInRangeOfAge(
                DataProvider.provideTestWithData(),
                lowestAge,
                ageLimit
            );
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(19)
    @DisplayName("Test age restriction.")
    void testAgeRestriction() {
        int lowestAge = -1;
        int ageLimit = 10;
        assertThatThrownBy(() -> HomeWork4Util.countTotalWeightOfAnimalsOfEachTypeInRangeOfAge(
            DataProvider.provideTestWithData(),
            lowestAge,
            ageLimit
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(AGE_RESTRICTION);
    }

    @Test
    @Order(20)
    @DisplayName("Test age range restriction.")
    void testAgeRangeRestriction() {
        int lowestAge = 10;
        int ageLimit = 10;
        assertThatThrownBy(() -> HomeWork4Util.countTotalWeightOfAnimalsOfEachTypeInRangeOfAge(
            DataProvider.provideTestWithData(),
            lowestAge,
            ageLimit
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(AGE_RANGE_RESTRICTION);
    }

}
