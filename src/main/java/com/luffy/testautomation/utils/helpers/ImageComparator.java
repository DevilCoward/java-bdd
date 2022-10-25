package com.luffy.testautomation.utils.helpers;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;

/** Compares images captured from elements screenshots. */
public class ImageComparator {

    private BufferedImage img1;

    private ImageComparator(BufferedImage img) {
        this.img1 = img;
    }

    public static ImageComparator imageFrom(WebElement element) {
        BufferedImage img = toBufferedImage(element.getScreenshotAs(OutputType.BYTES));
        return new ImageComparator(img);
    }

    private static BufferedImage toBufferedImage(byte[] bytes) {
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean matches(WebElement element) {
        BufferedImage img2 = toBufferedImage(element.getScreenshotAs(OutputType.BYTES));
        ImageComparisonResult result = new ImageComparison(img1, img2).compareImages();
        return result.getImageComparisonState() == ImageComparisonState.MATCH;
    }
}
