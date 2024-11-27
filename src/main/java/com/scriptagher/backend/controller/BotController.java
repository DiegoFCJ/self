package com.scriptagher.backend.controller;

import com.scriptagher.backend.model.Automation;
import com.scriptagher.backend.service.BotService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/bots")
public class BotController {

    @Autowired
    private BotService botService;

    // Endpoint per scaricare e salvare un bot
    @GetMapping("/{botName}/download")
    public Automation downloadBot(@PathVariable String botName) throws IOException {
        return botService.downloadBot(botName);
    }

    // Endpoint per eseguire un bot e inviare l'output in tempo reale
    @GetMapping("/{botName}/execute")
    public void executeBot(@PathVariable String botName, HttpServletResponse response) throws IOException {
        Automation automation = botService.downloadBot(botName); // Scarica prima il bot
        botService.executeBot(automation, response.getOutputStream());
    }

    @PostMapping("/upload")
    public String uploadBot(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadDir = "bots/";
        File uploadFile = new File(uploadDir + file.getOriginalFilename());

        // Salva il file localmente
        try (OutputStream outputStream = new FileOutputStream(uploadFile)) {
            outputStream.write(file.getBytes());
        }

        return "Bot caricato con successo: " + uploadFile.getAbsolutePath();
    }

    @GetMapping("/list")
    public List<String> fetchAvailableBots() throws IOException {
        return botService.fetchAvailableBots();
    }

}
