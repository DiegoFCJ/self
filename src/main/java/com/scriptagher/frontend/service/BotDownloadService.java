package com.scriptagher.frontend.service;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import com.scriptagher.shared.constants.CONST;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.function.Consumer;

public class BotDownloadService {

    // Listener to handle errors during the download process
    private Consumer<String> errorListener;

    // Sets a listener for download errors
    public void setOnDownloadError(Consumer<String> listener) {
        this.errorListener = listener;
    }

    // Checks if the bot is available locally
    public boolean isBotAvailableLocally(String language, String botName) {
        String botPath = CONST.BASE_DATA_PATH + "/" + language + "/" + botName;
        File botDir = new File(botPath);

        if (botDir.exists() && botDir.isDirectory()) {
            File botFile = new File(botDir, botName + ".zip");
            File botJson = new File(botDir, "Bot.json");
            return botFile.exists() && botJson.exists();
        }
        return false;
    }

    // Creates an icon with a specific image
    public ImageView createIcon(String iconPath, double size) {
        Image image = new Image(new File(iconPath).toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        return imageView;
    }

    // Returns the appropriate icon for the bot's availability status
    public ImageView getDownloadIcon(String language, String botName, double size) {
        boolean isAvailable = isBotAvailableLocally(language, botName);
        String iconPath = isAvailable ? CONST.LOCAL_ICON_PATH : CONST.REMOTE_ICON_PATH;
        return createIcon(iconPath, size);
    }

    // Simulates downloading the bot and updates the icon based on its status
    public void downloadBot(String language, String botName, ImageView icon) {
        new Thread(() -> {
            try {
                updateIcon(icon, CONST.DOWNLOADING_ICON_PATH);

                downloadBotFromApi(language, botName);

                if (isBotAvailableLocally(language, botName)) {
                    updateIcon(icon, CONST.LOCAL_ICON_PATH);
                } else {
                    updateIcon(icon, CONST.REMOTE_ICON_PATH);
                }
            } catch (Exception e) {
                e.printStackTrace();
                updateIcon(icon, CONST.ERROR_ICON_PATH);

                if (errorListener != null) {
                    errorListener.accept("Error while downloading the bot: " + e.getMessage());
                }
            }
        }).start();
    }

    // Updates the icon's image in a thread-safe way
    private void updateIcon(ImageView icon, String iconPath) {
        Platform.runLater(() -> icon.setImage(new Image(new File(iconPath).toURI().toString())));
    }

    // Downloads the bot from the backend API and saves it locally
    private void downloadBotFromApi(String language, String botName) throws IOException {
        String apiUrl = CONST.BOT_API_BASE_URL + "/" + language + "/" + botName + "/download";
        URL url = new URL(apiUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 200) {
            String botPath = CONST.BASE_DATA_PATH + "/" + language + "/" + botName;
            File botDir = new File(botPath);
            if (!botDir.exists()) {
                botDir.mkdirs();
            }

            File botZip = new File(botDir, botName + ".zip");
            Files.copy(connection.getInputStream(), botZip.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else {
            throw new IOException("Error during bot download: " + connection.getResponseCode());
        }
    }
}