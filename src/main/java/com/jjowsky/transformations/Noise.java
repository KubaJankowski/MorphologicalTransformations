package com.jjowsky.transformations;

import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Noise {
    public static double generateGaussian() {
        double temp1d;
        double temp2d = 0;
        double resultd;
        int pui = 1; //std

        while (pui > 0) {
            temp2d = ThreadLocalRandom.current().nextInt(0, 32767) / 32767d;

            if (temp2d == 0)
                pui = 1;
            else
                pui = -1;
        }

        temp1d = Math.cos((2.0 * Math.PI) * ThreadLocalRandom.current().nextInt(32767) / 32767d);
        resultd = Math.sqrt(-2.0 * Math.log(temp2d)) * temp1d;

        return  resultd;
    }

    public static BufferedImage addGaussianNoise(int[][] pixels, double std) {
        BufferedImage img = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_3BYTE_BGR);

        int[] minMaxRed = Utils.getColorMinMax(pixels, "red");
        int[] minMaxBlue = Utils.getColorMinMax(pixels, "blue");
        int[] minMaxGreen = Utils.getColorMinMax(pixels, "green");

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                int pixel = pixels[y][x];
                int alpha = (pixel >> 24 & 0xff);
                int red = (pixel >> 16 & 0xff);
                int green = (pixel >> 8 & 0xff);
                int blue = (pixel & 0xff);

                double redD = (double)red/255;
                double greenD = (double)green/255;
                double blueD = (double)blue/255;

                redD += Noise.generateGaussian() * std;
                greenD += Noise.generateGaussian() * std;
                blueD += Noise.generateGaussian() * std;

                red = (int)(redD * (minMaxRed[1] - minMaxRed[0]));
                green = (int)(greenD * (minMaxGreen[1] - minMaxGreen[0]));
                blue = (int)(blueD * (minMaxBlue[1] - minMaxBlue[0]));

                red = ((red > 255) ? 255 : red);
                green = ((green > 255) ? 255 : green);
                blue = ((blue > 255) ? 255 : blue);
                red = ((red < 0) ? 0 : red);
                green = ((green < 0) ? 0 : green);
                blue = ((blue < 0) ? 0 : blue);

                int rgb = (red << 16) | (green << 8) | blue;
                img.setRGB(x, y, rgb);
            }
        }
        return img;
    }

    public static BufferedImage addSaltPepperNoise(int[][] pixels, double std) {
        BufferedImage img = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_3BYTE_BGR);
        int size = pixels.length * pixels[0].length;
        Set<Integer> randomPixelsRed = new HashSet<>((int)(size * std));
        Set<Integer> randomPixelsGreen = new HashSet<>((int)(size * std));
        Set<Integer> randomPixelsBlue = new HashSet<>((int)(size * std));
        boolean redFlag = false;
        boolean greenFlag = false;
        boolean blueFlag = false;
        int currentIndex = 0;

        for (int i = 0; i < (int)(size * std);) {
            int number = ThreadLocalRandom.current().nextInt(0, size);
            if (randomPixelsRed.add(number)) {
                i++;
            }
        }
        for (int i = 0; i < (int)(size * std);) {
            int number = ThreadLocalRandom.current().nextInt(0, size);
            if (randomPixelsGreen.add(number)) {
                i++;
            }
        }
        for (int i = 0; i < (int)(size * std);) {
            int number = ThreadLocalRandom.current().nextInt(0, size);
            if (randomPixelsBlue.add(number)) {
                i++;
            }
        }

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                int pixel = pixels[y][x];
                int alpha = (pixel >> 24 & 0xff);
                int red = (pixel >> 16 & 0xff);
                int green = (pixel >> 8 & 0xff);
                int blue = (pixel & 0xff);

                if (randomPixelsRed.contains(currentIndex)) {
                    if (redFlag) {
                        red = 0;
                        redFlag = false;
                    }
                    else {
                        red = 255;
                        redFlag = true;
                    }
                }
                if (randomPixelsBlue.contains(currentIndex)) {
                    if (blueFlag) {
                        blue = 0;
                        blueFlag = false;
                    }
                    else {
                        blue = 255;
                        blueFlag = true;
                    }
                }
                if (randomPixelsGreen.contains(currentIndex)) {
                    if (greenFlag) {
                        green = 0;
                        greenFlag = false;
                    }
                    else {
                        green = 255;
                        greenFlag = true;
                    }
                }

                int rgb = (red << 16) | (green << 8) | blue;
                img.setRGB(x, y, rgb);

                currentIndex++;
            }
        }
        return img;
    }
}

