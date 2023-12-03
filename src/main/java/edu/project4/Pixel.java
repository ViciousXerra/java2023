package edu.project4;

public record Pixel(int r, int g, int b, long hitCount, double normal) {
    public Pixel(int r, int g, int b, long hitCount) {
        this(r, g, b, hitCount, 0);
    }

}
