package client.Menus.PostGameMenu;

import client.GameModes.MPGameModes.RaceMode.RaceModeController;
import client.Menus.MenuController;
import client.QBitzApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class PostGameMenuController extends MenuController {

    @FXML
    private VBox playerRankings;

    @FXML
    private Button gotolobby;

    @Override
    public void onMessageReceived(String message) {
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("backToLobby")){
            Platform.runLater(() -> {
                responseJSON.put("roomCode", "");
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    public void run() {
                        Platform.runLater(() -> {
                            QBitzApplication.getSceneController().gotoMenu("RoomLobbyMenu", responseJSON);
                        });

                    }

                };
                gotolobby.setDisable(true);
                timer.schedule(task, 5000l);
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Platform.runLater(() -> {
                if (payload.getInt("gameMode") == 1)
                    updatePlayers(payload.getJSONArray("userList"));
                else
                    updatePlayers(payload.getJSONArray("finishList"));
            });
        } catch (Exception e) {
            System.out.println("Null geldi  bence");
        }
    }

    @FXML
    public void gotoLobby(){
        JSONObject lobbyJSON = new JSONObject();
        lobbyJSON.put("requestType", "backToLobby");
        lobbyJSON.put("roomID", payload.getInt("roomID"));
        QBitzApplication.getSceneController().sendMessageToServer(lobbyJSON);
    }

    private void updatePlayers(JSONArray playersList){
        playerRankings.getChildren().clear();
        int players = playersList.length();
        Player player;
        HBox hBox;
        int id = -1;
        int rank = 0;
        String name = "";
        for (int i = 0; i < players; i++){
            JSONObject playerJSON = (JSONObject) playersList.get(i);
            id = playerJSON.getInt("id");
            name = playerJSON.getString("name");
            rank = playerJSON.getInt("rank");
            player = new Player(id, name, "", rank);
            hBox = new HBox(20);
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().add(new Label(player.getRank() + ""));
            hBox.getChildren().add(new Label(player.getName()));
            if (playerJSON.has("gatheredPoints")) {
                hBox.getChildren().add(new Label(playerJSON.getInt("gatheredPoints") + ""));
            }
            hBox.setStyle("-fx-background-color: #000");
            hBox.setStyle("-fx-background-radius: 5");
            hBox.setStyle("-fx-padding: 20 20;");
            playerRankings.getChildren().add(hBox);
        }

    }

    private class Player{
        private int id;
        private String name;
        private String finishTime;
        private int rank;

        public Player(int id, String name, String finishTime, int rank) {
            this.id = id;
            this.name = name;
            this.finishTime = finishTime;
            this.rank = rank;
        }

        public Player(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getFinishTime() {
            return finishTime;
        }

        public String getName() {
            return name;
        }

        public void setFinishTime(String finishTime) {
            this.finishTime = finishTime;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }

}
