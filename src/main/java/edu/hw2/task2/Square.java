package edu.hw2.task2;

public final class Square extends Rectangle {

    private final static int DEFAULT_SQUARE_SIZE = 20;

    public Square() {
        this(DEFAULT_SQUARE_SIZE);
    }

    public Square(int side) {
        super(side, side);
    }

    @Override
    public Rectangle setWidth(int width) {
        return super.setWidth(width);
    }

    @Override
    public Rectangle setHeight(int height) {
        return super.setHeight(height);
    }

}
