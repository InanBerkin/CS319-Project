package client;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URL;

public class SoundController {

    static String mainMenuSoundPath;
    static String gameSoundPath;
    static String turnFxPath;
    static String putFxPath;
    static Media menuMedia;
    static Media gameMedia;
    static MediaPlayer menuPlayer;
    static MediaPlayer gamePlayer;
    static URL resource;

    static String homeDir = System.getProperty("user.home");
    static String folderPath = homeDir + File.separator + "qbitz_configs";

    static double gameSoundVolume = 0;
    static double menuSoundVolume = 0;
    static double fxVolume = 0;


    public static void initSound() {


        mainMenuSoundPath = "/assets/qbitz_sound.mp3";
        System.out.println("here");
        System.out.println(System.getProperty("user.dir") + mainMenuSoundPath);
        resource = SoundController.class.getClassLoader().getResource("qbitz_sound.mp3");
        menuMedia = new Media(resource.toString());
        menuPlayer = new MediaPlayer(menuMedia);
        menuPlayer.setStartTime(new Duration(0));
        setVolume(50);



    }

    public static void playMenuSound() {
        menuPlayer.play();
    }

    public static void playGameSound() {

    }



    public static void setVolume(double volume) {
        volume = volume/100;
        menuPlayer.setVolume(volume);
    }


    public static double getSliderVolume() {
        return menuPlayer.getVolume()*100;
    }


    public static void saveFaces() {

        try {

            double writeableOffset = 0;
            // write object to file
            FileOutputStream fos = new FileOutputStream(folderPath + File.separator + "sound.qbitz");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(writeableOffset);
            oos.close();

            //System.out.println("One:" + result.getOne() + ", Two:" + result.getTwo());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadFaces() {
        FileInputStream fis = null;
        try {
            // read object from file
            fis = new FileInputStream(folderPath + File.separator + "sound.qbitz");
            ObjectInputStream ois = new ObjectInputStream(fis);
            fxVolume = (double) ois.readObject();
            ois.close();
            CubeFaces.setAllFaces();
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(OptionsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(OptionsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(OptionsController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                //Logger.getLogger(OptionsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }





}
