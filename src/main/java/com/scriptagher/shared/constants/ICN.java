package com.scriptagher.shared.constants;

public class ICN {

    // ============================================================================
    // BASE PATHS
    // ============================================================================
    public static final String RES = "src/main/resources";  // Base resources directory
    public static final String ICNS = "/icons/";            // Icons directory
    public static final String CLOUD = "/cloud/";           // Cloud-related icons directory

    // ============================================================================
    // ICONS
    // ============================================================================
    // General Icons
    public static final String CIRCLE = "icons8-circle-32.png";
    public static final String BIN = "icons8-elimina-50.png";
    public static final String GRABBING = "icons8-grabbing-tool-48.png";
    public static final String GRABBED = "icons8-grabbed-tool-48.png";

    // Cloud-related Icons
    public static final String CLOUD_MARK = "icons8-cloud-marcato-50.png";
    public static final String CLOUD_LOAD = "icons8-caricamento-dal-cloud-50.gif";
    public static final String CLOUD_DWNLD = "icons8-scarica-da-cloud-50.png";
    public static final String CLOUD_ERR = "icons8-nuvola-errore-50.png";
    public static final String CLOUD_RESTORE = "icons8-ripristino-32.png";

    // ============================================================================
    // ICON PATHS
    // ============================================================================
    // General Icon Paths
    public static final String PATH_BIN = RES + ICNS + BIN;
    public static final String PATH_GRABBING = RES + ICNS + GRABBING;
    public static final String PATH_GRABBED = RES + ICNS + GRABBED;

    // Cloud-related Icon Paths
    public static final String PATH_CLOUD_MARK = RES + ICNS + CLOUD + CLOUD_MARK;
    public static final String PATH_CLOUD_LOAD = RES + ICNS + CLOUD + CLOUD_LOAD;
    public static final String PATH_CLOUD_DWNLD = RES + ICNS + CLOUD + CLOUD_DWNLD;
    public static final String PATH_CLOUD_ERROR = RES + ICNS + CLOUD + CLOUD_ERR;
}