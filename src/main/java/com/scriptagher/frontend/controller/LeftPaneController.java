package com.scriptagher.frontend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.scriptagher.frontend.dto.Bot;
import com.scriptagher.frontend.service.BotMenuService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Tab;

public class LeftPaneController {

    @FXML
    private AnchorPane leftPane;

    @FXML
    private VBox menuVBox;  // Questa VBox conterrà i MenuButton

    private TabPaneController tabPaneController;

    private BotMenuService botService;  // Istanza di BotService per recuperare i bot

    public LeftPaneController() {
        botService = new BotMenuService();  // Costruttore per inizializzare il BotService
    }

    @FXML
    public void initialize() {
        System.out.println("Inizializzo LeftPaneController...");
        loadBotMenus();  // Carica i menu dinamici dei bot
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

    private void loadBotMenus() {
        List<Bot> bots = botService.fetchBots();  // Supponiamo che Bot rappresenti i bot

        // Raggruppa i bot per linguaggio
        Map<String, List<Bot>> botsByLanguage = bots.stream()
                .collect(Collectors.groupingBy(Bot::getLanguage));

        // Per ogni linguaggio, crea un MenuButton
        for (String language : botsByLanguage.keySet()) {
            // Crea un MenuButton per ogni linguaggio (es. Python, Java, JavaScript)
            MenuButton languageMenuButton = new MenuButton(language);
            languageMenuButton.setMinHeight(50);
            languageMenuButton.setMinWidth(200);

            // Recupera i bot per il linguaggio corrente
            List<Bot> languageBots = botsByLanguage.get(language);

            // Aggiungi un MenuItem per ogni bot nel linguaggio corrente
            for (Bot bot : languageBots) {
                MenuItem botItem = new MenuItem(bot.getBotName());
                botItem.setOnAction(event -> handleBotAction(bot));
                languageMenuButton.getItems().add(botItem);
            }

            // Aggiungi il MenuButton alla VBox
            menuVBox.getChildren().add(languageMenuButton);
        }
    }

    // Gestisce le azioni dei bot
    private void handleBotAction(Bot bot) {
        System.out.println("Bot: " + bot.getBotName() + " - Action triggered");
        createNewTab(bot.getBotName());
    }
}