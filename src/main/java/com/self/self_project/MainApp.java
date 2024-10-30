package com.self.self_project;

import com.self.self_project.constants.LOGS;
import com.self.self_project.front.BotUI;
import com.self.self_project.utils.logging.CustomLoggerUtils;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainApp {

    private static final long CLEAN_LOG_DELAY_MINUTES = 1; // Ritardo di 10 minuti

    /**
     * The main method to start the application.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        CustomLoggerUtils.info(LOGS.MAIN_APP, "Application starting...");

        // Crea un ScheduledExecutorService per eseguire cleanEmptyLogFiles con un ritardo
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Esegui cleanEmptyLogFiles con un ritardo di CLEAN_LOG_DELAY_MINUTES
        scheduler.schedule(() -> {
            CustomLoggerUtils.info(LOGS.MAIN_APP, "Starting cleanup of empty log files.");
            CustomLoggerUtils.cleanEmptyLogFiles();
        }, CLEAN_LOG_DELAY_MINUTES, TimeUnit.MINUTES);

        try {
            // Launch the JavaFX application
            Application.launch(AppLauncher.class, args);
            CustomLoggerUtils.info(LOGS.MAIN_APP, "Application launched successfully.");
        } catch (Exception e) {
            // Log any exception that occurs during the launch
            CustomLoggerUtils.error(LOGS.MAIN_APP, "Failed to launch the application: " + e.getMessage());
        }
    }

    /**
     * Inner class that extends Application to handle JavaFX initialization.
     */
    public static class AppLauncher extends Application {
        
        @Override
        public void start(Stage primaryStage) {
            try {
                // Imposta l'icona personalizzata
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/app-icon.png")));
                
                // Imposta il titolo della finestra
                primaryStage.setTitle("Self Project Bot");

                // Qui si lancia la UI come in BotUI (puoi caricare la scena da un file FXML o creare l'interfaccia manualmente)
                BotUI botUI = new BotUI();
                botUI.initialize(primaryStage);  // Avvia la UI

            } catch (Exception e) {
                CustomLoggerUtils.error(LOGS.MAIN_APP, "Failed to launch UI: " + e.getMessage());
            }
        }
    }
}