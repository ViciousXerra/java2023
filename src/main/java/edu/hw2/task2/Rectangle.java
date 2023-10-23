package edu.hw2.task2;

public class Rectangle {

    private final static int DEFAULT_RECTANGLE_WIDTH = 30;
    private final static int DEFAULT_RECTANGLE_HEIGHT = 20;

    private final int width;
    private final int height;

    public Rectangle() {
        this(DEFAULT_RECTANGLE_WIDTH, DEFAULT_RECTANGLE_HEIGHT);
    }

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Rectangle setWidth(int width) {
        return new Rectangle(width, this.height);
    }

    public Rectangle setHeight(int height) {
        return new Rectangle(this.width, height);
    }

    public final double area() {
        return width * height;
    }

}
