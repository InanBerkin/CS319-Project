package client.GameModes.MPGameModes.RaceMode;

import client.GameModes.GameInstance;
import client.GameModels.*;
import client.Menus.RoomLobbyMenu.RoomLobbyMenuController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

public class RaceModeController extends GameInstance {

    @FXML
    private HBox playersbar;
    private int[][] generatedArray;

    @Override
    public void initializeGameMode() {
        gameTimer.startTimer();
        board = new GameBoard(gridDimension, null);
        pattern = new Pattern(gridDimension,generatedArray);
        patternGroup = pattern.createPatternGroup();
    }

    private void addPlayers(JSONArray playersList){
        playersbar.getChildren().clear();
        int players = playersList.length();
        Player player;
        VBox vBox;
        int id;
        int level;
        String name;
        for (int i = 0; i < players; i++){
//            JSONObject playerJSON = (JSONObject) playersList.get(i);
//            id = playerJSON.getInt("id");
//            level = playerJSON.getInt("level");
//            name = playerJSON.getString("name");
//            player = new Player(id,level,name);
//            vBox = new VBox();
//            vBox.setAlignment(Pos.CENTER_LEFT);
//            vBox.getChildren().add(new Label(player.getName()));
//            playersbar.add(,i/4 ,hBox i);
        }
    }

    private class Player{
        private int id;
        private String name;
        private String time;

        public Player(int id, int level, String name, String time) {
            this.id = id;
            this.name = name;
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public String getTime() {
            return time;
        }

        public String getName() {
            return name;
        }
    }
}
