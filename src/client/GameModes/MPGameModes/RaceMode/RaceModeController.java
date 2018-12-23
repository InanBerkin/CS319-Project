package client.GameModes.MPGameModes.RaceMode;

import client.GameModes.GameInstance;
import client.GameModels.*;
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

/**
 * This class is the controller of the race mode of the game
 * @author Halil Şahiner, Berkin İnan
 */
public class RaceModeController extends GameInstance {

    @FXML
    private HBox playersBar;

    @FXML
    private Button submitButton;


    @Override
    public void onMessageReceived(String message) {
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("submit")){
            Platform.runLater(() -> {
                updatePlayers(responseJSON.getJSONArray("finishList"));
                if(responseJSON.getBoolean("isGameFinished")){
                    QBitzApplication.getSceneController().gotoMenu("PostGameMenu", responseJSON);
                }
            });
        }
    }

    @Override
    public void initializeGameMode() {
        gameTimer.startTimer();
        board = new GameBoard(gridDimension, null,null);
        pattern = new Pattern(gridDimension);
        pattern.setGivenPattern(jsonArrayToMatrix(payload.getJSONArray("patternMatrix"), gridDimension));
        updatePlayers(payload.getJSONArray("userList"));
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
            submitButton.setDisable(true);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * This method is update player list of the race mode
     * @param playersList this param is taken to iterate through the player list and change the necessary functionality
     *                    for some users
     */
    private void updatePlayers(JSONArray playersList){
        playersBar.getChildren().clear();
        int players = playersList.length();
        Player player;
        VBox vBox;
        int id;
        String finishTime = "Solving...";
        int rank = 0;
        String name;
        for (int i = 0; i < players; i++){
            JSONObject playerJSON = (JSONObject) playersList.get(i);
            id = playerJSON.getInt("id");
            name = playerJSON.getString("name");
            if(playerJSON.has("finishTime")){
                finishTime = playerJSON.getString("finishTime");
            }
            if(playerJSON.has("rank")){
                rank = playerJSON.getInt("rank");
            }
            player = new Player(id,name, finishTime, rank);
            vBox = new VBox(20);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(new Label(player.getName()));
            vBox.getChildren().add(new Label(player.getFinishTime()));
            if(player.getRank() != 0){
                vBox.getChildren().add(new Label(player.getRank() + ""));
            }
            vBox.setStyle("-fx-background-color: #000");
            vBox.setStyle("-fx-background-radius: 5");
            vBox.setStyle("-fx-padding: 20 20;");
            playersBar.getChildren().add(vBox);
        }
    }

    /**
     * This inner class is created to handle the players status for the elimination mode
     */
    private class Player{
        private int id;
        private String name;
        private String finishTime;
        private int rank;
        /**
         * This constructor creates the Player object to represent and store the information of the player for the users
         * in the game
         * @param id Player id
         * @param name Player name
         * @param finishTime Player finish time for the rounds
         * @param rank Player rank for the round
         */
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
