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


    @FXML
    private RadioButton gameModeRace;
    @FXML
    private RadioButton gameModeElim;
    @FXML
    private RadioButton gameModeImageRec;

    private ToggleGroup toggleGroupBoardSize;
    private ToggleGroup toggleGroupGameMode;

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
        initializeBoardSizeRadioGroup();
        initializeGameModeRadioGroup();
    }


    private void initializeGameModeRadioGroup(){
        toggleGroupGameMode = new ToggleGroup();
        gameModeRace.setToggleGroup(toggleGroupGameMode);
        gameModeRace.setUserData("0");
        gameModeElim.setToggleGroup(toggleGroupGameMode);
        gameModeElim.setUserData("1");
        gameModeImageRec.setToggleGroup(toggleGroupGameMode);
        gameModeImageRec.setUserData("2");
        boardSize3.setSelected(true);
    }


    private void initializeBoardSizeRadioGroup(){
        toggleGroupBoardSize = new ToggleGroup();
        boardSize3.setToggleGroup(toggleGroupBoardSize);
        boardSize3.setUserData("3");
        boardSize4.setToggleGroup(toggleGroupBoardSize);
        boardSize4.setUserData("4");
        boardSize5.setToggleGroup(toggleGroupBoardSize);
        boardSize5.setUserData("5");
        boardSize3.setSelected(true);
    }

    @FXML
    private void sendRoomData(){
        String roomName = roomNameTextField.getText();
        int gameMode = Integer.parseInt(toggleGroupGameMode.getSelectedToggle().getUserData().toString());
        int maxPlayers = Integer.parseInt(maxPlayersTextField.getText());
        int entranceLevel = Integer.parseInt(minEntranceLevelTextField.getText());
        int roomType = roomTypeCheckbox.isSelected() ? 1 : 0;
        int boardSize = Integer.parseInt(toggleGroupBoardSize.getSelectedToggle().getUserData().toString());
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
