package client.Menus.RoomCreateMenu;

import client.Menus.MenuController;
import client.QBitzApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    @FXML
    private RadioButton boardSize3;
    @FXML
    private RadioButton boardSize4;
    @FXML
    private RadioButton boardSize5;

    private ToggleGroup toggleGroupBoardSize;

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
        initializeBoardSizeRadioGroup();
    }

    private void initializeGameModeComboBox(){
        gameModeComboBox.getItems().addAll(
                "Race",
                "Elimination",
                "Image Rec"
        );
        gameModeComboBox.getSelectionModel().selectFirst();
    }


    private void initializeBoardSizeRadioGroup(){
        toggleGroupBoardSize = new ToggleGroup();
        boardSize3.setToggleGroup(toggleGroupBoardSize);
        boardSize3.setUserData("3");
        boardSize4.setToggleGroup(toggleGroupBoardSize);
        boardSize4.setUserData("4");
        boardSize5.setToggleGroup(toggleGroupBoardSize);
        boardSize4.setUserData("5");
        boardSize3.setSelected(true);
    }

    @FXML
    private void sendRoomData(){
        String roomName = roomNameTextField.getText();
        int gameMode = gameModeComboBox.getSelectionModel().getSelectedIndex();
        int maxPlayers = Integer.parseInt(maxPlayersTextField.getText());
        int entranceLevel = Integer.parseInt(minEntranceLevelTextField.getText());
        int roomType = roomTypeCheckbox.isSelected() ? 1 : 0;
        int boardSize = (int) toggleGroupBoardSize.getSelectedToggle().getUserData();
        JSONObject roomJSON = new JSONObject();
        roomJSON.put("requestType", "createRoom");
        roomJSON.put("name", roomName);
        roomJSON.put("gameMode", gameMode);
        roomJSON.put("maxPlayers", maxPlayers);
        roomJSON.put("entranceLevel", entranceLevel);
        roomJSON.put("roomType", roomType);
        roomJSON.put("boardSize", boardSize);
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
