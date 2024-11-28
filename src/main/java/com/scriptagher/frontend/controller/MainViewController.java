package com.scriptagher.frontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private BorderPane mainPane;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        try {
            System.out.println("Inizializzo MainViewController...");

            mainPane.getStyleClass().add("rounded-border");

            FXMLLoader headerBarLoader = new FXMLLoader(getClass().getResource("/fxml/HeaderBar.fxml"));
            FXMLLoader tabPaneLoader = new FXMLLoader(getClass().getResource("/fxml/TabPane.fxml"));
            FXMLLoader leftPaneLoader = new FXMLLoader(getClass().getResource("/fxml/LeftPane.fxml"));

            // Carica i componenti FXML
            mainPane.setTop(headerBarLoader.load());
            mainPane.setCenter(tabPaneLoader.load());
            mainPane.setLeft(leftPaneLoader.load());

            // Ottieni i controller dai file FXML
            HeaderBarController headerBarController = headerBarLoader.getController();
            TabPaneController tabPaneController = tabPaneLoader.getController();
            LeftPaneController leftPaneController = leftPaneLoader.getController();

            // Imposta il titolo e il stage nel controller della HeaderBar
            headerBarController.setTitle();

            // Passa il controller di LeftPane a TabPaneController se necessario
            leftPaneController.setTabPaneController(tabPaneController);

            // Configura il pulsante dashboard per estendere o comprimere il pannello sinistro
            tabPaneController.dashboardStretch(leftPaneController.getLeftPane(), headerBarController);

        } catch (Exception e) {
            System.err.println("Errore durante l'inizializzazione del MainViewController: ");
            e.printStackTrace();
        }
    }
}