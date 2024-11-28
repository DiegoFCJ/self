package com.scriptagher.frontend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.scriptagher.frontend.dto.Bot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

public class BotMenuService {

    private static final String API_URL = "http://localhost:8080/api/bots/list";  // URL dell'API

    public List<Bot> fetchBots() {
        // Inizializza il RestTemplate per inviare richieste HTTP
        RestTemplate restTemplate = new RestTemplate();

        // Fai una richiesta GET all'API
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(API_URL, JsonNode.class);

        // Se la risposta è OK, mappa i dati JSON in oggetti Java
        List<Bot> bots = new ArrayList<>();
        if (response.getStatusCode().is2xxSuccessful()) {
            JsonNode botsJson = response.getBody();
            if (botsJson != null) {
                botsJson.forEach(botNode -> {
                    Bot bot = new Bot();
                    bot.setBotName(botNode.path("botName").asText());
                    bot.setLanguage(botNode.path("language").asText());
                    bots.add(bot);
                });
            }
        }
        return bots;
    }
}
