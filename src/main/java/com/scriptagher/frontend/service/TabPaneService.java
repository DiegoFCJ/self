package com.scriptagher.frontend.service;

import com.scriptagher.frontend.dto.Bot;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

/**
 * Service class for managing the TabPane, including creating and selecting tabs
 * for bots.
 */
public class TabPaneService {

    BotExecutionService botExecutionService = new BotExecutionService();

    /**
     * Creates a new tab for the given bot in the TabPane. If the tab already
     * exists, it selects the existing tab.
     *
     * @param tabPane the TabPane where the tab should be created
     * @param bot     the bot for which the tab is created
     */
    public void createNewTab(TabPane tabPane, Bot bot) {
        String tabName = bot.getLanguage() + "/" + bot.getBotName();
    
        CustomLogger.debug(LOGS.TAB_PANE_SERVICE, LOGS.TAB_CREATION_ATTEMPT + tabName);
    
        Tab existingTab = getTabByName(tabPane, tabName);
    
        if (existingTab != null) {
            tabPane.getSelectionModel().select(existingTab);
            CustomLogger.info(LOGS.TAB_PANE_SERVICE, LOGS.TAB_EXISTS + tabName);
        } else {
            Tab newTab = new Tab(tabName);
    
            TextArea textArea = new TextArea();
            textArea.setEditable(false);
    
            newTab.setContent(textArea);
    
            tabPane.getTabs().add(newTab);
            tabPane.getSelectionModel().select(newTab);
    
            Task<Void> task = botExecutionService.executeBot(bot.getLanguage(), bot.getBotName());
    
            task.messageProperty().addListener((observable, oldValue, newValue) -> {
                textArea.appendText(newValue + "\n");
                textArea.setScrollTop(Double.MAX_VALUE);
            });
    
            new Thread(task).start();
    
            CustomLogger.info(LOGS.TAB_PANE_SERVICE, LOGS.NEW_TAB_CREATED + tabName);
        }
    }
    
    /**
     * Searches for a tab by name in the TabPane.
     *
     * @param tabPane the TabPane to search in
     * @param title   the title of the tab to find
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
     * @param title   the title of the new tab
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