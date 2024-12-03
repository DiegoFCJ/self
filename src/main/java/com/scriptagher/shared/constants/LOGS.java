package com.scriptagher.shared.constants;

public class LOGS {

    // ============================================================================
    // GENERAL LOGGING MESSAGES
    // ============================================================================

    // Log directory and file creation messages
    public static final String LOG_DIRECTORY_CREATED = "Log directory already existed.";
    public static final String INFO_LOG_DIR_CREATED = "Directory di log creata: ";
    public static final String INFO_FILE_CREATED = "File creato: ";
    public static final String ERROR_LOG_DIR_CREATION_FAILED = "Errore nella creazione della directory di log: ";
    public static final String ERROR_FILE_CREATION_FAILED = "Errore nella creazione del file: ";

    // Log format pattern
    public static final String LOG_FORMAT = "| %s | %s | %-10d | %-5s | %-30s | %-3d | %-30s | %s |";

    // Log levels
    public static final String DEBUG_LEVEL = "DEBUG";
    public static final String INFO_LEVEL = "INFO";
    public static final String WARN_LEVEL = "WARN";
    public static final String ERROR_LEVEL = "ERROR";
    public static final String TEST_LEVEL = "TEST";

    // ============================================================================
    // PATHS AND UTILITIES
    // ============================================================================
    public static final String PATH_UTILS = "Utils per il Percorso";

    // ============================================================================
    // APPLICATION TYPES
    // ============================================================================
    public static final String MAIN_APP = "MainApp";
    public static final String SPRING_APP = "SpringApp";

    // ============================================================================
    // CONTROLLERS
    // ============================================================================
    public static final String BOT_CONTROLLER = "BotController";
    public static final String HEADER_BAR_CONTROLLER = "HeaderBarController";
    public static final String MAIN_VIEW_CONTROLLER = "MainViewController";
    public static final String TAB_PANE_CONTROLLER = "TabPaneController";
    public static final String LEFT_PANE_CONTROLLER = "LeftPaneController";

    // ============================================================================
    // SERVICES
    // ============================================================================
    public static final String MAXIMIZE_SERVICE = "MaximizeService";
    public static final String TAB_PANE_SERVICE = "TabPaneService";
    public static final String BOT_SERVICE = "BotService";
    public static final String EXECUTION_SERVICE = "ExecutionService";
    public static final String BOT_MENU_SERVICE = "BotMenuService";
    public static final String BOT_DOWNLOAD_SERVICE = "BotDownloadService";

    // ============================================================================
    // OTHER COMPONENTS
    // ============================================================================
    public static final String ICON_UPDATE = "IconUpdate";
    public static final String STAGE_MANAGER = "StageManager";
    public static final String ICN_HOVER_EFFECT = "IconHoverEffect";
    public static final String IMAGE_PROCESSING = "ImageProcessing";

    // ============================================================================
    // FXML FILES
    // ============================================================================
    public static final String MAIN = "MainView";
    public static final String HEAD = "HeaderBar";
    public static final String TABPANE = "TabPane";
    public static final String LEFTPANE = "LeftPane";
    public static final String MAIN_FXML = "/fxml/" + MAIN + ".fxml";
    public static final String PATH_HEAD = "/fxml/" + HEAD + ".fxml";
    public static final String PATH_TABPANE = "/fxml/" + TABPANE + ".fxml";
    public static final String PATH_LEFTPANE = "/fxml/" + LEFTPANE + ".fxml";

    // ============================================================================
    // ERROR LOG MESSAGES
    // ============================================================================
    public static final String ERROR_BOT_EXECUTION = "Error executing bot: %s";
    public static final String ERROR_FETCHING_BOTS = "Error fetching bots: %s";
    public static final String ERROR_DOWNLOAD = "Failed to download file: %s";
    public static final String ERROR_PERMISSIONS = "Failed to set write permissions for: %s";
    public static final String ERROR_EXECUTING_BOT = "Error executing bot: %s";
    public static final String FXML_LOADING_ERROR = "Error loading FXML components: ";
    public static final String INITIALIZATION_ERROR = "Error during initialization of MainViewController: ";
    public static final String DASHBOARD_STRETCH_ERROR = "Error in dashboard stretch configuration.";
    public static final String FETCH_BOTS_ERROR = "Error occurred while fetching bots: ";
    public static final String ICON_LOADING_ERROR = "Error loading icons: ";
    public static final String DOWNLOAD_ERROR = "Error during bot download: ";
    public static final String ERR_DWNL_BOT = "Error while downloading the bot: ";
    public static final String ERR_BTO_DWNLD = "Error during bot download: ";
    public static final String IMAGE_PROCESSING_ERROR = "Error during image processing: ";
    public static final String API_FAILURE = "Failed to fetch bots. HTTP Status: ";
    public static final String API_EMPTY_RESPONSE = "API returned no bot data.";
    public static final String STAGE_NOT_INITIALIZED = "Stage not initialized!";
    public static final String SCENE_NOT_INITIALIZED = "La scena non è stata inizializzata";

