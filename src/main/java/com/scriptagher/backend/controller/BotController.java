package com.scriptagher.backend.controller;

import com.scriptagher.backend.model.Automation;
import com.scriptagher.backend.service.BotService;
import com.scriptagher.backend.service.ExecutionService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
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
     * Endpoint to download a specific bot.
     * 
     * @param botName The name of the bot to be downloaded.
     * @return Automation object representing the bot.
     * @throws IOException If the bot file cannot be found or downloaded.
     */
    @GetMapping("/{botName}/download")
    public Automation downloadBot(@PathVariable String botName) throws IOException {
        return botService.downloadBot(botName);
    }

    /**
     * Endpoint to execute a specific bot and stream the output in real-time.
     * 
     * @param botName  The name of the bot to be executed.
     * @param response The HttpServletResponse to stream the output.
     * @throws IOException If there is an error executing the bot or streaming the
     *                     output.
     */
    @GetMapping("/{botName}/execute")
    public void executeBot(@PathVariable String botName, HttpServletResponse response) throws IOException {
        // First, download the bot
        Automation automation = botService.downloadBot(botName);
        // Execute the bot and stream the output
        botService.executeBot(automation, response.getOutputStream());
    }

    /**
     * Endpoint to upload a bot.
     * 
     * @param file The bot file to be uploaded.
     * @return A success message with the path of the uploaded bot.
     * @throws IOException If there is an error saving the file.
     */
    @PostMapping("/upload")
    public String uploadBot(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadDir = "bots/";
        File uploadFile = new File(uploadDir + file.getOriginalFilename());

        // Save the file locally
        try (OutputStream outputStream = new FileOutputStream(uploadFile)) {
            outputStream.write(file.getBytes());
        }

        return "Bot uploaded successfully: " + uploadFile.getAbsolutePath();
    }

    /**
     * Endpoint to fetch the list of available bots.
     * 
     * @return A list of bot names.
     * @throws IOException If there is an error fetching the list of bots.
     */
    @GetMapping("/list")
    public List<String> fetchAvailableBots() throws IOException {
        return botService.fetchAvailableBots();
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