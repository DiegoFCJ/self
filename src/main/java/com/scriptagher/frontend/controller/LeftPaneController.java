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
import com.scriptagher.shared.constants.CONST;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LeftPaneController {

    @FXML
    private AnchorPane leftPane;

    @FXML
    private VBox menuVBox;

    private TabPaneController tabPaneController;

    private BotMenuService botService = new BotMenuService();

    @FXML
    public void initialize() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/resources/fxml/MainView.fxml"));
        tabPaneController = loader.getController();

        loadBotMenus();
    }

    // Metodo per collegare il TabPaneController
    public void setTabPaneController(TabPaneController tabPaneController) {
        this.tabPaneController = tabPaneController;
    }

    public AnchorPane getLeftPane() {
        return leftPane;
    }

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

            // Add a CustomMenuItem for each bot in the language group
            for (Bot bot : languageBots) {
                // Create a CustomMenuItem for the bot
                CustomMenuItem botItem = new CustomMenuItem();
                botItem.setHideOnClick(false); // Avoid closing the menu on click

                // Create an HBox to hold the text and icons
                HBox botContent = new HBox();
                botContent.setSpacing(5); // Spacing of 5 pixels between elements
                botContent.setAlignment(Pos.CENTER_LEFT);

                // Create a Label for the bot name
                Label botLabel = new Label(bot.getBotName());
                botLabel.setMinWidth(150); // Optional: Set a fixed width for alignment

                // Add the download icon
                ImageView initialDownloadIcon = botDownloadService.getDownloadIcon(language, bot.getBotName(), 20);
                StackPane downloadIconWrapper = createIconWithCircle(initialDownloadIcon);

                // Disable the download icon if it's "cloud-marcato"
                if (initialDownloadIcon.getImage().getUrl().contains(CONST.CLOUD_MARK)) {
                    downloadIconWrapper.setDisable(true);
                }

                // Add the delete icon (only visible if bot is available locally or on error)
                ImageView deleteIconImage = new ImageView(
                        new Image(new File(CONST.BIN_ICON).toURI().toString()));
                deleteIconImage.setFitWidth(20);
                deleteIconImage.setFitHeight(20);

                // Create the delete icon with a circle wrapper
                StackPane deleteIconWrapper = createIconWithCircle(deleteIconImage);

                // Visibility of the delete icon depends on bot state
                deleteIconWrapper.setVisible(botDownloadService.isBotAvailableLocally(language, bot.getBotName()));

                // Action for delete button
                deleteIconWrapper.setOnMouseClicked(event -> {
                    event.consume(); // Prevent the event from propagating to the MenuItem
                    boolean isDeleted = deleteBot(language, bot.getBotName());
                    if (isDeleted) {
                        // Hide the delete icon
                        deleteIconWrapper.setVisible(false);

                        // Change the cloud-marcato icon to scarica-da-cloud if present
                        if (initialDownloadIcon.getImage().getUrl().contains(CONST.CLOUD_MARK)) {
                            initialDownloadIcon.setImage(
                                    new Image(new File(CONST.REMOTE_ICON_PATH)
                                            .toURI().toString()));
                            downloadIconWrapper.setDisable(false); // Make the icon clickable again
                        }
                    }
                });

                // Action for download button
                downloadIconWrapper.setOnMouseClicked(event -> {
                    event.consume(); // Prevent the event from propagating to the MenuItem

                    // Safely retrieve the ImageView from the StackPane
                    ImageView downloadIcon = (ImageView) downloadIconWrapper.getChildren().stream()
                            .filter(node -> node instanceof ImageView)
                            .findFirst()
                            .orElse(null);

                    if (downloadIcon != null) {
                        // Start the download process
                        botDownloadService.downloadBot(language, bot.getBotName(), downloadIcon);
                        // Visibility of the delete icon depends on bot state
                        deleteIconWrapper
                                .setVisible(botDownloadService.isBotAvailableLocally(language, bot.getBotName()));
                        // Disable the download icon if it's "cloud-marcato"
                        if (initialDownloadIcon.getImage().getUrl().contains(CONST.CLOUD_MARK)) {
                            downloadIconWrapper.setDisable(true);
                        }
                    } else {
                        System.err.println("Download icon not found in StackPane!");
                    }
                });

                // Handle the error condition
                botDownloadService.setOnDownloadError(error -> {
                    // If there's an error, change the download icon to error icon
                    initialDownloadIcon.setImage(new Image(new File(CONST.ERROR_ICON_PATH).toURI().toString()));
                    downloadIconWrapper.setDisable(true); // Disable the icon during error state

                    // Optionally, you can also update the delete icon or any other elements
                    System.err.println("Error downloading bot: " + error);
                });

                // Add components to the HBox
                botContent.getChildren().addAll(botLabel, downloadIconWrapper, deleteIconWrapper);

                // Set the HBox as the content for the CustomMenuItem
                botItem.setContent(botContent);

                // Add a handler for clicking the bot item (ignoring events from icons)
                botContent.setOnMouseClicked(event -> {
                    // Check if the bot is already available locally
                    if (!botDownloadService.isBotAvailableLocally(language, bot.getBotName())) {
                        // If the bot is not available locally, start the download process
                        botDownloadService.downloadBot(language, bot.getBotName(),
                                (ImageView) downloadIconWrapper.getChildren().get(0));
                    } else {
                        // If the bot is available locally, handle the bot action
                        handleBotAction(bot);
                    }
                });

                // Add the CustomMenuItem to the MenuButton
                languageMenuButton.getItems().add(botItem);
            }

            // Add the MenuButton to the VBox for the UI
            menuVBox.getChildren().add(languageMenuButton);
        }
    }

    private StackPane createIconWithCircle(ImageView icon) {
        Circle backgroundCircle = new Circle(15); // Circle radius
        backgroundCircle.setFill(Color.TRANSPARENT); // Transparent background
        backgroundCircle.setStrokeWidth(1);

        // Wrap the icon in a StackPane with the circle
        StackPane iconWrapper = new StackPane();
        iconWrapper.getChildren().addAll(backgroundCircle, icon);

        // Imposta il clic solo sull'area visibile
        iconWrapper.setPickOnBounds(false);

        // Center-align the icon
        StackPane.setAlignment(icon, Pos.CENTER);

        // Apply hover effects dynamically
        applyHoverEffect(iconWrapper, backgroundCircle);

        return iconWrapper;
    }

    private void applyHoverEffect(StackPane container, Circle backgroundCircle) {
        container.setOnMouseEntered(event -> {
            container.setCursor(Cursor.HAND);
            backgroundCircle.setFill(Color.LIGHTGRAY.deriveColor(1, 1, 1, 0.3)); // Light gray hover effect
            backgroundCircle.setEffect(new DropShadow(10, Color.BLACK)); // Optional shadow effect
        });

        container.setOnMouseExited(event -> {
            backgroundCircle.setFill(Color.TRANSPARENT); // Reset to transparent
            backgroundCircle.setEffect(null); // Remove shadow effect
        });
    }

    private boolean deleteBot(String language, String botName) {

        String botPath = CONST.BASE_DATA_PATH + "/" + language + "/" + botName;
        File botDir = new File(botPath);

        if (!botDir.exists()) {
            return false;
        }

        try {
            // Walk the file tree, delete files, then directories
            Files.walk(botDir.toPath())
                    .map(Path::toFile)
                    .sorted(Comparator.reverseOrder()) // Ensure directories are deleted after files
                    .forEach(file -> {
                        boolean deleted = file.delete();
                        if (!deleted) {
                            System.err.println("Failed to delete file: " + file.getAbsolutePath());
                        }
                    });

            return true;

        } catch (IOException e) {
            System.err.println("An error occurred while deleting bot: " + language + "/" + botName);
            e.printStackTrace();
            return false;
        }
    }

    // Gestisce le azioni dei bot
    private void handleBotAction(Bot bot) {
        tabPaneController.createNewTab(bot);
    }
}