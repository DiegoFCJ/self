package com.scriptagher.backend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.scriptagher.shared.logger.CustomLogger;

/**
 * Controller class for the main view of the application.
 * Handles user interactions and updates the UI components accordingly.
 */
public class MainViewController {

    @FXML
    private Label welcomeLabel;

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