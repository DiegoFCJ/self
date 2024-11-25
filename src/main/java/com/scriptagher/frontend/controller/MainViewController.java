package com.scriptagher.frontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;
import com.scriptagher.shared.logger.CustomLogger;

public class MainViewController implements Initializable {

    @FXML
    private AnchorPane leftPane;

    @FXML
    private TabPane fullContent;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Label welcomeLabel;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        try {
            System.out.println("Inizializzo MainViewController...");

            // Carica l'HeaderBar e ottieni il suo controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HeaderBar.fxml"));
            mainPane.setTop(loader.load());
            HeaderBarController headerBarController = loader.getController();

            // Debug: Verifica se il pannello sinistro è stato inizializzato correttamente
            if (leftPane == null) {
                System.err.println("Errore: LeftPane non è stato trovato o non è collegato correttamente!");
            } else {
                System.out.println("LeftPane trovato e configurato.");
            }

            // Nascondi inizialmente il pannello sinistro
            if (leftPane != null) {
                leftPane.setTranslateX(-200.0);
                fullContent.setTranslateX(-200.0);
                BorderPane.setMargin(fullContent, new Insets(0, -200, 0, 0));
                leftPane.setVisible(false); // Nascondi all'inizializzazione
                headerBarController.configureDashboardButton(leftPane, fullContent);
                System.out.println("Pannello sinistro inizialmente nascosto.");
            }
            
            // Imposta dinamicamente il titolo
            headerBarController.setTitle("Scriptagher");
            System.out.println("Titolo impostato su 'Scriptagher'.");
        } catch (Exception e) {
            System.err.println("Errore durante l'inizializzazione del MainViewController: ");
            e.printStackTrace();
        }

    }

    /**
     * Handles the button click event.
     * This method is triggered when the button is clicked and updates the label's
     * text.
     */
    @FXML
    private void handleButtonClick() {
        // Update the label text when the button is clicked
        welcomeLabel.setText("Button clicked!");

        // Log the button click event
        CustomLogger.info("button", "Button clicked, label updated.");
    }
}