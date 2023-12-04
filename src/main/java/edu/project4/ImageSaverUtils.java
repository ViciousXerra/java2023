package edu.project4;

import edu.project4.plotters.Pixel;
import edu.project4.plotters.PixelsImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ImageSaverUtils {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static Pattern EXTENSION_PATTERN = Pattern.compile("\\.(\\w+)");
    private final static int EXTENSION_GROUP = 1;
    private final static int GREEN_BYTE_SHIFT = 8;
    private final static int RED_BYTE_SHIFT = 16;
    private final static String JPEG = "jpeg";
    private final static String PNG = "png";
    private final static String BMP = "bmp";

    private ImageSaverUtils() {

    }

    public static void saveImage(PixelsImage image, Path filepath) {
        try {
            File imageFile = Files.createFile(filepath).toFile();
            String extension = getExtension(filepath);
            BufferedImage pic = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_3BYTE_BGR);
            Pixel pixel;
            int color;
            for (int curWidth = 0; curWidth < image.width(); curWidth++) {
                for (int curHeight = 0; curHeight < image.height(); curHeight++) {
                    pixel = image.pixel(curWidth, curHeight);
                    color = pixel.b() | pixel.g() << GREEN_BYTE_SHIFT | pixel.r() << RED_BYTE_SHIFT;
                    pic.setRGB(curWidth, curHeight, color);
                }
            }
            ImageIO.write(pic, extension, imageFile);
        } catch (IOException e) {
            LOGGER.error(String.format("Caught exception: %s", e.getMessage()));
        }
    }

    private static String getExtension(Path filePath) {
        Matcher m = EXTENSION_PATTERN.matcher(filePath.toString());
        if (m.find()) {
            String extension = m.group(EXTENSION_GROUP);
            if (JPEG.equals(extension)) {
                return JPEG;
            } else if (PNG.equals(extension)) {
                return PNG;
            } else if (BMP.equals(extension)) {
                return BMP;
            } else {
                throw new IllegalArgumentException("This file extension is not supported.");
            }
        } else {
            throw new IllegalArgumentException("File extension is not specified.");
        }
    }

}
