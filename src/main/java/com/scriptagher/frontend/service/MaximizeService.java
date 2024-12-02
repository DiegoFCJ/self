package com.scriptagher.frontend.service;

import com.scriptagher.frontend.controller.HeaderBarController;
import com.scriptagher.shared.constants.ICN;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Service class for handling the maximize button state and updating its icon.
 */
public class MaximizeService {

    /**
     * Updates the maximize button icon based on the current state of the window.
     * If the window is in full screen, the restore icon is set, otherwise the maximize icon is set.
     *
     * @param primaryStage the main window stage (Stage) of the application
     * @param maximizeButton the Button used for maximizing/restoring the window
     */
    public void updateMaximizeButton(Stage primaryStage, Button maximizeButton) {
        try {
            ImageView imageView = null;

            // If the window is maximized to fill the screen, set the restore icon
            if (primaryStage.isFullScreen()) {
                imageView = new ImageView(
                        new Image(HeaderBarController.class.getResourceAsStream(ICN.CIRCLE)));
                CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, LOGS.WINDOW_FULL_SCREEN);
            } else {
                // Otherwise, set the maximize icon
                imageView = new ImageView(
                        new Image(HeaderBarController.class.getResourceAsStream(ICN.CLOUD_RESTORE)));
                CustomLogger.debug(LOGS.MAXIMIZE_SERVICE, LOGS.WINDOW_NOT_FULL_SCREEN);
            }

            // Set fixed icon size
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);

            // Set the icon on the maximize button
            maximizeButton.setGraphic(imageView);

        } catch (Exception e) {
            // Log error if icon loading fails
            CustomLogger.error(LOGS.MAXIMIZE_SERVICE, LOGS.ICON_LOADING_ERROR + e.getMessage());
        }

        // Log button icon update
        CustomLogger.info(LOGS.MAXIMIZE_SERVICE, LOGS.MAXIMIZE_BUTTON_UPDATED);
    }
}