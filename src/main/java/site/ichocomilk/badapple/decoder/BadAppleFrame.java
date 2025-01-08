package site.ichocomilk.badapple.decoder;

import java.awt.image.BufferedImage;

public record BadAppleFrame(
    BufferedImage image,
    int width,
    int height
) {}