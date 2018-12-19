package client.OptionsMenu;

import client.MenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

public class OptionsMenuController extends MenuController {

    OptionsMenu options;

    @FXML
    private Slider soundSlider;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initModel();
    }

    private void initModel() {
        options = new OptionsMenu();
    }

    @FXML
    private void applyOptions(){
        System.out.println(soundSlider.getValue());
    }

    public void playMusic() {
        options.startMusic();
    }

    @Override
    public void onMessageReceived(String message) {

    }
}
