package com.scriptagher.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptagher.backend.model.Automation;
import com.scriptagher.shared.constants.BOT_ENDPOINTS;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Service class responsible for downloading, executing, and fetching
 * information about bots.
 */
@Service
public class BotService {

    /**
     * Downloads a bot ZIP file and extracts its configuration.
     *
     * @param language The language folder of the bot (e.g., "python", "java").
     * @param botName  The name of the bot to be downloaded.
     * @return Automation object representing the bot.
     * @throws IOException If there is an issue downloading the bot.
     */
    public Automation downloadBot(String language, String botName) throws IOException {
        // Costruisci l'URL per scaricare il file ZIP
        String botZipUrl = String.format("%s/%s/%s/%s.zip", BOT_ENDPOINTS.BASE_URL, language, botName, botName);
        File botDir = new File("data/" + language + "/" + botName);
        File botZip = new File(botDir, botName + ".zip");

        // Verifica se il file ZIP esiste già prima di scaricarlo
        if (!botZip.exists()) {
            // Crea le directory se non esistono
            if (!botDir.exists()) {
                botDir.mkdirs();
            }

            // Scarica il file ZIP
            downloadFile(botZipUrl, botZip);
        }

        // Forza i permessi di scrittura sul file ZIP appena scaricato
        fixPermissions(botZip);

        // Scompatta il file ZIP
        unzipFile(botZip, botDir);

        // Leggi il file bot.json per ottenere i dettagli del bot
        String botJsonPath = botDir + "/Bot.json";
        Automation automation = fetchBotDetails(botJsonPath);

        // Imposta il linguaggio
        automation.setLanguage(language);

        System.out.println(botZip);
        // Elimina il file ZIP dopo l'estrazione
        botZip.delete();

        return automation;
    }

    private void fixPermissions(File file) {
        if (file.exists()) {
            // Imposta i permessi di scrittura per la cartella o il file
            if (!file.setWritable(true, false)) {
                System.err.println("Impossibile impostare i permessi di scrittura per: " + file.getAbsolutePath());
            } else {
                System.out.println("Permessi di scrittura settati correttamente per: " + file.getAbsolutePath());
            }
    
            // Se è una directory, percorri ricorsivamente i suoi contenuti
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        fixPermissions(f); // Ricorsione sui file e cartelle all'interno
                    }
                }
            }
        }
    }

    /**
     * Downloads a file from a given URL to a specified location.
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
            throw new IOException("Failed to download file. Response code: " + connection.getResponseCode());
        }
    }

    /**
     * Unzips a ZIP file to the specified directory.
     */
    public void unzipFile(File zipFile, File destinationDir) throws IOException {
        boolean extractionSuccessful = false;

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                // Verifica se l'entry è vuota
                if (entry.getSize() == 0) {
                    System.err.println("Empty file entry detected: " + entry.getName());
                    continue; // Salta l'entry vuota
                }

                // Salta l'entry che è il file zip stesso
                if (entry.getName().equals(zipFile.getName())) {
                    System.err.println("Skipping the zip file itself: " + entry.getName());
                    continue; // Non estrarre il file zip
                }

                String fileName = Paths.get(entry.getName()).getFileName().toString();
                File extractedFile = new File(destinationDir, fileName);

                // Se l'entry è una directory, la creo
                if (entry.isDirectory()) {
                    if (!extractedFile.exists()) {
                        extractedFile.mkdirs();
                    }
                } else {
                    // Crea la directory del file, se non esiste
                    File parentDir = extractedFile.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    // Estrai il file
                    try (FileOutputStream fos = new FileOutputStream(extractedFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipIn.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }
                }

                zipIn.closeEntry();
            }

            // Solo dopo che tutte le estrazioni sono state completate correttamente, setto
            // extractionSuccessful a true
            extractionSuccessful = true;

        } catch (IOException e) {
            e.printStackTrace(); // Gestisce gli errori di IO
        } finally {
            // Debugging: Verifica se il file esiste prima di tentare la cancellazione
            System.out.println("Controllo del file ZIP prima della cancellazione: " + zipFile.getAbsolutePath());

            if (extractionSuccessful) {
                if (zipFile.exists()) {
                    // Forza i permessi di scrittura sul file ZIP prima di tentare la cancellazione
                    fixPermissions(zipFile);
                    
                    zipFile.delete();
                    
                    if (zipFile.exists()) {
                        System.out.println("File ZIP eliminato con successo.");
                    } else {
                        int i=0;
                        while (zipFile.exists()) {
                            zipFile.delete();
                            i++;
                            System.out.println("Cancello sta merda tentativo N"+ i);
                        }
                        System.err.println("Impossibile eliminare il file ZIP. Verifica i permessi.");
                    }
                } else {
                    System.err.println("File ZIP non trovato al percorso: " + zipFile.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Reads the bot.json file and creates an Automation object.
     */
    private Automation fetchBotDetails(String botJsonPath) throws IOException {
        File botJsonFile = new File(botJsonPath);
        if (!botJsonFile.exists()) {
            throw new IOException("bot.json not found in extracted bot directory.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try (BufferedReader reader = new BufferedReader(new FileReader(botJsonPath))) {
            return objectMapper.readValue(reader, Automation.class);
        }
    }

    /**
     * Executes a downloaded bot and streams its output in real-time.
     *
     * @param automation   The automation object representing the bot to execute.
     * @param clientOutput The output stream to which the bot's output will be
     *                     written.
     * @throws IOException If there is an error during execution or streaming the
     *                     output.
     */
    public void executeBot(Automation automation, OutputStream clientOutput) throws IOException {
        String command = automation.getStartCommand();
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("Invalid start command for bot: " + automation.getBotName());
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                PrintWriter writer = new PrintWriter(clientOutput)) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
                writer.flush();
            }
        }
    }

    /**
     * Fetches a list of all available bots grouped by language.
     * 
     * This method retrieves a JSON file containing information about bots,
     * grouped by their respective programming languages.
     * It then parses the JSON into a map where the key is the language and the
     * value is a list of Automation objects corresponding to each bot available in
     * that language.
     *
     * @return A map where the keys are languages (e.g., "python", "java",
     *         "javascript") and the values are lists of
     *         Automation objects representing the bots available for that language.
     * @throws IOException If there is an error fetching or parsing the list of bots
     *                     from the external URL.
     */
    public Map<String, List<Automation>> fetchAvailableBots() throws IOException {
        String botsJsonUrl = BOT_ENDPOINTS.BASE_URL + "/bots.json";
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream inputStream = new URL(botsJsonUrl).openStream()) {
            Map<String, List<Map<String, String>>> rawData = mapper.readValue(inputStream,
                    new TypeReference<Map<String, List<Map<String, String>>>>() {
                    });

            return rawData.entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().stream().map(bot -> {
                        Automation automation = new Automation();
                        automation.setBotName(bot.get("botName"));
                        automation.setLanguage(entry.getKey());
                        automation.setDescription("No description available");
                        return automation;
                    }).collect(Collectors.toList())));
        }
    }
}