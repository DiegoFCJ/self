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

@Service
public class BotService {

    private static final String BOT_DIRECTORY = "bots/";

    // Scarica il bot da GitHub Pages
    public Automation downloadBot(String botName) throws IOException {
        String botUrl = BOT_ENDPOINTS.BASE_URL + botName;
        File botFile = new File(BOT_DIRECTORY + botName);

        // Crea la directory se non esiste
        File directory = new File(BOT_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Download del file
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
            throw new IOException("Impossibile scaricare il bot. Codice risposta: " + connection.getResponseCode());
        }

        return new Automation(botName, botFile.getAbsolutePath());
    }

    // Esegue il bot e restituisce l'output in tempo reale
    public void executeBot(Automation automation, OutputStream clientOutput) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python", automation.getFilePath());
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

    public List<String> fetchAvailableBots() throws IOException {
    String url = BOT_ENDPOINTS.BASE_URL + "index.json";

    try (InputStream inputStream = new URL(url).openStream();
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        // Usa una libreria JSON (come Jackson o Gson) per parsare il contenuto
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> data = mapper.readValue(json.toString(), new TypeReference<>() {});
        return data.get("bots");
    }
}

}