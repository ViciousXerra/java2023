package edu.hw4;

import java.util.Arrays;
import java.util.List;

final class DataProvider {

    private DataProvider() {

    }

    static List<Animal> provideTestWithData() {
        return Arrays.asList(
            new Animal("Julia", Animal.Type.SPIDER, Animal.Sex.F, 10, 1, 2, true),
            new Animal("Frank", Animal.Type.SPIDER, Animal.Sex.M, 12, 2, 1, false),
            new Animal("Sally", Animal.Type.DOG, Animal.Sex.F, 7, 65, 15, false),
            new Animal("Leo", Animal.Type.DOG, Animal.Sex.M, 11, 110, 18, true),
            new Animal("Daisy", Animal.Type.CAT, Animal.Sex.F, 5, 30, 5, false),
            new Animal("Atlas", Animal.Type.CAT, Animal.Sex.M, 6, 45, 7, false),
            new Animal("Coco", Animal.Type.BIRD, Animal.Sex.M, 13, 23, 4, true),
            new Animal("Bubbles Snow", Animal.Type.FISH, Animal.Sex.M, 3, 1, 1, false),
            null,
            new Animal("Caramel", Animal.Type.BIRD, null, 1, 1, 1, false),
            new Animal("Mr. Whiskers", Animal.Type.CAT, Animal.Sex.M, 0, -1, 0, true)
        );
    }

}
