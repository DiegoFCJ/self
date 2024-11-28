package com.scriptagher.frontend.service;

import com.scriptagher.frontend.controller.HeaderBarController;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MaximizeService {

    // Metodo per aggiornare l'icona del pulsante di massimizzazione
    public void updateMaximizeButton(Stage primaryStage, Button maximizeButton) {
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
}
