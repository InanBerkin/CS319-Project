package LoginMenu;

import MainMenu.MainMenu;
import Qbitz.QbitzApplication;
import SceneController.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginMenuController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    @FXML
    public void login(ActionEvent event) throws Exception{
        QbitzApplication.getSceneController().gotoMainMenu();
        System.out.println("Login");
    }
}
