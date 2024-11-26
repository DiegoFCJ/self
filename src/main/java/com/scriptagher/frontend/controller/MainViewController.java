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
            mainPane.setTop(headerBarLoader.load());
            HeaderBarController headerBarController = headerBarLoader.getController();

            headerBarController.setTitle();

            FXMLLoader tabPaneLoader = new FXMLLoader(getClass().getResource("/fxml/TabPane.fxml"));
            mainPane.setCenter(tabPaneLoader.load());
            TabPaneController tabPaneController = tabPaneLoader.getController();

            FXMLLoader leftPaneLoader = new FXMLLoader(getClass().getResource("/fxml/LeftPane.fxml"));
            mainPane.setLeft(leftPaneLoader.load());
            LeftPaneController leftPaneController = leftPaneLoader.getController();

            leftPaneController.setTabPaneController(tabPaneController);

            tabPaneController.dashboardStretch(leftPaneController.getLeftPane(), headerBarController);

        } catch (Exception e) {
            System.err.println("Errore durante l'inizializzazione del MainViewController: ");
            e.printStackTrace();
        }
    }
}