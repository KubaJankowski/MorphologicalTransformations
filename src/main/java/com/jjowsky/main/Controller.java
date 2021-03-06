package com.jjowsky.main;

import com.jjowsky.transformations.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Controller {

    @FXML
    private Pane displayPane;
    @FXML
    private ImageView mainImageView;
    @FXML
    private MenuItem loadFileMenuItem;
    @FXML
    private ComboBox optionsComboBox;
    @FXML
    private ComboBox noisesComboBox;
    @FXML
    private ComboBox imageTypeComboBox;
    @FXML
    private TextField inputValue;
    @FXML
    private Label stdLabel;

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
            displayPane.getChildren().clear();
            displayPane.getChildren().add(iv);

            image = ImageIO.read(selectedFile);
        }
    }

    public void initialize() {
        imageTypeComboBox.getItems().clear();
        imageTypeComboBox.getItems().addAll("RGB", "Mono");

        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().addAll("Noise", "Labeling", "Entropy thresholding", "Open image");

        noisesComboBox.getItems().clear();
        noisesComboBox.getItems().addAll("Gaussian", "Salt and pepper", "Speckle");
        noisesComboBox.setValue("Gaussian");
        imageTypeComboBox.setVisible(false);
        noisesComboBox.setVisible(false);
        inputValue.setVisible(false);
        stdLabel.setVisible(false);

    }

    public void transformOnAction() throws IOException {
        int[][] pixels = Utils.convertTo2D(image);
        switch (optionsComboBox.getValue().toString()) {
            case "Noise": {
                String imageType = imageTypeComboBox.getValue().toString();
                String noiseType = noisesComboBox.getValue().toString();
                if (noiseType.equals("Gaussian")) {
                    if (imageType.equals("Mono")) {
                        BufferedImage bufImg = Noise.addGaussianNoiseMono(pixels, Double.valueOf(inputValue.getText()));
                        displayBufferedImage(bufImg);
                        saveImageToFile(bufImg);
                    } else {
                        BufferedImage bufImg = Noise.addGaussianNoiseRGB(pixels, Double.valueOf(inputValue.getText()));
                        displayBufferedImage(bufImg);
                        saveImageToFile(bufImg);
                    }

                } else if (noiseType.equals("Salt and pepper")) {
                    if (imageType.equals("Mono")) {
                        BufferedImage bufImg = Noise.addSaltPepperNoiseMono(pixels, Double.valueOf(inputValue.getText()));
                        displayBufferedImage(bufImg);
                        saveImageToFile(bufImg);
                    } else {
                        BufferedImage bufImg = Noise.addSaltPepperNoiseRGB(pixels, Double.valueOf(inputValue.getText()));
                        displayBufferedImage(bufImg);
                        saveImageToFile(bufImg);
                    }
                } else if (noiseType.equals("Speckle")) {
                    if (imageType.equals("Mono")) {
                        BufferedImage bufImg = Noise.addSpeckleNoiseMono(pixels, Double.valueOf(inputValue.getText()));
                        displayBufferedImage(bufImg);
                        saveImageToFile(bufImg);
                    } else {
                        BufferedImage bufImg = Noise.addSpeckleNoiseRGB(pixels, Double.valueOf(inputValue.getText()));
                        displayBufferedImage(bufImg);
                        saveImageToFile(bufImg);
                    }
                }
                break;
            }
            case "Labeling": {
                BufferedImage bufImg = Labeling.label(pixels);
                displayBufferedImage(bufImg);
                saveImageToFile(bufImg);
                break;
            }
            case "Entropy thresholding": {
                String imageType = imageTypeComboBox.getValue().toString();
                if (imageType.equals("Mono")) {
                    BufferedImage bufImg = EntropyThresholding.applyMono(pixels);
                    displayBufferedImage(bufImg);
                    saveImageToFile(bufImg);
                } else {
                    BufferedImage bufImg = EntropyThresholding.applyRGB(pixels);
                    displayBufferedImage(bufImg);
                    saveImageToFile(bufImg);
                }
                break;
            }
            case "Open image": {
                BufferedImage bufImg = OpenImage.apply(pixels, Integer.valueOf(inputValue.getText()));
                displayBufferedImage(bufImg);
                saveImageToFile(bufImg);
            }
        }
    }

    public void displayBufferedImage(BufferedImage bufImg) {
        Image img = SwingFXUtils.toFXImage(bufImg, null);
        displayPane.getChildren().remove(0);
        ImageView iv = new ImageView(img);
        displayPane.getChildren().add(iv);
    }

    public void optionsOnAction() {
        switch (optionsComboBox.getValue().toString()) {
            case "Noise": {
                imageTypeComboBox.setVisible(true);
                noisesComboBox.setVisible(true);
                inputValue.setVisible(true);
                stdLabel.setVisible(true);
                break;
            }
            case "Labeling": {
                imageTypeComboBox.setVisible(false);
                noisesComboBox.setVisible(false);
                inputValue.setVisible(false);
                stdLabel.setVisible(false);
                break;
            }
            case "Entropy thresholding": {
                imageTypeComboBox.setVisible(true);
                noisesComboBox.setVisible(false);
                inputValue.setVisible(false);
                stdLabel.setVisible(false);
                break;
            }
            case "Open image": {
                imageTypeComboBox.setVisible(false);
                noisesComboBox.setVisible(false);
                inputValue.setVisible(true);
                stdLabel.setVisible(true);
                stdLabel.setText("radius:");
            }
        }
    }

    public void noisesOnAction() {
        String noiseType = noisesComboBox.getValue().toString();
        if (!noiseType.equals("Salt and pepper"))
            stdLabel.setText("std:");
        else
            stdLabel.setText("density:");
    }

    public void saveImageToFile(BufferedImage img) throws IOException {
        File output = new File("output.png");
        ImageIO.write(img, "png", output);
    }
}


