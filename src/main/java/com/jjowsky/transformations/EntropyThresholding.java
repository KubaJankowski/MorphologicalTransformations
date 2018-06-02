package com.jjowsky.transformations;

import java.awt.image.BufferedImage;

public class EntropyThresholding {
    public static BufferedImage applyRGB(int[][] pixels) {
        BufferedImage img = Utils.toGreyScale(pixels);
        pixels = Utils.convertTo2D(img);

        double[] histogram = new double[256];
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                int pixel = pixels[y][x];
                int value = (pixel >> 16 & 0xff);
                histogram[value]++;
            }
        }
        for (int i = 0; i < 256; i++)
            histogram[i] /= pixels.length * pixels[0].length;

        double[] cumulative = new double[256];
        double sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += histogram[i];
            cumulative[i] = sum;
        }

        double[] entropyLow = new double[256];
        double[] entropyHigh = new double[256];
        for (int th = 0; th < 256; th++) {
            double lowSum = cumulative[th];
            if (lowSum > 0) {
                for (int i = 0; i <= th; i++) {
                    if (histogram[i] > 0)
                        entropyLow[th] -= histogram[i] * Utils.log((histogram[i] / lowSum), 2);
                }
            }
            entropyLow[th] /= lowSum;

            double highSum = cumulative[255] - cumulative[th];
            if (highSum > 0) {
                for (int i = th + 1; i < 256; i++) {
                    if (histogram[i] > 0)
                        entropyHigh[th] -= histogram[i] * Utils.log((histogram[i] / highSum), 2);
                }
            }
            entropyHigh[th] /= highSum;
        }

        double max = entropyHigh[0];
        int threshold = 0;

        for (int k = 1; k < 256; k++) {
            double h = entropyLow[k] + entropyHigh[k];
            if (h > max) {
                max = h;
                threshold = k;
            }
        }


        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                int pixel = pixels[y][x];
                int red = (pixel >> 16 & 0xff);
                int green = (pixel >> 8 & 0xff);
                int blue = (pixel & 0xff);

                if (red > threshold)
                    red = green = blue = 255;
                else
                    red = green = blue = 0;

                int rgb = (red << 16) | (green << 8) | blue;
                img.setRGB(x, y, rgb);
            }
        }
        return img;
    }

    public static BufferedImage applyMono(int[][] pixels) {
        BufferedImage img =  new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_3BYTE_BGR);

        double[] histogram = new double[256];
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                int pixel = pixels[y][x];
                int value = (pixel & 0xff);
                histogram[value]++;
            }
        }
        for (int i = 0; i < 256; i++)
            histogram[i] /= pixels.length * pixels[0].length;

        double[] cumulative = new double[256];
        double sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += histogram[i];
            cumulative[i] = sum;
        }

        double[] entropyLow = new double[256];
        double[] entropyHigh = new double[256];

        for (int th = 0; th < 256; th++) {
            double lowSum = cumulative[th];
            if (lowSum > 0) {
                for (int i = 0; i <= th; i++) {
                    if (histogram[i] > 0)
                        entropyLow[th] -= histogram[i] * Utils.log((histogram[i] / lowSum), 2);
                }
            }
            entropyLow[th] /= lowSum;

            double highSum = cumulative[255] - cumulative[th];
            if (highSum > 0) {
                for (int i = th + 1; i < 256; i++) {
                    if (histogram[i] > 0)
                        entropyHigh[th] -= histogram[i] * Utils.log((histogram[i] / highSum), 2);
                }
            }
            entropyHigh[th] /= highSum;
        }

        double max = entropyHigh[0];
        int threshold = 0;

        for (int k = 1; k < 256; k++) {
            double h = entropyLow[k] + entropyHigh[k];
            if (h > max) {
                max = h;
                threshold = k;
            }
        }


        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                int pixel = pixels[y][x];
                int value = (pixel & 0xff);

                if (value > threshold)
                    value = 255;
                else
                    value = 0;

                value = (value << 16) | (value << 8) | value;
                img.setRGB(x, y, value);
            }
        }
        return img;
    }
}
