package edu.hw4;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public final class HomeWork4Util {

    private HomeWork4Util() {

    }

    public static List<Animal> sortByHeight(@NotNull List<Animal> list) {
        return list
            .stream()
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(Animal::height))
            .toList();
    }

    public static List<Animal> sortByDescentWeight(@NotNull List<Animal> list, int theFirstCounters) {
        if (theFirstCounters < 0) {
            throw new IllegalArgumentException("The first counters must be greater or equal to 0.");
        }
        return list
            .stream()
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(Animal::weight).reversed())
            .limit(theFirstCounters)
            .toList();
    }

    public static Map<Animal.Type, Integer> getAnimalTypeFrequencyMapping(@NotNull List<Animal> list) {
        return list
            .stream()
            .filter(Objects::nonNull)
            .collect(
                Collectors.toUnmodifiableMap(
                    Animal::type,
                    animalCount -> 1,
                    Integer::sum
                ));
    }

    public static Animal getLongestName(@NotNull List<Animal> list) {
        Optional<Animal> animal =
            list
                .stream()
                .filter(Objects::nonNull)
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
        long maleCount = list.stream().filter(Objects::nonNull).filter(a -> a.sex() == Animal.Sex.M).count();
        long femaleCount = list.stream().filter(Objects::nonNull).filter(a -> a.sex() == Animal.Sex.F).count();
        return maleCount > femaleCount ? Animal.Sex.M : Animal.Sex.F;
    }

    public static Map<Animal.Type, Animal> getHeaviestAnimalOfEachType(@NotNull List<Animal> list) {
        return list
            .stream()
            .filter(Objects::nonNull)
            .collect(
                Collectors.toUnmodifiableMap(
                    Animal::type,
                    animal -> animal,
                    (animal1, animal2) -> animal1.weight() > animal2.weight() ? animal1 : animal2
                ));
    }

    public static Animal getOldestAnimal(@NotNull List<Animal> list) {
        Optional<Animal> animal =
            list
                .stream()
                .filter(Objects::nonNull)
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
        return list
            .stream()
            .filter(Objects::nonNull)
            .filter(animal -> animal.height() < height)
            .reduce((
                (animal1, animal2) -> animal1.weight() > animal2.weight() ? animal1 : animal2
            ));
    }

    public static Integer totalPawsCount(@NotNull List<Animal> list) {
        return list
            .stream()
            .filter(Objects::nonNull)
            .mapToInt(Animal::paws)
            .sum();
    }

    public static List<Animal> getAnimalsWithMismatchOfPawsAndAgeCount(@NotNull List<Animal> list) {
        return list
            .stream()
            .filter(Objects::nonNull)
            .filter(animal -> animal.paws() != animal.age())
            .toList();
    }

    public static List<Animal> getBitingAnimalWithHeightOfOneMeter(@NotNull List<Animal> list) {
        return list
            .stream()
            .filter(Objects::nonNull)
            .filter(animal -> animal.bites() && animal.height() > 100)
            .toList();
    }

    public static Integer getAnimalsWhichWeightGreaterThanHeight(@NotNull List<Animal> list) {
        return (int) list
            .stream()
            .filter(Objects::nonNull)
            .filter(animal -> animal.weight() > animal.height())
            .count();
    }

    public static List<Animal> getAnimalsWhichNameCountAtLeastTwoWords(@NotNull List<Animal> list) {
        return list
            .stream()
            .filter(Objects::nonNull)
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
        long count = list
            .stream()
            .filter(Objects::nonNull)
            .filter(animal -> animal.type() == Animal.Type.DOG && animal.height() > height)
            .count();
        return count > 0;
    }

    public static Integer countTotalWeightOfAnimalsOfEachTypeInRangeOfAge(
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
        return list
            .stream()
            .filter(Objects::nonNull)
            .filter(animal -> animal.age() >= lowestAge && animal.age() < highestAge)
            .mapToInt(Animal::weight)
            .sum();
    }

    //------------------------------------------------------------------
    /*
    Нужны проверки на animal == null, можно написать метод или просто фильтровать в стриме, второе предпочтительнее
    Нужны проверки на ссылочные типы данных в рекорде, нужен метод.
    Нужны проверки на численные отрицательные примитивы, нужен метод
    Реализовать два метода снизу на генерацию валидных стримов и дефектных
    Рефакторить два метода сверху, используя методы генерации валидных стримов
     */

    /*private static Stream<Animal> getPreparedStream(List<Animal> list) {

    }*/

    /*private static Stream<Animal> getStreamOfInvalidInstances(List<Animal> list) {

    }*/

}
