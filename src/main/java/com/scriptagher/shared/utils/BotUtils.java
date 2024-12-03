package com.scriptagher.shared.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptagher.backend.model.Automation;
import com.scriptagher.shared.constants.APIS;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;

public class BotUtils {
    /**
     * Reads the bot.json file and creates an Automation object representing the
     * bot.
     *
     * @param botJsonPath The path to the bot.json file.
     * @return The Automation object representing the bot.
     * @throws IOException If there is an error reading the bot.json file.
     */
    public static Automation fetchBotDetails(String botJsonPath) throws IOException {
        File botJsonFile = new File(botJsonPath);
        if (!botJsonFile.exists()) {
            throw new IOException("bot.json not found in extracted bot directory.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try (BufferedReader reader = new BufferedReader(new FileReader(botJsonPath))) {
            // Deserialize the JSON into an Automation object
            return objectMapper.readValue(reader, Automation.class);
        }
    }

    /**
     * Ensures write permissions are set for the specified file or directory.
     * 
     * If the file is a directory, the method is applied recursively to its
     * contents.
     *
     * @param file The file or directory for which permissions need to be fixed.
     */
    public static void fixPermissions(File file) {
        // Check if the file or directory exists
        if (file.exists()) {
            // Attempt to set write permissions
            if (!file.setWritable(true, false)) {
                // Log a warning if unable to set permissions
                CustomLogger.warn(LOGS.BOT_SERVICE, String.format(LOGS.ERROR_PERMISSIONS, file.getAbsolutePath()));
            }

            // If the file is a directory, recursively fix permissions for its contents
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        fixPermissions(f); // Recursive call for sub-files and sub-directories
                    }
                }
            }
        }
    }

    /**
     * Checks if a bot is available locally by verifying the presence of required
     * files.
     *
     * @param language the language of the bot.
     * @param botName  the name of the bot.
     * @return true if the bot is available locally; false otherwise.
     */
    public static boolean isBotAvailableLocally(String language, String botName) {
        String botPath = APIS.BOT_DIR_DATA + "/" + language + "/" + botName;
        File botDir = new File(botPath);

        System.out.println(botDir.exists() && botDir.isDirectory());
        if (botDir.exists() && botDir.isDirectory()) {
            File botJson = new File(botDir, APIS.BOT_FILE_CONFIG);
            System.out.println(botJson.exists());
            return botJson.exists();
        }
        return false;
    }

}
