package edu.hw4;

public record Animal(
    String name,
    Type type,
    Sex sex,
    int age,
    int height,
    int weight,
    boolean bites
) {

    private final static int CAT_PAWS_QUANTITY = 4;
    private final static int DOG_PAWS_QUANTITY = 4;
    private final static int BIRD_PAWS_QUANTITY = 2;
    private final static int FISH_PAWS_QUANTITY = 0;
    private final static int SPIDER_PAWS_QUANTITY = 4;

    enum Type {
        CAT, DOG, BIRD, FISH, SPIDER
    }

    enum Sex {
        M, F
    }

    public int paws() {
        return switch (type) {
            case CAT -> CAT_PAWS_QUANTITY;
            case DOG -> DOG_PAWS_QUANTITY;
            case BIRD -> BIRD_PAWS_QUANTITY;
            case FISH -> FISH_PAWS_QUANTITY;
            case SPIDER -> SPIDER_PAWS_QUANTITY;
        };
    }

}
