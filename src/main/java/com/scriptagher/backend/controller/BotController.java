package com.scriptagher.backend.controller;

import com.scriptagher.backend.model.Automation;
import com.scriptagher.backend.service.BotService;
import com.scriptagher.backend.service.ExecutionService;
import com.scriptagher.shared.constants.APIS;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
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
@RequestMapping(APIS.REQ_MAP)
public class BotController {

    @Autowired
    private BotService botService;

    @Autowired
    private ExecutionService executionService;

    /**
     * Endpoint to download a specific bot by its language and name.
     *
     * @param language The language of the bot (e.g., python, java).
     * @param botName  The name of the bot to be downloaded.
     * @return Automation object representing the bot.
     * @throws IOException If the bot file cannot be found or downloaded.
     */
    @GetMapping(APIS.BOT_DWNLD)
    public Automation downloadBot(@PathVariable String language, @PathVariable String botName) throws IOException {
        CustomLogger.info(LOGS.BOT_CONTROLLER, "Downloading bot: " + language + "/" + botName);
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
    @GetMapping(APIS.BOT_EXEC_STREAM)
    public void executeBot(@PathVariable String language, @PathVariable String botName, HttpServletResponse response)
            throws IOException {
        CustomLogger.info(LOGS.BOT_CONTROLLER, "Executing bot: " + language + "/" + botName);
        Automation automation = botService.downloadBot(language, botName);
        botService.executeBot(automation, response.getOutputStream());
    }

    /**
     * Endpoint to upload a bot.
     *
     * @param file The bot file to be uploaded.
     * @return A success message with the path of the uploaded bot.
     * @throws IOException If there is an error saving the file.
     */
    @PostMapping(APIS.BOT_UPL)
    public String uploadBot(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadDir = APIS.BOT_UPLOAD_DIR;
        File uploadFile = new File(uploadDir + file.getOriginalFilename());

        try (OutputStream outputStream = new FileOutputStream(uploadFile)) {
            outputStream.write(file.getBytes());
        }

        String message = String.format(LOGS.BOT_UPLOADED, uploadFile.getAbsolutePath());
        CustomLogger.info(LOGS.BOT_CONTROLLER, message);
        return message;
    }

    /**
     * Endpoint to fetch the list of available bots.
     *
     * @return A ResponseEntity containing a list of Automation objects.
     * @throws IOException If there is an error fetching the bots.
     */
    @GetMapping(APIS.BOT_LIST)
    public ResponseEntity<List<Automation>> fetchAvailableBots() throws IOException {
        try {
            CustomLogger.info(LOGS.BOT_CONTROLLER, "Fetching available bots.");
            Map<String, List<Automation>> botsByLanguage = botService.fetchAvailableBots();
            List<Automation> allBots = new ArrayList<>();

            for (List<Automation> botList : botsByLanguage.values()) {
                allBots.addAll(botList);
            }

            return ResponseEntity.ok(allBots);
        } catch (Exception e) {
            String errorMessage = String.format(LOGS.ERROR_FETCHING_BOTS, e.getMessage());
            CustomLogger.error(LOGS.BOT_CONTROLLER, errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint to trigger the execution of a bot.
     *
     * @param botName The name of the bot to execute.
     * @return A response indicating if the execution was successfully started.
     */
    @PostMapping()
    public ResponseEntity<?> executeBot(@PathVariable String botName) {
        try {
            CustomLogger.info(LOGS.BOT_CONTROLLER, "Starting execution of bot: " + botName);
            executionService.executeBot(botName);
            return ResponseEntity.ok("Bot execution started");
        } catch (Exception e) {
            String errorMessage = String.format(LOGS.ERROR_BOT_EXECUTION, e.getMessage());
            CustomLogger.error(LOGS.BOT_CONTROLLER, errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error executing bot");
        }
    }
}
