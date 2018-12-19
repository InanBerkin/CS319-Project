package client;

import client.SceneController.SceneController;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QBitzApplication extends Application {

    private static SceneController sceneController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
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
                }
            }
            sceneController.changeScene("SingleplayerMenu");
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static SceneController getSceneController(){
        return sceneController;
    }

}
