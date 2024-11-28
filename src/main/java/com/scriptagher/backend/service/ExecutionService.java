package com.scriptagher.backend.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.stereotype.Service;

/**
 * Service for executing automation bots and streaming their output in real-time.
 */
@Service
public class ExecutionService {

    /**
     * Executes a bot and streams its output in real-time to the console.
     *
     * @param botName the name of the bot file to be executed. The bot can be a Java, Python, or JavaScript file.
     */
    public void executeBot(String botName) {
        try {
            // Configure the ProcessBuilder based on the bot type
            ProcessBuilder processBuilder = new ProcessBuilder();

            // Determine the appropriate command to execute based on the file extension
            if (botName.endsWith(".java")) {
                // Compile the Java file
                Process compileProcess = new ProcessBuilder("javac", "bots/" + botName).start();
                compileProcess.waitFor(); // Wait for the compilation to complete
                
                // Run the compiled Java class
                String className = botName.substring(0, botName.lastIndexOf('.'));
                processBuilder.command("java", "-cp", "bots", className);
            } else if (botName.endsWith(".py")) {
                processBuilder.command("python3", "bots/" + botName);
            } else if (botName.endsWith(".js")) {
                processBuilder.command("node", "bots/" + botName);
            } else {
                throw new IllegalArgumentException("Unsupported file type for bot: " + botName);
            }

            // Start the process
            Process process = processBuilder.start();

            // Stream the output of the process in real-time
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line); // Output to the console (or use WebSocket for real-time client updates)
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Wait for the process to complete
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}