package com.self.self_project;

import com.self.self_project.constants.LOGS;
import com.self.self_project.utils.logging.CustomLoggerUtils;

import javafx.application.Platform;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class MainAppTest {

    private static CountDownLatch latch;

    /**
     * Set up the JavaFX environment before running tests.
     */
    @BeforeAll
    public static void setUp() {
        latch = new CountDownLatch(1); // Initialize the latch

        // Launch the JavaFX application
        new Thread(() -> {
            MainApp.main(new String[]{}); // Start the JavaFX application
            latch.countDown(); // Release the latch once the app starts
        }).start();

        // Allow some time for the application to start
        try {
            latch.await(5, TimeUnit.SECONDS); // Wait for up to 5 seconds for the app to start
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Close the JavaFX application after all tests are completed.
     */
    @AfterAll
    public static void tearDown() {
        Platform.runLater(() -> {
            Platform.exit();
        });
    }

    /**
     * Test the main method of MainApp.
     * This test ensures that the application starts correctly and logs the appropriate messages.
     */
    @Test
    public void testMainMethod() {
        // Logging the start of the test
        CustomLoggerUtils.debug(LOGS.TEST_LEVEL, "Starting testMainMethod...");

        // Set up the behavior for the static methods before calling the main method
        try (MockedStatic<CustomLoggerUtils> mockedLogger = mockStatic(CustomLoggerUtils.class)) {
            // Setup mocked logging methods
            mockedLogger.when(() -> CustomLoggerUtils.info(LOGS.MAIN_APP, "Application starting..."))
                        .thenAnswer(invocation -> null);
            mockedLogger.when(() -> CustomLoggerUtils.info(LOGS.MAIN_APP, "Application launched successfully."))
                        .thenAnswer(invocation -> null);
            // Use a more general matcher for the error method
            mockedLogger.when(() -> CustomLoggerUtils.error(eq(LOGS.MAIN_APP), anyString()))
                        .thenAnswer(invocation -> null);

            // Allow some time for the logging to complete
            Thread.sleep(1000); // Adjust the delay as necessary

            // Verify that the appropriate log methods were called
            mockedLogger.verify(() -> CustomLoggerUtils.info(LOGS.MAIN_APP, "Application starting..."), times(1));
            mockedLogger.verify(() -> CustomLoggerUtils.info(LOGS.MAIN_APP, "Application launched successfully."), times(1));

            // Logging the successful completion of the test
            CustomLoggerUtils.debug(LOGS.TEST_LEVEL, "Completed testMainMethod successfully.");
        } catch (Exception e) {
            // Log any exceptions that occur during the test
            CustomLoggerUtils.error(LOGS.TEST_LEVEL, "Error during testMainMethod: " + e.getMessage());
        }
    }
}