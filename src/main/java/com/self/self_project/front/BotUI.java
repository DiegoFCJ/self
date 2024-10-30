package com.self.self_project.front;

import com.self.self_project.back.factories.SceneFactory;
import com.self.self_project.back.factories.window.WindowMover;
import com.self.self_project.back.factories.window.WindowResizeFactory;
import com.self.self_project.dtos.WindowDTO;
import com.self.self_project.utils.logging.CustomLoggerUtils;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BotUI {
    
    private SceneFactory sceneFactory;
    private WindowDTO windowState;

    public void initialize(Stage primaryStage) {
        try {
            CustomLoggerUtils.info("ApplicationStart", "Initializing the BotUI Application");

            primaryStage.setTitle("Gestione Bot");
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.initStyle(StageStyle.TRANSPARENT);

            // Initialize the SceneFactory
            sceneFactory = new SceneFactory(primaryStage);
            CustomLoggerUtils.info("SceneFactory", "Creating the main menu scene");
            // Now using the scene factory to create the main menu scene
            sceneFactory.createMainMenuScene(false, false); // This will internally set the scene

            // Initialize the DTO with the initial dimensions
            windowState = new WindowDTO(primaryStage.getWidth(), primaryStage.getHeight());

            // Create handlers for resizing and moving the window
            WindowResizeFactory resizeFactory = new WindowResizeFactory(windowState);
            resizeFactory.addResizeEventHandlers(primaryStage);

            WindowMover windowMover = new WindowMover(windowState);
            windowMover.addGrabMoveEventHandlers(primaryStage);

            primaryStage.show();
            CustomLoggerUtils.info("ApplicationStart", "BotUI Application started successfully");

        } catch (Exception e) {
            CustomLoggerUtils.error("ApplicationError", "Failed to start BotUI Application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}