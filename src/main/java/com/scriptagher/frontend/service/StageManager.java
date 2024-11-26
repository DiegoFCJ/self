package com.scriptagher.frontend.service;

import javafx.application.Platform;
import javafx.stage.Stage;
import com.scriptagher.shared.logger.CustomLogger;

public class StageManager {

    private static Stage stage;

    /**
     * Sets the Stage.
     * Logs a message when the stage is set or if it's null.
     *
     * @param stage the stage to be set
     */
    public static void setStage(Stage stage) {
        if (stage != null) {
            CustomLogger.info("Stage set: ", "" + stage);
        } else {
            CustomLogger.warn("Attempt to set a null stage.", "");
        }
        StageManager.stage = stage;
    }

    /**
     * Gets the Stage.
     * Logs a warning if the stage is not initialized.
     *
     * @return the current stage
     */
    public static Stage getStage() {
        if (stage == null) {
            CustomLogger.warn("Stage not initialized!", "");
        }
        return stage;
    }

    /**
     * Minimizes the Stage if it's not null.
     */
    public static void minimize() {
        if (stage != null) {
            stage.setIconified(true);
            CustomLogger.info("Stage minimized.", "");
        } else {
            CustomLogger.warn("Attempt to minimize a null stage.", "");
        }
    }

    /**
     * Maximizes the Stage and applies the corresponding CSS.
     * If the stage is already maximized, it toggles back.
     */
    public static void maximize() {
        if (stage != null) {
            applyMaximizedStyles();
            stage.setFullScreen(!stage.isFullScreen());
        } else {
            CustomLogger.warn("Attempt to maximize a null stage.", "");
        }
    }

    /**
     * Checks if the Stage is maximized.
     *
     * @return true if the stage is maximized, false otherwise
     */
    public static boolean isMaximized() {
        return stage != null && stage.isMaximized();
    }

    /**
     * Dynamically applies or removes CSS styles based on window maximization.
     */
    private static void applyMaximizedStyles() {
        Platform.runLater(() -> {
            Stage currentStage = StageManager.getStage();
            if (currentStage != null && currentStage.getScene() != null) {
                // Ottieni tutti i nodi della scena
                currentStage.getScene().getRoot().lookupAll("*").forEach(node -> {
                    if (StageManager.isMaximized()) {
                        if (node instanceof javafx.scene.layout.Region) {
                            CustomLogger.info("Applying styles to node: " + node.getClass().getSimpleName(), "");
                            // Forza i bordi e il background radius a 0 con !important
                            node.setStyle("-fx-background-radius: 0 !important; -fx-border-radius: 0 !important;");
                            CustomLogger.info(
                                    "Current style of " + node.getClass().getSimpleName() + ": " + node.getStyle(), "");
                        }
                    } else {
                        node.setStyle(""); // Ripristina lo stile originale
                        CustomLogger.info("Removing styles from node: " + node.getClass().getSimpleName(), "");
                    }
                });
                CustomLogger.info("Maximized styles applied or removed.", "");
            } else {
                CustomLogger.warn("Stage or scene is not initialized.", "");
            }
        });
    }

}