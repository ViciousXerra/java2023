package edu.project4;

import edu.project4.nonlineartransformations.NonLinearTransformation;
import edu.project4.nonlineartransformations.affinetransformations.AffineTranslation;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SingleThreadRenderer implements Renderer {

    private final String pathToSave;
    private PixelsImage image;

    public SingleThreadRenderer(String pathToSave) {
        this.pathToSave = pathToSave;
    }

    @Override
    public void render(
        int width,
        int height,
        int samples,
        int iterations,
        long seed,
        int affineTransformationCount,
        List<String> nonLinearKeys
    ) {
        validate(width, height, affineTransformationCount, nonLinearKeys);
        image = PixelsImage.create(width, height);
        double xMin = -(double) width / height;
        double xMax = -xMin;
        double yMin = -1;
        double yMax = 1;
        Dimension dim = new Dimension(2 * xMax, 2, xMin, yMin);
        //Generation
        List<NonLinearTransformation> transformations = getTransformationList(affineTransformationCount, nonLinearKeys);
        Random random = new Random(seed);
        for (int n = 0; n < samples; n++) {
            Point point = new Point(random.nextDouble(xMin, xMax), random.nextDouble(yMin, yMax));
            NonLinearTransformation transformation;
            int curWidth;
            int curHeight;
            for (int step = -20; step < iterations; step++) {
                transformation = transformations.get(random.nextInt(transformations.size()));
                point = transformation.apply(point);
                if (step >= 0 && dim.contains(point)) {
                    curWidth = width - (int) (((xMax - point.x()) / (xMax - xMin)) * width);
                    curHeight = height - (int) (((yMax - point.y()) / (yMax - yMin)) * height);
                    if (image.contains(curWidth, curHeight)) {
                        int red = transformation.getRedChannel();
                        int green = transformation.getGreenChannel();
                        int blue = transformation.getBlueChannel();
                        long hitCount = image.pixel(curWidth, curHeight).hitCount();
                        if (image.pixel(curWidth, curHeight).hitCount() != 0) {
                            red = ((image.pixel(curWidth, curHeight).r() + red) / 2);
                            green = ((image.pixel(curWidth, curHeight).g() + green) / 2);
                            blue = ((image.pixel(curWidth, curHeight).b() + blue) / 2);
                        }
                        hitCount++;
                        image.data()[curHeight][curWidth] = new Pixel(red, green, blue, hitCount);
                    }
                }
            }
        }
        //Correction
        double max = 0;
        double normal;
        double gamma = 2.2;
        Pixel p;
        for (int curWidth = 0; curWidth < width; curWidth++) {
            for (int curHeight = 0; curHeight < height; curHeight++) {
                if ((p = image.pixel(curWidth, curHeight)).hitCount() != 0) {
                    normal = Math.log10(p.hitCount());
                    image.data()[curHeight][curWidth] = new Pixel(p.r(), p.g(), p.b(), p.hitCount(), normal);
                    if (normal > max) {
                        max = normal;
                    }
                }
            }
        }
        int red;
        int green;
        int blue;
        for (int curWidth = 0; curWidth < width; curWidth++) {
            for (int curHeight = 0; curHeight < height; curHeight++) {
                normal = image.pixel(curWidth, curHeight).normal() / max;
                image.data()[curHeight][curWidth] = new Pixel(
                    image.pixel(curWidth, curHeight).r(),
                    image.pixel(curWidth, curHeight).g(),
                    image.pixel(curWidth, curHeight).b(),
                    image.pixel(curWidth, curHeight).hitCount(),
                    normal
                );
                red = (int) (image.pixel(curWidth, curHeight).r() * Math.pow(image.pixel(curWidth, curHeight).normal(), 1 / gamma));
                green = (int) (image.pixel(curWidth, curHeight).g() * Math.pow(image.pixel(curWidth, curHeight).normal(), 1 / gamma));
                blue = (int) (image.pixel(curWidth, curHeight).b() * Math.pow(image.pixel(curWidth, curHeight).normal(), 1 / gamma));
                image.data()[curHeight][curWidth] = new Pixel(
                    red,
                    green,
                    blue,
                    image.pixel(curWidth, curHeight).hitCount(),
                    normal
                );
            }
        }
    }

    @Override
    public void save() throws IOException {
        File file = Files.createFile(Path.of(pathToSave)).toFile();
        BufferedImage pic = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_3BYTE_BGR);
        Pixel pixel;
        int color;
        for (int curWidth = 0; curWidth < image.width(); curWidth++) {
            for (int curHeight = 0; curHeight < image.height(); curHeight++) {
                pixel = image.pixel(curWidth, curHeight);
                color = pixel.b() | pixel.g() << 8 | pixel.r() << 16;
                pic.setRGB(curWidth, curHeight, color);
            }
        }
        ImageIO.write(pic, "bmp", file);
    }

    private static void validate(int width, int height, int affineTransformationCount, List<String> nonLinearKeys) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Image resolution values must be a positive nums.");
        }
        if (affineTransformationCount <= 0) {
            throw new IllegalArgumentException("Affine transformation count argument must be at least 2.");
        }
        if (nonLinearKeys == null || nonLinearKeys.isEmpty()) {
            throw new IllegalArgumentException("Non-linear transformation keys must exist.");
        }
    }

    private static List<NonLinearTransformation> getTransformationList(int affineTranslationCount, List<String> keys) {
        List<AffineTranslation> translations = new ArrayList<>(affineTranslationCount);
        for (int i = 0; i < affineTranslationCount; i++) {
            translations.add(AffineTranslation.getInstance());
        }
        return keys
            .stream()
            .flatMap(key -> {
                List<NonLinearTransformation> list = new ArrayList<>();
                translations.forEach(affineTranslation -> list.add(NonLinearTransformation.getInstance(
                    affineTranslation,
                    key
                )));
                return list.stream();
            })
            .toList();
    }

}
