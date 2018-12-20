package client;

import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Settings {
    static KeyCode selectLeft;
    static KeyCode selectRight;

    static KeyCode up;
    static KeyCode down;
    static KeyCode left;
    static KeyCode right;
    static String musicUrl;
    static Media music;
    static MediaPlayer musicPlayer;
    static int musicLevel = 0;

    public static KeyCode getSelectLeft() {
        return selectLeft;
    }

    public static void setSelectLeft(KeyCode selectLeft) {
        Settings.selectLeft = selectLeft;
    }

    public static KeyCode getSelectRight() {
        return selectRight;
    }

    public static void setSelectRight(KeyCode selectRight) {
        Settings.selectRight = selectRight;
    }

    public static KeyCode getUp() {
        return up;
    }

    public static void setUp(KeyCode up) {
        Settings.up = up;
    }

    public static KeyCode getDown() {
        return down;
    }

    public static void setDown(KeyCode down) {
        Settings.down = down;
    }

    public static KeyCode getLeft() {
        return left;
    }

    public static void setLeft(KeyCode left) {
        Settings.left = left;
    }

    public static KeyCode getRight() {
        return right;
    }

    public static void setRight(KeyCode right) {
        Settings.right = right;
    }

    public static String getMusicUrl() {
        return musicUrl;
    }

    public static void setMusicUrl(String musicUrl) {
        Settings.musicUrl = musicUrl;
    }

    public static Media getMusic() {
        return music;
    }

    public static void setMusic(Media music) {
        Settings.music = music;
    }

    public static MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public static void setMusicPlayer(MediaPlayer musicPlayer) {
        Settings.musicPlayer = musicPlayer;
    }

    public static int getMusicLevel() {
        return musicLevel;
    }

    public static void setMusicLevel(int musicLevel) {
        Settings.musicLevel = musicLevel;
    }
}
