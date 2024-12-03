package com.scriptagher.shared.constants;

public class APIS {

    // ============================================================================
    // GITHUB USER AND PROJECT
    // ============================================================================
    public static final String GH_USR = "diegofcj"; // GitHub username
    public static final String PROJECT = "scriptagher"; // GitHub project name

    // ============================================================================
    // BASE URLs
    // ============================================================================
    public static final String BASE_URL = "https://raw.githubusercontent.com/" + GH_USR + "/" + PROJECT
            + "/bot-list/bots";
    public static final String BASE_URL_GH_PAGES = "https://" + GH_USR + ".github.io/" + PROJECT + "/";

    // ============================================================================
    // FILE AND FOLDER NAMES
    // ============================================================================
    public static final String BOT_DIR_DATA = "data/remote"; // Directory to store data
    public static final String BOT_DIR_UPLOAD = "bots/"; // Directory for bot uploads
    public static final String BOT_FILE_CONFIG = "Bot.json"; // Bot configuration file name
    public static final String ZIP_EXTENSION = ".zip"; // File extension for zip files

    // ============================================================================
    // API ENDPOINTS (Bot API)
    // ============================================================================
    public static final String BOT_API_BASE_URL = "http://localhost:8080/api/bots"; // Base URL for the bot API
    public static final String REQ_MAP = "/api/bots"; // API request map

    // API Endpoints for Bots
    public static final String LANG = "/{language}"; // Language parameter
    public static final String B_NAME = "/{botName}"; // Bot name parameter
    public static final String EXEC = "/execute"; // Execute bot endpoint
    public static final String DWNLD = "/download"; // Download bot endpoint

    // Combined API Endpoints
    public static final String BOT_DWNLD = LANG + B_NAME + DWNLD; // Bot download endpoint
    public static final String BOT_EXEC_STREAM = LANG + B_NAME + EXEC; // Bot execution stream
    public static final String BOT_UPL = "/upload"; // Bot upload endpoint
    public static final String BOT_LIST = "/list"; // Bot list endpoint
    public static final String BOT_EXEC = EXEC + B_NAME; // Bot execution by name

    // ============================================================================
    // FINAL API ENDPOINTS
    // ============================================================================
    public static final String API_BOT_LIST = APIS.BOT_API_BASE_URL + BOT_LIST; // Full bot list API URL
}