    // ============================================================================
    // SUCCESS LOG MESSAGES
    // ============================================================================
    public static final String BOT_UPLOADED = "Bot uploaded successfully: %s";
    public static final String DASHBOARD_STRETCH_CONFIGURED = "Dashboard stretch configured successfully.";
    public static final String API_SUCCESS = "Received successful response from API.";
    public static final String BOTS_MAPPED = "Successfully mapped ";
    public static final String DOWNLOAD_SUCCESS = "Bot downloaded successfully: ";
    public static final String IMAGE_PROCESSING_SUCCESS = "Image processing completed successfully: ";
    public static final String DOWNLOAD_COMPLETE = "Download completed: %s";
    public static final String EXTRACT_COMPLETE = "Extraction complete: %s";
    public static final String BOT_EXECUTION_COMPLETED = "Bot execution completed: %s";
    public static final String TUTORIAL_TAB_SETUP = "Tutorial tab setup complete.";
    public static final String TOGGLE_TUTORIAL_TAB_SETUP = "Toggle tutorial tab setup complete.";
    public static final String WINDOW_DRAGGED = "Window dragged";
    public static final String ANIMATION_STARTED = "Animation started for component: %s";
    public static final String FXML_COMPONENT_LOADED = "FXML component loaded: %s";
    public static final String NEW_TAB_CREATED = "Created and selected new tab: ";
    public static final String NEW_TAB_ADDED = "New tab added: ";
    public static final String STAGE_SET = "Stage set: ";
    public static final String STAGE_MINIMIZED = "Stage minimized.";
    public static final String SCENE_SET = "Scene impostata: ";
    public static final String TUT_START = "Tutorial started";

    // ============================================================================
    // INFO LOG MESSAGES
    // ============================================================================
    public static final String DOWNLOAD_START = "Starting download of bot: %s/%s";
    public static final String EXTRACT_START = "Extracting ZIP file: %s";
    public static final String STARTING_EXECUTION = "Starting execution of bot: %s";
    public static final String CONTROLLER_SETUP = "Setting up controllers for components.";
    public static final String TAB_EXISTS = "Tab already exists. Selecting existing tab: ";
    public static final String BUTTON_CLICKED = "Button clicked: %s";
    public static final String DASHBOARD_TOGGLE = "Toggling dashboard panel visibility";
    public static final String TUTORIAL_SHOWN = "Tutorial tab is now visible.";
    public static final String TUTORIAL_HIDDEN = "Tutorial tab hidden.";
    public static final String SENDING_API_REQUEST = "Sending GET request to API: ";
    public static final String TAB_CREATION_ATTEMPT = "Trying to create or select tab for: ";
    public static final String APPLYING_STYLE = "Applying styles to node: ";
    public static final String REMOVING_STYLE = "Removing styles from node: ";
    public static final String INIT_TAB_PANE = "Initializing TabPaneController...";
    public static final String INIT_MAIN_CONTR = "Initializing MainViewController...";
    public static final String WINDOW_FULL_SCREEN = "Window is in full screen, set restore icon.";
    public static final String WINDOW_NOT_FULL_SCREEN = "Window is not in full screen, set maximize icon.";
    public static final String WINDOW_MAXIMIZED = "Window is maximized.";
    public static final String MAXIMIZE_BUTTON_UPDATED = "Maximize button graphic updated.";
    public static final String NULL_STAGE_SET = "Attempt to set a null stage.";
    public static final String NULL_STAGE_MINIMIZE = "Attempt to minimize a null stage.";
    public static final String NULL_STAGE_MAXIMIZE = "Attempt to maximize a null stage.";
    public static final String NODE_STYLE = "Current style of ";
    public static final String BOT_NOT_FOUND_LOCALLY = "Bot not found locally after download: ";
    public static final String STAGE_OR_SCENE_NOT_INITIALIZED = "Stage or scene is not initialized.";
    public static final String DWNLD_SAVED = "Bot downloaded and saved locally: ";
    public static final String ICN_HOVER_EFFECT_APPL = "Hover effect applied.";
    public static final String ICN_HOVER_EFFECT_REM = "Hover effect removed.";
    public static final String MAXIMIZED_STYLE_APPLIED = "Maximized styles applied or removed.";
    public static final String DWNLD_BTN_DISABLED = "Download button disabled.";
    public static final String ICN_UPDTD = "Icon updated to: ";
    public static final String DEL_ICN_VIS = "Delete icon visibility set to: ";
    public static final String CREATED_DIR = "Created directory for bot: ";
    public static final String LEFT_PAN_HIDDEN = "Left panel initially hidden.";
    public static final String NULL_SCENE_SET = "Tentativo di impostare una scena nulla";

}