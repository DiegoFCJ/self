package com.scriptagher.frontend.controller;

import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import com.scriptagher.frontend.dto.Bot;
import com.scriptagher.frontend.service.TabPaneService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * This controller manages the tab pane UI, including creating and switching
 * between tabs.
 * It also manages the tutorial tab and the logic for showing or hiding it.
 */
public class TabPaneController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tutorialTab;

    private Tab toggleTutorialTab;

    private TabPaneService tabPaneService = new TabPaneService();

    /**
     * Initializes the TabPaneController, setting up the tutorial tab and the toggle
     * tutorial tab.
     */
    @FXML
    public void initialize() {
        CustomLogger.info(LOGS.TAB_PANE_CONTROLLER, LOGS.INIT_TAB_PANE);

        setupTutorialTab();
        setupToggleTutorialTab();
    }

    /**
     * Creates a new tab for a given bot and adds it to the tab pane.
     * 
     * @param bot the bot to create a tab for.
     */
    public void createNewTab(Bot bot) {
        tabPaneService.createNewTab(tabPane, bot);
    }

    /**
     * Sets up the tutorial tab with its initial content and hides it by default.
     */
    private void setupTutorialTab() {
        VBox tutorialContent = new VBox();
        tutorialContent.setSpacing(10);

        Label welcomeMessage = new Label("Welcome to Scriptagher!\n\n"
                + "This tutorial will guide you through using the software.\n"
                + "1. You can navigate between tabs to access functions.\n"
                + "2. Use the left menu to dynamically create new functions.\n"
                + "3. Follow the tutorial to get started!");

        Button hideTutorialButton = new Button("Hide Tutorial");
        hideTutorialButton.setOnAction(event -> hideTutorial());

        tutorialContent.getChildren().addAll(welcomeMessage, hideTutorialButton);
        tutorialTab.setContent(tutorialContent);
        tutorialTab.setText("Tutorial");

        CustomLogger.info(LOGS.TAB_PANE_CONTROLLER, LOGS.TUTORIAL_TAB_SETUP);
    }

    /**
     * Sets up the toggle tutorial tab, which allows users to show the tutorial
     * again.
     */
    private void setupToggleTutorialTab() {
        toggleTutorialTab = new Tab("T");
        toggleTutorialTab.setClosable(false);
        toggleTutorialTab.setOnSelectionChanged(event -> {
            if (toggleTutorialTab.isSelected()) {
                showTutorial();
                tabPane.getTabs().remove(toggleTutorialTab);
            }
        });

        CustomLogger.info(LOGS.TAB_PANE_CONTROLLER, LOGS.TOGGLE_TUTORIAL_TAB_SETUP);
    }

    /**
     * Displays the tutorial tab at the first position in the tab pane.
     */
    private void showTutorial() {
        if (!tabPane.getTabs().contains(tutorialTab)) {
            tabPane.getTabs().add(0, tutorialTab);
            tabPane.getSelectionModel().select(tutorialTab);
            CustomLogger.info(LOGS.TAB_PANE_CONTROLLER, LOGS.TUTORIAL_SHOWN);
        }
    }

    /**
     * Hides the tutorial tab and shows the toggle tutorial tab.
     */
    private void hideTutorial() {
        tabPane.getTabs().remove(tutorialTab);
        if (!tabPane.getTabs().contains(toggleTutorialTab)) {
            tabPane.getTabs().add(0, toggleTutorialTab);
        }
        CustomLogger.info(LOGS.TAB_PANE_CONTROLLER, LOGS.TUTORIAL_HIDDEN);
    }

    /**
     * Configures the dashboard panel by hiding the left panel and setting up the
     * appropriate margins.
     * 
     * @param leftPane            the left panel to be hidden.
     * @param headerBarController the header bar controller to configure the
     *                            dashboard button.
     */
    public void dashboardStretch(AnchorPane leftPane, HeaderBarController headerBarController) {
        if (leftPane == null) {
            CustomLogger.error(LOGS.TAB_PANE_CONTROLLER, LOGS.DASHBOARD_STRETCH_ERROR);
        } else {
            CustomLogger.info(LOGS.TAB_PANE_CONTROLLER, LOGS.DASHBOARD_STRETCH_CONFIGURED);
        }

        if (leftPane != null) {
            leftPane.setTranslateX(-200.0);
            tabPane.setTranslateX(-200.0);
            BorderPane.setMargin(tabPane, new Insets(0, -200, 0, 0));
            leftPane.setVisible(false);
            headerBarController.configureDashboardButton(leftPane, tabPane);
            CustomLogger.info(LOGS.TAB_PANE_CONTROLLER, LOGS.LEFT_PAN_HIDDEN);
        }
    }

    /**
     * Starts the tutorial when the "Start Tutorial" button is clicked.
     * 
     * @param event the action event for the button click.
     */
    @FXML
    public void handleStartTutorial(ActionEvent event) {
        CustomLogger.info(LOGS.TAB_PANE_CONTROLLER, LOGS.TUT_START);
    }
}