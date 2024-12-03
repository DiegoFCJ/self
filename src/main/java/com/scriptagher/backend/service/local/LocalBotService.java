package com.scriptagher.backend.service.local;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptagher.backend.model.LocalBot;
import com.scriptagher.shared.constants.APIS;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class LocalBotService {

    /**
     * Fetches the list of local bots by reading or creating the `local_bots.json` file.
     * 
     * @return A map where keys are programming languages and values are lists of bots.
     * @throws IOException If there is an error during file operations.
     */
    public Map<String, List<LocalBot>> fetchLocalBots() throws IOException {
        // Ensure the local directory and JSON file exist
        ensureLocalBotsFile();

        // Parse the `local_bots.json` file if it exists
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<LocalBot>> localBots = new HashMap<>();

        File botsFile = new File(APIS.LOCAL_BOTS_FILE);
        if (botsFile.exists()) {
            localBots = mapper.readValue(botsFile, Map.class);
        }

        // Scan the local directory for bots
        File localDir = new File(APIS.BOT_DIR_DATA_LOCAL);
        File[] languageDirs = localDir.listFiles(File::isDirectory);

        if (languageDirs != null) {
            for (File languageDir : languageDirs) {
                String language = languageDir.getName();
                File[] botDirs = languageDir.listFiles(File::isDirectory);

                List<LocalBot> bots = new ArrayList<>();
                if (botDirs != null) {
                    for (File botDir : botDirs) {
                        File botJson = new File(botDir, "Bot.json");
                        File[] otherFiles = botDir.listFiles(file -> !file.getName().equals("Bot.json"));

                        if (botJson.exists() && otherFiles != null && otherFiles.length > 0) {
                            String botName = botDir.getName();
                            String otherFilePath = otherFiles[0].getName();
                            bots.add(new LocalBot(botName, otherFilePath));
                        }
                    }
                }
                localBots.put(language, bots);
            }
        }

        // Save the updated bot list back to `local_bots.json`
        mapper.writeValue(new File(APIS.LOCAL_BOTS_FILE), localBots);

        return localBots;
    }

    /**
     * Ensures the local directory and `local_bots.json` file exist.
     * 
     * @throws IOException If there's an error creating the file or directory.
     */
    private void ensureLocalBotsFile() throws IOException {
        // Ensure local directory exists
        File localDir = new File(APIS.BOT_DIR_DATA_REMOTE);
        if (!localDir.exists()) {
            Files.createDirectories(Paths.get(APIS.BOT_DIR_DATA_REMOTE));
        }

        // Ensure local_bots.json file exists
        File botsFile = new File(APIS.LOCAL_BOTS_FILE);
        if (!botsFile.exists()) {
            Files.createFile(Paths.get(APIS.LOCAL_BOTS_FILE));
            // Initialize the file with an empty JSON object
            Files.write(Paths.get(APIS.LOCAL_BOTS_FILE), "{}".getBytes());
        }
    }
}
