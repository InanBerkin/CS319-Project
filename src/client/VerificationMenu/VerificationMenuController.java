package client.VerificationMenu;

import client.MenuController;
import client.QBitzApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class VerificationMenuController extends MenuController {

    @FXML
    public TextField verificationCode;

    @Override
    public void onMessageReceived(String message) {
        JSONObject response = new JSONObject(message);
        if(response.getBoolean("result")){
            Platform.runLater(() -> {
                QBitzApplication.getSceneController().changeScene("LoginMenu");
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void checkVerificationCode(){
        if(verificationCode.getText().equals("")){
            return;
        }
        JSONObject verificationJSON = payload;
        payload.put("code", verificationCode.getText());
        QBitzApplication.getSceneController().sendMessageToServer(payload);
    }
}
