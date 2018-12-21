package client.GameModes.SPGameModes.TimeAttackMode;


import client.GameModes.GameInstance;
import client.GameModels.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TimeAttackModeController extends GameInstance {



    @Override
    public void initializeGameMode() {
        gameTimer.startTimer();
        board = new GameBoard(gridDimension, null);
        pattern = new Pattern(gridDimension);
    }

    @Override
    public boolean submit(){
        boolean isPatternTrue = super.submit();
        System.out.println("Is pattern true: " + isPatternTrue);

        return isPatternTrue;
    }
}