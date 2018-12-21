package client.GameModes.MPGameModes.RaceMode;

import client.GameModes.GameInstance;
import client.GameModels.*;
import client.Menus.RoomLobbyMenu.RoomLobbyMenuController;
import client.QBitzApplication;
import javafx.application.Platform;
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
    public void onMessageReceived(String message) {
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("submit")){
            Platform.runLater(() -> {

            });
        }
    }

    @Override
    public void initializeGameMode() {
        gameTimer.startTimer();
        board = new GameBoard(gridDimension, null);
        pattern = new Pattern(gridDimension);
        pattern.setGivenPattern(jsonArrayToMatrix(payload.getJSONArray("patternMatrix"), gridDimension));
        addPlayers(payload.getJSONArray("userList"));
    }

    @Override
    public boolean submit(){
        boolean isPatternTrue = super.submit();
        if(isPatternTrue){
            JSONObject submitJSON = new JSONObject();
            submitJSON.put("requestType", "submit");
            submitJSON.put("roomID", payload.getInt("roomID"));
            submitJSON.put("finishTime", gameTimer.getGameTime().getValue());
            QBitzApplication.getSceneController().sendMessageToServer(submitJSON);
            return true;
        }
        else{
            return false;
        }
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
            JSONObject playerJSON = (JSONObject) playersList.get(i);
            id = playerJSON.getInt("id");
            name = playerJSON.getString("name");
            player = new Player(id,name);
            vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(new Label(player.getName()));
            playersbar.getChildren().add(vBox);
        }
    }

    private class Player{
        private int id;
        private String name;
        private String time;

        public Player(int id, String name) {
            this.id = id;
            this.name = name;
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

        public void setTime(String time) {
            this.time = time;
        }
    }
}
