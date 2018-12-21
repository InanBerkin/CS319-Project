package client.Menus.OptionsMenu;

import client.Menus.MenuController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
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

    @FXML
    private Slider soundSlider;

    @FXML
    private Button cycleLeft;

    @FXML
    private Button cycleRight;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initModel();
        leftBtn = false;
        rightBtn = false;
    }

    private void initModel() {
        //options = loadOptions();
    }

    @FXML
    private void setLeftCycleBtn(ActionEvent e) {


        cycleLeft.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCode().getName());

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

                cycleRight.removeEventHandler(KeyEvent.KEY_PRESSED, this);

            }
        });

    }

    public  void handleKey(KeyEvent event) {
        System.out.println(event.getCode().getName());
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
