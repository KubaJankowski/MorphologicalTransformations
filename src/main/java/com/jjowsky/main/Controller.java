package com.jjowsky.main;

import com.jjowsky.noises.Noise;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Controller {

    @FXML
    private Pane displayPane;
    @FXML
    private ImageView mainImageView;
    @FXML
    private MenuItem loadFileMenuItem;

    private BufferedImage image;

    public void onActionLoadFileMenuItem() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image files", "*.png", "*jpg", "*bmp")
        );
        File selectedFile = fileChooser.showOpenDialog(displayPane.getScene().getWindow());
        if (selectedFile != null) {
            Image img = new Image(selectedFile.toURI().toString());
            ImageView iv = new ImageView(img);
            displayPane.getChildren().add(iv);
            image = ImageIO.read(selectedFile);
//            int[][] r = convertTo2D(image);
//            BufferedImage im = getImage(r);
//
//            img = SwingFXUtils.toFXImage(im, null);
//            displayPane.getChildren().remove(0);
//            iv = new ImageView(img);
//            displayPane.getChildren().add(iv);
        }
    }

    private static int[][] convertTo2D(BufferedImage image) {
        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
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

    private static BufferedImage getImage(int[][] pixels) {
        BufferedImage img = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_3BYTE_BGR);
        Random rnd = new Random();

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                int pixel = pixels[y][x];
                int alpha = (pixel >> 24 & 0xff);
                int red = (pixel >> 16 & 0xff);
                int green = (pixel >> 8 & 0xff);
                int blue = (pixel & 0xff);
//                double redd = (double)red/255;
//                double greend = (double)green/255;
//                double blued = (double)blue/255;
//                redd += (int)Noise.generateGaussian();
//                greend += (int)Noise.generateGaussian();
//                blued += (int)Noise.generateGaussian();
//                red = (int)(redd*255);
//                green = (int)(greend*255);
//                blue = (int)(blued*255);
//
//                if (red > 255) {
//                    red = 255;
//                }
//                if (green > 255) {
//                    green = 255;
//                }
//                if (blue > 255) {
//                    blue = 255;
//                }
                int rgb = (red << 16) | (green << 8) | blue;
                img.setRGB(x, y, rgb);
            }
        }
        return img;
    }
}
