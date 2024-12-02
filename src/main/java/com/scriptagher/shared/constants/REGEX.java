package com.scriptagher.shared.constants;

public class REGEX {

    // ============================================================================
    // REGEX FOR HANDLING LOG FILES
    // ============================================================================

    // Regex to handle .log files with a date in the name (e.g.,
    // example.log.2024-12-02)
    public static final String LOG_WITH_DATE = ".*\\.log\\.\\d{4}-\\d{2}-\\d{2}$";

    // Regex to handle .gz files with a date in the name (e.g.,
    // example.log.2024-12-02.gz)
    public static final String LOG_GZ_WITH_DATE = ".*\\.log\\.\\d{4}-\\d{2}-\\d{2}\\.gz$";

    // Regex to handle .gz files with a date and a number in the name (e.g.,
    // example.log.2024-12-02-12345.gz)
    public static final String LOG_GZ_WITH_DATE_AND_NUMBER = ".*\\.log\\.\\d{4}-\\d{2}-\\d{2}-\\d+\\.gz$";

    // ============================================================================
    // DATE FORMATS
    // ============================================================================

    public static final String DATE = "yyyy-MM-dd"; // Simple date format (year-month-day)
    public static final String DATET_OFORM = "yyyy-MM-dd_HH-mm-ss"; // Date format with time
                                                                    // (year-month-day_hour-minute-second)
    public static final String DATET_FORM = "yyyy-MM-dd | HH:mm:ss,SSS"; // Date format with time and milliseconds
                                                                         // (year-month-day |
                                                                         // hour:minute:second,milliseconds)

    // ============================================================================
    // REGEX FOR EXTRACTION FROM FILE NAMES
    // ============================================================================

    // Regex to extract the date from the file name (e.g., 2024-12-02)
    public static final String DATE_COMP = "\\d{4}-\\d{2}-\\d{2}";

    // Regex to match a file name that contains a date in the format yyyy-MM-dd
    public static final String DATE_PATTERN = ".*" + DATE_COMP + ".*";

    // ============================================================================
    // REGEX FOR EXTRACTING MACHINE NUMBER
    // ============================================================================

    // Regex to extract the machine number from the file name (e.g., v12345)
    public static final String MACHINE_NUMBER_PATTERN = "v\\d{5}";
}