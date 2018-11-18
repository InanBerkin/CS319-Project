package client.PostGameMenu;

import client.MenuController;
import client.QBitzApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.json.JSONObject;


import java.net.URL;
import java.util.ResourceBundle;

public class PostGameMenuController extends MenuController {

    @FXML
    private Label timeLabel;

    @Override
    public void onMessageReceived(String message) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        JSONObject obj = payload;
//        System.out.println(obj.getString("time"));
//        timeLabel.setText(obj.getString("time"));
    }

    @FXML
    public void gotoSingleplayer(){
        QBitzApplication.getSceneController().changeScene("SingleplayerMenu");
    }
}
