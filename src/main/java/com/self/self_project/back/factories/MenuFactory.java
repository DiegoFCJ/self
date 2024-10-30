package com.self.self_project.back.factories;

import com.self.self_project.dtos.MenuDTO;
import com.self.self_project.dtos.SubmenuDTO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.self.self_project.utils.logging.CustomLoggerUtils;
import com.self.self_project.constants.LOGS;
import com.self.self_project.constants.MENU;
import com.self.self_project.db.JsonUtil;

public class MenuFactory {

    private ButtonsFactory buttonsFactory;

    public MenuFactory() {
        this.buttonsFactory = new ButtonsFactory();
        CustomLoggerUtils.info(LOGS.MENU_FACTORY, "MenuFactory initialized.");
    }

    public VBox createMainMenu(Stage primaryStage, SceneFactory sceneFactory) {
        MenuDTO menu = JsonUtil.loadMenu();
        VBox mainMenu = new VBox(MENU.SPACING);
        mainMenu.getStyleClass().add("vbox");

        if (menu != null) {
            Label mainMenuLabel = new Label(menu.getTitle());
            mainMenuLabel.getStyleClass().add("label");
            mainMenu.getChildren().add(mainMenuLabel);

            // Itera attraverso i sottomenu
            for (SubmenuDTO submenu : menu.getSubmenus()) {
                Button submenuButton = buttonsFactory.createButton(submenu.getTitleString(), e -> {
                    CustomLoggerUtils.info(LOGS.MENU_FACTORY, submenu.getTitleString() + " button clicked.");
                    sceneFactory.showSubMenuScene(primaryStage, submenu); // Passa il sottomenu alla scena
                });
                mainMenu.getChildren().add(submenuButton);
            }
        } else {
            CustomLoggerUtils.warn(LOGS.MENU_FACTORY, "Menu data is null.");
        }

        CustomLoggerUtils.info(LOGS.MENU_FACTORY, "Main menu created.");
        return mainMenu;
    }
}
