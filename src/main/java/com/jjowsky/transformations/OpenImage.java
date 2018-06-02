package com.jjowsky.transformations;

import java.awt.image.BufferedImage;

public class OpenImage {
    public static BufferedImage dylate(int[][] pixels, int radius) {
        int width = pixels[0].length;
        int height = pixels.length;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        // BufferedImage img = Utils.toGreyScale(pixels);
        //pixels = Utils.convertTo2D(img);

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
        //BufferedImage img = Utils.toGreyScale(pixels);
        //pixels = Utils.convertTo2D(img);

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

    public static BufferedImage applyToLogical(int[][] pixels, int radius) {
        BufferedImage img = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_3BYTE_BGR);
        int[][] tmpPixels = Utils.convertTo2D(erode(pixels, radius));
        tmpPixels = Utils.convertTo2D(dylate(tmpPixels, radius));
        int rgb;

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                if ((x > radius + 1 && y > radius + 1) && (x < pixels[y].length - radius - 1 && y < pixels.length - radius - 1)) {
                    int pixel = tmpPixels[y][x];
                    int value = (pixel & 0xff);
                    rgb = (value << 16) | (value << 8) | value;

                } else {
                    int pixel = pixels[y][x];
                    int value = (pixel & 0xff);
                    rgb = (value << 16) | (value << 8) | value;
                }
                img.setRGB(x, y, rgb);
            }
        }
        return img;
    }
}
