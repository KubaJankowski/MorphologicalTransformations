package com.jjowsky.transformations;

import java.awt.image.BufferedImage;

public class OpenImage {
    public static BufferedImage dylate(int[][] pixels, int radius) {
        int width = pixels[0].length;
        int height = pixels.length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        for (int y = radius; y < height - radius; y++) {
            for (int x = radius; x < width - radius; x++) {
                int rgb = (255 << 16) | (255 << 8) | 255;

                for (int i = -radius; i < radius; i++) {
                    for (int j = -radius; j < radius; j++) {
                        if (i * i + j * j <= radius * radius) {
                            int pixel = pixels[j + y][i + x];
                            int value = (pixel & 0xff);
                            int currentRGB = (value << 16) | (value << 8) | value;

                            if (value < (rgb & 0xff))
                                rgb = currentRGB;
                        }
                    }

                }
                img.setRGB(x, y, rgb);
            }
        }
        return img;
    }

    public static BufferedImage erode(int[][] pixels, int radius) {
        int width = pixels[0].length;
        int height = pixels.length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        ;

        for (int y = radius; y < height - radius; y++) {
            for (int x = radius; x < width - radius; x++) {
                int rgb = (0 << 16) | (0 << 8) | 0;

                for (int i = -radius; i < radius; i++) {
                    for (int j = -radius; j < radius; j++) {
                        if (i * i + j * j <= radius * radius) {
                            int pixel = pixels[j + y][i + x];
                            int value = (pixel & 0xff);
                            int currentRGB = (value << 16) | (value << 8) | value;

                            if (value > (rgb & 0xff))
                                rgb = currentRGB;
                        }
                    }

                }
                img.setRGB(x, y, rgb);
            }
        }
        return img;
    }

    public static BufferedImage apply(int[][] pixels, int radius) {
        int[][] tmpPixels = Utils.convertTo2D(erode(pixels, radius));
        tmpPixels = Utils.convertTo2D(dylate(tmpPixels, radius));
        BufferedImage img = new BufferedImage(tmpPixels[0].length - 2*radius, tmpPixels.length - 2*radius, BufferedImage.TYPE_3BYTE_BGR);
        int rgb;

        for (int y = 0; y < tmpPixels.length - 2*radius; y++) {
            for (int x = 0; x < tmpPixels[y].length - 2*radius; x++) {
                int pixel;

                if (y < 2*radius || x < 2*radius)
                    pixel = pixels[y][x];
                else
                    pixel = tmpPixels[y][x];

                int value = (pixel & 0xff);
                rgb = (value << 16) | (value << 8) | value;
                img.setRGB(x, y, rgb);
            }
        }
        return img;
    }
}
