package edu.project4;

public record PixelsImage(Pixel[][] data, int width, int height) {
    public static PixelsImage create(int width, int height) {
        Pixel[][] data = new Pixel[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] = new Pixel(0, 0, 0, 0L);
            }
        }
        return new PixelsImage(data, width, height);
    }
    boolean contains(int x, int y) {
        boolean inRangeAxisX = x >= 0 && x < width;
        boolean inRangeAxisY = y >= 0 && y < height;
        return inRangeAxisX && inRangeAxisY;
    }
    Pixel pixel(int x, int y) {
        return data[y][x];
    }
}
