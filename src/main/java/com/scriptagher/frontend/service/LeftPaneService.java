package com.scriptagher.frontend.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.scriptagher.frontend.controller.TabPaneController;
import com.scriptagher.frontend.dto.Bot;
import com.scriptagher.shared.constants.APIS;
import com.scriptagher.shared.constants.ICN;
import com.scriptagher.shared.logger.CustomLogger;
import com.scriptagher.shared.utils.BotUtils;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LeftPaneService {

    private final BotDownloadService botDownloadService = new BotDownloadService();

    public void getLanguageMenu(
            Map<String, List<Bot>> botsByLanguage,
            String language,
            VBox menuVBox,
            TabPaneController tabPaneController) {
        CustomLogger.debug("Creating Menu", "Creating menu for language: " + language);
        MenuButton languageMenuButton = new MenuButton(language);
        languageMenuButton.setMinHeight(50);
        languageMenuButton.setMinWidth(200);
        languageMenuButton.setPopupSide(Side.RIGHT);

        List<Bot> languageBots = botsByLanguage.get(language);
        for (Bot bot : languageBots) {
            getMenuItem(language, languageMenuButton, bot, tabPaneController);
        }
        menuVBox.getChildren().add(languageMenuButton);
    }

    /**
     * Creates a menu item for a bot and adds it to the language menu.
     *
     * @param language           the programming language of the bot.
     * @param languageMenuButton the menu button to add the bot item to.
     * @param bot                the bot instance.
     */
    private void getMenuItem(String language, MenuButton languageMenuButton, Bot bot,
            TabPaneController tabPaneController) {
        CustomLogger.debug("Creating MenuItem", "Adding bot to menu: " + bot.getBotName());
        CustomMenuItem botItem = new CustomMenuItem();
        botItem.setHideOnClick(false);

        HBox botContent = new HBox();
        botContent.setSpacing(5);
        botContent.setAlignment(Pos.CENTER_LEFT);

        Label botLabel = new Label(bot.getBotName());
        botLabel.setMinWidth(150);

        ImageView initialDownloadIcon = botDownloadService.getDownloadIcon(language, bot.getBotName(), 20);
        StackPane downloadIconWrapper = botDownloadService.createIconWithCircle(initialDownloadIcon);
        botDownloadService.disableDwnld(initialDownloadIcon, downloadIconWrapper);
        StackPane deleteIconWrapper = botDownloadService.getDeleteIconWrapper();
        botDownloadService.makeDeleteIcnVisible(language, bot.getBotName(), deleteIconWrapper);

        handleDeleteBtn(language, bot, initialDownloadIcon, downloadIconWrapper, deleteIconWrapper);
        handleDwnldBtn(language, bot, initialDownloadIcon, downloadIconWrapper, deleteIconWrapper, tabPaneController);
        handleErrBtn(initialDownloadIcon, downloadIconWrapper);

        botContent.getChildren().addAll(botLabel, downloadIconWrapper, deleteIconWrapper);
        botItem.setContent(botContent);

        handleMenuItemClick(language, bot, botContent, downloadIconWrapper, deleteIconWrapper, tabPaneController);
        languageMenuButton.getItems().add(botItem);
    }

    /**
     * Handles the click event for a bot menu item.
     *
     * @param language            the programming language of the bot.
     * @param bot                 the bot instance.
     * @param botContent          the HBox containing bot information.
     * @param downloadIconWrapper the wrapper for the download icon.
     * @param deleteIconWrapper   the wrapper for the delete icon.
     */
    private void handleMenuItemClick(
            String language,
            Bot bot,
            HBox botContent,
            StackPane downloadIconWrapper,
            StackPane deleteIconWrapper,
            TabPaneController tabPaneController) {
        botContent.setOnMouseClicked(event -> {
            CustomLogger.info("MenuItem Click", "Bot clicked: " + bot.getBotName());
            if (!BotUtils.isBotAvailableLocally(language, bot.getBotName())) {
                ImageView downloadIcon = (ImageView) downloadIconWrapper.getChildren().stream()
                        .filter(node -> node instanceof ImageView)
                        .findFirst()
                        .orElse(null);

                botDownloadService.downloadBot(
                        language,
                        bot.getBotName(),
                        downloadIcon,
                        deleteIconWrapper,
                        downloadIconWrapper);
            } else {
                handleBotAction(bot, tabPaneController);
            }
        });
    }

    /**
     * Handles errors during the bot download process.
     *
     * @param initialDownloadIcon the download icon to update in case of an error.
     * @param downloadIconWrapper the wrapper containing the download icon.
     */
    private void handleErrBtn(ImageView initialDownloadIcon, StackPane downloadIconWrapper) {
        botDownloadService.setOnDownloadError(error -> {
            // Log the error for debugging purposes
            CustomLogger.error("Download Error", "Error occurred while downloading bot: " + error);

            // Change the download icon to indicate an error state
            initialDownloadIcon.setImage(new Image(new File(ICN.PATH_CLOUD_ERROR).toURI().toString()));

            // Disable the download icon to prevent further interaction
            downloadIconWrapper.setDisable(true);

            // Optionally log additional actions for error handling
            CustomLogger.debug("Error Handling", "Download icon set to error state and disabled.");
        });
    }

    /**
     * Handles the click event for the download button.
     * Initiates the bot download process and updates the UI accordingly.
     *
     * @param language            the programming language of the bot.
     * @param bot                 the bot to be downloaded.
     * @param initialDownloadIcon the icon representing the download state.
     * @param downloadIconWrapper the wrapper containing the download icon.
     * @param deleteIconWrapper   the wrapper containing the delete icon.
     */
    private void handleDwnldBtn(
            String language, Bot bot,
            ImageView initialDownloadIcon,
            StackPane downloadIconWrapper,
            StackPane deleteIconWrapper,
            TabPaneController tabPaneController) {
        downloadIconWrapper.setOnMouseClicked(event -> {
            // Prevent the event from propagating to other UI elements
            event.consume();

            // Retrieve the ImageView for the download icon
            ImageView downloadIcon = (ImageView) downloadIconWrapper.getChildren().stream()
                    .filter(node -> node instanceof ImageView)
                    .findFirst()
                    .orElse(null);

            if (downloadIcon != null) {
                // Log the start of the download process
                CustomLogger.info("Download Start", "Starting download for bot: " + bot.getBotName());

                // Start the bot download
                botDownloadService.downloadBot(language, bot.getBotName(), downloadIcon, deleteIconWrapper,
                        downloadIconWrapper);

                // Disable the download icon to prevent repeated clicks
                botDownloadService.disableDwnld(initialDownloadIcon, downloadIconWrapper);

                // Perform any actions required after initiating the download
                handleBotAction(bot, tabPaneController);
            } else {
                // Log a warning if the download icon cannot be found
                CustomLogger.warn("Download Icon Missing", "Download icon not found for bot: " + bot.getBotName());
            }
        });
    }

    /**
     * Handles the click event for the delete button.
     * Deletes the bot files and updates the UI.
     *
     * @param language            the programming language of the bot.
     * @param bot                 the bot to be deleted.
     * @param initialDownloadIcon the icon representing the download state.
     * @param downloadIconWrapper the wrapper containing the download icon.
     * @param deleteIconWrapper   the wrapper containing the delete icon.
     */
    private void handleDeleteBtn(String language, Bot bot, ImageView initialDownloadIcon, StackPane downloadIconWrapper,
            StackPane deleteIconWrapper) {
        deleteIconWrapper.setOnMouseClicked(event -> {
            // Prevent the event from propagating to other UI elements
            event.consume();

            // Log the delete action
            CustomLogger.info("Delete Action", "Deleting bot: " + bot.getBotName());

            // Attempt to delete the bot files
            boolean isDeleted = deleteBot(language, bot.getBotName());

            if (isDeleted) {
                // Hide the delete icon after successful deletion
                deleteIconWrapper.setVisible(false);
                CustomLogger.info("Delete Success", "Successfully deleted bot: " + bot.getBotName());

                // Reset the download icon if necessary
                if (initialDownloadIcon.getImage().getUrl().contains(ICN.CLOUD_MARK)) {
                    initialDownloadIcon.setImage(
                            new Image(new File(ICN.PATH_CLOUD_DWNLD).toURI().toString()));
                    downloadIconWrapper.setDisable(false);
                    CustomLogger.debug("Download Icon Restored", "Download icon reset for bot: " + bot.getBotName());
                }
            } else {
                // Log an error if the deletion failed
                CustomLogger.error("Delete Failure", "Failed to delete bot: " + bot.getBotName());
            }
        });
    }

    /**
     * Deletes the bot files from the local storage.
     *
     * @param language the programming language of the bot.
     * @param botName  the name of the bot to be deleted.
     * @return true if the bot was successfully deleted, false otherwise.
     */
    private boolean deleteBot(String language, String botName) {
        // Construct the path to the bot's directory
        String botPath = APIS.BOT_DIR_DATA_REMOTE + "/" + language + "/" + botName;
        File botDir = new File(botPath);

        // Check if the directory exists
        if (!botDir.exists()) {
            CustomLogger.warn("Directory Missing", "Directory not found for bot: " + language + "/" + botName);
            return false;
        }

        try {
            // Walk the directory tree and delete files and directories in reverse order
            Files.walk(botDir.toPath())
                    .map(Path::toFile)
                    .sorted(Comparator.reverseOrder())
                    .forEach(file -> {
                        boolean deleted = file.delete();
                        if (deleted) {
                            CustomLogger.debug("File Deleted", "Deleted file: " + file.getAbsolutePath());
                        } else {
                            CustomLogger.error("File Deletion Failure",
                                    "Failed to delete file: " + file.getAbsolutePath());
                        }
                    });

            return true;
        } catch (IOException e) {
            // Log any exception that occurs during deletion
            CustomLogger.error("Delete Bot Error",
                    "IOException occurred while deleting bot: " + language + "/" + botName + ". " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Handles actions for a bot when it is clicked or selected.
     *
     * @param bot the bot to handle.
     */
    private void handleBotAction(Bot bot, TabPaneController tabPaneController) {

        if (tabPaneController == null) {
            CustomLogger.error("Bot Action", "TabPaneController è null. Azione non eseguibile.");
            return; // Evita di procedere se il controller è null
        }
        if (bot == null) {
            CustomLogger.error("Bot Action", "Bot è null. Azione non eseguibile.");
            return; // Evita di procedere se il bot è null
        }
        // Log the action of opening a new tab for the bot
        CustomLogger.info("Bot Action", "Opening bot tab for: " + bot.getBotName());

        Optional.ofNullable(bot)
                .ifPresentOrElse(
                        b -> tabPaneController.createNewTab(b),
                        () -> CustomLogger.error("Bot Action", "Bot è null."));
    }
}