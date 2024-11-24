package com.scriptagher.frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HeaderBarController {

    @FXML
    private Label titleLabel;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button maximizeButton;

    @FXML
    private Button closeButton;

    @FXML
    public void initialize() {
        // Azioni per i pulsanti
        closeButton.setOnAction(e -> System.exit(0));
        minimizeButton.setOnAction(e -> {
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });
        maximizeButton.setOnAction(e -> {
            Stage stage = (Stage) maximizeButton.getScene().getWindow();
            stage.setFullScreen(!stage.isFullScreen());
        });
    }

    // Metodo per aggiornare dinamicamente il titolo
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}