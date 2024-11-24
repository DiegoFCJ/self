package com.scriptagher.frontend.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.scriptagher.frontend.service.MaximizeService;
import com.scriptagher.frontend.service.WindowService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class HeaderBarController implements Initializable {

    private final MaximizeService maximizeService = new MaximizeService();
    private final WindowService windowService = new WindowService();

    @FXML
    private Button titleLabel;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button maximizeButton;

    @FXML
    private Button closeButton;

    @FXML
    private Button back;

    @FXML
    private Button dashboard;


    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        // Azioni per i pulsanti
        closeButton.setOnAction(e -> System.exit(0));
        minimizeButton.setOnAction(e -> {
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });
        maximizeButton.setOnAction(e -> {
            Stage stage = (Stage) maximizeButton.getScene().getWindow();
            maximizeService.updateMaximizeButton(stage, maximizeButton);
            stage.setFullScreen(!stage.isFullScreen());
        });
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    @FXML
    private void moveWindow(MouseEvent event) {
        windowService.moveWindow(event);
    }

    @FXML
    private void releaseAndChangeCursor(MouseEvent event) {
        windowService.releaseAndChangeCursor(event);
    }

    @FXML
    private void onMouseReleased(MouseEvent event) {
        windowService.onMouseReleased(event);
    }
}