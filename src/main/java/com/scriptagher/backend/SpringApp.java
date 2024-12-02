package com.scriptagher.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.scriptagher.shared.logger.CustomLogger;
import com.scriptagher.shared.constants.LOGS;

/**
 * Main class for starting the Spring Boot application.
 * This serves as the entry point for the backend context of the application.
 */
@SpringBootApplication
public class SpringApp {

    /**
     * Main method to launch the Spring Boot application.
     *
     * @param args Command-line arguments passed during the application's start.
     */
    public static void main(String[] args) {
        // Usando la costante LOGS.SPRING_APP per loggare l'avvio
        CustomLogger.info(LOGS.SPRING_APP, "Launching Spring Boot application");
        SpringApplication.run(SpringApp.class, args);
        CustomLogger.info(LOGS.SPRING_APP, "Spring Boot application started successfully");
    }
}