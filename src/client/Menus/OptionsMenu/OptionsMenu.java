package client.Menus.OptionsMenu;

import client.Settings;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.Serializable;
import java.nio.file.Paths;

public class OptionsMenu implements Serializable {

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
        musicUrl = "TODO";

//        music = new Media(Paths.get(musicUrl).toUri().toString());
//        musicPlayer = new MediaPlayer(music);

    }

    public void updateSettings() {
        Settings.setSelectLeft(selectLeft);
        Settings.setSelectRight(selectRight);
        Settings.setUp(up);
        Settings.setDown(down);
        Settings.setLeft(left);
        Settings.setRight(right);
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

    public KeyCode getSelectLeft() {
        return selectLeft;
    }

    public void setSelectLeft(KeyCode selectLeft) {
        this.selectLeft = selectLeft;
    }

    public KeyCode getSelectRight() {
        return selectRight;
    }

    public void setSelectRight(KeyCode selectRight) {
        this.selectRight = selectRight;
    }

    public KeyCode getUp() {
        return up;
    }

    public void setUp(KeyCode up) {
        this.up = up;
    }

    public KeyCode getDown() {
        return down;
    }

    public void setDown(KeyCode down) {
        this.down = down;
    }

    public KeyCode getLeft() {
        return left;
    }

    public void setLeft(KeyCode left) {
        this.left = left;
    }

    public KeyCode getRight() {
        return right;
    }

    public void setRight(KeyCode right) {
        this.right = right;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public Media getMusic() {
        return music;
    }

    public void setMusic(Media music) {
        this.music = music;
    }

    public MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public void setMusicPlayer(MediaPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public int getMusicLevel() {
        return musicLevel;
    }

    public void setMusicLevel(int musicLevel) {
        this.musicLevel = musicLevel;
    }
}
