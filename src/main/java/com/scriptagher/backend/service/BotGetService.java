package com.scriptagher.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptagher.backend.model.Automation;
import com.scriptagher.shared.constants.APIS;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class responsible for downloading, executing, and fetching
 * information about bots.
 */
@Service
public class BotGetService {
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