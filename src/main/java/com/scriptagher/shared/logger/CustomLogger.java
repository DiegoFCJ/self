package com.scriptagher.shared.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.scriptagher.shared.constants.LOGS;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for logging operations throughout the application.
 * This class provides methods to log messages at various levels, 
 * including debug, info, warn, and error.
 * It also facilitates the creation of a ChromeDriver service with logging capabilities.
 */
public class CustomLogger {
    private static final Logger logger = LoggerFactory.getLogger(CustomLogger.class);
    
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String todayDate = LocalDateTime.now().format(dateFormatter);

    /**
     * Logs a debug level message.
     *
     * @param operationType The type of operation being logged.
     * @param description   A description of the log message.
     */
    public static void debug(String operationType, String description) {
        log(LOGS.DEBUG_LEVEL, operationType, description);
    }

    /**
     * Logs an info level message.
     *
     * @param operationType The type of operation being logged.
     * @param description   A description of the log message.
     */
    public static void info(String operationType, String description) {
        log(LOGS.INFO_LEVEL, operationType, description);
    }

    /**
     * Logs a warning level message.
     *
     * @param operationType The type of operation being logged.
     * @param description   A description of the log message.
     */
    public static void warn(String operationType, String description) {
        log(LOGS.WARN_LEVEL, operationType, description);
    }

    /**
     * Logs an error level message.
     *
     * @param operationType The type of operation being logged.
     * @param description   A description of the log message.
     */
    public static void error(String operationType, String description) {
        log(LOGS.ERROR_LEVEL, operationType, description);
    }

    private static String componentCheck(String operationType) {
        String component;
        // Use keywords in the operationType to determine the log file category
        if (operationType.contains("backend")) {
            component = "BACK";
        } else if (operationType.contains("front")) {
            component = "FRONT";
        } else if (operationType.contains("data")) {
            component = "DATA";
        } else {
            component = "GENERAL";
        }
        return component;
    }

    /**
     * Logs a message with a specified level, operation type, and description.
     *
     * @param level        The level of the log (DEBUG, INFO, WARN, ERROR).
     * @param operationType The type of operation being logged.
     * @param description   A description of the log message.
     */
    private static void log(String level, String operationType, String description) {
        String componentTocheck;
        componentTocheck = componentCheck(operationType);
        String logDirectory = String.format("LOGS/%s/BASE-APP/%s", todayDate, componentTocheck);
        String component = determineComponent();

        Logger componentLogger = LoggerFactory.getLogger("com.scriptagher." + component.toLowerCase());
        File logFile = createLogFile(logDirectory);

        // Ensure the directory exists
        if (!logFile.getParentFile().exists() && !logFile.getParentFile().mkdirs()) {
            logger.error("Failed to create directory: " + logFile.getParentFile().getAbsolutePath());
            return;
        }
        String formattedMessage = formatLogMessage(level, operationType, description);
        logMessage(level, formattedMessage, componentLogger);
    }

    /**
     * Finds the first caller StackTraceElement that is not part of the logger utility
     * or the Java standard libraries.
     *
     * @return The StackTraceElement representing the caller.
     */
    private static StackTraceElement findCallerStackTraceElement() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        
        for (int i = 3; i < stackTrace.length; i++) {
            StackTraceElement element = stackTrace[i];
            String className = element.getClassName();
            
            // Check if this class name is NOT the logger utility class or Java standard classes
            if (!className.contains("shared.logger.CustomLogger") && !className.startsWith("java.")) {
                return element; // Return the first non-logger and non-standard class
            }
        }
        
