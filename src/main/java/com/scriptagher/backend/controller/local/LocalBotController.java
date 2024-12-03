package com.scriptagher.backend.controller.local;

import com.scriptagher.backend.model.LocalBot;
import com.scriptagher.backend.service.local.LocalBotService;
import com.scriptagher.shared.constants.APIS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(APIS.REQ_MAP_LOCAL)
public class LocalBotController {

    @Autowired
    private LocalBotService localBotService;

    /**
     * Endpoint to fetch the list of local bots.
     *
     * @return A ResponseEntity containing a map of languages and their bots.
     */
    @GetMapping(APIS.LOCAL_BOT_LIST)
    public ResponseEntity<Map<String, List<LocalBot>>> getLocalBots() {
        try {
            Map<String, List<LocalBot>> localBots = localBotService.fetchLocalBots();
            return ResponseEntity.ok(localBots);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
