package com.scriptagher.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptagher.backend.model.Automation;
import com.scriptagher.shared.constants.APIS;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        CustomLogger.info(LOGS.BOT_SERVICE, String.format(LOGS.DOWNLOAD_START, language, botName));

        // Construct the URL for the ZIP file
        String botZipUrl = String.format("%s/%s/%s/%s" + APIS.ZIP_EXTENSION, APIS.BASE_URL, language, botName, botName);
        File botDir = new File(APIS.DIR_DATA + "/" + language + "/" + botName);
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
        fixPermissions(botZip);

        // Unzip the ZIP file
        CustomLogger.info(LOGS.BOT_SERVICE, String.format(LOGS.EXTRACT_START, botZip.getAbsolutePath()));
        unzipFile(botZip, botDir);
        CustomLogger.info(LOGS.BOT_SERVICE, String.format(LOGS.EXTRACT_COMPLETE, botDir.getAbsolutePath()));

        // Read the bot.json file to get bot details
        String botJsonPath = botDir + "/" + APIS.BOT_CONFIG_FILE;
        Automation automation = fetchBotDetails(botJsonPath);

        // Set the language
        automation.setLanguage(language);

        // Delete the ZIP file after extraction
        botZip.delete();

        CustomLogger.info(LOGS.BOT_SERVICE, String.format(LOGS.DOWNLOAD_COMPLETE, automation.getBotName()));
        return automation;
    }

    /**
     * Ensures write permissions are set for the specified file or directory.
     * 
     * If the file is a directory, the method is applied recursively to its
     * contents.
     *
     * @param file The file or directory for which permissions need to be fixed.
     */
    private void fixPermissions(File file) {
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

    /**
     * Unzips a ZIP file to the specified directory.
     *
     * @param zipFile The ZIP file to extract.
     * @param destinationDir The directory to extract the contents into.
     * @throws IOException If there is an error during extraction.
     */
    public void unzipFile(File zipFile, File destinationDir) throws IOException {
        boolean extractionSuccessful = false;

        // Try-with-resources to handle the ZipInputStream
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                getExtractedFile(zipFile, destinationDir, zipIn, entry);
            }

            // Extraction is considered successful if no errors occurred
            extractionSuccessful = true;

        } catch (IOException e) {
            e.printStackTrace(); // Handle IO errors during extraction
        } finally {
            // Check if extraction was successful before attempting to delete the zip
            dleteZipIfUnzipped(zipFile, extractionSuccessful);
        }
    }

    /**
     * Deletes the ZIP file if it was successfully unzipped.
     *
     * @param zipFile The ZIP file to delete.
     * @param extractionSuccessful Whether the extraction was successful or not.
     */
    private void dleteZipIfUnzipped(File zipFile, boolean extractionSuccessful) {
        if (extractionSuccessful) {
            if (zipFile.exists()) {
                // Fix permissions before attempting to delete the ZIP file
                fixPermissions(zipFile);

                // Delete the ZIP file after extraction
                zipFile.delete();

                if (zipFile.exists()) {
                    //System.out.println("File ZIP eliminato con successo.");
                } else {
                    zipFile.delete();
                }
            } else {
                System.err.println("File ZIP non trovato al percorso: " + zipFile.getAbsolutePath());
            }
        }
    }

    /**
     * Extracts each file from the ZIP entry to the destination directory.
     *
     * @param zipFile The ZIP file being processed.
     * @param destinationDir The directory to extract files into.
     * @param zipIn The ZipInputStream reading the ZIP file.
     * @param entry The current entry (file or directory) in the ZIP archive.
     * @throws IOException If there is an error during extraction.
     */
    private void getExtractedFile(File zipFile, File destinationDir, ZipInputStream zipIn, ZipEntry entry)
            throws IOException, FileNotFoundException {
        // Skip empty entries
        if (entry.getSize() == 0) {
            System.err.println("Empty file entry detected: " + entry.getName());
            return;
        }

        // Skip the entry if it's the zip file itself
        if (entry.getName().equals(zipFile.getName())) {
            System.err.println("Skipping the zip file itself: " + entry.getName());
            return;
        }

        String fileName = Paths.get(entry.getName()).getFileName().toString();
        File extractedFile = new File(destinationDir, fileName);

        // If it's a directory, create it
        if (entry.isDirectory()) {
            if (!extractedFile.exists()) {
                extractedFile.mkdirs();
            }
        } else {
            // Create the parent directory for the file if it doesn't exist
            File parentDir = extractedFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Extract the file from the ZIP stream
            try (FileOutputStream fos = new FileOutputStream(extractedFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = zipIn.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }

        // Close the current entry
        zipIn.closeEntry();
    }

    /**
     * Reads the bot.json file and creates an Automation object representing the bot.
     *
     * @param botJsonPath The path to the bot.json file.
     * @return The Automation object representing the bot.
     * @throws IOException If there is an error reading the bot.json file.
     */
    private Automation fetchBotDetails(String botJsonPath) throws IOException {
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
     * Executes a downloaded bot and streams its output in real-time.
     *
     * @param automation   The automation object representing the bot to execute.
     * @param clientOutput The output stream to which the bot's output will be written.
     * @throws IOException If there is an error during execution or streaming the output.
     */
    public void executeBot(Automation automation, OutputStream clientOutput) throws IOException {
        String command = automation.getStartCommand();
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("Invalid start command for bot: " + automation.getBotName());
        }

        // Create a process builder to execute the command
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);

        // Start the process and stream its output
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                PrintWriter writer = new PrintWriter(clientOutput)) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
                writer.flush(); // Stream the output to the client in real-time
            }
        }
    }

    /**
     * Fetches a list of all available bots grouped by language.
     * 
     * This method retrieves a JSON file containing information about bots,
     * grouped by their respective programming languages. It parses the JSON
     * into a map where the key is the language and the value is a list of
     * Automation objects for each bot available in that language.
     *
     * @return A map where the keys are languages (e.g., "python", "java",
     *         "javascript") and the values are lists of Automation objects
     *         representing the bots available for that language.
     * @throws IOException If there is an error fetching or parsing the list of bots
     *                     from the external URL.
     */
    public Map<String, List<Automation>> fetchAvailableBots() throws IOException {
        String botsJsonUrl = APIS.BASE_URL + "/bots.json";
        ObjectMapper mapper = new ObjectMapper();

        // Fetch the JSON file containing bot information
        try (InputStream inputStream = new URL(botsJsonUrl).openStream()) {
            Map<String, List<Map<String, String>>> rawData = mapper.readValue(inputStream,
                    new TypeReference<Map<String, List<Map<String, String>>>>() {
                    });

            // Process the raw data into a map of Automation objects
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