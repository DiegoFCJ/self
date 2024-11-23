package com.scriptagher.front.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainViewController {

    @FXML
    private Label welcomeLabel;

    // Metodo che viene chiamato quando si preme il pulsante
    @FXML
    private void handleButtonClick() {
        // Esegui un'azione, ad esempio, cambia il testo della label
        welcomeLabel.setText("Button clicked!");
        System.out.println("Button clicked!");
    }
}