package com.scriptagher.frontend.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.scriptagher.frontend.dto.Bot;
import com.scriptagher.frontend.service.BotMenuService;
import com.scriptagher.frontend.service.LeftPaneService;
import com.scriptagher.shared.logger.CustomLogger;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the left pane of the UI.
 * Handles the initialization and rendering of bot menus and their associated
 * actions.
 */
public class LeftPaneController {

    @FXML
    private AnchorPane leftPane;

    @FXML
    private VBox menuVBox;

    private TabPaneController tabPaneController;

    private final BotMenuService botService = new BotMenuService();

    private final LeftPaneService leftPaneService = new LeftPaneService();
    private boolean pendingInitialization = false;

    /**
     * Initializes the controller and loads bot menus.
     */
    @FXML
    public void initialize() {
        CustomLogger.info("LeftPaneController", "Controller inizializzato.");
        if (tabPaneController == null) {
            CustomLogger.warn("LeftPaneController", "TabPaneController non ancora disponibile.");
        }
        loadBotMenus();
    }

    /**
     * Sets the TabPaneController reference for interaction.
     *
     * @param tabPaneController the TabPaneController instance.
     */
    public void setTabPaneController(TabPaneController tabPaneController) {
        CustomLogger.debug("Setter", "Setting TabPaneController instance.");
        this.tabPaneController = tabPaneController;
    
        if (pendingInitialization) {
            CustomLogger.info("LeftPaneController", "Eseguendo inizializzazione ritardata.");
            loadBotMenus();
            pendingInitialization = false;
        }
    }

    /**
     * Returns the AnchorPane instance for the left pane.
     *
     * @return the leftPane AnchorPane instance.
     */
    public AnchorPane getLeftPane() {
        return leftPane;
    }

    /**
     * Loads bot menus by fetching bot data from the backend service
     * and grouping them by language.
     */
    private void loadBotMenus() {
        if (tabPaneController == null) {
            CustomLogger.warn("LeftPaneController", "TabPaneController è null. Inizializzazione ritardata.");
            pendingInitialization = true;
            return;
        }

        CustomLogger.info("Loading Bots", "Fetching bot data and grouping by language.");
        try {
            List<Bot> bots = botService.fetchBots();
            Map<String, List<Bot>> botsByLanguage = bots.stream()
                    .collect(Collectors.groupingBy(Bot::getLanguage));

            for (String language : botsByLanguage.keySet()) {
                getLanguageMenu(botsByLanguage, language);
            }
        } catch (Exception e) {
            CustomLogger.error("Bot Loading Error", "Failed to load bot menus: " + e.getMessage());
        }
    }

    /**
     * Creates a menu for a specific programming language and adds it to the UI.
     *
     * @param botsByLanguage a map grouping bots by their programming language.
     * @param language       the programming language for the menu.
     */
    private void getLanguageMenu(Map<String, List<Bot>> botsByLanguage, String language) {
        leftPaneService.getLanguageMenu(botsByLanguage, language, menuVBox, tabPaneController);
    }
}