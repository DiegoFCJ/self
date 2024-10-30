package com.self.self_project.constants;

public class LOGS {

    public static final String DB = "DB";

    public static final String MAIN_APP = "MainApp";
    public static final String BOT_FACTORY = "BotFactory";
    public static final String BUTTON_FACTORY = "ButtonsFactory";
    public static final String HEADER_FACTORY = "HeaderFactory";
    public static final String MENU_FACTORY = "MenuFactory";
    public static final String SCENE_FACTORY = "SceneFactory";
    public static final String BOT_STRATEGY = "BotStrategy";
    public static final String LOG_DIRECTORY_CREATED = "Log directory already existed.";

    public static final String LOG_FORMAT = "| %s | %s | %-10d | %-5s | %-30s | %-3d | %-30s | %s |";
    public static final String DEBUG_LEVEL = "DEBUG";
    public static final String INFO_LEVEL = "INFO";
    public static final String WARN_LEVEL = "WARN";
    public static final String ERROR_LEVEL = "ERROR";
    public static final String TEST_LEVEL = "TEST";
    public static final String PATH_UTILS = "Utils per il Percorso";
    public static final String INFO_LOG_DIR_CREATED = "Directory di log creata: ";
    public static final String INFO_FILE_CREATED = "File creato: ";
    public static final String ERROR_LOG_DIR_CREATION_FAILED = "Errore nella creazione della directory di log: ";
    public static final String ERROR_FILE_CREATION_FAILED = "Errore nella creazione del file: ";


    public static String infoLogDirCreated(String path) {
        return INFO_LOG_DIR_CREATED + path;
    }

    public static String infoFileCreated(String path) {
        return INFO_FILE_CREATED + path;
    }

    public static String errorLogDirCreationFailed(String path) {
        return ERROR_LOG_DIR_CREATION_FAILED + path;
    }

    public static String errorFileCreationFailed(String fileName) {
        return ERROR_FILE_CREATION_FAILED + fileName;
    }
}
