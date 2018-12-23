package client.GameModes.SPGameModes.TimeAttackMode;


import client.GameModes.GameInstance;
import client.GameModels.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TimeAttackModeController extends GameInstance {

    @FXML
    private Label gameStatusLabel;


    @Override
    public void initializeGameMode() {
        gameTimer.startTimer();
        board = new GameBoard(gridDimension, null,null);
        pattern = new Pattern(gridDimension);
    }

    @Override
    public boolean submit(){
        boolean isPatternTrue = super.submit();
        if(isPatternTrue){
            gameStatusLabel.setStyle("-fx-text-fill: #43d873");
            gameStatusLabel.setText("You solved the pattern!");
            gameTimer.stopTimer();
        }
        else{
            gameStatusLabel.setStyle("-fx-text-fill: #FF1654");
            gameStatusLabel.setText("Wrong Pattern");
        }
        return isPatternTrue;
    }
}