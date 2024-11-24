package com.scriptagher.frontend.controller;

import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
            updateMaximizeButton(stage, maximizeButton);
            stage.setFullScreen(!stage.isFullScreen());
        });
    }

    /**
     * Updates the maximize button's icon based on the current size of the stage.
     * If the stage is maximized to fill the screen, it shows the restore icon;
     * otherwise, it shows the maximize icon.
     *
     * @param primaryStage   The stage whose size is used to determine the button
     *                       icon.
     * @param maximizeButton The maximize button whose icon will be updated.
     */
    private void updateMaximizeButton(Stage primaryStage, Button maximizeButton) {
        try {
            ImageView imageView = null;

            // If the window is maximized to fill the screen
            if (primaryStage.isFullScreen()) {
                imageView = new ImageView(
                        new Image(HeaderBarController.class.getResourceAsStream("/icons/icons8-circle-32.png")));
                System.out.println("Set restore icon.");
            } else {
                imageView = new ImageView(
                        new Image(HeaderBarController.class.getResourceAsStream("/icons/icons8-ripristino-32.png")));
                System.out.println("Set maximize icon.");
            }

            // Imposta le dimensioni fisse dell'icona
            imageView.setFitHeight(20); // Imposta l'altezza desiderata
            imageView.setFitWidth(20); // Imposta la larghezza desiderata

            // Assegna l'icona al pulsante
            maximizeButton.setGraphic(imageView);

        } catch (Exception e) {
            System.out.println("Error loading icons: " + e.getMessage());
        }
        CustomLogger.info(LOGS.BUTTON_FACTORY, "Maximize button graphic updated.");
    }

    // Metodo per aggiornare dinamicamente il titolo
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}