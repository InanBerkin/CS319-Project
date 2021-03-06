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

/**
 * This class is created to simulate the memory mode of the Qbitz game in digital version for single player game
 * author Sait Akturk
 */
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

    @FXML
    public Button submitButton;


    @Override
    public void initializeGameMode() {
        //**********************FOR_MEMORY************************************
        this.memoryTime = memoryTime(gridDimension);
        board = new GameBoard(gridDimension, null,null);
        pattern = new Pattern(gridDimension);

    }

    @Override
    public void setQuestMark() {
        super.setQuestMark();
        pattern.setMatQuestMark();
    }

    @Override
    public void handleKeys(VBox vBox) {

    }
    @Override
    public void timerStopped() {
        super.handleKeys(vBox);

        pattern.setMatQuestMark();

        this.memoryLabel.setText("You could start to solve");
        gameTimer = new GameTimer(this, timerLabel);
        gameTimer.startTimer();
    }

    /**
     * Set the memorization time for the player according to board dimension
     * @param dimension board dimension
     * @return int the memorization time
     */
    public int memoryTime(int dimension) {
        if (dimension == 3)
            return 5;
        else if (dimension == 4 )
            return 15;
        else
            return 30;
    }

    /**
     * This method is start to show pattern to make the player memorize the pattern
     */
    @FXML
    public void startShowPattern(){
        this.memoryLabel.setText("Memorize in "+this.memoryTime+ " seconds");
        this.memoryLabel.setVisible(true);
        this.gameTimer.startTimer(this.memoryTime);
        this.pattern.showPattern();
        this.startButton.setVisible(false);
    }
    @Override
    public boolean submit(){
        boolean isPatternTrue = super.submit();

        if( isPatternTrue) {
            this.memoryLabel.setText("You solved the pattern!");
            this.gameTimer.stopTimer();
            this.pattern.showPattern();
        }
        else {
            this.memoryLabel.setText("Wrong Pattern. Think Again!");
        }
        return isPatternTrue;
    }
}
