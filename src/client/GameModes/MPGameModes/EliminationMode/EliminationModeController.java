package client.GameModes.MPGameModes.EliminationMode;

import client.GameModels.GameBoard;
import client.GameModels.Pattern;
import client.GameModes.GameInstance;
import client.QBitzApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

public class EliminationModeController extends GameInstance {

    @FXML
    private HBox playersBar;

    @Override
    public void onMessageReceived(String message) {
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("submit")){
            Platform.runLater(() -> {
                updatePlayers(responseJSON.getJSONArray("finishList"));
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
        playersBar.getChildren().clear();
        int players = playersList.length();
        EliminationModeController.Player player;
        VBox vBox;
        int id;
        String name;
        for (int i = 0; i < players; i++){
            JSONObject playerJSON = (JSONObject) playersList.get(i);
            id = playerJSON.getInt("id");
            name = playerJSON.getString("name");
            player = new EliminationModeController.Player(id,name);
            vBox = new VBox(20);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(new Label(player.getName()));
            vBox.setStyle("-fx-background-color: #000");
            playersBar.getChildren().add(vBox);
        }
    }

    private void removePlayer(JSONObject playerToRemove){

    }

    private void updatePlayers(JSONArray playersList){
        playersBar.getChildren().clear();
        int players = playersList.length();
        client.GameModes.MPGameModes.EliminationMode.EliminationModeController.Player player;
        VBox vBox;
        int id;
        String finishTime;
        int rank;
        String name;
        for (int i = 0; i < players; i++){
            JSONObject playerJSON = (JSONObject) playersList.get(i);
            id = playerJSON.getInt("id");
            name = playerJSON.getString("name");
            finishTime = playerJSON.getString("finishTime");
            rank = playerJSON.getInt("rank");
            player = new client.GameModes.MPGameModes.EliminationMode.EliminationModeController.Player(id,name, finishTime, rank);
            vBox = new VBox(20);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(new Label(player.getName()));
            vBox.getChildren().add(new Label(player.getFinishTime()));
            vBox.getChildren().add(new Label(player.getRank() + ""));
            vBox.setStyle("-fx-background-color: #000");
            playersBar.getChildren().add(vBox);
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
