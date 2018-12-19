package client.RoomLobbyMenu;

import client.MenuController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class RoomLobbyMenuController extends MenuController {
    @FXML
    private GridPane playersGridPane;

    @Override
    public void onMessageReceived(String message) {
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("joinAnnouncement")){
            Platform.runLater(() -> {
                addPlayers(responseJSON.getJSONArray("usersList"), responseJSON.getInt("players"));
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() ->{
            addPlayers(payload.getJSONArray("userList"), payload.getInt("players"));
        });
    }

    private void addPlayers(JSONArray playersList, int players){
        Player player;
        int id;
        int level;
        String name;

        for (int i = 0; i < players; i++){
            JSONObject playerJSON = (JSONObject) playersList.get(i);
            id = playerJSON.getInt("id");
            level = playerJSON.getInt("level");
            name = playerJSON.getString("name");
            player = new Player(id,level,name);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getChildren().add(new Label(player.getName()));
            System.out.println(i/4 + "  " + i);
            playersGridPane.add(hBox,i/4 , i);
        }

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
