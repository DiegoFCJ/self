package com.scriptagher.backend.controller;

import com.scriptagher.backend.model.Automation;
import com.scriptagher.backend.service.BotService;
import com.scriptagher.backend.service.ExecutionService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller that handles requests related to bot management.
 * Provides endpoints for downloading, uploading, and executing bots.
 */
@RestController
@RequestMapping("/api/bots")
public class BotController {

    @Autowired
    private BotService botService;

    @Autowired
    private ExecutionService executionService;

    /**
     * Endpoint to download a specific bot by its language and name.
     * 
     * @param language The language of the bot (e.g., python, java).
     * @param botName The name of the bot to be downloaded.
     * @return Automation object representing the bot.
     * @throws IOException If the bot file cannot be found or downloaded.
     */
    @GetMapping("/{language}/{botName}/download")
    public Automation downloadBot(@PathVariable String language, @PathVariable String botName) throws IOException {
        return botService.downloadBot(language, botName);
    }

    /**
     * Endpoint to execute a specific bot and stream the output in real-time.
     * 
     * @param language The language of the bot (e.g., python, java).
     * @param botName  The name of the bot to be executed.
     * @param response The HttpServletResponse to stream the output.
     * @throws IOException If there is an error executing the bot or streaming the output.
     */
    @GetMapping("/{language}/{botName}/execute")
    public void executeBot(@PathVariable String language, @PathVariable String botName, HttpServletResponse response)
            throws IOException {
        Automation automation = botService.downloadBot(language, botName);
        botService.executeBot(automation, response.getOutputStream());
    }

    /**
     * Endpoint to upload a bot (this part could be specific to your local
     * deployment).
     * 
     * @param file The bot file to be uploaded.
     * @return A success message with the path of the uploaded bot.
     * @throws IOException If there is an error saving the file.
     */
    @PostMapping("/upload")
    public String uploadBot(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadDir = "bots/";
        File uploadFile = new File(uploadDir + file.getOriginalFilename());

        try (OutputStream outputStream = new FileOutputStream(uploadFile)) {
            outputStream.write(file.getBytes());
        }

        return "Bot uploaded successfully: " + uploadFile.getAbsolutePath();
    }

    /**
     * Endpoint to fetch the list of available bots.
     * 
     * This endpoint retrieves all available bots, grouped by language, from the service layer.
     * The map of bots (grouped by language) is flattened into a single list of Automation objects
     * before being returned in the response.
     *
     * @return A ResponseEntity containing a list of Automation objects, each representing a bot with its details.
     * @throws IOException If there is an error fetching or processing the list of bots from the service layer.
     */
    @GetMapping("/list")
    public ResponseEntity<List<Automation>> fetchAvailableBots() throws IOException {
        Map<String, List<Automation>> botsByLanguage = botService.fetchAvailableBots();
        List<Automation> allBots = new ArrayList<>();

        for (List<Automation> botList : botsByLanguage.values()) {
            allBots.addAll(botList);
        }
        return ResponseEntity.ok(allBots);
    }

    /**
     * Endpoint to trigger the execution of a bot.
     * 
     * @param botName The name of the bot to execute.
     * @return A response indicating if the execution was successfully started.
     */
    @PostMapping("/execute/{botName}")
    public ResponseEntity<?> executeBot(@PathVariable String botName) {
        try {
            executionService.executeBot(botName);
            return ResponseEntity.ok("Bot execution started");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error executing bot");
        }
    }
}