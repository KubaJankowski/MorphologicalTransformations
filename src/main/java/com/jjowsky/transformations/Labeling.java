package com.jjowsky.transformations;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Labeling {
    public static BufferedImage convertToLogical(int[][] pixels) {
        BufferedImage img = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                int pixel = pixels[y][x];
                int value = (pixel & 0xff);

                if (value >= 127)
                    value = (255 << 16) | (255 << 8) | 255;
                else
                    value = (0 << 16) | (0 << 8) | 0;

                img.setRGB(x, y, value);
            }
        }
        return img;
    }

    public static BufferedImage label(int[][] pixels) {
        BufferedImage img = convertToLogical(pixels);

        ArrayList<ArrayList<Integer>> linked = new ArrayList<>();
        int[][] labels = new int[pixels.length][pixels[0].length];
        int nextLabel = 0;

        for (int y = 1; y < pixels.length; y++) {          //bez 1 pixela na brzegu
            for (int x = 1; x < pixels[y].length; x++) {    //bez 1 pixela na brzegu
                int pixel = pixels[y][x];
                int value = (pixel & 0xff);

                if (value != 0) {
                    ArrayList<Integer> neighbors = new ArrayList<>();
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (y + i < 0 || x + j < 0 || y + i > labels.length - 1 || x + j > labels[0].length - 1) {
                                continue;
                            } else {
                                if (y + i == 0 && y + j == 0)
                                    continue;
                                if (labels[y + i][x + j] != 0)
                                    neighbors.add(labels[y + i][x + j]);
                            }
                        }
                    }
                    if (neighbors.size() == 0) {
                        ArrayList<Integer> tempArrayList = new ArrayList<>();
                        tempArrayList.add(nextLabel);
                        linked.add(nextLabel, tempArrayList);
                        labels[y][x] = nextLabel;
                        nextLabel++;
                    } else {
                        labels[y][x] = Integer.MAX_VALUE;
                        for (int neighbor : neighbors) {
                            if (neighbor < labels[y][x])
                                labels[y][x] = neighbor;
                        }
                        for (int neighbor : neighbors) {
                            linked.set(neighbor, Utils.union(linked.get(neighbor), neighbors));
                        }
                    }
                }
            }
        }

        for (int y = 1; y < pixels.length; y++) {          //bez 1 pixela na brzegu
            for (int x = 1; x < pixels[y].length; x++) {    //bez 1 pixela na brzegu
                ArrayList<Integer> equivalentLabels = linked.get(labels[y][x]);
                labels[y][x] = Integer.MAX_VALUE;
                for (int label : equivalentLabels) {
                    if (label < labels[y][x])
                        labels[y][x] = label;
                }
            }
        }

        for (int y = 1; y < pixels.length; y++) {          //bez 1 pixela na brzegu
            for (int x = 1; x < pixels[y].length; x++) {    //bez 1 pixela na brzegu
                int value = labels[y][x];

                if (value != 0)
                    value = (value + 40 << 16) | (value << 8) | value * 10; // do wyświetlania
                else
                    value = (value << 16) | (value << 8) | value;

                img.setRGB(x, y, value);
            }
        }
        return img;
    }
}


