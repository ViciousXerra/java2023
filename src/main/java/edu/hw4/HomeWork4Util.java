package edu.hw4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public final class HomeWork4Util {

    private final static String INVALID_FIRST_COUNTERS = "The first counters must be greater or equal to 0.";
    private final static String EMPTY_OR_NULL_FILLED_LIST = "An empty or null-filled list was passed.";
    private final static String HEIGHT_RESTRICTION = "Height can't be less than or equal to 0.";
    private final static String AGE_RESTRICTION = "Age can't be negative.";
    private final static String AGE_RANGE_RESTRICTION = "Highest age must be higher than lowest age.";
    private final static String NAME_FORMAT_RESTRICTION =
        "\"Name\" field must contain characters and can't be followed with several whitespaces.";
    private final static String HEAVIEST_FISH_UNEXIST = "The heaviest fish does not exist.";
    private final static int ONE_METER = 100;


    private HomeWork4Util() {

    }

    //#1
    public static List<Animal> sortByHeight(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .sorted(Comparator.comparing(Animal::height))
            .toList();
    }

    //#2
    public static List<Animal> sortByDescentWeight(@NotNull List<Animal> list, int theFirstCounters) {
        if (theFirstCounters < 0) {
            throw new IllegalArgumentException(INVALID_FIRST_COUNTERS);
        }
        return getPreparedStream(list)
            .sorted(Comparator.comparing(Animal::weight).reversed())
            .limit(theFirstCounters)
            .toList();
    }

    //#3
    public static Map<Animal.Type, Integer> getAnimalTypeFrequencyMapping(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .collect(
                Collectors.toUnmodifiableMap(
                    Animal::type,
                    animalCount -> 1,
                    Integer::sum
                ));
    }

    //#4
    public static Animal getLongestName(@NotNull List<Animal> list) {
        Optional<Animal> animal =
            getPreparedStream(list)
                .reduce((
                    (animal1, animal2) -> animal1.name().length() > animal2.name().length() ? animal1 : animal2
                ));
        if (animal.isEmpty()) {
            throw new NoSuchElementException(EMPTY_OR_NULL_FILLED_LIST);
        } else {
            return animal.get();
        }
    }

    //#5
    public static Animal.Sex getPredominantSex(@NotNull List<Animal> list) {
        List<Animal> prepared = getPreparedStream(list).toList();
        long maleCount = prepared.stream().filter(a -> a.sex() == Animal.Sex.M).count();
        long femaleCount = prepared.stream().filter(a -> a.sex() == Animal.Sex.F).count();
        return maleCount > femaleCount ? Animal.Sex.M : Animal.Sex.F;
    }

    //#6
    public static Map<Animal.Type, Animal> getHeaviestAnimalOfEachType(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .collect(
                Collectors.toUnmodifiableMap(
                    Animal::type,
                    animal -> animal,
                    (animal1, animal2) -> animal1.weight() > animal2.weight() ? animal1 : animal2
                ));
    }

    //#7
    public static Animal getOldestAnimal(@NotNull List<Animal> list) {
        Optional<Animal> animal =
            getPreparedStream(list)
                .reduce((
                    (animal1, animal2) -> animal1.age() > animal2.age() ? animal1 : animal2
                ));
        if (animal.isEmpty()) {
            throw new NoSuchElementException(EMPTY_OR_NULL_FILLED_LIST);
        } else {
            return animal.get();
        }
    }

    //#8
    public static Optional<Animal> getHeaviestAnimalWithHeightLessThan(@NotNull List<Animal> list, int height) {
        if (height <= 0) {
            throw new IllegalArgumentException(HEIGHT_RESTRICTION);
        }
        return getPreparedStream(list)
            .filter(animal -> animal.height() < height)
            .reduce((
                (animal1, animal2) -> animal1.weight() > animal2.weight() ? animal1 : animal2
            ));
    }

    //#9
    public static Integer totalPawsCount(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .mapToInt(Animal::paws)
            .sum();
    }

    //#10
    public static List<Animal> getAnimalsWithMismatchOfPawsAndAgeCount(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .filter(animal -> animal.paws() != animal.age())
            .toList();
    }

    //#11
    public static List<Animal> getBitingAnimalWithHeightOfOneMeter(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .filter(animal -> animal.bites() && animal.height() > ONE_METER)
            .toList();
    }

    //#12
    public static Integer getAnimalsWhichWeightGreaterThanHeight(@NotNull List<Animal> list) {
        return (int) getPreparedStream(list)
            .filter(animal -> animal.weight() > animal.height())
            .count();
    }

    //#13
    public static List<Animal> getAnimalsWhichNameCountAtLeastTwoWords(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .filter(animal -> {
                String[] splitted = animal.name().split(" ");
                if (splitted.length == 2 && !splitted[0].isEmpty() && !splitted[1].isEmpty()) {
                    return true;
                } else {
                    throw new IllegalArgumentException(NAME_FORMAT_RESTRICTION);
                }
            })
            .toList();
    }

    //#14
    public static Boolean isListContainsDogsWithHeightGreaterThan(@NotNull List<Animal> list, int height) {
        if (height <= 0) {
            throw new IllegalArgumentException(HEIGHT_RESTRICTION);
        }
        long count = getPreparedStream(list)
            .filter(animal -> animal.type() == Animal.Type.DOG && animal.height() > height)
            .count();
        return count > 0;
    }

    //#15
    public static Map<Animal.Type, Integer> countTotalWeightOfAnimalsOfEachTypeInRangeOfAge(
        @NotNull List<Animal> list,
        int lowestAge,
        int highestAge
    ) {
        if (lowestAge < 0 || highestAge < 0) {
            throw new IllegalArgumentException(AGE_RESTRICTION);
        }
        if (lowestAge >= highestAge) {
            throw new IllegalArgumentException(AGE_RANGE_RESTRICTION);
        }
        return getPreparedStream(list)
            .filter(animal -> animal.age() >= lowestAge && animal.age() < highestAge)
            .collect(Collectors.toUnmodifiableMap(
                    Animal::type,
                    Animal::weight,
                    Integer::sum
                )
            );
    }

    //#16
    public static List<Animal> getSortedByTypeAndSexAndName(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .sorted(
                Comparator.comparing(Animal::type)
                    .thenComparing(Animal::sex)
                    .thenComparing(Animal::name)
            )
            .toList();
    }

    //#17
    public static Boolean isSpiderBitesMoreOftenThanDogs(@NotNull List<Animal> list) {
        List<Animal> preparedBites = getPreparedStream(list).filter(Animal::bites).toList();
        long spiderWhichBites = preparedBites
            .stream()
            .filter(animal -> animal.type() == Animal.Type.SPIDER)
            .count();
        long dogsWhichBites = preparedBites
            .stream()
            .filter(animal -> animal.type() == Animal.Type.DOG)
            .count();
        return spiderWhichBites > dogsWhichBites;
    }

    //#18
    public static Animal getHeaviestFish(@NotNull List<Animal>[] lists) {
        List<Animal> copy = new ArrayList<>();
        for (List<Animal> list : lists) {
            copy.addAll(list.stream().filter(animal -> animal.type() == Animal.Type.FISH).toList());
        }
        Optional<Animal> heaviestFish = getPreparedStream(copy)
            .max(Comparator.comparing(Animal::weight));
        if (heaviestFish.isEmpty()) {
            throw new NoSuchElementException(HEAVIEST_FISH_UNEXIST);
        } else {
            return heaviestFish.get();
        }
    }

    //#19
    public static Map<String, Set<ValidationError>> getInstanceErrors(@NotNull List<Animal> list) {
        return getStreamOfInvalidInstances(list)
            .collect(Collectors.toUnmodifiableMap(
                    Animal::name,
                    ValidationError::getErrors
                )
            );
    }

    //#20
    public static Map<String, String> getInstanceErrorsDescription(@NotNull List<Animal> list) {
        return getStreamOfInvalidInstances(list)
            .collect(Collectors.toUnmodifiableMap(
                    Animal::name,
                    animal -> {
                        Set<ValidationError> errors = ValidationError.getErrors(animal);
                        StringBuilder builder = new StringBuilder();
                        errors.forEach(builder::append);
                        return builder.toString();
                    }
                )
            );
    }

    private static Stream<Animal> getPreparedStream(List<Animal> list) {
        return list
            .stream()
            .filter(Objects::nonNull)
            .filter(HomeWork4Util::isValidRecord);
    }

    private static Stream<Animal> getStreamOfInvalidInstances(List<Animal> list) {
        return list
            .stream()
            .filter(Objects::nonNull)
            .filter(Predicate.not(HomeWork4Util::isValidRecord));
    }

    private static boolean isValidRecord(Animal animal) {
        return isValidRefs(animal) && isValidPrimitives(animal);
    }

    private static boolean isValidRefs(Animal animal) {
        return animal.name() != null && animal.type() != null && animal.sex() != null;
    }

    private static boolean isValidPrimitives(Animal animal) {
        return animal.age() > 0 && animal.height() > 0 && animal.weight() > 0;
    }

}
