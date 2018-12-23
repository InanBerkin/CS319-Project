package client.GameModes.MPGameModes.EliminationMode;

import client.GameModels.GameBoard;
import client.GameModels.Pattern;
import client.GameModes.GameInstance;
import client.QBitzApplication;
import client.UserConfiguration;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

public class EliminationModeController extends GameInstance {

    @FXML
    private HBox playersBar;

    @FXML
    private Label gameStatusLabel;

    @FXML
    private Button submitButton;

    @Override
    public void onMessageReceived(String message) {
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("submit")){
            Platform.runLater(() -> {
                updatePlayers(responseJSON.getJSONArray("finishList"));
                if (responseJSON.getBoolean("isGameFinished")) {
                    System.out.println("Game finished");
                    QBitzApplication.getSceneController().gotoMenu("PostGameMenu", responseJSON);
                    return;
                }
                if(responseJSON.getBoolean("isRoundFinished")){
                    QBitzApplication.getSceneController().gotoGameMode(false, "EliminationMode", responseJSON);
                }
            });
        }
    }

    @Override
    public void initializeGameMode() {
        gameTimer.startTimer();
        board = new GameBoard(gridDimension, null, null);
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
            gameStatusLabel.setStyle("-fx-text-fill: #43d873");
            gameStatusLabel.setText("You solved the pattern!");
            submitButton.setDisable(true);
        }
        else{
            gameStatusLabel.setStyle("-fx-text-fill: #FF1654");
            gameStatusLabel.setText("Wrong Pattern");
        }

        return isPatternTrue;
    }


    private void removePlayer(JSONObject playerToRemove){

    }

    private void updatePlayers(JSONArray playersList){
        playersBar.getChildren().clear();
        int players = playersList.length();
        Player player;
        VBox vBox;
        int id;
        boolean isEliminated = false;
        String finishTime;
        int rank = 0;
        String name;
        for (int i = 0; i < players; i++){
            finishTime = "Solving...";
            JSONObject playerJSON = (JSONObject) playersList.get(i);
            id = playerJSON.getInt("id");
            name = playerJSON.getString("name");
            if(playerJSON.has("finishTime")){
                finishTime = playerJSON.getString("finishTime");
            }
            if (playerJSON.has("isEliminated")) {
                isEliminated = playerJSON.getBoolean("isEliminated");
                if (isEliminated) {
                    finishTime = "Eliminated";
                }
            }
            if(playerJSON.has("rank")){
                rank = playerJSON.getInt("rank");
            }
            System.out.println("ID userconfifg: " + UserConfiguration.userID);
            player = new Player(id, name, finishTime, rank, isEliminated);
            System.out.println("ID player: " + player.getId());
            System.out.println("Elim: " + isEliminated);
            if (player.getId() == UserConfiguration.userID && isEliminated) {
                submitButton.setDisable(true);
            }
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

    private class Player{
        private int id;
        private String name;
        private String finishTime;
        private int rank;
        private boolean isEliminated;

        public Player(int id, String name, String finishTime, int rank, boolean isEliminated) {
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

        public boolean isEliminated() {
            return isEliminated;
        }

        public void setEliminated(boolean eliminated) {
            isEliminated = eliminated;
        }
    }
}
