package client;

import client.Menus.OptionsMenu.OptionsMenu;
import client.Network.ClientSocketHandler;
import client.Network.NetworkAnalyzer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QBitzApplication extends Application {

    private static SceneController sceneController;

    private String folderPath;
    OptionsMenu opts;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            CubeFaces.initPaths();
            CubeFaces.initImages();


            String homeDir = System.getProperty("user.home");
            folderPath = homeDir + File.separator + "qbitz_configs";


            if(Files.notExists(Paths.get(folderPath))) {
                // create folder and write default options
                System.out.println("DEBUG: Options does not exist.");
                new File(folderPath).mkdir();
                System.out.println("DEBUG: "  + folderPath + " created.");

                opts = new OptionsMenu();
                opts.initDefaultOptions();
                opts.updateSettings();
                saveOptions(opts);

            }
            else {
                System.out.println("DEBUG: Options exists");
                // init existing options
                opts = loadOptions();
                opts.updateSettings();
            }

            if(Files.notExists(Paths.get(folderPath + File.separator + "faces.qbitz"))) {
                CubeFaces.saveFaces();
            }
            else {
                CubeFaces.loadFaces();
            }

            SoundController.initSound();
            SoundController.playMenuSound();

            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            sceneController = new SceneController(primaryStage);

            NetworkAnalyzer networkAnalyzer = new NetworkAnalyzer("https://www.google.com.tr");
            if (networkAnalyzer.isOnline()) {
                try {
                    ClientSocketHandler clientSocketHandler = new ClientSocketHandler("localhost", 9999);
                  clientSocketHandler.start();
                    sceneController.setSocketHandler(clientSocketHandler);
                    UserConfiguration.isOnline = true;
               }
                catch (IOException e) {
                    System.out.println("» Server is unreachable.");
                    UserConfiguration.isOnline = false;
                }
            }
            primaryStage.setOnCloseRequest( e -> {
                Platform.exit();
                System.exit(0);
            });
            sceneController.gotoMenu("MainMenu");
            primaryStage.show();
//            addEscKeyListener(primaryStage);
        } catch (Exception ex) {
            Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static SceneController getSceneController(){
        return sceneController;
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
            Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }
    
    public void addEscKeyListener(Stage stage){
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {
                    public void handle(KeyEvent e) {
                        if(e.getCode() == KeyCode.ESCAPE){
                            if(QBitzPopup.display(stage ,"Exit", "Return to Main Menu?")){
                                QBitzApplication.getSceneController().gotoMenu("MainMenu");
                            }
                        }
                    };
                });
    }

}
