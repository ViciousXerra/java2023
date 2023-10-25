package edu.hw4;

import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class HomeWork4Util {

    private HomeWork4Util() {

    }

    public static List<Animal> sortByHeight(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .sorted(Comparator.comparing(Animal::height))
            .toList();
    }

    public static List<Animal> sortByDescentWeight(@NotNull List<Animal> list, int theFirstCounters) {
        if (theFirstCounters < 0) {
            throw new IllegalArgumentException("The first counters must be greater or equal to 0.");
        }
        return getPreparedStream(list)
            .sorted(Comparator.comparing(Animal::weight).reversed())
            .limit(theFirstCounters)
            .toList();
    }

    public static Map<Animal.Type, Integer> getAnimalTypeFrequencyMapping(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .collect(
                Collectors.toUnmodifiableMap(
                    Animal::type,
                    animalCount -> 1,
                    Integer::sum
                ));
    }

    public static Animal getLongestName(@NotNull List<Animal> list) {
        Optional<Animal> animal =
            getPreparedStream(list)
                .reduce((
                    (animal1, animal2) -> animal1.name().length() > animal2.name().length() ? animal1 : animal2
                ));
        if (animal.isEmpty()) {
            throw new NoSuchElementException("An empty or null-filled list was passed.");
        } else {
            return animal.get();
        }
    }

    public static Animal.Sex getPredominantSex(@NotNull List<Animal> list) {
        List<Animal> prepared = getPreparedStream(list).toList();
        long maleCount = prepared.stream().filter(a -> a.sex() == Animal.Sex.M).count();
        long femaleCount = prepared.stream().filter(a -> a.sex() == Animal.Sex.F).count();
        return maleCount > femaleCount ? Animal.Sex.M : Animal.Sex.F;
    }

    public static Map<Animal.Type, Animal> getHeaviestAnimalOfEachType(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .collect(
                Collectors.toUnmodifiableMap(
                    Animal::type,
                    animal -> animal,
                    (animal1, animal2) -> animal1.weight() > animal2.weight() ? animal1 : animal2
                ));
    }

    public static Animal getOldestAnimal(@NotNull List<Animal> list) {
        Optional<Animal> animal =
            getPreparedStream(list)
                .reduce((
                    (animal1, animal2) -> animal1.age() > animal2.age() ? animal1 : animal2
                ));
        if (animal.isEmpty()) {
            throw new NoSuchElementException("An empty or null-filled list was passed.");
        } else {
            return animal.get();
        }
    }

    public static Optional<Animal> getHeaviestAnimalWithHeightLessThan(@NotNull List<Animal> list, int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Height can't be less than or equal to 0.");
        }
        return getPreparedStream(list)
            .filter(animal -> animal.height() < height)
            .reduce((
                (animal1, animal2) -> animal1.weight() > animal2.weight() ? animal1 : animal2
            ));
    }

    public static Integer totalPawsCount(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .mapToInt(Animal::paws)
            .sum();
    }

    public static List<Animal> getAnimalsWithMismatchOfPawsAndAgeCount(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .filter(animal -> animal.paws() != animal.age())
            .toList();
    }

    public static List<Animal> getBitingAnimalWithHeightOfOneMeter(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .filter(animal -> animal.bites() && animal.height() > 100)
            .toList();
    }

    public static Integer getAnimalsWhichWeightGreaterThanHeight(@NotNull List<Animal> list) {
        return (int) getPreparedStream(list)
            .filter(animal -> animal.weight() > animal.height())
            .count();
    }

    public static List<Animal> getAnimalsWhichNameCountAtLeastTwoWords(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .filter(animal -> {
                String[] splitted = animal.name().split(" ");
                if (splitted.length == 2 && !splitted[0].isEmpty() && !splitted[1].isEmpty()) {
                    return true;
                } else {
                    throw new IllegalArgumentException(
                        "\"Name\" field of record \"Animal\" must contains characters and can't be followed with several whitespaces");
                }
            })
            .toList();
    }

    public static Boolean isListContainsDogsWithHeightGreaterThan(@NotNull List<Animal> list, int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Height can't be less than or equal to 0.");
        }
        long count = getPreparedStream(list)
            .filter(animal -> animal.type() == Animal.Type.DOG && animal.height() > height)
            .count();
        return count > 0;
    }

    public static Map<Animal.Type, Integer> countTotalWeightOfAnimalsOfEachTypeInRangeOfAge(
        @NotNull List<Animal> list,
        int lowestAge,
        int highestAge
    ) {
        if (lowestAge < 0 || highestAge < 0) {
            throw new IllegalArgumentException("Age can't be negative.");
        }
        if (lowestAge >= highestAge) {
            throw new IllegalArgumentException("Highest age must be higher than lowest age.");
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

    public static List<Animal> getSortedByTypeAndSexAndName(@NotNull List<Animal> list) {
        return getPreparedStream(list)
            .sorted(
                Comparator.comparing(Animal::type)
                    .thenComparing(Animal::sex)
                    .thenComparing(Animal::name)
            )
            .toList();
    }

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

    @SafeVarargs
    public static Animal getHeaviestFish(@NotNull List<Animal>... lists) {
        List<Animal> copy = new ArrayList<>();
        for (List<Animal> list : lists) {
            copy.addAll(list.stream().filter(animal -> animal.type() == Animal.Type.FISH).toList());
        }
        Optional<Animal> heaviestFish = getPreparedStream(copy)
            .max(Comparator.comparing(Animal::weight));
        if (heaviestFish.isEmpty()) {
            throw new NoSuchElementException("The heaviest fish does not exist.");
        } else {
            return heaviestFish.get();
        }
    }

    //------------------------------------------------------------------

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
