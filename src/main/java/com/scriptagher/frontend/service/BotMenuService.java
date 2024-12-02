package com.scriptagher.frontend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.scriptagher.frontend.dto.Bot;
import com.scriptagher.shared.constants.APIS;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for fetching the list of bots from an external API.
 */
public class BotMenuService {

    /**
     * Fetches the list of bots from the backend API.
     * 
     * @return a list of {@link Bot} objects representing the available bots.
     */
    public List<Bot> fetchBots() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            CustomLogger.info(LOGS.BOT_MENU_SERVICE, LOGS.SENDING_API_REQUEST + APIS.API_BOT_LIST);

            // Send GET request to the API
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(APIS.API_BOT_LIST, JsonNode.class);

            List<Bot> bots = new ArrayList<>();

            if (response.getStatusCode().is2xxSuccessful()) {
                CustomLogger.info(LOGS.BOT_MENU_SERVICE, LOGS.API_SUCCESS);
                JsonNode botsJson = response.getBody();

                if (botsJson != null) {
                    botsJson.forEach(botNode -> {
                        Bot bot = new Bot();
                        bot.setBotName(botNode.path("botName").asText());
                        bot.setLanguage(botNode.path("language").asText());
                        bots.add(bot);
                    });
                    CustomLogger.debug(LOGS.BOT_MENU_SERVICE, LOGS.BOTS_MAPPED + bots.size());
                } else {
                    CustomLogger.warn(LOGS.BOT_MENU_SERVICE, LOGS.API_EMPTY_RESPONSE);
                }
            } else {
                CustomLogger.error(LOGS.BOT_MENU_SERVICE, LOGS.API_FAILURE + response.getStatusCode());
            }

            return bots;

        } catch (Exception e) {
            CustomLogger.error(LOGS.BOT_MENU_SERVICE, LOGS.FETCH_BOTS_ERROR + e.getMessage());
            return new ArrayList<>();
        }
    }
}