package com.scriptagher.frontend.service;

import com.scriptagher.shared.constants.APIS;
import javafx.concurrent.Task;
import javafx.application.Platform;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

public class BotExecutionService {

    private static final String API_URL = APIS.BOT_API_BASE_URL + APIS.BOT_EXEC_STREAM;

    public Task<Void> executeBot(String language, String botName) {
        System.out.println(language);
        System.out.println(botName);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                HttpClient client = HttpClient.newHttpClient();
                URI uri = URI.create(API_URL.replace("{language}", language).replace("{botName}", botName));
                HttpRequest request = HttpRequest.newBuilder(uri).build();

                // Fai la richiesta HTTP e ottieni l'InputStream direttamente
                HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

                try (InputStream inputStream = response.body();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Crea una variabile finale all'interno del ciclo while
                        final String message = line;  // La variabile è finale per il lambda

                        // Usa Platform.runLater per aggiornare la UI nel thread principale
                        Platform.runLater(() -> updateMessage(message));  // Passa 'message' al lambda
                    }
                } catch (Exception e) {
                    Platform.runLater(() -> updateMessage("Error: " + e.getMessage()));  // Gestisci errori nella UI
                }
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            // Gestisci il successo
        });

        task.setOnFailed(event -> {
            // Gestisci l'errore
        });

        return task;
    }
}
