package client.PostGameMenu;

import client.MenuController;
import client.QBitzApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.json.JSONObject;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class PostGameMenuController extends MenuController {

    @FXML
    private Label timeLabel;

    @Override
    public void onMessageReceived(String message) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()-> timeLabel.setText(payload.getString("time")));
            }
        };
        timer.schedule(timerTask, 100l);

    }

    @FXML
    public void gotoSingleplayer(){
        QBitzApplication.getSceneController().changeScene("SingleplayerMenu");
    }

}
