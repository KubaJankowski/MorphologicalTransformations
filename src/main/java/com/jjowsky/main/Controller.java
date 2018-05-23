package com.jjowsky.main;

import com.jjowsky.transformations.Noise;
import com.jjowsky.transformations.Utils;
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
    @FXML
    private ComboBox optionsComboBox;
    @FXML
    private ComboBox noisesComboBox;
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
        optionsComboBox.getItems().clear();
        optionsComboBox.getItems().addAll("Noise");

        noisesComboBox.getItems().clear();
        noisesComboBox.getItems().addAll("Gaussian", "Salt and pepper");
        noisesComboBox.setVisible(false);
        inputValue.setVisible(false);
        stdLabel.setVisible(false);

    }

    public void transformOnAction() {
        int[][] pixels = Utils.convertTo2D(image);

        switch (optionsComboBox.getValue().toString()) {
            case "Noise": {
                if (noisesComboBox.getValue().toString().equals("Gaussian")) {
                    BufferedImage bufImg = Noise.addGaussianNoise(pixels, Double.valueOf(inputValue.getText()));
                    displayBufferedImage(bufImg);
                } else if (noisesComboBox.getValue().toString().equals("Salt and pepper")) {
                    BufferedImage bufImg = Noise.addSaltPepperNoise(pixels, Double.valueOf(inputValue.getText()));
                    displayBufferedImage(bufImg);
                }
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
                noisesComboBox.setVisible(true);
                inputValue.setVisible(true);
                stdLabel.setVisible(true);
            }
        }
    }

    public void noisesOnAction() {

    }
}


