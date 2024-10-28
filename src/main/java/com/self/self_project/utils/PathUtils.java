package com.self.self_project.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.self.self_project.constants.LOGS;
import com.self.self_project.utils.logging.CustomLoggerUtils;

/**
 * Utility class for managing file paths and directories.
 * Provides methods to handle user paths, project root paths, 
 * and ensure the existence of directories and files.
 */
public class PathUtils {

    /**
     * Retrieves the root path of the current project.
     * 
     * @return The absolute path of the project's root directory.
     */
    public static String getProjectRootPath() {
        String projectRootPath = Paths.get("").toAbsolutePath().toString();
        CustomLoggerUtils.info(LOGS.PATH_UTILS, "Project root path retrieved: " + projectRootPath);
        return projectRootPath;
    }

    /**
     * Retrieves the base path of the current logged-in user.
     *
     * @return The user's home directory path as a string (e.g. "E:/Users/username").
     */
    public static String getUserBasePath() {
        String userBasePath = System.getProperty("user.home");
        CustomLoggerUtils.info(LOGS.PATH_UTILS, "User base path retrieved: " + userBasePath);
        return userBasePath;
    }

    /**
     * Constructs a full path by joining the user's base path with a relative path.
     *
     * @param relativePath The relative path to append to the base path.
     * @return The full path as a string.
     */
    public static String getFullPath(String relativePath) {
        String fullPath = Paths.get(getUserBasePath(), relativePath).toString();
        CustomLoggerUtils.info(LOGS.PATH_UTILS, "Full path constructed: " + fullPath);
        return fullPath;
    }

    /**
     * Ensures that the specified log directory exists.
     * If the directory does not exist, attempts to create it.
     *
     * @param path The path to the log directory.
     */
    public static void ensureLogDirectoryExists(String path) {
        File logDir = new File(path);
        if (!logDir.exists()) {
            if (logDir.mkdirs()) {
                CustomLoggerUtils.info(LOGS.PATH_UTILS, LOGS.infoLogDirCreated(logDir.getAbsolutePath()));
            } else {
                CustomLoggerUtils.error(LOGS.PATH_UTILS, LOGS.errorLogDirCreationFailed(logDir.getAbsolutePath()));
            }
        } else {
            CustomLoggerUtils.info(LOGS.PATH_UTILS, "Log directory already exists: " + logDir.getAbsolutePath());
        }
    }

    /**
     * Creates a file if it does not already exist.
     *
     * @param directory The directory path where the file should be created.
     * @param fileName The name of the file to create.
     */
    public static void createFileIfNotExists(String directory, String fileName) {
        File file = new File(directory + "/" + fileName);
        try {
            if (!file.exists()) {
                if (file.getParentFile().mkdirs()) {
                    CustomLoggerUtils.info(LOGS.PATH_UTILS, "Directories created for file: " + file.getAbsolutePath());
                }
                if (file.createNewFile()) {
                    CustomLoggerUtils.info(LOGS.PATH_UTILS, LOGS.infoFileCreated(file.getAbsolutePath()));
                }
            } else {
                CustomLoggerUtils.info(LOGS.PATH_UTILS, "File already exists: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            CustomLoggerUtils.error(LOGS.PATH_UTILS, LOGS.errorFileCreationFailed(fileName));
        }
    }

    /**
     * Retrieves a File object for the specified directory and file name.
     *
     * @param directory The path to the directory.
     * @param fileName The name of the file.
     * @return A File object representing the specified file.
     */
    public static File getFile(String directory, String fileName) {
        File file = new File(directory + "/" + fileName);
        CustomLoggerUtils.info(LOGS.PATH_UTILS, "Retrieved file: " + file.getAbsolutePath());
        return file;
    }
}