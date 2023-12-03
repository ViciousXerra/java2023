package edu.project4;

import edu.project4.renderers.plotters.Pixel;
import edu.project4.renderers.plotters.PixelsImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ImageSaverUtils {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static Pattern EXTENSION_PATTERN = Pattern.compile("\\.(\\w+)");
    private final static int EXTENSION_GROUP = 1;
    private static final int GREEN_BYTE_SHIFT = 8;
    private static final int RED_BYTE_SHIFT = 16;

    private ImageSaverUtils() {

    }

    static void saveImage(PixelsImage image, Path filepath) {
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
            if ("jpeg".equals(extension)) {
                return "jpeg";
            } else if ("png".equals(extension)) {
                return "png";
            } else if ("bmp".equals(extension)){
                return "bmp";
            } else {
                throw new IllegalArgumentException("This file extension is not supported.");
            }
        } else {
            throw new IllegalArgumentException("File extension is not specified.");
        }
    }

}
