package client;

import client.SceneController.SceneController;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.logging.Level;
import java.util.logging.Logger;

public class QBitzApplication extends Application {

    private static SceneController sceneController;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            ClientSocketHandler clientSocketHandler = new ClientSocketHandler("139.179.134.161", 9999);
            clientSocketHandler.start();
            sceneController = new SceneController(primaryStage);
            sceneController.setSocketHandler(clientSocketHandler);
            sceneController.changeScene("MainMenu");
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static SceneController getSceneController(){
        return sceneController;
    }

}
