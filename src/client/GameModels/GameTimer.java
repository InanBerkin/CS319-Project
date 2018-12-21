package client.GameModels;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameTimer {

    private static final boolean FORWARD = true;
    private static final boolean BACKWARD = false;

    private TimerSignable callback;

    private SimpleDateFormat simpleDateFormat;
    private SimpleStringProperty gameTime;
    private long time;
    private Timeline timeline;
    private Label gameLabel;

    public GameTimer(TimerSignable callback, Label gameLabel) {
        this.simpleDateFormat = new SimpleDateFormat("mm:ss:S");
        this.gameTime = new SimpleStringProperty("00:00:00");
        this.time = 0;
        this.timeline = new Timeline();
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.gameLabel = null;
        this.callback = callback;
        this.gameLabel = gameLabel;
    }

    public void startTimer() {
        this.time = 0;
        System.out.println("started.");
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        Platform.runLater(() -> {
                            gameLabel.setText(getGameTime().getValue());
                        });
                        updateTime(FORWARD);
                    }
                }));
        timeline.play();
    }

    public void startTimer(long time) {
        moveToTime(time * 1000);

        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        Platform.runLater(() -> {
                            gameLabel.setText(getGameTime().getValue());
                        });

                        if (!isZero()) {
                            updateTime(BACKWARD);
                        }
                        else {
                            try {
                                callback.timerStopped();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            stopTimer();
                        }
                    }
                }));
        timeline.play();
    }

    private void updateTime(boolean isForward) {
        if (isForward)
            this.time = this.time + 10;
        else
            this.time = this.time - 10;

        String[] split = simpleDateFormat.format(new Date(this.time)).split(":");
        gameTime.set(split[0] + ":" + split[1] + ":" + (split[2].length() == 1 ? "0" + split[2] : split[2].substring(0, 2)));
    }

    private void moveToTime(long time) {
        stopTimer();
        this.time = time;
        String[] split = simpleDateFormat.format(new Date(time)).split(":");
        gameTime.set(split[0] + ":" + split[1] + ":" + (split[2].length() == 1 ? "0" + split[2] : split[2].substring(0, 2)));
    }

    public void stopTimer() {
        timeline.stop();
    }

    private boolean isZero(){
        return time <= 0;
    }

    public SimpleStringProperty getGameTime() {
        return gameTime;
    }

    public long getTime() {
        return time / 1000;
    }
}