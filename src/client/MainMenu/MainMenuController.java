package client.MainMenu;

import client.MenuController;
import client.Qbitz.QbitzApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController extends MenuController {

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    @Override
    public void onMessageReceived(String message) {
        System.out.println(message);
    }

    @FXML
    public void sait(){
        QbitzApplication.getSceneController().sendMessageToServer("Ben sait");
    }
}
