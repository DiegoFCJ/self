package com.scriptagher.frontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import com.scriptagher.shared.logger.CustomLogger;

public class MainViewController implements Initializable {

    @FXML
    private Label welcomeLabel;

    @FXML
    private BorderPane mainPane;

    private HeaderBarController headerBarController;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        try {
            // Carica l'HeaderBar e ottieni il suo controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HeaderBar.fxml"));
            mainPane.setTop(loader.load());
            headerBarController = loader.getController();

            // Imposta dinamicamente il titolo
            headerBarController.setTitle("Scriptagher");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the button click event.
     * This method is triggered when the button is clicked and updates the label's text.
     */
    @FXML
    private void handleButtonClick() {
        // Update the label text when the button is clicked
        welcomeLabel.setText("Button clicked!");
        
        // Log the button click event
        CustomLogger.info("button", "Button clicked, label updated.");
    }
}