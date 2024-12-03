package com.scriptagher.backend.service;

import com.scriptagher.backend.model.Automation;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.Paths;

/**
 * Service for executing automation bots and streaming their output in
 * real-time.
 */
@Service
public class ExecutionService {

    /**
     * Executes a bot and streams its output in real-time to the console.
     *
     * @param botName the name of the bot file to be executed. The bot can be a
     *                Java, Python, or JavaScript file.
     */
    public void executeBot(String botName) {
        try {
            // Log l'inizio dell'esecuzione
            CustomLogger.info(LOGS.EXECUTION_SERVICE, String.format(LOGS.STARTING_EXECUTION, botName));

            // Costruzione del percorso del file Bot.json
            String botDirectory = "data"; // Base directory
            String botJsonPath = Paths
                    .get(botDirectory, getLanguageFolder(botName), botName.replace(".java", ""), "Bot.json").toString();

            // Leggi il file JSON
            ObjectMapper objectMapper = new ObjectMapper();
            File botJsonFile = new File(botJsonPath);
            if (!botJsonFile.exists()) {
                throw new IllegalArgumentException("Bot.json non trovato per il bot: " + botName);
            }

            // Carica i dettagli del bot in un oggetto Automation
            Automation automation = objectMapper.readValue(botJsonFile, Automation.class);

            // Costruisci il comando da eseguire
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", automation.getStartCommand());
            processBuilder.directory(new File(botDirectory)); // Imposta la directory di lavoro

            // Avvia il processo
            Process process = processBuilder.start();

            // Stream l'output del processo in tempo reale
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    CustomLogger.error(LOGS.EXECUTION_SERVICE,
                            "Errore durante la lettura dell'output: " + e.getMessage());
                }
            }).start();

            // Aspetta il completamento del processo
            process.waitFor();

            // Log fine esecuzione
            CustomLogger.info(LOGS.EXECUTION_SERVICE, String.format(LOGS.BOT_EXECUTION_COMPLETED, botName));

        } catch (Exception e) {
            CustomLogger.error(LOGS.EXECUTION_SERVICE, String.format(LOGS.ERROR_EXECUTING_BOT, e.getMessage()));
        }
    }

    private String getLanguageFolder(String botName) {
        if (botName.endsWith(".java"))
            return "java";
        if (botName.endsWith(".py"))
            return "python";
        if (botName.endsWith(".js"))
            return "javascript";
        throw new IllegalArgumentException("Tipo di bot non supportato: " + botName);
    }

}