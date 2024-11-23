package com.self.self_project.back.factories.window;

import com.self.self_project.dtos.WindowDTO;
import com.self.self_project.utils.logging.CustomLoggerUtils;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WindowMover {

    private WindowDTO windowState;

    public WindowMover(WindowDTO windowState) {
        this.windowState = windowState;
    }

    public void addGrabMoveEventHandlers(Stage stage) {
        stage.getScene().setOnMousePressed(event -> {
            //CustomLoggerUtils.info("MousePressed", "Mouse pressed for window movement at X = " + event.getScreenX() + ", Y = " + event.getScreenY());
            if (event.getButton() == MouseButton.PRIMARY && !isNearEdge(event, stage)) {
                windowState.setDragOffsetX(event.getSceneX());
                windowState.setDragOffsetY(event.getSceneY());
                stage.getScene().setCursor(Cursor.CLOSED_HAND);
                //CustomLoggerUtils.info("WindowDragStart", "Starting window drag, dragOffsetX = " + windowState.getDragOffsetX() + ", dragOffsetY = " + windowState.getDragOffsetY());
            }
        });

        stage.getScene().setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY && !isNearEdge(event, stage)) {
                stage.setX(event.getScreenX() - windowState.getDragOffsetX());
                stage.setY(event.getScreenY() - windowState.getDragOffsetY());
                //CustomLoggerUtils.info("WindowDragged", "Dragging window to X = " + stage.getX() + ", Y = " + stage.getY());
            }
        });

        stage.getScene().setOnMouseReleased(event -> {
            stage.getScene().setCursor(Cursor.DEFAULT);
            CustomLoggerUtils.info("MouseReleased", "Window movement operation completed.");
        });
    }

    private boolean isNearEdge(MouseEvent event, Stage stage) {
        double edgeSize = 5;
        double mouseX = event.getScreenX() - stage.getX();
        double mouseY = event.getScreenY() - stage.getY();
        return (mouseX < edgeSize || mouseX > (stage.getWidth() - edgeSize) ||
                mouseY < edgeSize || mouseY > (stage.getHeight() - edgeSize));
    }
}