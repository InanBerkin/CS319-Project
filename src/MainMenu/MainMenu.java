package MainMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenu extends Scene {

    public MainMenu() throws Exception{
        super( FXMLLoader.load(MainMenu.class.getResource("MainMenuView.fxml")), 800, 600);
    }

}