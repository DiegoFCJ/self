package com.scriptagher.shared.utils;

import java.io.File;

import com.scriptagher.shared.constants.APIS;

public class BotDwnldUtils {

    /**
     * Checks if a bot is available locally by verifying the presence of required
     * files.
     *
     * @param language the language of the bot.
     * @param botName  the name of the bot.
     * @return true if the bot is available locally; false otherwise.
     */
    public static boolean isBotAvailableLocally(String language, String botName) {
        String botPath = APIS.DIR_DATA + "/" + language + "/" + botName;
        File botDir = new File(botPath);

        System.out.println(botDir.exists() && botDir.isDirectory());
        if (botDir.exists() && botDir.isDirectory()) {
            File botJson = new File(botDir, APIS.BOT_CONFIG_FILE);
            System.out.println(botJson.exists());
            return botJson.exists();
        }
        return false;
    }
    
}
