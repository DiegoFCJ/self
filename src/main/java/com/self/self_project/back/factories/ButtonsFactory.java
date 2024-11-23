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
     * Creates a maximize button for the provided stage. This button toggles between
     * maximizing the window to fill the screen and restoring the window to its
     * previous size and position.
     *
     * @param primaryStage The primary stage to be maximized or restored.
     * @return The maximize button.
     */
    public static Button createMaximizeButton(Stage primaryStage) {
        Button maximizeButton = new Button();
        maximizeButton.setShape(new Circle(8));
        maximizeButton.getStyleClass().add("head-btn");
        maximizeButton.setCursor(Cursor.HAND);

        // Variables to save previous dimensions
        final double[] previousWidth = { 0 };
        final double[] previousHeight = { 0 };

        // Flag to track if the window was forced to maximize
        final boolean[] isForcedMaximized = { false };

        // Update the maximize button's icon based on the stage's state
        updateMaximizeButton(primaryStage, maximizeButton);

        // Listener to synchronize the button with the stage's maximized state
        primaryStage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            if (!isForcedMaximized[0]) {
                updateMaximizeButton(primaryStage, maximizeButton);
            }
            System.out.println("Maximized property changed: " + isNowMaximized);
        });

        // Handling button click action to maximize or restore the window
        maximizeButton.setOnAction(e -> {
            System.out.println("Is window maximized (real): " + primaryStage.isMaximized() + " Forced maximized: "
                    + isForcedMaximized[0]);
            if (!isForcedMaximized[0]) {
                if (!primaryStage.isMaximized()) {
                    // Save the current dimensions before maximizing
                    previousWidth[0] = primaryStage.getWidth();
                    previousHeight[0] = primaryStage.getHeight();

                    System.out.println("Saved current dimensions: " + previousWidth[0] + "x" + previousHeight[0]);

                    // Force maximize to full screen
                    Platform.runLater(() -> {
                        // Get the current screen where the window is located
                        Rectangle2D windowBounds = new Rectangle2D(primaryStage.getX(), primaryStage.getY(),
                                primaryStage.getWidth(), primaryStage.getHeight());
                        Screen currentScreen = Screen.getScreensForRectangle(windowBounds).get(0);
                        Rectangle2D bounds = currentScreen.getVisualBounds();

                        // Maximize the window within the bounds of the current screen
                        primaryStage.setX(bounds.getMinX());
                        primaryStage.setY(bounds.getMinY());
                        primaryStage.setWidth(bounds.getWidth());
                        primaryStage.setHeight(bounds.getHeight());

                        // Set the flag that the window is forced to maximize
                        isForcedMaximized[0] = true;

                        System.out.println("Maximized to full screen on the current screen.");

                        // Immediately update the maximize button's icon after maximizing
                        updateMaximizeButton(primaryStage, maximizeButton);
                    });
                }
            } else {
                // Restore the dimensions but center the window on the screen
                Platform.runLater(() -> {
                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();

                    // Calculate the coordinates to center the window
                    double centerX = bounds.getMinX() + (bounds.getWidth() - previousWidth[0]) / 2;
                    double centerY = bounds.getMinY() + (bounds.getHeight() - previousHeight[0]) / 2;

                    primaryStage.setX(centerX);
                    primaryStage.setY(centerY);
                    primaryStage.setWidth(previousWidth[0]);
                    primaryStage.setHeight(previousHeight[0]);

                    System.out.println(
                            "Restored previous dimensions and centered: " + previousWidth[0] + "x" + previousHeight[0]);

                    // Immediately update the maximize button's icon after restoring
                    updateMaximizeButton(primaryStage, maximizeButton);

                    // Reset the flag after restoring
                    isForcedMaximized[0] = false;
                });
            }
        });

        return maximizeButton;
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
    public static void updateMaximizeButton(Stage primaryStage, Button maximizeButton) {
        System.out.println("Updating maximize button. Stage width: " + primaryStage.getWidth() + " Height: "
                + primaryStage.getHeight());
        try {
            // If the window is maximized to fill the screen
            if (primaryStage.getWidth() >= Screen.getPrimary().getVisualBounds().getWidth() &&
                    primaryStage.getHeight() >= Screen.getPrimary().getVisualBounds().getHeight()) {
                maximizeButton.setGraphic(new ImageView(
                        new Image(HeaderFactory.class.getResourceAsStream("/icons/icons8-ripristino-32.png"))));
                System.out.println("Set restore icon.");
            } else {
                maximizeButton.setGraphic(new ImageView(
                        new Image(HeaderFactory.class.getResourceAsStream("/icons/icons8-circle-32.png"))));
                System.out.println("Set maximize icon.");
            }
        } catch (Exception e) {
            System.out.println("Error loading icons: " + e.getMessage());
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