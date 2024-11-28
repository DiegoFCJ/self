package com.scriptagher.shared.constants;

public class REGEX {

    // Regex per la gestione dei file .log con una data nel nome
    public static final String LOG_WITH_DATE = ".*\\.log\\.\\d{4}-\\d{2}-\\d{2}$";

    // Regex per la gestione dei file .gz con una data nel nome
    public static final String LOG_GZ_WITH_DATE = ".*\\.log\\.\\d{4}-\\d{2}-\\d{2}\\.gz$";

    // Regex per la gestione dei file .gz con una data e un numero nel nome
    public static final String LOG_GZ_WITH_DATE_AND_NUMBER = ".*\\.log\\.\\d{4}-\\d{2}-\\d{2}-\\d+\\.gz$";

    public static final String DATE = "yyyy-MM-dd";
    public static final String DATET_OFORM = "yyyy-MM-dd_HH-mm-ss";
    public static final String DATET_FORM = "yyyy-MM-dd | HH:mm:ss,SSS";

    // Regex per estrarre la data dal nome del file
    public static final String DATE_COMP = "\\d{4}-\\d{2}-\\d{2}";
    public static final String DATE_PATTERN = ".*" + DATE_COMP + ".*";

    // Regex per estrarre il numero della macchina dal nome del file
    public static final String MACHINE_NUMBER_PATTERN = "v\\d{5}";
    
}
