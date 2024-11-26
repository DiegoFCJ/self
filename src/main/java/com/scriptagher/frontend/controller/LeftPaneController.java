package com.scriptagher.frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class LeftPaneController {

    @FXML
    private AnchorPane leftPane;

    @FXML
    private MenuItem menuItemAction1;

    @FXML
    private MenuItem menuItemAction2;

    private TabPaneController tabPaneController;

    @FXML
    public void initialize() {
        System.out.println("Inizializzo LeftPaneController...");

        // Aggiungi listener per i menu item
        menuItemAction1.setOnAction(event -> createNewTab("Action 1"));
        menuItemAction2.setOnAction(event -> createNewTab("Action 2"));
    }

    // Metodo per creare un nuovo tab
    private void createNewTab(String actionName) {
        if (tabPaneController != null) {
            // Verifica se il tab con lo stesso nome esiste già
            Tab existingTab = getTabByName(actionName);

            if (existingTab != null) {
                // Se il tab esiste già, lo selezioniamo
                tabPaneController.selectTab(existingTab);
            } else {
                // Se il tab non esiste, creiamo un nuovo tab
                tabPaneController.addTab(actionName, "Contenuto del tab per " + actionName);
            }
        }
    }

    // Metodo per cercare un tab esistente per nome
    private Tab getTabByName(String title) {
        for (Tab tab : tabPaneController.getTabPane().getTabs()) {
            if (tab.getText().equals(title)) {
                return tab;
            }
        }
        return null; // Nessun tab trovato con il titolo dato
    }

    // Metodo per collegare il TabPaneController
    public void setTabPaneController(TabPaneController tabPaneController) {
        this.tabPaneController = tabPaneController;
    }

    public AnchorPane getLeftPane() {
        return leftPane;
    }

    @FXML
    public void handleMenuAction1(ActionEvent event) {
        System.out.println("Action 1 triggered");
        // Aggiungi la logica per gestire la prima azione del menu
    }

    @FXML
    public void handleMenuAction2(ActionEvent event) {
        System.out.println("Action 2 triggered");
        // Aggiungi la logica per gestire la seconda azione del menu
    }
}