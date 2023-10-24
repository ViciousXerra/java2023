package edu.hw4;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
                    frequency -> 1,
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


}
