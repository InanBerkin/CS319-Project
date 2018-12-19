package client;

public class OptionsController {

    Options options;

    public OptionsController() {
        options = new Options();
    }

    public void playMusic() {
        options.startMusic();
    }
}
