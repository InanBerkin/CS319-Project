package client.MemoryMode;

import client.GameInstance.GameTimer;
import client.GameInstance.Pattern;
import client.GameInstance.TimerSignable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Timer;

public class MemoryMode implements TimerSignable {


    private  boolean isPatternVisible;
    private GameTimer timer;
    private Pattern pattern;
    private int memoryTime;





    public MemoryMode(int gridDimension, GameTimer timer, Pattern pattern ){


        this.pattern = pattern;
        this.timer = timer;
        this.timer.setCallback(this);
        this.isPatternVisible = false;
        this.memoryTime = memoryTime(gridDimension);



    }



    @Override
    public void timerStopped() {
        this.isPatternVisible = false;
        this.pattern.setMatQuestMark();
        this.timer.startTimer();
    }


    public int memoryTime(int dimension ){

        if (dimension == 3)
            return 5;
        else if (dimension == 4 ) {
            return 15;
        }
        else
            return 30;

    }

    public boolean startShowPattern(){
        this.timer.startTimer(this.memoryTime);
        this.isPatternVisible = true;
        return true;

    }


    public boolean isPatternShown() {
        return isPatternVisible;
    }

    public void setPatternShown(boolean patternShown) {
        isPatternVisible = patternShown;
    }

    public GameTimer getTimer() {
        return timer;
    }

    public void setTimer(GameTimer timer) {
        this.timer = timer;
    }
}
