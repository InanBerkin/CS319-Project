package client.Menus.RoomLobbyMenu;

import client.Menus.MenuController;
import client.QBitzApplication;
import client.UserConfiguration;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomLobbyMenuController extends MenuController {

    @FXML
    private GridPane playersGridPane;

    @FXML
    private Label roomName;

    @FXML
    private Button startButton;

    @FXML
    private Label roomCodeText;

    private int ownerID;

    private JSONArray userList;


    @Override
    public void onMessageReceived(String message) {
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("userAnnouncement")){
            Platform.runLater(() -> {
                addPlayers(responseJSON.getJSONArray("userList"));
                if (responseJSON.getBoolean("isStartable") || payload.getBoolean("isStartable")){
                    startButton.setDisable(false);
                }
                else if(responseJSON.getBoolean("isStartable")){
                    startButton.setDisable(true);
                }
            });
        }
        else if(responseJSON.getString("responseType").equals("startCounter")){
            Platform.runLater(() -> {
                roomName.setText("Game Starts in " + Integer.toString(responseJSON.getInt("count")));
            });
        }
        else if(responseJSON.getString("responseType").equals("changeOwner")){
            Platform.runLater(() -> {
                ownerID = responseJSON.getInt("ownerID");
                startButton.setVisible(ownerID == UserConfiguration.userID);
            });
        }
        else if(responseJSON.getString("responseType").equals("interruptCounter")){
            Platform.runLater(() -> {
                roomName.setText((payload.getString("name")));
                startButton.setDisable(true);
            });
        }
        else if(responseJSON.getString("responseType").equals("startGame")){
            Platform.runLater(() -> {
                JSONObject gamePayload = new JSONObject();
                gamePayload.put("boardSize", responseJSON.getInt("boardSize"));
                gamePayload.put("patternMatrix", responseJSON.getJSONArray("patternMatrix"));
                gamePayload.put("roomID", payload.getInt("roomID"));
                gamePayload.put("userList", responseJSON.getJSONArray("userList"));
                if(responseJSON.getInt("gameMode") == 0)
                    QBitzApplication.getSceneController().gotoGameMode(false, "RaceMode", gamePayload);
                else if(responseJSON.getInt("gameMode") == 2) {
                    gamePayload.put("encodedImage", responseJSON.getString("encodedImage"));
                    QBitzApplication.getSceneController().gotoGameMode(false, "MultiplayerImageRecreationMode", gamePayload);
                }
                else if(responseJSON.getInt("gameMode") == 3) {
                    QBitzApplication.getSceneController().gotoGameMode(false, "EliminationMode", gamePayload);
                }
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() ->{
            ownerID = payload.getInt("ownerID");
            addPlayers(payload.getJSONArray("userList"));
            roomName.setText(payload.getString("name"));
            startButton.setVisible(ownerID == UserConfiguration.userID);
            roomCodeText.setText(payload.getString("roomCode"));
        });
    }

    private void addPlayers(JSONArray playersList){
        playersGridPane.getChildren().clear();
        int players = playersList.length();
        Player player;
        HBox hBox;
        int id;
        int level;
        String name;
        for (int i = 0; i < players; i++){
            JSONObject playerJSON = (JSONObject) playersList.get(i);
            id = playerJSON.getInt("id");
            level = playerJSON.getInt("level");
            name = playerJSON.getString("name");
            player = new Player(id,level,name);
            hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getChildren().add(new Label(player.getName()));
            if(ownerID == player.getId()){
                hBox.setStyle("-fx-background-color: #eeff25;");
            }
            else {
                hBox.setStyle("-fx-background-color: #fff;");
            }

            playersGridPane.add(hBox,i/4 , i);
        }
    }

    @FXML
    private void sendStartRequest(){
        JSONObject startJSON = new JSONObject();
        startJSON.put("requestType", "startCounter");
        startJSON.put("roomID", payload.getInt("roomID"));
        QBitzApplication.getSceneController().sendMessageToServer(startJSON);
    }

    @FXML
    private void goBack(){
        JSONObject quitJSON = new JSONObject();
        quitJSON.put("requestType", "exitRoom");
        quitJSON.put("roomID", payload.getInt("roomID"));
        QBitzApplication.getSceneController().sendMessageToServer(quitJSON);
        QBitzApplication.getSceneController().gotoMenu("RoomMenu");
    }

    private class Player{
        private int id;
        private int level;
        private String name;

        public Player(int id, int level, String name) {
            this.id = id;
            this.level = level;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public int getLevel() {
            return level;
        }

        public String getName() {
            return name;
        }
    }
}
