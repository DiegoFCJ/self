package com.scriptagher.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptagher.backend.model.Automation;
import com.scriptagher.shared.constants.APIS;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

/**
 * Service for executing automation bots and streaming their output in
 * real-time.
 */
@Service
public class BotExecutionService {

    /**
     * Executes a downloaded bot and streams its output in real-time.
     *
     * @param language     The programming language folder containing the bot (e.g.,
     *                     "java", "python").
     * @param botName      The name of the bot to execute.
     * @param clientOutput The output stream to which the bot's output will be
     *                     written.
     * @throws IOException If there is an error during execution or streaming the
     *                     output.
     */
    public void executeBot(String language, String botName, OutputStream clientOutput) throws IOException {
        // Costruisce il percorso del file Bot.json
        String botJsonPath = Paths.get(APIS.BOT_DIR_DATA, "/", language, botName, "Bot.json").toString();
        File botJsonFile = new File(botJsonPath);

        if (!botJsonFile.exists()) {
            throw new FileNotFoundException("Bot.json non trovato: " + botJsonPath);
        }

        // Carica il file Bot.json
        ObjectMapper objectMapper = new ObjectMapper();
        Automation automation = objectMapper.readValue(botJsonFile, Automation.class);

        // Ottieni il comando di avvio
        String botCommand = automation.getStartCommand();
        if (botCommand == null || botCommand.isEmpty()) {
            throw new IllegalArgumentException("Comando di avvio non valido per il bot: " + automation.getBotName());
        }

        // Comando completo: prima cd nella directory, poi esecuzione del comando del
        // bot
        String workingDir = Paths.get(APIS.BOT_DIR_DATA, "/", language).toString();
        String fullCommand = String.format("cd %s && %s", workingDir, botCommand);

        // Configura il processo
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", fullCommand);
        processBuilder.redirectErrorStream(true); // Unifica stdout e stderr

        // Avvia il processo
        Process process = processBuilder.start();

        // Gestione dell'output in parallelo
        try (
                BufferedReader processOutputReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                PrintWriter clientWriter = new PrintWriter(clientOutput, true) // Auto-flush attivato
        ) {
            // Legge l'output del processo e lo invia al client in tempo reale
            Executors.newSingleThreadExecutor().submit(() -> {
                String line;
                try {
                    while ((line = processOutputReader.readLine()) != null) {
                        clientWriter.println(line);
                    }
                } catch (IOException e) {
                    clientWriter.println("Errore durante la lettura dell'output: " + e.getMessage());
                }
            });

            // Aspetta la fine del processo
            int exitCode = process.waitFor();
            clientWriter.println("Processo terminato con codice di uscita: " + exitCode);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Esecuzione del processo interrotta", e);
        }
    }
}