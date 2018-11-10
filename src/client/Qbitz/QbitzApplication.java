package client.Qbitz;

import client.ClientSocketHandler;
import client.SceneController.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class QbitzApplication extends Application {

    private static SceneController sceneController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            ClientSocketHandler clientSocketHandler = new ClientSocketHandler("localhost", 9999);
            clientSocketHandler.start();
            sceneController = new SceneController(primaryStage);
            sceneController.setSocketHandler(clientSocketHandler);
            sceneController.gotoLogin();
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(QbitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static SceneController getSceneController(){
        return sceneController;
    }



}
