package com.self.self_project.back.factories.window;

import com.self.self_project.dtos.WindowDTO;
import com.self.self_project.utils.logging.CustomLoggerUtils;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WindowResizeFactory {

    private WindowDTO windowState;
    private boolean isResizing = false;

    public WindowResizeFactory(WindowDTO windowState) {
        this.windowState = windowState;
    }

    public void addResizeEventHandlers(Stage stage) {
        stage.getScene().setOnMousePressed(event -> {
            // Log mouse press event
            CustomLoggerUtils.info("MousePressed",
                    "Mouse pressed at X = " + event.getScreenX() + ", Y = " + event.getScreenY());
            // Check if the primary mouse button is pressed and if near an edge
            if (event.getButton() == MouseButton.PRIMARY && isNearEdge(event, stage)) {
                isResizing = true; // Start resizing
                windowState.setInitialX(event.getScreenX());
                windowState.setInitialY(event.getScreenY());
                windowState.setWidth(stage.getWidth());
                windowState.setHeight(stage.getHeight());
                event.consume(); // Prevent other handlers from interfering
            }
        });

        stage.getScene().setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY && isResizing) {
                double deltaX = event.getScreenX() - windowState.getInitialX();
                double deltaY = event.getScreenY() - windowState.getInitialY();
                CustomLoggerUtils.info("MouseDragged", "Mouse dragged, deltaX = " + deltaX + ", deltaY = " + deltaY);

                // Calculate new width and height dynamically based on mouse position
                if (isNearRightEdge(event, stage)) {
                    double newWidth = Math.max(windowState.getWidth() + deltaX, 100); // Minimum width of 100
                    stage.setWidth(newWidth); // Set new width
                }
                if (isNearBottomEdge(event, stage)) {
                    double newHeight = Math.max(windowState.getHeight() + deltaY, 100); // Minimum height of 100
                    stage.setHeight(newHeight); // Set new height
                }

                // Update initial position for next drag
                windowState.setInitialX(event.getScreenX());
                windowState.setInitialY(event.getScreenY());
                event.consume(); // Consume the event to stop propagation
            }
        });

        stage.getScene().setOnMouseReleased(event -> {
            if (isResizing) {
                CustomLoggerUtils.info("MouseReleased", "Resize operation completed.");
            }
            isResizing = false; // Reset resizing flag
        });

        stage.getScene().setOnMouseMoved(event -> {
            Cursor cursor = getResizeCursor(event, stage);
            stage.getScene().setCursor(cursor); // Set appropriate resize cursor
            //CustomLoggerUtils.info("MouseMoved", "Cursor changed to " + cursor.toString() + " at X = "
            //        + event.getScreenX() + ", Y = " + event.getScreenY());
        });
    }

    private boolean isNearEdge(MouseEvent event, Stage stage) {
        double edgeSize = 5; // Size of the edge to consider
        double mouseX = event.getScreenX() - stage.getX();
        double mouseY = event.getScreenY() - stage.getY();
        boolean nearEdge = (mouseX < edgeSize || mouseX > (stage.getWidth() - edgeSize) ||
                mouseY < edgeSize || mouseY > (stage.getHeight() - edgeSize));
        CustomLoggerUtils.info("isNearEdge", "Mouse near edge: " + nearEdge);
        return nearEdge;
    }

    private boolean isNearRightEdge(MouseEvent event, Stage stage) {
        double edgeSize = 5; // Size of the right edge
        double mouseX = event.getScreenX() - stage.getX();
        boolean nearRightEdge = mouseX > (stage.getWidth() - edgeSize);
        CustomLoggerUtils.info("isNearRightEdge", "Mouse near right edge: " + nearRightEdge);
        return nearRightEdge;
    }

    private boolean isNearBottomEdge(MouseEvent event, Stage stage) {
        double edgeSize = 5; // Size of the bottom edge
        double mouseY = event.getScreenY() - stage.getY();
        boolean nearBottomEdge = mouseY > (stage.getHeight() - edgeSize);
        CustomLoggerUtils.info("isNearBottomEdge", "Mouse near bottom edge: " + nearBottomEdge);
        return nearBottomEdge;
    }

    private Cursor getResizeCursor(MouseEvent event, Stage stage) {
        double edgeSize = 5; // Size of the edge to consider
        double mouseX = event.getScreenX() - stage.getX();
        double mouseY = event.getScreenY() - stage.getY();

        boolean nearRightEdge = mouseX > (stage.getWidth() - edgeSize);
        boolean nearBottomEdge = mouseY > (stage.getHeight() - edgeSize);
        boolean nearLeftEdge = mouseX < edgeSize;
        boolean nearTopEdge = mouseY < edgeSize;

        // Determine cursor type based on edge proximity
        if (nearRightEdge && nearBottomEdge) {
            return Cursor.SE_RESIZE;
        } else if (nearRightEdge && nearTopEdge) {
            return Cursor.NE_RESIZE;
        } else if (nearLeftEdge && nearBottomEdge) {
            return Cursor.SW_RESIZE;
        } else if (nearLeftEdge && nearTopEdge) {
            return Cursor.NW_RESIZE;
        } else if (nearRightEdge) {
            return Cursor.E_RESIZE;
        } else if (nearLeftEdge) {
            return Cursor.W_RESIZE;
        } else if (nearBottomEdge) {
            return Cursor.S_RESIZE;
        } else if (nearTopEdge) {
            return Cursor.N_RESIZE;
        } else {
            return Cursor.DEFAULT;
        }
    }
}