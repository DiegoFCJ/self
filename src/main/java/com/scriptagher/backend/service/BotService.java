package com.scriptagher.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptagher.backend.model.Automation;
import com.scriptagher.shared.constants.BOT_ENDPOINTS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * Service class responsible for downloading, executing, and fetching
 * information about bots.
 */
@Service
public class BotService {

    private static final String BOT_DIRECTORY = "bots/";

    /**
     * Downloads a bot from GitHub Pages.
     * 
     * @param botName The name of the bot to be downloaded.
     * @return Automation object representing the bot.
     * @throws IOException If there is an issue downloading the bot.
     */
    public Automation downloadBot(String botName) throws IOException {
        // Construct the URL to download the bot
        String botUrl = BOT_ENDPOINTS.BASE_URL + botName;
        File botFile = new File(BOT_DIRECTORY + botName);

        // Create the directory if it doesn't exist
        File directory = new File(BOT_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Start the download process
        URL url = new URL(botUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 200) {
            try (InputStream inputStream = connection.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(botFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } else {
            throw new IOException("Unable to download the bot. Response code: " + connection.getResponseCode());
        }

        return new Automation(botName, botFile.getAbsolutePath());
    }

    /**
     * Executes a downloaded bot and streams its output in real-time.
     * 
     * @param automation   The automation object that represents the bot to be
     *                     executed.
     * @param clientOutput The output stream to which the bot's output will be
     *                     written.
     * @throws IOException If there is an error during execution or streaming the
     *                     output.
     */
    public void executeBot(Automation automation, OutputStream clientOutput) throws IOException {
        // Create a process to run the bot (assuming it's a Python script)
        ProcessBuilder processBuilder = new ProcessBuilder("python", automation.getFilePath());
        processBuilder.redirectErrorStream(true); // Combine stdout and stderr

        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                PrintWriter writer = new PrintWriter(clientOutput)) {

            String line;
            // Stream the output to the client in real-time
            while ((line = reader.readLine()) != null) {
                writer.println(line);
                writer.flush();
            }
        }
    }

    /**
     * Fetches a list of available bots from the server.
     * 
     * @return A list of bot names.
     * @throws IOException If there is an error fetching the list of bots.
     */
    public List<String> fetchAvailableBots() throws IOException {
        // Fetch the bot list from the index.json file on the server
        String url = BOT_ENDPOINTS.BASE_URL + "index.json";

        try (InputStream inputStream = new URL(url).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            // Use Jackson (or another JSON library) to parse the content
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<String>> data = mapper.readValue(json.toString(), new TypeReference<>() {
            });
            return data.get("bots");
        }
    }
}