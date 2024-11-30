package com.scriptagher.frontend.service;

import com.scriptagher.frontend.dto.Bot;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabPaneService {

    // Metodo per creare un nuovo tab
    public void createNewTab(TabPane tabPane, Bot bot) {
        String tabName = bot.getLanguage() + "/" + bot.getBotName();
        // Verifica se il tab con lo stesso nome esiste gi
        Tab existingTab = getTabByName(tabPane, tabName);

        if (existingTab != null) {
            // Se il tab esiste già, lo selezioniamo
            tabPane.getSelectionModel().select(existingTab);
        } else {
            // Se il tab non esiste, creiamo un nuovo tab
            this.addTab(tabPane, tabName, "Contenuto del tab per " + tabName);
        }
    }

    // Metodo per cercare un tab esistente per nome
    private Tab getTabByName(TabPane tabPane, String title) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(title)) {
                return tab;
            }
        }
        // Nessun tab trovato con il titolo dato
        return null;
    }

    // Aggiungi un nuovo tab dinamicamente
    public void addTab(TabPane tabPane, String title, String content) {
        Tab newTab = new Tab(title);
        Label contentLabel = new Label(content);
        newTab.setContent(contentLabel);
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
    }
}
