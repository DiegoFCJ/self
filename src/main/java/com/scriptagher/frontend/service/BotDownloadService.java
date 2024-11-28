package com.scriptagher.frontend.service;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.scriptagher.shared.constants.BOT_ENDPOINTS;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BotDownloadService {

    // Controlla se il bot è presente in locale
    public boolean isBotAvailableLocally(String language, String botName) {
        String botPath = BOT_ENDPOINTS.BASE_DATA_PATH + "/" + language + "/" + botName;
        File botDir = new File(botPath);

        if (botDir.exists() && botDir.isDirectory()) {
            File botFile = new File(botDir, botName + ".zip");
            File botJson = new File(botDir, "Bot.json");
            return botFile.exists() && botJson.exists(); // Il bot è completo se entrambi i file esistono
        }
        return false;
    }

    // Crea un'icona con immagine specifica
    public ImageView createIcon(String iconPath, double size) {
        Image image = new Image(new File(iconPath).toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        return imageView;
    }

    // Restituisce l'icona appropriata
    public ImageView getDownloadIcon(String language, String botName, double size) {
        boolean isAvailable = isBotAvailableLocally(language, botName);
        String iconPath = isAvailable ? BOT_ENDPOINTS.LOCAL_ICON_PATH : BOT_ENDPOINTS.REMOTE_ICON_PATH;
        return createIcon(iconPath, size);
    }

    // Simula il download del bot e aggiorna l'icona
    public void downloadBot(String language, String botName, ImageView icon) {
        new Thread(() -> {
            try {
                // Cambia l'icona in "download in corso"
                updateIcon(icon, BOT_ENDPOINTS.DOWNLOADING_ICON_PATH);

                // Scarica il bot dal backend
                downloadBotFromApi(language, botName);

                // Verifica la presenza dei file dopo il download
                if (isBotAvailableLocally(language, botName)) {
                    // Cambia l'icona in "disponibile in locale"
                    updateIcon(icon, BOT_ENDPOINTS.LOCAL_ICON_PATH);
                } else {
                    // Se il download non è riuscito, torna a "scarica da cloud"
                    updateIcon(icon, BOT_ENDPOINTS.REMOTE_ICON_PATH);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Cambia l'icona in "errore"
                updateIcon(icon, BOT_ENDPOINTS.ERROR_ICON_PATH);
            }
        }).start();
    }

    // Aggiorna l'immagine dell'icona (thread-safe)
    private void updateIcon(ImageView icon, String iconPath) {
        Platform.runLater(() -> icon.setImage(new Image(new File(iconPath).toURI().toString())));
    }

    // Scarica il bot dal backend e salva i file in locale
    private void downloadBotFromApi(String language, String botName) throws IOException {
        // Costruisci l'URL dell'API per il download
        String apiUrl = BOT_ENDPOINTS.BOT_API_BASE_URL + "/" + language + "/" + botName + "/download";
        URL url = new URL(apiUrl);

        // Effettua la richiesta al backend
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Verifica la risposta
        if (connection.getResponseCode() == 200) {
            // Crea il percorso della directory per il bot
            String botPath = BOT_ENDPOINTS.BASE_DATA_PATH + "/" + language + "/" + botName;
            File botDir = new File(botPath);
            if (!botDir.exists()) {
                botDir.mkdirs();
            }

            // Salva il file zip scaricato
            File botZip = new File(botDir, botName + ".zip");
            Files.copy(connection.getInputStream(), botZip.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else {
            throw new IOException("Errore durante il download del bot: " + connection.getResponseCode());
        }
    }
}