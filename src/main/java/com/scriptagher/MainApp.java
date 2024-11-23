package com.scriptagher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.scriptagher.backend.SpringApp;
import java.io.IOException;

public class MainApp extends Application {

    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        // Avvia l'applicazione JavaFX
        launch(args);
    }

    @Override
    public void init() throws Exception {
        // Avvia Spring Boot in background
        springContext = new SpringApplicationBuilder(SpringApp.class) // Specifica SpringApp come classe principale
                            .run();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Carica il file FXML per l'interfaccia JavaFX
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/view/fxml/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Mostra la finestra principale
        primaryStage.setScene(scene);
        primaryStage.setTitle("Your App Title");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // Ferma Spring Boot quando JavaFX termina
        springContext.close();
        super.stop();
    }
}