package client.Menus.MainMenu;

import client.Menus.MenuController;
import client.QBitzApplication;
import client.QBitzPopup;
import client.UserConfiguration;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController extends MenuController {

    @FXML
    private GridPane gridPane;

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    @Override
    public void onMessageReceived(String message) {
        System.out.println(message);
    }

    @FXML
    public void gotoSingleplayerMenu(){
        QBitzApplication.getSceneController().gotoMenu("SingleplayerMenu");
    }

    @FXML
    public void exitGame(){
        Platform.exit();
        System.exit(0);
    }

    public void gotoOptionsMenu(){
        QBitzApplication.getSceneController().gotoMenu("OptionsMenu");
    }

    public void gotoLogin(){
        if (UserConfiguration.isOnline){
            if(UserConfiguration.isLoggedIn){
                QBitzApplication.getSceneController().gotoMenu("RoomMenu");
            }
            else {
                QBitzApplication.getSceneController().gotoMenu("LoginMenu");
            }
        }
        else
            System.out.println("» You are offline.");
    }
}
