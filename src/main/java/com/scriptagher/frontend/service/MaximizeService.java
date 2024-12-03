package com.scriptagher.frontend.service;

import com.scriptagher.frontend.controller.HeaderBarController;
import com.scriptagher.shared.constants.ICN;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
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
        // Ottieni le dimensioni dello schermo corrente
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        // Percorso al file full-screen.css
        String cssPath = getClass().getResource("/css/full-screen.css").getPath();

        // Modifica dinamica del file CSS
        File cssFile = new File(cssPath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cssFile, false))) {
            writer.write(".border-pane {");
            writer.newLine();
            writer.write(String.format("-fx-min-width: %.2fpx;", screenWidth));
            writer.newLine();
            writer.write(String.format("-fx-min-height: %.2fpx;", screenHeight));
            writer.newLine();
            writer.write("}");
            CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, "Updated full-screen.css with dynamic dimensions.");
        } catch (IOException e) {
            CustomLogger.error(LOGS.MAXIMIZE_SERVICE, "Failed to update full-screen.css: " + e.getMessage());
        }

        // Applica il foglio di stile aggiornato
        if (!primaryStage.getScene().getRoot().getStylesheets()
                .contains(getClass().getResource("/css/full-screen.css").toExternalForm())) {
            primaryStage.getScene().getRoot().getStylesheets()
                    .add(getClass().getResource("/css/full-screen.css").toExternalForm());
        }
    }

    /**
     * Restores the CSS for a normal window state.
     */
    private void restoreCss(Stage primaryStage) {
        // Ripristina il file CSS originale (opzionale: resettare il file
        // full-screen.css)
        String cssPath = getClass().getResource("/css/full-screen.css").getPath();
        File cssFile = new File(cssPath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cssFile, false))) {
            writer.write(".border-pane {");
            writer.newLine();
            writer.write("-fx-min-width: 700px;");
            writer.newLine();
            writer.write("-fx-min-height: 500px;");
            writer.newLine();
            writer.write("}");
            CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, "Restored default dimensions in full-screen.css.");
        } catch (IOException e) {
            CustomLogger.error(LOGS.MAXIMIZE_SERVICE, "Failed to reset full-screen.css: " + e.getMessage());
        }

        // Rimuove full-screen.css e aggiunge global.css
        if (primaryStage.getScene().getRoot().getStylesheets()
                .contains(getClass().getResource("/css/full-screen.css").toExternalForm())) {
            primaryStage.getScene().getRoot().getStylesheets()
                    .remove(getClass().getResource("/css/full-screen.css").toExternalForm());
        }
        if (!primaryStage.getScene().getRoot().getStylesheets()
                .contains(getClass().getResource("/css/global.css").toExternalForm())) {
            primaryStage.getScene().getRoot().getStylesheets()
                    .add(getClass().getResource("/css/global.css").toExternalForm());
        }
    }
}