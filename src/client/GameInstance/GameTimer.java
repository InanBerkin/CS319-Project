package client.GameInstance;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

import javafx.scene.control.Label;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {
    private SimpleDateFormat simpleDateFormat;
    private SimpleStringProperty gameTime;
    private long time;
    private Timer timer;
    private TimerTask timerTask;
    private boolean isTiming;
    private Label gameLabel;

    public GameTimer() {
        this.simpleDateFormat = new SimpleDateFormat("mm:ss:S");
        this.gameTime = new SimpleStringProperty("00:00:00");
        this.time = 0;
        this.timer = new Timer("Metronome", true);
        this.timerTask = null;
        this.isTiming = false;
        this.gameLabel = null;
    }

    public void setGameLabel(Label label) {
        this.gameLabel = label;
    }

    public void startTimer(final long time) {
        this.time = time;
        isTiming = true;

        timerTask = new TimerTask() {

            @Override
            public void run() {
                if (!isTiming) {
                    try {
                        timerTask.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Platform.runLater(() -> {
                        gameLabel.setText(getGameTime().getValue());
                    });
                    updateTime();
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, 10, 10);
    }

    public synchronized void stopTimer() {
        isTiming = false;
    }

    public synchronized void updateTime() {
        this.time = this.time + 10;
        String[] split = simpleDateFormat.format(new Date(this.time)).split(":");
        gameTime.set(split[0] + ":" + split[1] + ":" + (split[2].length() == 1 ? "0" + split[2] : split[2].substring(0, 2)));
    }

    public synchronized void moveToTime(long time) {
        stopTimer();
        this.time = time;
        String[] split = simpleDateFormat.format(new Date(time)).split(":");
        gameTime.set(split[0] + ":" + split[1] + ":" + (split[2].length() == 1 ? "0" + split[2] : split[2].substring(0, 2)));
    }

    public synchronized long getTime() {
        return time;
    }

    public synchronized SimpleStringProperty getGameTime() {
        return gameTime;
    }


}
