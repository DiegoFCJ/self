
package com.scriptagher.frontend.service;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import com.scriptagher.shared.constants.APIS;
import com.scriptagher.shared.constants.ICN;
import com.scriptagher.shared.logger.CustomLogger;
import com.scriptagher.shared.utils.BotUtils;
import com.scriptagher.shared.constants.LOGS;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.function.Consumer;

/**
 * Service class for managing bot downloads, including status updates and icon
 * management.
 */
public class BotDownloadService {

    // Listener to handle errors during the download process
    private Consumer<String> errorListener;

    /**
     * Sets a listener for handling download errors.
     *
     * @param listener the error listener to handle error messages.
     */
    public void setOnDownloadError(Consumer<String> listener) {
        this.errorListener = listener;
    }

    /**
     * Creates an image icon from a given file path.
     *
     * @param iconPath the path to the icon image.
     * @param size     the desired size of the icon.
     * @return an ImageView object representing the icon.
     */
    public ImageView createIcon(String iconPath, double size) {
        Image image = new Image(new File(iconPath).toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        return imageView;
    }

    /**
     * Retrieves the appropriate icon for the bot's availability status.
     *
     * @param language the language of the bot.
     * @param botName  the name of the bot.
     * @param size     the desired size of the icon.
     * @return an ImageView object representing the bot's status.
     */
    public ImageView getDownloadIcon(String language, String botName, double size) {
        boolean isAvailable = BotUtils.isBotAvailableLocally(language, botName);
        String iconPath = isAvailable ? ICN.PATH_CLOUD_MARK : ICN.PATH_CLOUD_DWNLD;
        return createIcon(iconPath, size);
    }

    /**
     * Initiates the bot download process and updates the UI accordingly.
     *
     * @param language            the language of the bot.
     * @param botName             the name of the bot.
     * @param icon                the icon to update during the download process.
     * @param deleteIconWrapper   the wrapper containing the delete icon.
     * @param downloadIconWrapper the wrapper containing the download icon.
     */
    public void downloadBot(String language, String botName, ImageView icon, StackPane deleteIconWrapper,
            StackPane downloadIconWrapper) {
        new Thread(() -> {
            try {
                CustomLogger.info(LOGS.BOT_DOWNLOAD_SERVICE, LOGS.DOWNLOAD_START + botName);
                updateIcon(icon, ICN.PATH_CLOUD_LOAD);

                downloadBotFromApi(language, botName);

                if (BotUtils.isBotAvailableLocally(language, botName)) {
                    updateIcon(icon, ICN.PATH_CLOUD_MARK);
                    makeDeleteIcnVisible(language, botName, deleteIconWrapper);
                    disableDwnld(getDownloadIcon(language, botName, 20), downloadIconWrapper);
                    CustomLogger.info(LOGS.BOT_DOWNLOAD_SERVICE, LOGS.DOWNLOAD_SUCCESS + botName);
                } else {
                    updateIcon(icon, ICN.PATH_CLOUD_DWNLD);
                    CustomLogger.warn(LOGS.BOT_DOWNLOAD_SERVICE, LOGS.BOT_NOT_FOUND_LOCALLY + botName);
                }
            } catch (Exception e) {
                CustomLogger.error(LOGS.BOT_DOWNLOAD_SERVICE, LOGS.DOWNLOAD_ERROR + botName + ". " + e.getMessage());
                updateIcon(icon, ICN.PATH_CLOUD_ERROR);

                if (errorListener != null) {
                    errorListener.accept(LOGS.ERR_DWNL_BOT + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Updates the icon's image in a thread-safe manner.
     *
     * @param icon     the ImageView to update.
     * @param iconPath the path to the new icon image.
     */
    private void updateIcon(ImageView icon, String iconPath) {
        Platform.runLater(() -> {
            icon.setImage(new Image(new File(iconPath).toURI().toString()));
            CustomLogger.info(LOGS.ICON_UPDATE, LOGS.ICN_UPDTD + iconPath);
        });
    }

    /**
     * Disables the download button when a bot is already downloaded.
     *
     * @param initialDownloadIcon the initial download icon.
     * @param downloadIconWrapper the wrapper containing the download icon.
     */
    public void disableDwnld(ImageView initialDownloadIcon, StackPane downloadIconWrapper) {
        if (initialDownloadIcon.getImage().getUrl().contains(ICN.CLOUD_MARK)) {
            downloadIconWrapper.setDisable(true);
            CustomLogger.debug(LOGS.BOT_DOWNLOAD_SERVICE, LOGS.DWNLD_BTN_DISABLED);
        }
    }

    /**
     * Retrieves a StackPane containing the delete icon.
     *
     * @return a StackPane containing the delete icon.
     */
    public StackPane getDeleteIconWrapper() {
        ImageView deleteIconImage = new ImageView(
                new Image(new File(ICN.PATH_BIN).toURI().toString()));
        deleteIconImage.setFitWidth(20);
        deleteIconImage.setFitHeight(20);

        return createIconWithCircle(deleteIconImage);
    }

    /**
     * Wraps an icon with a circular background and applies hover effects.
     *
     * @param icon the icon to wrap.
     * @return a StackPane containing the wrapped icon.
     */
    public StackPane createIconWithCircle(ImageView icon) {
        Circle backgroundCircle = new Circle(15);
        backgroundCircle.setFill(Color.TRANSPARENT);
        backgroundCircle.setStrokeWidth(1);

        StackPane iconWrapper = new StackPane();
        iconWrapper.getChildren().addAll(backgroundCircle, icon);
        iconWrapper.setPickOnBounds(false);
        StackPane.setAlignment(icon, Pos.CENTER);

        applyHoverEffect(iconWrapper, backgroundCircle);

        return iconWrapper;
    }

    /**
     * Applies hover effects to an icon container.
     *
     * @param container        the container to apply hover effects to.
     * @param backgroundCircle the circle to apply background changes to.
     */
    private void applyHoverEffect(StackPane container, Circle backgroundCircle) {
        container.setOnMouseEntered(event -> {
            container.setCursor(Cursor.HAND);
            backgroundCircle.setFill(Color.LIGHTGRAY.deriveColor(1, 1, 1, 0.3));
            backgroundCircle.setEffect(new DropShadow(10, Color.BLACK));
            CustomLogger.debug(LOGS.ICN_HOVER_EFFECT, LOGS.ICN_HOVER_EFFECT_APPL);
        });

        container.setOnMouseExited(event -> {
            backgroundCircle.setFill(Color.TRANSPARENT);
            backgroundCircle.setEffect(null);
            CustomLogger.debug(LOGS.ICN_HOVER_EFFECT, LOGS.ICN_HOVER_EFFECT_REM);
        });
    }

    /**
     * Makes the delete icon visible if the bot is available locally.
     *
     * @param language          the language of the bot.
     * @param botName           the name of the bot.
     * @param deleteIconWrapper the wrapper containing the delete icon.
     */
    public void makeDeleteIcnVisible(String language, String botName, StackPane deleteIconWrapper) {
        boolean isAvailable = BotUtils.isBotAvailableLocally(language, botName);
        deleteIconWrapper.setVisible(isAvailable);
        CustomLogger.info(LOGS.BOT_DOWNLOAD_SERVICE, LOGS.DEL_ICN_VIS + isAvailable);
    }

    /**
     * Downloads the bot from the backend API and saves it locally.
     *
     * @param language the language of the bot.
     * @param botName  the name of the bot.
     * @throws IOException if an I/O error occurs during the download.
     */
    private void downloadBotFromApi(String language, String botName) throws IOException {
        String apiUrl = APIS.BOT_API_BASE_URL + "/" + language + "/" + botName + APIS.DWNLD;
        URL url = new URL(apiUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 200) {
            String botPath = APIS.BOT_DIR_DATA_REMOTE + "/" + language + "/" + botName;
            File botDir = new File(botPath);
            if (!botDir.exists()) {
                botDir.mkdirs();
                CustomLogger.info(LOGS.BOT_DOWNLOAD_SERVICE, LOGS.CREATED_DIR + botName);
            }

            File botZip = new File(botDir, botName + APIS.ZIP_EXTENSION);
            Files.copy(connection.getInputStream(), botZip.toPath(), StandardCopyOption.REPLACE_EXISTING);
            CustomLogger.info(LOGS.BOT_DOWNLOAD_SERVICE, LOGS.DWNLD_SAVED + botName);
        } else {
            throw new IOException(LOGS.ERR_BTO_DWNLD + connection.getResponseCode());
        }
    }
}