package client.Menus.RoomCreateMenu;

import client.Menus.MenuController;
import client.QBitzApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomCreateMenuController extends MenuController {
    @FXML
    private TextField roomNameTextField;
    @FXML
    private ComboBox gameModeComboBox;
    @FXML
    private TextField maxPlayersTextField;
    @FXML
    private TextField minEntranceLevelTextField;
    @FXML
    private CheckBox roomTypeCheckbox;

    private String roomCode = "";


    @Override
    public void onMessageReceived(String message) {
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("joinRoom")){
            int resultCode = responseJSON.getInt("result");
            if (resultCode == 0){
                Platform.runLater(() -> {
                    responseJSON.put("roomCode", roomCode);
                    QBitzApplication.getSceneController().gotoMenu("RoomLobbyMenu", responseJSON);
                });
            }
        }
        else if(responseJSON.getString("responseType").equals("createRoom")){
            Platform.runLater(() -> {
                if (responseJSON.has("roomCode")){
                    roomCode = responseJSON.getString("roomCode");
                }
                joinRoom(responseJSON.getInt("roomID"));
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGameModeComboBox();
    }

    private void initializeGameModeComboBox(){
        gameModeComboBox.getItems().addAll(
                "RaceModeController",
                "Elimination",
                "Image Rec"
        );
        gameModeComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void sendRoomData(){
        String roomName = roomNameTextField.getText();
        int gameMode = gameModeComboBox.getSelectionModel().getSelectedIndex();
        int maxPlayers = Integer.parseInt(maxPlayersTextField.getText());
        int entranceLevel = Integer.parseInt(minEntranceLevelTextField.getText());
        int roomType = roomTypeCheckbox.isSelected() ? 1 : 0;
        JSONObject roomJSON = new JSONObject();
        roomJSON.put("requestType", "createRoom");
        roomJSON.put("name", roomName);
        roomJSON.put("gameMode", gameMode);
        roomJSON.put("maxPlayers", maxPlayers);
        roomJSON.put("entranceLevel", entranceLevel);
        roomJSON.put("roomType", roomType);
        QBitzApplication.getSceneController().sendMessageToServer(roomJSON);
    }

    @FXML
    private void goBack(){
        QBitzApplication.getSceneController().gotoMenu("RoomMenu");
    }

    private void joinRoom(int id){
        JSONObject roomJSON = new JSONObject();
        roomJSON.put("requestType", "joinRoom");
        roomJSON.put("roomID", id);
        QBitzApplication.getSceneController().sendMessageToServer(roomJSON);
    }


}
