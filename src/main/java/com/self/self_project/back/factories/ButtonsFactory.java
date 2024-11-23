package com.self.self_project.back.factories;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
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

    public static Button createMaximizeButton(Stage primaryStage) {
        Button maximizeButton = new Button();
        maximizeButton.setShape(new Circle(8));
        maximizeButton.getStyleClass().add("head-btn");
        maximizeButton.setCursor(Cursor.HAND);

        // Variabili per salvare dimensioni e posizione precedenti
        final double[] previousWidth = { 0 };
        final double[] previousHeight = { 0 };
        final double[] previousX = { 0 };
        final double[] previousY = { 0 };

        // Flag per sapere se la finestra è stata forzatamente massimizzata
        final boolean[] isForcedMaximized = { false };

        // Funzione per aggiornare l'icona del bottone
        updateMaximizeButton(primaryStage, maximizeButton);

        // Listener per sincronizzare il bottone con lo stato del `Stage`
        primaryStage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            if (!isForcedMaximized[0]) {
                updateMaximizeButton(primaryStage, maximizeButton);
            }
            System.out.println("Maximized property changed: " + isNowMaximized);
        });

        // Gestione del click sul bottone
        maximizeButton.setOnAction(e -> {
            System.out.println("Is window maximized (real): " + primaryStage.isMaximized() + " Forced maximized: "
                    + isForcedMaximized[0]);
            if (!isForcedMaximized[0]) {
                if (!primaryStage.isMaximized()) {
                    // Salva le dimensioni e posizione precedenti prima della massimizzazione
                    previousWidth[0] = primaryStage.getWidth();
                    previousHeight[0] = primaryStage.getHeight();
                    previousX[0] = primaryStage.getX();
                    previousY[0] = primaryStage.getY();

                    System.out.println("Saved current dimensions: " + previousWidth[0] + "x" + previousHeight[0]);

                    // Forza massimizzazione a schermo intero
                    Platform.runLater(() -> {
                        Screen screen = Screen.getPrimary();
                        Rectangle2D bounds = screen.getVisualBounds();

                        primaryStage.setX(bounds.getMinX());
                        primaryStage.setY(bounds.getMinY());
                        primaryStage.setWidth(bounds.getWidth());
                        primaryStage.setHeight(bounds.getHeight());

                        // Imposta il background per coprire tutto lo schermo
                        updateBackground(primaryStage);

                        // Imposta il flag che la finestra è stata forzata a massimizzarsi
                        isForcedMaximized[0] = true;

                        System.out.println("Maximized to full screen.");

                        // Aggiorna subito l'icona dopo la massimizzazione
                        updateMaximizeButton(primaryStage, maximizeButton);
                    });
                }
            } else {
                // Ripristina le dimensioni precedenti solo se è forzata la massimizzazione
                Platform.runLater(() -> {
                    primaryStage.setX(previousX[0]);
                    primaryStage.setY(previousY[0]);
                    primaryStage.setWidth(previousWidth[0]);
                    primaryStage.setHeight(previousHeight[0]);

                    System.out.println("Restored previous dimensions: " + previousWidth[0] + "x" + previousHeight[0]);

                    // Ripristina anche la posizione
                    updateBackground(primaryStage);

                    // Aggiorna subito l'icona dopo il ripristino
                    updateMaximizeButton(primaryStage, maximizeButton);

                    // Reset del flag dopo il ripristino
                    isForcedMaximized[0] = false;
                });
            }
        });

        return maximizeButton;
    }

    public static void updateMaximizeButton(Stage primaryStage, Button maximizeButton) {
        System.out.println("Updating maximize button. Stage width: " + primaryStage.getWidth() + " Height: "
                + primaryStage.getHeight());
        try {
            // Se la finestra è forzata a essere massimizzata
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

    // Funzione per aggiornare il background in base alle dimensioni dello schermo
    public static void updateBackground(Stage primaryStage) {
        // Accediamo alla scena principale della finestra
        Scene scene = primaryStage.getScene();

        // Impostiamo il background per coprire l'intero schermo
        if (primaryStage.getWidth() >= Screen.getPrimary().getVisualBounds().getWidth() &&
                primaryStage.getHeight() >= Screen.getPrimary().getVisualBounds().getHeight()) {
            scene.setFill(Color.BLACK); // Esempio di background nero
        } else {
            scene.setFill(Color.LIGHTGRAY); // Background di default quando non è massimizzato
        }
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