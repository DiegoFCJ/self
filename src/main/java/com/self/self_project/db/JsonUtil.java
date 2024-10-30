package com.self.self_project.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.self.self_project.constants.LOGS;
import com.self.self_project.dtos.MenuDTO;
import com.self.self_project.utils.logging.CustomLoggerUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonUtil {
    private static final String MENU_FILE_PATH = "DB/menu.json"; // Path to your JSON file

    /**
     * Load the menu from a JSON file.
     * @return MenuDTO object if the file is read successfully, otherwise null.
     */
    public static MenuDTO loadMenu() {
        try (FileReader reader = new FileReader(MENU_FILE_PATH)) {
            Gson gson = new Gson();
            CustomLoggerUtils.info(LOGS.DB, "Loading menu from JSON file: " + MENU_FILE_PATH);
            return gson.fromJson(reader, MenuDTO.class);
        } catch (IOException e) {
            CustomLoggerUtils.error(LOGS.DB, "Failed to load menu from JSON file: " + MENU_FILE_PATH + ", error: " + e.getMessage());
            return null; // Handle the error appropriately in your application
        }
    }

    /**
     * Save the menu to a JSON file.
     * @param menu The MenuDTO object to save.
     */
    public static void saveMenu(MenuDTO menu) {
        try (FileWriter writer = new FileWriter(MENU_FILE_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(menu, writer);
            CustomLoggerUtils.info(LOGS.DB, "Menu saved to JSON file: " + MENU_FILE_PATH);
        } catch (IOException e) {
            CustomLoggerUtils.error(LOGS.DB, "Failed to save menu to JSON file: " + MENU_FILE_PATH + ", error: " + e.getMessage());
        }
    }
}