        throw new IllegalStateException("No suitable caller found in the stack trace.");
    }

    /**
     * Determines the log component based on the caller's class name.
     *
     * @return The log component (BACK, FRONT, DRIVER, DB, GENERAL).
     */
    private static String determineComponent() {
        // Get the calling class and line number by examining the stack trace
        StackTraceElement caller = findCallerStackTraceElement();

        // Check if the class name matches any of the expected components
        String classUpperFolder = getClassNameAfter(caller.getClassName(), "scriptagher");
        String component;
        component = componentCheck(classUpperFolder);
        
        return component;
    }

    /**
     * Creates a log file with the current date and time in the specified log directory.
     *
     * @param logDirectory The directory where the log file will be created.
     * @return A File object representing the log file.
     */
    private static File createLogFile(String logDirectory) {
        return new File(logDirectory + "/scriptagher_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".log");
    }

    /**
     * Formats a log message with the current timestamp, thread ID, log level,
     * class name, line number, operation type, and description.
     *
     * @param level         The level of the log (DEBUG, INFO, WARN, ERROR).
     * @param operationType The type of operation being logged.
     * @param description   A description of the log message.
     * @return A formatted log message string.
     */
    private static String formatLogMessage(String level, String operationType, String description) {
        LocalDateTime now = LocalDateTime.now();
        long threadId = Thread.currentThread().getId(); // Using thread ID

        // Get the calling class and line number by examining the stack trace
        StackTraceElement caller = findCallerStackTraceElement();

        return String.format(
            LOGS.LOG_FORMAT,
            now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),     // Date
            now.format(DateTimeFormatter.ofPattern("HH:mm:ss,SSS")),   // Time with milliseconds
            threadId,                                                  // Thread ID
            level,                                                     // Log level
            getClassNameAfter(caller.getClassName(), "scriptagher"), // Class name
            caller.getLineNumber(),                                   // Line number
            operationType,                                            // Operation type
            description                                               // Description
        );
    }

    /**
     * Logs the message at the specified level using the appropriate logger.
     *
     * @param level           The level of the log (DEBUG, INFO, WARN, ERROR).
     * @param formattedMessage The formatted log message to be logged.
     * @param componentLogger  The logger instance for the specific component.
     */
    private static void logMessage(String level, String formattedMessage, Logger componentLogger) {
        switch (level) {
            case LOGS.DEBUG_LEVEL:
                componentLogger.debug(formattedMessage);
                break;
            case LOGS.INFO_LEVEL:
                componentLogger.info(formattedMessage);
                break;
            case LOGS.WARN_LEVEL:
                componentLogger.warn(formattedMessage);
                break;
            case LOGS.ERROR_LEVEL:
                componentLogger.error(formattedMessage);
                break;
            default:
                componentLogger.info(formattedMessage); // Default to INFO level
                break;
        }
    }

    /**
     * Extracts the simple class name from the full class name based on a specified package part.
     *
     * @param fullClassName The full name of the class.
     * @param packagePart   The package part to be stripped from the full class name.
     * @return The simple class name.
     */
    private static String getClassNameAfter(String fullClassName, String packagePart) {
        int index = fullClassName.indexOf(packagePart);
        if (index >= 0) {
            return fullClassName.substring(index + packagePart.length() + 1);
        }
        return fullClassName;
    }

    /**
     * Determines the log directory based on the specified component.
     * 
     * @param component The component for which the log directory needs to be determined.
     * @return The log directory path.
     */
    public static void cleanEmptyLogFiles() {
        String logDirectoryPath = "LOGS/" + todayDate; // Path to the log directory
    
        File logDirectory = new File(logDirectoryPath);
        if (logDirectory.exists() && logDirectory.isDirectory()) {
            // List all subdirectories in the log directory (e.g., BASE-APP)
            File[] componentDirs = logDirectory.listFiles(File::isDirectory);
    
            if (componentDirs != null) {
                for (File componentDir : componentDirs) {
                    
                    // List all subdirectories in the component directory (e.g., BACK, FRONT, DB, DRIVER, GENERAL)
                    File[] subDirs = componentDir.listFiles(File::isDirectory);
                    if (subDirs != null) {
                        for (File subDir : subDirs) {
                            
                            // List all log files in the subdirectory
                            File[] logFiles = subDir.listFiles(File::isFile);
                            
                            if (logFiles != null) {
                                boolean foundEmptyFile = false; // Flag to check if any empty file is found
    
                                for (File logFile : logFiles) {
                                    
                                    // Check if the file is empty
                                    if (logFile.length() == 0) {
                                        foundEmptyFile = true; // Set flag to true
                                        // Try to delete the empty log file
                                        if (logFile.delete()) {
                                            logger.info("Deleted empty log file: " + logFile.getAbsolutePath());
                                        } else {
                                            logger.warn("Failed to delete empty log file: " + logFile.getAbsolutePath());
                                        }
                                    } else {
                                    }
                                }
    
                                // Check if any empty files were found in this subdirectory
                                if (!foundEmptyFile) {
                                    logger.info("No empty log files found in subdirectory: " + subDir.getAbsolutePath());
                                }
                            } else {
                                logger.warn("No log files found in subdirectory: " + subDir.getAbsolutePath());
                            }
                        }
                    } else {
                        logger.warn("No subdirectories found in component directory: " + componentDir.getAbsolutePath());
                    }
                }
            } else {
                logger.warn("No component directories found in: " + logDirectoryPath);
            }
        } else {
            logger.warn("Log directory does not exist: " + logDirectoryPath);
        }
    }
    
}