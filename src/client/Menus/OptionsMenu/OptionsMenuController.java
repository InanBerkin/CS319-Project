package client.Menus.OptionsMenu;

import client.CubeFaces;
import client.Menus.MenuController;
import client.Settings;
import client.SoundController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OptionsMenuController extends MenuController {

    OptionsMenu options;
    String homeDir = System.getProperty("user.home");
    String folderPath = homeDir + File.separator + "qbitz_configs";
    boolean leftBtn;
    boolean rightBtn;

    // Temporary options
    KeyCode selectLeft;
    KeyCode selectRight;

    KeyCode up;
    KeyCode down;
    KeyCode left;
    KeyCode right;

    @FXML
    private Slider soundSlider;

    @FXML
    private Button cycleLeft;

    @FXML
    private Button cycleRight;

    @FXML
    private ComboBox rotationComboBox;

    @FXML
    private Slider colorSlider;

    @FXML
    private ImageView faceView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        leftBtn = false;
        rightBtn = false;

        colorSlider.setValue(CubeFaces.colorOffset);
        faceView.setImage(CubeFaces.getImageAt(1));

        rotationComboBox.getItems().addAll(
                "WASD", "Arrow Keys"
        );
        rotationComboBox.getSelectionModel().selectFirst();

        colorSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                CubeFaces.changeColor(colorSlider.getValue());
                faceView.setImage(CubeFaces.getImageAt(1));

            }
        }

        );

        soundSlider.setValue(SoundController.getSliderVolume());
        soundSlider.valueProperty().addListener(new ChangeListener<Number>() {
                                                    @Override
                                                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                                        SoundController.setVolume(soundSlider.getValue());

                                                    }
                                                }

        );



        initModel();
    }

    private void initModel() {
        options = loadOptions();

        options.updateSettings();


        System.out.println(Settings.getUp());
        if(Settings.getUp() == KeyCode.UP) {
            System.out.println("Here");
            rotationComboBox.getSelectionModel().select(1);

        }
        else {
            rotationComboBox.getSelectionModel().select(0);
        }
    }

    @FXML
    private void setLeftCycleBtn(ActionEvent e) {


        cycleLeft.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCode().getName());
                selectLeft = event.getCode();
                cycleLeft.removeEventHandler(KeyEvent.KEY_PRESSED, this);

            }
        });

    }

    @FXML
    private void setRightCycleBtn(ActionEvent e) {


        cycleRight.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCode().getName());
                selectRight = event.getCode();
                cycleRight.removeEventHandler(KeyEvent.KEY_PRESSED, this);

            }
        });

    }

    public  void handleKey(KeyEvent event) {
        System.out.println(event.getCode().getName());
    }
    @FXML
    private void applyOptions(){

        int comboIndex = rotationComboBox.getSelectionModel().getSelectedIndex();
        if(comboIndex == 0) {
            up = KeyCode.W;
            down = KeyCode.S;
            left = KeyCode.A;
            right = KeyCode.D;
        }
        else {
            up = KeyCode.UP;
            down = KeyCode.DOWN;
            left = KeyCode.LEFT;
            right = KeyCode.RIGHT;
        }

        options.setDown(down);
        options.setUp(up);
        options.setRight(right);
        options.setLeft(left);
        options.setSelectLeft(selectLeft);
        options.setSelectRight(selectRight);

        saveOptions(options);
        options.updateSettings();

        CubeFaces.saveFaces();
        CubeFaces.loadFaces();
        System.out.println(soundSlider.getValue());
    }

    public void playMusic() {
        options.startMusic();
    }

    @Override
    public void onMessageReceived(String message) {

    }

    public void saveOptions(OptionsMenu opts) {
        try {

            // write object to file
            FileOutputStream fos = new FileOutputStream(folderPath + File.separator + "opts.qbitz");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(opts);
            oos.close();

            //System.out.println("One:" + result.getOne() + ", Two:" + result.getTwo());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OptionsMenu loadOptions() {

        FileInputStream fis = null;
        try {
            // read object from file
            fis = new FileInputStream(folderPath + File.separator + "opts.qbitz");
            ObjectInputStream ois = new ObjectInputStream(fis);
            OptionsMenu result = (OptionsMenu) ois.readObject();
            ois.close();

            return result;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OptionsMenuController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OptionsMenuController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OptionsMenuController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(OptionsMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;

    }
}
