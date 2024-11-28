package com.scriptagher.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.scriptagher.shared.logger.CustomLogger;

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
        CustomLogger.info("main", "Launching Spring Boot application");
        SpringApplication.run(SpringApp.class, args);
        CustomLogger.info("main", "Spring Boot application started successfully");
    }
}