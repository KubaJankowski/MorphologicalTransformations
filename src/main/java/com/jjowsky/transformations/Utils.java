package com.jjowsky.transformations;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Utils {

    public static boolean isMonochrome(BufferedImage image) {
        final int type = image.getColorModel().getColorSpace().getType();
        final boolean isMonochrome = (type == ColorSpace.TYPE_GRAY || type == ColorSpace.CS_GRAY);

        return isMonochrome;
    }

    public static int[][] convertTo2D(BufferedImage image) {
        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];

        if (isMonochrome(image)) {
            final int pixelLength = 1;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int grey = ((int) pixels[pixel] & 0xff); // blue
                result[row][col] = grey;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
            return result;
        }

        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }
        return result;
    }

    public static int[] getColorMinMax(int[][] pixels, String flag) {
        int min = 255;
        int max = 0;

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                int pixel = pixels[y][x];
                int alpha = (pixel >> 24 & 0xff);
                int red = (pixel >> 16 & 0xff);
                int green = (pixel >> 8 & 0xff);
                int blue = (pixel & 0xff);

                switch (flag) {
                    case "red": {
                        min = ((red < min) ? red : min);
                        max = ((red > max) ? red : max);
                    }
                    case "blue": {
                        min = ((blue < min) ? blue : min);
                        max = ((blue > max) ? blue : max);
                    }
                    case "green": {
                        min = ((green < min) ? green : min);
                        max = ((green > max) ? green : max);
                    }
                }
            }
        }
        int[] result = {min, max};
        return result;
    }
}
