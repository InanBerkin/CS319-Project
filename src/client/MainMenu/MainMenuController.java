package client.MainMenu;

import client.MenuController;
import client.QBitzApplication;
import client.QBitzPopup;
import client.UserConfiguration;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        QBitzApplication.getSceneController().changeScene("SingleplayerMenu");
    }

    public void gotoLogin(){
        QBitzApplication.getSceneController().changeScene("RoomMenu");
        if (UserConfiguration.isOnline){
            if(UserConfiguration.isLoggedIn){
                QBitzApplication.getSceneController().changeScene("RoomMenu");
            }
            else {
                QBitzApplication.getSceneController().changeScene("LoginMenu");
            }
        }
        else
            System.out.println("» You are offline.");
    }
}
