package com.scriptagher.backend.service.remote;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.stereotype.Service;
import com.scriptagher.backend.model.Automation;
import com.scriptagher.shared.constants.APIS;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import com.scriptagher.shared.utils.BotUtils;
import com.scriptagher.shared.utils.ZipUtils;

@Service
public class BotDownloadService {

    /**
     * Downloads a bot ZIP file and extracts its configuration.
     *
     * @param language The language folder of the bot (e.g., "python", "java").
     * @param botName  The name of the bot to be downloaded.
     * @return Automation object representing the bot.
     * @throws IOException If there is an issue downloading the bot.
     */
    public Automation downloadBot(String language, String botName) throws IOException {
        CustomLogger.info(LOGS.BOT_SERVICE, String.format(LOGS.DOWNLOAD_START, language, botName));

        // Construct the URL for the ZIP file
        String botZipUrl = String.format("%s/%s/%s/%s" + APIS.ZIP_EXTENSION, APIS.BASE_URL, language, botName, botName);
        File botDir = new File(APIS.BOT_DIR_DATA_REMOTE + "/" + language + "/" + botName);
        File botZip = new File(botDir, botName + APIS.ZIP_EXTENSION);

        // Check if the ZIP file already exists
        if (!botZip.exists()) {
            // Create directories if they do not exist
            if (!botDir.exists()) {
                botDir.mkdirs();
            }

            // Download the ZIP file
            downloadFile(botZipUrl, botZip);
        }

        // Force write permissions on the downloaded ZIP file
        BotUtils.fixPermissions(botZip);

        // Unzip the ZIP file
        CustomLogger.info(LOGS.BOT_SERVICE, String.format(LOGS.EXTRACT_START, botZip.getAbsolutePath()));
        ZipUtils.unzipFile(botZip, botDir);
        CustomLogger.info(LOGS.BOT_SERVICE, String.format(LOGS.EXTRACT_COMPLETE, botDir.getAbsolutePath()));

        // Read the bot.json file to get bot details
        String botJsonPath = botDir + "/" + APIS.BOT_FILE_CONFIG;
        Automation automation = BotUtils.fetchBotDetails(botJsonPath);

        // Set the language
        automation.setLanguage(language);

        // Delete the ZIP file after extraction
        botZip.delete();

        CustomLogger.info(LOGS.BOT_SERVICE, String.format(LOGS.DOWNLOAD_COMPLETE, automation.getBotName()));
        return automation;
    }

    /**
     * Downloads a file from a given URL to a specified location.
     *
     * @param fileUrl     The URL to download from.
     * @param destination The file location to save the downloaded content.
     * @throws IOException If the download fails.
     */
    private void downloadFile(String fileUrl, File destination) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(fileUrl).openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 200) {
            try (InputStream inputStream = connection.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(destination)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } else {
            String errorMessage = String.format(LOGS.ERROR_DOWNLOAD, fileUrl);
            CustomLogger.error(LOGS.BOT_SERVICE, errorMessage);
            throw new IOException("Failed to download file. Response code: " + connection.getResponseCode());
        }
    }

}
