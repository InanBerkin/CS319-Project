package client.SingleplayerMenu;

import client.MenuController;
import client.QBitzApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.net.URL;
import java.sql.SQLOutput;
import java.util.ResourceBundle;

public class SingleplayerController extends MenuController {

    @FXML
    private Button taEasy;
    @FXML
    private Button taNormal;
    @FXML
    private Button taHard;
    @FXML
    private Button irEasy;
    @FXML
    private Button irNormal;
    @FXML
    private Button irHard;
    @FXML
    private Button memEasy;
    @FXML
    private Button memNormal;
    @FXML
    private Button memHard;


    @Override
    public void onMessageReceived(String message) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setButtonMethods();
    }

    private void setButtonMethods()
    {
        taEasy.setOnAction(e-> startGame(3,0));
        taNormal.setOnAction(e-> startGame(4,0));
        taHard.setOnAction(e-> startGame(5,0));
        irEasy.setOnAction(e-> startGame(3,1));
        irNormal.setOnAction(e-> startGame(4,1));
        irHard.setOnAction(e-> startGame(5,1));
        memEasy.setOnAction(e-> startMemoryGame(3));
        memNormal.setOnAction(e-> startMemoryGame(4));
        memHard.setOnAction(e-> startMemoryGame(5));
    }

    @FXML
    public void startGame(int boardSize, int gameMode){
        JSONObject payload = new JSONObject();
        payload.put("boardSize", boardSize);
        payload.put("gameMode", gameMode);
        QBitzApplication.getSceneController().changeScene("GameInstance", payload);
    }

    @FXML
    public void startMemoryGame(int boardSize){
        JSONObject payload = new JSONObject();
        payload.put("boardSize", boardSize);
        QBitzApplication.getSceneController().changeScene("MemoryMode", payload);
    }


}
