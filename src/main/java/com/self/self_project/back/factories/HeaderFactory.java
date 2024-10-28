package com.self.self_project.back.factories;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import com.self.self_project.constants.LOGS;
import com.self.self_project.utils.logging.CustomLoggerUtils;

/**
 * Factory class for creating the application header with navigation and control buttons.
 */
public class HeaderFactory {

    /**
     * Creates a header containing control buttons for the specified stage.
     *
     * @param primaryStage The stage to which the header belongs.
     * @param showBackButton Indicates whether to show a back button or not.
     * @param sceneFactory The scene factory used to change scenes.
     * @return An HBox containing the header with buttons.
     */
    public static HBox createHeader(Stage primaryStage, boolean showBackButton, SceneFactory sceneFactory) {
        HBox header = new HBox();
        header.setSpacing(2); // Use a constant from MENU or any spacing
        header.getStyleClass().add("header");

        ButtonsFactory.createHomeButton(sceneFactory, header);

        ButtonsFactory.createBackButton(sceneFactory, header);

        // Add minimize, maximize, and close buttons
        header.getChildren().addAll(
            ButtonsFactory.createMinimizeButton(primaryStage),
            ButtonsFactory.createMaximizeButton(primaryStage),
            ButtonsFactory.createCloseButton(primaryStage)
        );
        CustomLoggerUtils.info(LOGS.HEADER_FACTORY, "Control buttons added to header.");

        return header;
    }
}
