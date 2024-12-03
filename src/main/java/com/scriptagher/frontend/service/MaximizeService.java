package com.scriptagher.frontend.service;

import com.scriptagher.frontend.controller.HeaderBarController;
import com.scriptagher.shared.constants.ICN;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MaximizeService {

    /**
     * Updates the maximize button icon based on the current state of the window.
     * If the window is in full screen, the restore icon is set, otherwise the
     * maximize icon is set.
     *
     * @param maximizeButton the Button used for maximizing/restoring the window
     */
    public void updateMaximizeButton(Button maximizeButton) {
        // Ottieni lo Stage e la Scene dal StageManager
        Stage primaryStage = StageManager.getStage();
        if (primaryStage == null || primaryStage.getScene() == null) {
            CustomLogger.error(LOGS.MAXIMIZE_SERVICE, "Stage or Scene not initialized.");
            return;
        }

        try {
            ImageView imageView = null;

            // Verifica se la finestra è a schermo intero (fullscreen) o massimizzata
            boolean isFullScreen = primaryStage.isFullScreen();
            boolean isMaximized = primaryStage.isMaximized();

            CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, "isFullScreen: " + isFullScreen);
            CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, "isMaximized: " + isMaximized);

            if (isFullScreen) {
                updateCssForMaximized(primaryStage);
                // Se la finestra è in modalità fullscreen, setta l'icona di restore
                imageView = new ImageView(
                        new Image(HeaderBarController.class.getResourceAsStream(ICN.CIRCLE)));
                CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, LOGS.WINDOW_FULL_SCREEN);
            } else if (isMaximized) {
                updateCssForMaximized(primaryStage);
                // Se la finestra è massimizzata ma non a schermo intero, setta l'icona di
                // restore
                imageView = new ImageView(
                        new Image(HeaderBarController.class.getResourceAsStream(ICN.CIRCLE)));
                CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, LOGS.WINDOW_MAXIMIZED);
            } else {
                restoreCss(primaryStage);
                // Se la finestra non è né a schermo intero né massimizzata, setta l'icona di
                // maximizza
                imageView = new ImageView(
                        new Image(HeaderBarController.class.getResourceAsStream(ICN.CLOUD_RESTORE)));
                CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, LOGS.WINDOW_NOT_FULL_SCREEN);
            }

            // Imposta la dimensione fissa dell'icona
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);

            // Imposta l'icona sul bottone di massimizzazione
            maximizeButton.setGraphic(imageView);

        } catch (Exception e) {
            // Log dell'errore se il caricamento dell'icona fallisce
            CustomLogger.error(LOGS.MAXIMIZE_SERVICE, LOGS.ICON_LOADING_ERROR + e.getMessage());
        }

        // Log di aggiornamento dell'icona del bottone
        CustomLogger.info(LOGS.MAXIMIZE_SERVICE, LOGS.MAXIMIZE_BUTTON_UPDATED);
    }

    /**
     * Updates the CSS for a maximized window.
     */
    private void updateCssForMaximized(Stage primaryStage) {
        System.out.println("Maximized Foglio di stile: " + primaryStage.getScene().getRoot().getStylesheets());

        // Rimuove il file CSS 'global.css' quando la finestra è massimizzata
        if (primaryStage.getScene().getRoot().getStylesheets()
                .contains(getClass().getResource("/css/global.css").toExternalForm())) {
            primaryStage.getScene().getRoot().getStylesheets()
                    .remove(getClass().getResource("/css/global.css").toExternalForm());
            System.out.println(
                    "Maximized Foglio di stile rimosso?: " + primaryStage.getScene().getRoot().getStylesheets());

            // Aggiungi un altro CSS per la modalità fullscreen
            primaryStage.getScene().getRoot().getStylesheets()
                    .add(getClass().getResource("/css/full-screen.css").toExternalForm());
            System.out.println(
                    "Maximized Foglio di stile aggiunto?: " + primaryStage.getScene().getRoot().getStylesheets());
            CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, "Removed global.css from the scene and added full-screen.css.");
        } else {
            // Solo se full-screen.css non è già stato aggiunto
            if (!primaryStage.getScene().getRoot().getStylesheets()
                    .contains(getClass().getResource("/css/full-screen.css").toExternalForm())) {
                primaryStage.getScene().getRoot().getStylesheets()
                        .add(getClass().getResource("/css/full-screen.css").toExternalForm());
                System.out.println(
                        "Maximized Foglio di stile aggiunto?: " + primaryStage.getScene().getRoot().getStylesheets());
                CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, "Added full-screen.css to the scene.");
            }
        }
    }

    private void restoreCss(Stage primaryStage) {
        System.out.println("restore Foglio di stile: " + primaryStage.getScene().getRoot().getStylesheets());

        // Rimuove il foglio di stile 'full-screen.css' se presente
        if (primaryStage.getScene().getRoot().getStylesheets()
                .contains(getClass().getResource("/css/full-screen.css").toExternalForm())) {
            primaryStage.getScene().getRoot().getStylesheets()
                    .remove(getClass().getResource("/css/full-screen.css").toExternalForm());
            System.out.println(
                    "Full-Screen Foglio di stile rimosso?: " + primaryStage.getScene().getRoot().getStylesheets());
            CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, "Removed full-screen.css from the scene.");
        }

        // Ripristina il CSS originale quando la finestra non è massimizzata
        if (!primaryStage.getScene().getRoot().getStylesheets()
                .contains(getClass().getResource("/css/global.css").toExternalForm())) {
            // Aggiungi global.css se non presente
            primaryStage.getScene().getRoot().getStylesheets()
                    .add(getClass().getResource("/css/global.css").toExternalForm());
            System.out.println(
                    "restore Foglio di stile aggiunto?: " + primaryStage.getScene().getRoot().getStylesheets());
            CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, "Added global.css back to the scene.");
        }
    }
}