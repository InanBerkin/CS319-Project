package client.GameModes.SPGameModes.MemoryMode;

import client.GameModes.GameInstance;
import client.Menus.MenuController;
import client.GameModels.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.transform.Rotate;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;

public class MemoryModeController extends GameInstance {

    //**********************FOR_MEMORY************************************
    private int memoryTime;

    @FXML
    private Button startButton;

    @FXML
    private Label memoryLabel;

    @FXML
    private VBox vBox;


    @FXML
    private Label timerLabel;


    @Override
    public void initializeGameMode() {
        //**********************FOR_MEMORY************************************
        this.memoryTime = memoryTime(gridDimension);
        board = new GameBoard(gridDimension, null);
        pattern = new Pattern(gridDimension,null);
        try {
            pattern.setMatQuestMark();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleKeys(VBox vBox) {

    }
    //**********************FOR_MEMORY************************************
    @Override
    public void timerStopped() {
        super.handleKeys(vBox);

        try {
            pattern.setMatQuestMark();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.memoryLabel.setText("You could start to solve");
        gameTimer = new GameTimer(this, timerLabel);
        gameTimer.startTimer();
    }

    public int memoryTime(int dimension) {
        if (dimension == 3)
            return 5;
        else if (dimension == 4 )
            return 15;
        else
            return 30;
    }
    @FXML
    public boolean startShowPattern(){
        this.memoryLabel.setText("Memorize in "+this.memoryTime+ " seconds");
        this.memoryLabel.setVisible(true);
        this.gameTimer.startTimer(this.memoryTime);
        this.pattern.showPattern();
        this.startButton.setVisible(false);

        return true;
    }
    @FXML
    public boolean submitCreatedPattern(){
        boolean isPatternTrue = pattern.checkPattern(board.getBoardImageViews());

        if( isPatternTrue) {
            this.memoryLabel.setText("You solved goddammit!!!");
            this.gameTimer.stopTimer();
            this.pattern.showPattern();
            System.out.println("Is pattern true: " + isPatternTrue);
        }
        else {
            this.memoryLabel.setText("You cannot solve it. Think Again");
        }



        return isPatternTrue;
    }
}
