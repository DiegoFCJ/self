package com.scriptagher.frontend.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.scriptagher.frontend.dto.Bot;
import com.scriptagher.frontend.service.BotDownloadService;
import com.scriptagher.frontend.service.BotMenuService;
import com.scriptagher.shared.constants.BOT_ENDPOINTS;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Tab;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LeftPaneController {

    @FXML
    private AnchorPane leftPane;

    @FXML
    private VBox menuVBox;

    private TabPaneController tabPaneController;

    private BotMenuService botService;

    public LeftPaneController() {
        botService = new BotMenuService();
    }

    @FXML
    public void initialize() {
        System.out.println("Inizializzo LeftPaneController...");
        loadBotMenus(); // Carica i menu dinamici dei bot
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

    /**
     * Loads the bot menus by fetching the available bots and grouping them by
     * language.
     * For each language, a `MenuButton` is created with the list of bots for that
     * language.
     * Each bot is displayed with its download icon, and the appropriate actions are
     * set for each bot.
     */
    private void loadBotMenus() {
        // Initialize the BotDownloadService to manage download actions
        BotDownloadService botDownloadService = new BotDownloadService();

        // Fetch the list of available bots from the backend service
        List<Bot> bots = botService.fetchBots(); // Retrieve bots list from the backend

        // Group the bots by their programming language
        Map<String, List<Bot>> botsByLanguage = bots.stream()
                .collect(Collectors.groupingBy(Bot::getLanguage));

        // Iterate over each language group
        for (String language : botsByLanguage.keySet()) {
            // Create a MenuButton for each language (e.g., Python, Java, JavaScript)
            MenuButton languageMenuButton = new MenuButton(language);
            languageMenuButton.setMinHeight(50);
            languageMenuButton.setMinWidth(200);

            // Configure the menu to open to the right
            languageMenuButton.setPopupSide(Side.RIGHT);

            // Retrieve the list of bots for the current language
            List<Bot> languageBots = botsByLanguage.get(language);

            // Add a MenuItem for each bot in the language group
            for (Bot bot : languageBots) {
                // Create a MenuItem for the bot
                MenuItem botItem = new MenuItem();

                // Create an HBox to hold the text and icons
                HBox botContent = new HBox();
                botContent.setSpacing(5); // Spacing of 5 pixels between elements
                botContent.setAlignment(Pos.CENTER_LEFT);

                // Create a Label for the bot name
                Label botLabel = new Label(bot.getBotName());
                botLabel.setMinWidth(150); // Optional: Set a fixed width for alignment

                // Add the appropriate download icon
                ImageView downloadIcon = botDownloadService.getDownloadIcon(language, bot.getBotName(), 20);

                // Add the delete icon (only visible if bot is available locally or on error)
                ImageView deleteIcon = new ImageView(
                        new Image(new File("src/main/resources/icons/icons8-elimina-50.png").toURI().toString()));
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);

                // Visibility of the delete icon depends on bot state
                deleteIcon.setVisible(botDownloadService.isBotAvailableLocally(language, bot.getBotName()));

                // Add hover effects for downloadIcon and deleteIcon
                applyHoverEffect(downloadIcon);
                applyHoverEffect(deleteIcon);

                // Action for delete button
                deleteIcon.setOnMouseClicked(event -> {
                    deleteBot(language, bot.getBotName());
                    downloadIcon.setImage(new Image(
                            new File("src/main/resources/icons/cloud/icons8-scarica-da-cloud-50.png").toURI()
                                    .toString()));
                    deleteIcon.setVisible(false);
                });

                // Add components to the HBox
                botContent.getChildren().addAll(botLabel, downloadIcon, deleteIcon);

                // Set the HBox as the graphic for the MenuItem
                botItem.setGraphic(botContent);

                // Set the action when a bot is selected
                botItem.setOnAction(event -> {
                    // Check if the bot is already available locally
                    if (!botDownloadService.isBotAvailableLocally(language, bot.getBotName())) {
                        // If the bot is not available locally, start the download process and update
                        // the icon
                        botDownloadService.downloadBot(language, bot.getBotName(), downloadIcon);
                    } else {
                        // If the bot is available locally, handle the bot action
                        handleBotAction(bot);
                    }
                });

                // Add the MenuItem to the MenuButton
                languageMenuButton.getItems().add(botItem);
            }

            // Add the MenuButton to the VBox for the UI
            menuVBox.getChildren().add(languageMenuButton);
        }
    }

    // Method to delete a bot from the local directory
    private void deleteBot(String language, String botName) {
        System.out.println("clicked delete " + language + "/" + botName);
        String botPath = BOT_ENDPOINTS.BASE_DATA_PATH + "/" + language + "/" + botName;
        File botDir = new File(botPath);
        if (botDir.exists()) {
            try {
                Files.walk(botDir.toPath())
                        .map(Path::toFile)
                        .sorted(Comparator.reverseOrder()) // Ensure directories are deleted after files
                        .forEach(File::delete); // Delete all files and the directory itself
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to apply hover effect to icons dynamically
    private void applyHoverEffect(ImageView icon) {
        icon.setOnMouseEntered(event -> {
            icon.setOpacity(0.7); // Reduce opacity on hover
            icon.setCursor(Cursor.HAND);;
            icon.setEffect(new DropShadow(10, Color.BLACK)); // Add a shadow effect
        });

        icon.setOnMouseExited(event -> {
            icon.setOpacity(1.0); // Restore original opacity
            icon.setEffect(null); // Remove the shadow effect
        });
    }

    // Gestisce le azioni dei bot
    private void handleBotAction(Bot bot) {
        System.out.println("Bot: " + bot.getBotName() + " - Action triggered");
        createNewTab(bot.getBotName());
    }
}