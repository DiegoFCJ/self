package com.scriptagher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.scriptagher.backend.SpringApp;
import com.scriptagher.frontend.service.StageManager;
import com.scriptagher.shared.logger.CustomLogger;

import java.io.IOException;

/**
 * Entry point for the application, combining JavaFX and Spring Boot.
 * Handles the initialization, starting, and stopping of both frameworks.
 */
public class MainApp extends Application {

    private ConfigurableApplicationContext springContext;

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        CustomLogger.info("main", "Launching JavaFX application");
        launch(args);
    }

    /**
     * Initializes the application by starting the Spring Boot context.
     *
     * @throws Exception if an error occurs during initialization.
     */
    @Override
    public void init() throws Exception {
        CustomLogger.info("init", "Starting Spring Boot context");
        springContext = new SpringApplicationBuilder(SpringApp.class) // Specify SpringApp as the main class
                .run();
        CustomLogger.info("init", "Spring Boot context started successfully");
    }

    /**
     * Starts the JavaFX application by loading the main FXML view and displaying
     * the primary stage.
     *
     * @param primaryStage The primary stage for this JavaFX application.
     * @throws IOException if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        CustomLogger.info("start", "Loading MainView.fxml");

        // Imposta lo stage nel StageManager
        if (primaryStage != null) {
            StageManager.setStage(primaryStage);
            System.out.println("Stage impostato correttamente: " + primaryStage);
        } else {
            System.err.println("primaryStage è nullo");
        }

        // Continua con il caricamento della scena
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Scriptagher");
        primaryStage.show();
    }

    /**
     * Stops the application by closing the Spring Boot context and performing
     * necessary cleanup.
     *
     * @throws Exception if an error occurs during shutdown.
     */
    @Override
    public void stop() throws Exception {
        CustomLogger.info("stop", "Shutting down Spring Boot context");
        springContext.close();
        CustomLogger.info("stop", "Spring Boot context closed successfully");
        super.stop();
        CustomLogger.info("stop", "JavaFX application terminated");
    }
}