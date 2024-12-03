package com.scriptagher.backend.controller;

import com.scriptagher.backend.model.Automation;
import com.scriptagher.backend.service.BotGetService;
import com.scriptagher.backend.service.BotUploadService;
import com.scriptagher.backend.service.BotDownloadService;
import com.scriptagher.backend.service.BotExecutionService;
import com.scriptagher.shared.constants.APIS;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import java.io.IOException;
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
    private BotGetService botGetService;

    @Autowired
    private BotDownloadService botDownloadService;

    @Autowired
    private BotExecutionService botExecutionService;

    @Autowired
    private BotUploadService botUploadService;

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
        return botDownloadService.downloadBot(language, botName);
    }

    /**
     * Endpoint to execute a specific bot and stream the output in real-time.
     *
     * @param language The language of the bot (e.g., python, java).
     * @param botName  The name of the bot to be executed.
     * @param response The HttpServletResponse to stream the output.
     * @throws IOException If there is an error executing the bot or streaming the
     *                     output.
     */
    @GetMapping(APIS.BOT_EXEC_STREAM)
    public void executeBot(@PathVariable String language, @PathVariable String botName, HttpServletResponse response)
            throws IOException {
        CustomLogger.info(LOGS.BOT_CONTROLLER, "Executing bot: " + language + "/" + botName);
        botExecutionService.executeBot(language, botName, response.getOutputStream());
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
        CustomLogger.info(LOGS.BOT_CONTROLLER, "Uploading bot");
        return botUploadService.uploadBot(file);
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
            Map<String, List<Automation>> botsByLanguage = botGetService.fetchAvailableBots();
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
}
