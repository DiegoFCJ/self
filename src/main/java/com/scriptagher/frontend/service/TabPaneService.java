package com.scriptagher.frontend.service;

import com.scriptagher.frontend.dto.Bot;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Service class for managing the TabPane, including creating and selecting tabs for bots.
 */
public class TabPaneService {

    /**
     * Creates a new tab for the given bot in the TabPane. If the tab already exists, it selects the existing tab.
     *
     * @param tabPane the TabPane where the tab should be created
     * @param bot the bot for which the tab is created
     */
    public void createNewTab(TabPane tabPane, Bot bot) {
        String tabName = bot.getLanguage() + "/" + bot.getBotName();
        
        // Log the attempt to create or select a tab
        CustomLogger.debug(LOGS.TAB_PANE_SERVICE, LOGS.TAB_CREATION_ATTEMPT + tabName);
        
        // Check if the tab with the same name already exists
        Tab existingTab = getTabByName(tabPane, tabName);

        if (existingTab != null) {
            // If the tab already exists, select it
            tabPane.getSelectionModel().select(existingTab);
            CustomLogger.info(LOGS.TAB_PANE_SERVICE, LOGS.TAB_EXISTS + tabName);
        } else {
            // If the tab doesn't exist, create a new one
            this.addTab(tabPane, tabName, "Content for tab: " + tabName);
            CustomLogger.info(LOGS.TAB_PANE_SERVICE, LOGS.NEW_TAB_CREATED + tabName);
        }
    }

    /**
     * Searches for a tab by name in the TabPane.
     *
     * @param tabPane the TabPane to search in
     * @param title the title of the tab to find
     * @return the Tab if found, otherwise null
     */
    private Tab getTabByName(TabPane tabPane, String title) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(title)) {
                return tab;
            }
        }
        // Return null if no tab with the given title is found
        return null;
    }

    /**
     * Adds a new tab to the TabPane with the given title and content.
     *
     * @param tabPane the TabPane where the tab will be added
     * @param title the title of the new tab
     * @param content the content to display in the new tab
     */
    public void addTab(TabPane tabPane, String title, String content) {
        Tab newTab = new Tab(title);
        Label contentLabel = new Label(content);
        newTab.setContent(contentLabel);

        // Add the new tab to the TabPane and select it
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
        
        // Log the creation of a new tab
        CustomLogger.info(LOGS.TAB_PANE_SERVICE, LOGS.NEW_TAB_ADDED + title);
    }
}