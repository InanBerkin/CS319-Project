package client;

import client.Menus.OptionsMenu.OptionsMenu;
import client.Network.ClientSocketHandler;
import client.Network.NetworkAnalyzer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.*;
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

            String homeDir = System.getProperty("user.home");
            folderPath = homeDir + File.separator + "qbitz_configs";

            /*
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
            }*/

            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            sceneController = new SceneController(primaryStage);

            NetworkAnalyzer networkAnalyzer = new NetworkAnalyzer("https://www.google.com.tr");
            if (networkAnalyzer.isOnline()) {
                try {
                    ClientSocketHandler clientSocketHandler = new ClientSocketHandler("139.179.224.94", 9999);
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

}
