package client.SingleplayerMenu;

import client.MenuController;
import client.QBitzApplication;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class SingleplayerController extends MenuController {

    @Override
    public void onMessageReceived(String message) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void startGame(){
        QBitzApplication.getSceneController().changeScene("GameInstance");
    }


}
