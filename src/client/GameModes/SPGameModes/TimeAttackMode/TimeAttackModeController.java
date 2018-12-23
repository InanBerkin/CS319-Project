package client.GameModes.SPGameModes.TimeAttackMode;


import client.GameModes.GameInstance;
import client.GameModels.*;
import javafx.fxml.FXML;
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
        if( isPatternTrue) {
            this.gameStatusLabel.setText("You solved the pattern!");
            this.gameTimer.stopTimer();
        }
        else {
            this.gameStatusLabel.setText("The pattern on the board is incorrect. Think Again!");
        }
        return isPatternTrue;
    }
}