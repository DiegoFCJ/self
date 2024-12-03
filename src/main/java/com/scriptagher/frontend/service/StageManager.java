package com.scriptagher.frontend.service;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.scriptagher.shared.logger.CustomLogger;
import com.scriptagher.shared.constants.LOGS;

/**
 * Service class for managing the application's Stage and Scene,
 * including minimizing, maximizing, and applying appropriate styles when the window state changes.
 */
public class StageManager {

    private static Stage stage;
    private static Scene scene;

    /**
     * Sets the Stage and Scene.
     * Logs a message when the stage or scene is set or if they are null.
     *
     * @param stage the stage to be set
     * @param scene the scene to be set
     */
    public static void setStageAndScene(Stage stage, Scene scene) {
        if (stage != null) {
            CustomLogger.info(LOGS.STAGE_MANAGER, LOGS.STAGE_SET + stage);
        } else {
            CustomLogger.warn(LOGS.STAGE_MANAGER, LOGS.NULL_STAGE_SET);
        }
        if (scene != null) {
            CustomLogger.info(LOGS.STAGE_MANAGER, LOGS.SCENE_SET + scene);
        } else {
            CustomLogger.warn(LOGS.STAGE_MANAGER, LOGS.NULL_SCENE_SET);
        }
        StageManager.stage = stage;
        StageManager.scene = scene;
    }

    /**
     * Gets the Stage.
     * Logs a warning if the stage is not initialized.
     *
     * @return the current stage
     */
    public static Stage getStage() {
        if (stage == null) {
            CustomLogger.warn(LOGS.STAGE_MANAGER, LOGS.STAGE_NOT_INITIALIZED);
        }
        return stage;
    }

    /**
     * Gets the Scene.
     * Logs a warning if the scene is not initialized.
     *
     * @return the current scene
     */
    public static Scene getScene() {
        if (scene == null) {
            CustomLogger.warn(LOGS.STAGE_MANAGER, LOGS.SCENE_NOT_INITIALIZED);
        }
        return scene;
    }

    /**
     * Minimizes the Stage if it's not null.
     */
    public static void minimize() {
        if (stage != null) {
            stage.setIconified(true);
            CustomLogger.info(LOGS.STAGE_MANAGER, LOGS.STAGE_MINIMIZED);
        } else {
            CustomLogger.warn(LOGS.STAGE_MANAGER, LOGS.NULL_STAGE_MINIMIZE);
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
            CustomLogger.warn(LOGS.STAGE_MANAGER, LOGS.NULL_STAGE_MAXIMIZE);
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
            Scene currentScene = StageManager.getScene();
            if (currentStage != null && currentScene != null && currentScene.getRoot() != null) {
                // Get all nodes in the scene
                currentScene.getRoot().lookupAll("*").forEach(node -> {
                    if (StageManager.isMaximized()) {
                        if (node instanceof javafx.scene.layout.Region) {
                            CustomLogger.info(LOGS.STAGE_MANAGER, LOGS.APPLYING_STYLE + node.getClass().getSimpleName());
                            // Force background and border radius to 0 with !important
                            node.setStyle("-fx-background-radius: 0 !important; -fx-border-radius: 0 !important;");
                            CustomLogger.info(LOGS.STAGE_MANAGER, LOGS.NODE_STYLE + node.getClass().getSimpleName() + ": " + node.getStyle());
                        }
                    } else {
                        node.setStyle(""); // Restore original style
                        CustomLogger.info(LOGS.STAGE_MANAGER, LOGS.REMOVING_STYLE + node.getClass().getSimpleName());
                    }
                });
                CustomLogger.info(LOGS.STAGE_MANAGER, LOGS.MAXIMIZED_STYLE_APPLIED);
            } else {
                CustomLogger.warn(LOGS.STAGE_MANAGER, LOGS.STAGE_OR_SCENE_NOT_INITIALIZED);
            }
        });
    }
}