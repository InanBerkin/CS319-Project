package client.GameModes.MPGameModes.RaceMode;

import client.GameModes.GameInstance;
import client.GameModels.*;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class RaceModeController extends GameInstance {

    @FXML
    private HBox playersbar;

    @Override
    public void initializeGameMode() {
        gameTimer.startTimer();
        board = new GameBoard(gridDimension, null);
        pattern = new Pattern(gridDimension,null);
    }
}
