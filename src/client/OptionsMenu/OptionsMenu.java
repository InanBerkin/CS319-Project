package client.OptionsMenu;

import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class OptionsMenu {

    KeyCode selectLeft;
    KeyCode selectRight;

    KeyCode up;
    KeyCode down;
    KeyCode left;
    KeyCode right;
    String musicUrl;
    Media music;
    MediaPlayer musicPlayer;
    int musicLevel = 0;

    int cubeColor = 0;

    public OptionsMenu() {
        initDefaultOptions();
    }


    public void initDefaultOptions() {
        selectLeft = KeyCode.Q;
        selectRight = KeyCode.E;
        up = KeyCode.W;
        down = KeyCode.S;
        left = KeyCode.A;
        right = KeyCode.D;

        musicLevel = 0;
        cubeColor = 0;
        musicUrl = "TODO";
        //music = new Media(Paths.get(musicUrl).toUri().toString());
        //musicPlayer = new MediaPlayer(music);

    }


    public void startMusic() {
        System.out.println("Music Playing");
        musicPlayer.stop();
        musicPlayer.play();
        System.out.println(musicPlayer.getVolume());
        System.out.println(musicPlayer.getStatus());
        System.out.println("Music test");
    }

    public void pauseMusic() {
        musicPlayer.pause();
    }




}
