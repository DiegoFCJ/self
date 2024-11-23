package com.self.self_project.back.factories;

import com.self.self_project.constants.MENU;
import com.self.self_project.dtos.MenuDTO;
import com.self.self_project.dtos.SceneDTO;
import com.self.self_project.dtos.SubmenuDTO;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.self.self_project.utils.logging.CustomLoggerUtils;
import com.self.self_project.constants.LOGS;
import java.util.Stack;

public class SceneFactory {

    private Stage primaryStage;
    private VBox sceneLayout;
    private javafx.scene.shape.Rectangle clip;
    private MenuFactory menuFactory;
    private ButtonsFactory buttonsFactory;
    private Stack<SceneDTO> sceneHistory;

    public SceneFactory(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.menuFactory = new MenuFactory();
        this.sceneHistory = new Stack<>();
        CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "SceneFactory initialized.");
    }

    public void showSubMenuScene(Stage stage, SubmenuDTO submenu) {
        VBox subMenuBox = new VBox(MENU.SPACING);
        subMenuBox.getStyleClass().add("vbox");

        // Titolo del sottomenu
        Label subMenuLabel = new Label(submenu.getTitleString());
        subMenuLabel.getStyleClass().add("label");
        subMenuBox.getChildren().add(subMenuLabel);

        // Aggiunta dei bottoni per ogni azione nel sottomenu
        for (MenuDTO actionMenu : submenu.getMenuActions()) {
            Button menuButton = buttonsFactory.createButton(actionMenu.getTitle(), e -> {
                CustomLoggerUtils.info(LOGS.SCENE_FACTORY, actionMenu.getTitle() + " button clicked.");
                // Qui puoi implementare la logica per le azioni o cambiare scena se ci sono
                // altri sottomenu
            });
            subMenuBox.getChildren().add(menuButton);
        }

        Scene subMenuScene = new Scene(subMenuBox, 700, 700);
        stage.setScene(subMenuScene);
        stage.show();
        CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Submenu scene shown for " + submenu.getTitleString());
    }

    /**
     * Changes the current scene based on the provided SceneDTO.
     *
     * @param sceneDTO The DTO containing parameters for scene creation.
     */
    public void changeScene(SceneDTO sceneDTO) {
        CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Attempting to change scene to: " + sceneDTO.getMenuType());

        // Push the current scene to the history stack before changing
        if (!sceneHistory.isEmpty()) {
            SceneDTO currentScene = sceneHistory.peek();
            if (!currentScene.equals(sceneDTO)) { // Now this comparison will work properly
                sceneHistory.push(currentScene);
                CustomLoggerUtils.info(LOGS.SCENE_FACTORY,
                        "Pushed current scene to history: " + currentScene.getMenuType());
            } else {
                CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Scene is the same as current. No change made.");
            }
        } else {
            // Add initial scene if stack is empty
            sceneHistory.push(sceneDTO);
            CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Added initial scene to history: " + sceneDTO.getMenuType());
        }

        VBox menuLayout;
        menuLayout = menuFactory.createMainMenu(primaryStage, this);

        // Determine which menu layout to create based on SceneDTO
        switch (sceneDTO.getMenuType()) {
            case "Main":
                menuLayout = menuFactory.createMainMenu(primaryStage, this);
                break;
            case "Automation":
                // menuLayout = menuFactory.getAutomationMenu(primaryStage, this);
                break;
            case "University":
                // menuLayout = menuFactory.getUniversityMenu(primaryStage, this);
                break;
            default:
                CustomLoggerUtils.error(LOGS.SCENE_FACTORY, "Unknown menu type: " + sceneDTO.getMenuType());
                throw new IllegalArgumentException("Unknown menu type: " + sceneDTO.getMenuType());
        }

        // Create the scene layout with header and menu
        sceneLayout = new VBox(HeaderFactory.createHeader(primaryStage, sceneDTO.isShowBackButton(), this), menuLayout);
        CustomLoggerUtils.info(LOGS.SCENE_FACTORY, sceneDTO.getMenuType() + " menu scene created.");

        // Call createScene with the new layout
        createScene(sceneLayout, sceneDTO.getWidth(), sceneDTO.getHeight());
    }

    /**
     * Goes back to the previous scene.
     */
    public void goBack() {
        CustomLoggerUtils.info(LOGS.SCENE_FACTORY,
                "Attempting to go back to the previous scene. Scene History: " + sceneHistory);
        if (sceneHistory.size() > 1) {
            // Pop the current scene
            sceneHistory.pop();
            // Peek at the previous scene
            SceneDTO previousScene = sceneHistory.peek();
            CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Changing scene to previous: " + previousScene.getMenuType());
            changeScene(previousScene); // Change to the previous scene
        } else {
            CustomLoggerUtils.warn(LOGS.SCENE_FACTORY, "No previous scene to go back to.");
        }
    }

    /**
     * Creates a Scene with the specified layout and dimensions.
     *
     * @param layout The layout for the scene.
     * @param width  The width of the scene.
     * @param height The height of the scene.
     */
    private void createScene(VBox layout, double width, double height) {
        clip = new javafx.scene.shape.Rectangle(width, height);
        layout.setClip(clip);

        CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Creating scene with width: " + width + " and height: " + height);

        Scene scene = new Scene(layout, width, height);
        scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/header.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/menu.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/styles/btn.css").toExternalForm());

        CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Scene created with layout.");

        // Set the new scene to the primary stage
        primaryStage.setScene(scene);
        primaryStage.show(); // Show the updated stage
        CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Scene displayed on primary stage.");

        // Aggiungi un listener per aggiornare il ritaglio quando il palco viene
        // ridimensionato
        primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            clip.setWidth(newWidth.doubleValue());
        });
        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            clip.setHeight(newHeight.doubleValue());
        });
    }

    /**
     * Creates the main menu scene.
     *
     * @param showHomeButton Indicates whether to show the home button in the
     *                       header.
     * @param showBackButton Indicates whether to show the back button in the
     *                       header.
     */
    public void createMainMenuScene(boolean showHomeButton, boolean showBackButton) {
        SceneDTO sceneDTO = new SceneDTO(showHomeButton, showBackButton, "Main", MENU.WINDOW_WIDTH, MENU.WINDOW_HEIGHT);
        CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Creating main menu scene.");
        changeScene(sceneDTO);
    }

    /**
     * Creates the automation menu scene.
     */
    public void createAutomationMenuScene() {
        SceneDTO sceneDTO = new SceneDTO(false, true, "Automation", MENU.WINDOW_WIDTH, MENU.WINDOW_HEIGHT);
        CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Creating automation menu scene.");
        changeScene(sceneDTO);
    }

    /**
     * Creates the university menu scene.
     */
    public void createUniversityMenuScene() {
        SceneDTO sceneDTO = new SceneDTO(true, true, "University", MENU.WINDOW_WIDTH, MENU.WINDOW_HEIGHT);
        CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Creating university menu scene.");
        changeScene(sceneDTO);
    }

    /**
     * Adjusts the UI components for window resize.
     */
    public void adjustUIForResize() {
        if (clip != null) {
            primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
                if (clip != null) {
                    clip.setWidth(newVal.doubleValue());
                    CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Clip width updated to: " + newVal);
                }
            });

            primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
                if (clip != null) {
                    clip.setHeight(newVal.doubleValue());
                    CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "Clip height updated to: " + newVal);
                }
            });

            sceneLayout.setPrefWidth(primaryStage.getWidth());
            sceneLayout.setPrefHeight(primaryStage.getHeight());
            sceneLayout.requestLayout();
            CustomLoggerUtils.info(LOGS.SCENE_FACTORY, "UI adjusted for window resize to width: "
                    + primaryStage.getWidth() + " and height: " + primaryStage.getHeight());
        } else {
            CustomLoggerUtils.warn(LOGS.SCENE_FACTORY, "Clip is null, unable to adjust UI for resize.");
        }
    }
}