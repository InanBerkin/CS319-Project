package client.Menus;

import client.QBitzApplication;
import javafx.fxml.Initializable;
import org.json.JSONObject;

public abstract class MenuController implements Initializable {

    public JSONObject payload;

    public abstract void onMessageReceived(String message);

    public void setPayload(JSONObject payload) {
        this.payload = payload;
    }

    public void gotoMainMenu(){
        QBitzApplication.getSceneController().gotoMenu("MainMenu");
    }
}
