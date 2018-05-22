package com.jjowsky.main;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
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
        }
    }

}
