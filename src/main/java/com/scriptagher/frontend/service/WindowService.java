package com.scriptagher.frontend.service;

import com.scriptagher.shared.constants.ICN;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;

/**
 * Service class for handling window-related operations such as dragging and
 * changing cursors.
 */
public class WindowService {

    private double xOffset = 0;
    private double yOffset = 0;

    private final ImageCursor openHandCursor;  // Cursor for open hand
    private final ImageCursor closedHandCursor; // Cursor for closed hand

    /**
     * Initializes the service and loads custom cursors for window interactions.
     */
    public WindowService() {
        openHandCursor = new ImageCursor(new Image(getClass().getResourceAsStream(ICN.ICNS + ICN.GRABBING)));
        closedHandCursor = new ImageCursor(new Image(getClass().getResourceAsStream(ICN.ICNS + ICN.GRABBED)));
    }

    /**
     * Handles the initial setup for dragging the window.
     * This method registers the mouse's initial position and updates the window's
     * position during drag events.
     *
     * @param event the mouse event triggered by pressing the window header
     */
    public void moveWindow(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();

        HBox header = (HBox) event.getSource();
        Stage stage = (Stage) header.getScene().getWindow();

        // Update the window position during dragging
        header.setOnMouseDragged(e -> {
            header.setCursor(closedHandCursor); // Change to closed hand cursor
            double newX = e.getScreenX() - xOffset;
            double newY = e.getScreenY() - yOffset;
            stage.setX(newX);
            stage.setY(newY);
        });

        // Restore the cursor and opacity when the mouse is released
        header.setOnMouseReleased(e -> {
            header.setCursor(openHandCursor);
            stage.setOpacity(1.0);
        });

        stage.setOpacity(0.8); // Optional visual feedback during drag
        header.setCursor(openHandCursor); // Set initial cursor to open hand
    }

    /**
     * Resets the cursor to default or open hand when the mouse stops dragging.
     *
     * @param event the mouse event triggered when the drag ends
     */
    public void releaseAndChangeCursor(MouseEvent event) {
        HBox header = (HBox) event.getSource();
        header.setCursor(Cursor.DEFAULT); // Restore the default cursor
    }

    /**
     * Restores the window's opacity and resets the cursor when the mouse is
     * released.
     *
     * @param event the mouse event triggered when the mouse button is released
     */
    public void onMouseReleased(MouseEvent event) {
        HBox header = (HBox) event.getSource();
        Stage stage = (Stage) header.getScene().getWindow();
        stage.setOpacity(1.0); // Restore full opacity
        header.setCursor(Cursor.DEFAULT); // Restore the default cursor
    }
}