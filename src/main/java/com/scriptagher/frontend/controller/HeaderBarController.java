package com.scriptagher.frontend.controller;

import com.scriptagher.frontend.service.MaximizeService;
import com.scriptagher.frontend.service.StageManager;
import com.scriptagher.frontend.service.WindowService;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

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
        // Configura i pulsanti della finestra
        closeButton.setOnAction(e -> {
            CustomLogger.info(LOGS.HEADER_BAR_CONTROLLER, String.format(LOGS.BUTTON_CLICKED, "Close"));
            System.exit(0);
        });

        minimizeButton.setOnAction(e -> {
            CustomLogger.info(LOGS.HEADER_BAR_CONTROLLER, String.format(LOGS.BUTTON_CLICKED, "Minimize"));
            // Usa StageManager per minimizzare la finestra
            StageManager.minimize();
        });

        maximizeButton.setOnAction(e -> {
            CustomLogger.info(LOGS.HEADER_BAR_CONTROLLER, String.format(LOGS.BUTTON_CLICKED, "Maximize"));
            // Usa StageManager per gestire la massimizzazione
            StageManager.maximize();
            maximizeService.updateMaximizeButton(maximizeButton);
        });

        back.setOnAction(e -> {
            CustomLogger.info(LOGS.HEADER_BAR_CONTROLLER, String.format(LOGS.BUTTON_CLICKED, "Back"));
        });
    }

    /**
     * Configura il bottone della dashboard per gestire la visibilità del pannello
     * sinistro
     * e l'animazione del contenuto principale.
     */
    public void configureDashboardButton(AnchorPane leftPane, TabPane fullContent) {
        int widthChange = -200;

        // Ottieni l'immagine corrente del pulsante
        ImageView imageView = (ImageView) dashboard.getGraphic();

        dashboard.setOnAction(event -> {
            if (leftPane == null || fullContent == null) {
                CustomLogger.error(LOGS.HEADER_BAR_CONTROLLER, "Errore: Pannelli non inizializzati!");
                return;
            }

            CustomLogger.info(LOGS.HEADER_BAR_CONTROLLER, LOGS.DASHBOARD_TOGGLE);

            if (leftPane.isVisible()) {
                // Nascondi il pannello sinistro con animazione
                animateSlide(leftPane, widthChange, false);

                // Sposta il contenuto principale verso sinistra con animazione
                animateFullContent(fullContent, 0, widthChange);
                animateMargins(fullContent, 0, widthChange);

                // Aggiorna immagine del bottone
                imageView.setImage(
                        new Image(getClass().getResource("/icons/icons8-dashboard-open-32.png").toExternalForm()));
            } else {
                // Mostra il pannello sinistro con animazione
                animateSlide(leftPane, 0, true);

                // Riporta il contenuto principale alla posizione originale con animazione
                animateFullContent(fullContent, widthChange, 0);
                animateMargins(fullContent, widthChange, 0);

                // Aggiorna immagine del bottone
                imageView.setImage(
                        new Image(getClass().getResource("/icons/icons8-dashboard-close-32.png").toExternalForm()));
            }
        });
    }

    /**
     * Anima i margini di un nodo in modo fluido utilizzando una Timeline.
     */
    private void animateMargins(TabPane fullContent, int startMargin, int endMargin) {
        CustomLogger.info(LOGS.HEADER_BAR_CONTROLLER, String.format(LOGS.ANIMATION_STARTED, "Margins"));

        Timeline timeline = new Timeline();

        // Numero di step per l'animazione (può essere aggiustato)
        int steps = 20;

        // Calcola l'incremento del margine per ogni step
        double stepSize = (endMargin - startMargin) / (double) steps;

        // Durata totale dell'animazione
        double totalDuration = 0.1; // secondi
        double stepDuration = totalDuration / steps;

        for (int i = 0; i <= steps; i++) {
            int currentMargin = (int) (startMargin + i * stepSize);

            // Crea un frame per aggiornare il margine
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i * stepDuration),
                    e -> BorderPane.setMargin(fullContent, new Insets(0, currentMargin, 0, 0)));

            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }

    /**
     * Anima lo spostamento del pannello sinistro con una transizione fluida.
     */
    private void animateSlide(AnchorPane leftPane, int widthChange, boolean visible) {
        CustomLogger.info(LOGS.HEADER_BAR_CONTROLLER, String.format(LOGS.ANIMATION_STARTED, "Left pane slide"));

        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.1));
        slide.setNode(leftPane);
        slide.setToX(visible ? 0 : widthChange);
        slide.setOnFinished(e -> leftPane.setVisible(visible));
        slide.play();
    }

    /**
     * Anima il contenuto principale per simulare un effetto di margine fluido.
     */
    private void animateFullContent(TabPane fullContent, double fromX, double toX) {
        CustomLogger.info(LOGS.HEADER_BAR_CONTROLLER, String.format(LOGS.ANIMATION_STARTED, "Full content"));

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.1));
        transition.setNode(fullContent);
        transition.setFromX(fromX);
        transition.setToX(toX);
        transition.play();
    }

    /**
     * Imposta dinamicamente il titolo della barra.
     */
    public void setTitle() {
        String userName = System.getProperty("user.name");
        titleLabel.setText(userName.toUpperCase());
    }

    @FXML
    private void moveWindow(MouseEvent event) {
        CustomLogger.info(LOGS.HEADER_BAR_CONTROLLER, LOGS.WINDOW_DRAGGED);
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