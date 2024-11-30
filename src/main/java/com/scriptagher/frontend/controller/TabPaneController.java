package com.scriptagher.frontend.controller;

import com.scriptagher.frontend.dto.Bot;
import com.scriptagher.frontend.service.TabPaneService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TabPaneController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tutorialTab;

    private Tab toggleTutorialTab;

    private TabPaneService tabPaneService = new TabPaneService();

    @FXML
    public void initialize() {
        System.out.println("Inizializzo TabPaneController...");

        // Configura il tab tutorial
        setupTutorialTab();

        // Crea il tab per riattivare il tutorial
        setupToggleTutorialTab();
    }

    public void createNewTab(Bot bot) {
        tabPaneService.createNewTab(tabPane, bot);
    }

    // Configura il tutorial tab con contenuto iniziale
    private void setupTutorialTab() {
        VBox tutorialContent = new VBox();
        tutorialContent.setSpacing(10);

        Label welcomeMessage = new Label("Benvenuto in Scriptagher!\n\n"
                + "Questo tutorial ti guiderà nell'utilizzo del software.\n"
                + "1. Puoi navigare tra i tab per accedere alle funzioni.\n"
                + "2. Usa il menu sulla sinistra per creare nuove funzioni dinamicamente.\n"
                + "3. Segui il tutorial per iniziare!");

        Button hideTutorialButton = new Button("Nascondi Tutorial");
        hideTutorialButton.setOnAction(event -> hideTutorial());

        tutorialContent.getChildren().addAll(welcomeMessage, hideTutorialButton);
        tutorialTab.setContent(tutorialContent);
        tutorialTab.setText("Tutorial");
    }

    // Crea il tab per riattivare il tutorial
    private void setupToggleTutorialTab() {
        toggleTutorialTab = new Tab("Mostra Tutorial");
        toggleTutorialTab.setClosable(false);
        toggleTutorialTab.setOnSelectionChanged(event -> {
            if (toggleTutorialTab.isSelected()) {
                showTutorial();
                tabPane.getTabs().remove(toggleTutorialTab);
            }
        });
    }

    // Mostra il tutorial
    private void showTutorial() {
        if (!tabPane.getTabs().contains(tutorialTab)) {
            tabPane.getTabs().add(0, tutorialTab);
            tabPane.getSelectionModel().select(tutorialTab);
        }
    }

    // Nascondi il tutorial e mostra il tab di attivazione
    private void hideTutorial() {
        tabPane.getTabs().remove(tutorialTab);
        if (!tabPane.getTabs().contains(toggleTutorialTab)) {
            tabPane.getTabs().add(0, toggleTutorialTab);
        }
    }

    public void dashboardStretch(AnchorPane leftPane, HeaderBarController headerBarController) {
        if (leftPane == null) {
            System.err.println("Errore: LeftPane non è stato trovato o non è collegato correttamente!");
        } else {
            System.out.println("LeftPane trovato e configurato.");
        }

        // Nascondi inizialmente il pannello sinistro
        if (leftPane != null) {
            leftPane.setTranslateX(-200.0);
            tabPane.setTranslateX(-200.0);
            BorderPane.setMargin(tabPane, new Insets(0, -200, 0, 0));
            leftPane.setVisible(false); // Nascondi all'inizializzazione
            headerBarController.configureDashboardButton(leftPane, tabPane);
            System.out.println("Pannello sinistro inizialmente nascosto.");
        }
    }

    @FXML
    public void handleStartTutorial(ActionEvent event) {
        System.out.println("Tutorial started");
        // Aggiungi la logica per avviare il tutorial
    }    
}