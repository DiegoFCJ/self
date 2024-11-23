package com.self.self_project.back.factories;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.self.self_project.utils.logging.CustomLoggerUtils;
import com.self.self_project.constants.LOGS;
import com.self.self_project.constants.MENU;
import com.self.self_project.dtos.SceneDTO;

/**
 * Factory class for creating various buttons used in the application.
 */
public class ButtonsFactory {

    /**
     * Creates a button with specified text and action.
     *
     * @param text   The text to display on the button.
     * @param action The action to perform when the button is clicked.
     * @return A Button instance.
     */
    public Button createButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-btn");
        button.setCursor(Cursor.HAND);
        button.setOnAction(action);
        CustomLoggerUtils.info(LOGS.BUTTON_FACTORY, "Button created with text: " + text);
        return button;
    }

    /**
     * Creates a button for minimizing the specified stage.
     *
     * @param primaryStage The stage to minimize.
     * @return A Button instance that minimizes the stage.
     */
    public static Button createMinimizeButton(Stage primaryStage) {
        Button minimizeButton = new Button();
        minimizeButton.setOnAction(e -> {
            primaryStage.setIconified(true);
            CustomLoggerUtils.info(LOGS.BUTTON_FACTORY, "Minimize button clicked, stage minimized.");
        });
        minimizeButton.setGraphic(new ImageView(
                new Image(HeaderFactory.class.getResourceAsStream("/icons/icons8-macos-riduci-a-icona-32.png"))));
        minimizeButton.setShape(new Circle(8));
        minimizeButton.getStyleClass().add("head-btn");
        minimizeButton.setCursor(Cursor.HAND);
        return minimizeButton;
    }

    /**
     * Creates a button for maximizing/restoring the specified stage.
     *
     * @param primaryStage The stage to maximize.
     * @return A Button instance that maximizes/restores the stage.
     */
    public static Button createMaximizeButton(Stage primaryStage) {
        Button maximizeButton = new Button();
        maximizeButton.setShape(new Circle(8));
        maximizeButton.getStyleClass().add("head-btn");
        maximizeButton.setCursor(Cursor.HAND);

        final double[] previousWidth = { 0 };
        final double[] previousHeight = { 0 };

        updateMaximizeButton(primaryStage, maximizeButton);

        primaryStage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            updateMaximizeButton(primaryStage, maximizeButton);
            System.out.println("Maximized property changed: " + isNowMaximized);
        });

        maximizeButton.setOnAction(e -> {
            if (!primaryStage.isMaximized()) {
                // Salva le dimensioni precedenti
                previousWidth[0] = primaryStage.getWidth();
                previousHeight[0] = primaryStage.getHeight();
                System.out.println("Saved current dimensions: " + previousWidth[0] + "x" + previousHeight[0]);

                // Forza massimizzazione
                Platform.runLater(() -> {
                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();

                    primaryStage.setX(bounds.getMinX());
                    primaryStage.setY(bounds.getMinY());
                    primaryStage.setWidth(bounds.getWidth());
                    primaryStage.setHeight(bounds.getHeight());

                    System.out.println("Maximized to full screen.");
                });

            } else {
                // Ripristina le dimensioni precedenti
                Platform.runLater(() -> {
                    primaryStage.setX((Screen.getPrimary().getVisualBounds().getWidth() - previousWidth[0]) / 2);
                    primaryStage.setY((Screen.getPrimary().getVisualBounds().getHeight() - previousHeight[0]) / 2);
                    primaryStage.setWidth(previousWidth[0]);
                    primaryStage.setHeight(previousHeight[0]);
                    System.out.println("Restored previous dimensions: " + previousWidth[0] + "x" + previousHeight[0]);
                });
            }
        });

        return maximizeButton;
    }

    /**
     * Updates the maximize button's graphic based on the stage's current state.
     *
     * @param primaryStage   The stage to check the maximized state of.
     * @param maximizeButton The button to update.
     */
    public static void updateMaximizeButton(Stage primaryStage, Button maximizeButton) {
        if (primaryStage.isMaximized()) {
            maximizeButton.setGraphic(new ImageView(
                    new Image(HeaderFactory.class.getResourceAsStream("/icons/icons8-ripristino-32.png"))));
        } else {
            maximizeButton.setGraphic(
                    new ImageView(new Image(HeaderFactory.class.getResourceAsStream("/icons/icons8-circle-32.png"))));
        }
        CustomLoggerUtils.info(LOGS.BUTTON_FACTORY, "Maximize button graphic updated.");
    }

    /**
     * Creates a close button for the specified stage.
     *
     * @param primaryStage The stage to close.
     * @return A Button instance that closes the stage.
     */
    public static Button createCloseButton(Stage primaryStage) {
        Button closeButton = new Button();
        closeButton.setOnAction(e -> {
            primaryStage.close();
            CustomLoggerUtils.info(LOGS.BUTTON_FACTORY, "Close button clicked, stage closed.");
        });
        closeButton.setGraphic(
                new ImageView(new Image(HeaderFactory.class.getResourceAsStream("/icons/icons8-spegnere-32.png"))));
        closeButton.setShape(new Circle(8));
        closeButton.getStyleClass().add("head-btn");
        closeButton.setCursor(Cursor.HAND);
        return closeButton;
    }

    /**
     * Creates a Back button for navigating to the previous menu in the specified
     * HBox.
     *
     * @param sceneFactory An instance of {@link SceneFactory} used to change
     *                     scenes.
     * @param header       The HBox where the back button will be added.
     */
    public static void createBackButton(SceneFactory sceneFactory, HBox header) {
        Button backButton = createIconButton("/icons/icons8-go-back-32.png", e -> {
            // Call the goBack method in SceneFactory
            sceneFactory.goBack();
            CustomLoggerUtils.info(LOGS.BUTTON_FACTORY, "Back button clicked, switched to previous menu.");
        });
        header.getChildren().add(backButton);
        CustomLoggerUtils.info(LOGS.BUTTON_FACTORY, "Back button created and added to header.");
    }

    /**
     * Creates a Home button for navigating to the main menu in the specified HBox.
     *
     * @param sceneFactory An instance of {@link SceneFactory} used to change
     *                     scenes.
     * @param header       The HBox where the home button will be added.
     */
    public static void createHomeButton(SceneFactory sceneFactory, HBox header) {
        Button homeButton = createIconButton("/icons/icons8-casa-32.png", e -> {
            // Logic to return to the main menu
            sceneFactory.changeScene(new SceneDTO(true, false, "Main", MENU.WINDOW_WIDTH, MENU.WINDOW_HEIGHT));
            CustomLoggerUtils.info(LOGS.BUTTON_FACTORY, "Home button clicked, switched to main menu.");
        });
        header.getChildren().add(homeButton);
        CustomLoggerUtils.info(LOGS.BUTTON_FACTORY, "Home button created and added to header.");
    }

    /**
     * Creates an icon button with the specified icon path and action.
     *
     * @param iconPath The path to the icon resource.
     * @param action   The action to perform when the button is clicked.
     * @return A Button instance with an icon.
     */
    private static Button createIconButton(String iconPath,
            javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button();
        button.setGraphic(new ImageView(new Image(HeaderFactory.class.getResourceAsStream(iconPath))));
        button.setShape(new Circle(8));
        button.getStyleClass().add("head-btn");
        button.setCursor(Cursor.HAND);
        button.setOnAction(action);
        return button;
    }
}