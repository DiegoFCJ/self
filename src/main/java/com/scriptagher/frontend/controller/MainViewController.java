package com.scriptagher.frontend.controller;

import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private BorderPane mainPane;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        try {
            CustomLogger.info(LOGS.MAIN_VIEW_CONTROLLER, LOGS.INIT_MAIN_CONTR);

            mainPane.getStyleClass().add("rounded-border");

            FXMLLoader headerBarLoader = new FXMLLoader(getClass().getResource(LOGS.PATH_HEAD));
            FXMLLoader tabPaneLoader = new FXMLLoader(getClass().getResource(LOGS.PATH_TABPANE));
            FXMLLoader leftPaneLoader = new FXMLLoader(getClass().getResource(LOGS.PATH_LEFTPANE));

            // Carica i componenti FXML
            mainPane.setTop(headerBarLoader.load());
            CustomLogger.info(LOGS.MAIN_VIEW_CONTROLLER, String.format(LOGS.FXML_COMPONENT_LOADED, LOGS.HEAD));

            mainPane.setCenter(tabPaneLoader.load());
            CustomLogger.info(LOGS.MAIN_VIEW_CONTROLLER, String.format(LOGS.FXML_COMPONENT_LOADED, LOGS.TABPANE));

            mainPane.setLeft(leftPaneLoader.load());
            CustomLogger.info(LOGS.MAIN_VIEW_CONTROLLER, String.format(LOGS.FXML_COMPONENT_LOADED, LOGS.LEFTPANE));

            // Ottieni i controller dai file FXML
            HeaderBarController headerBarController = headerBarLoader.getController();
            TabPaneController tabPaneController = tabPaneLoader.getController();
            LeftPaneController leftPaneController = leftPaneLoader.getController();

            // Passa il controller di TabPaneController a LeftPane se necessario
            leftPaneController.setTabPaneController(tabPaneController);

            CustomLogger.info(LOGS.MAIN_VIEW_CONTROLLER, LOGS.CONTROLLER_SETUP);

            // Configura il pulsante dashboard per estendere o comprimere il pannello
            tabPaneController.dashboardStretch(leftPaneController.getLeftPane(), headerBarController);

        } catch (Exception e) {
            CustomLogger.error(LOGS.MAIN_VIEW_CONTROLLER, LOGS.INITIALIZATION_ERROR);
            e.printStackTrace();
        }
    }
}