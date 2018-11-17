package client.SceneController;

import client.ClientSocketHandler;
import client.MenuController;
import client.QbitzApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SceneController {

    private Stage stage;
    private FXMLLoader loader;
    private ClientSocketHandler socketHandler;
    private final String BASE_URL = "../";

    public SceneController(Stage stage){
        this.stage = stage;
    }

    private Parent replaceSceneContent(String fxml) throws Exception {
        loader = new FXMLLoader(getClass().getResource(fxml));
        Parent page = (Parent) loader.load();
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page, 800, 600);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(page);
        }
        stage.sizeToScene();
        return page;
    }
    public void changeScene( String sceneName ){
        try {
            stage.setTitle(sceneName);
            replaceSceneContent(BASE_URL + sceneName + "/"+ sceneName + "View.fxml");
        } catch (Exception ex) {
            Logger.getLogger(QbitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    public MenuController getController(){
        return loader.getController();
    }

    public void setSocketHandler(ClientSocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    public void onMessageReceived(String message){
        if(getController() != null){
            System.out.println(getController().getClass().toString());
            getController().onMessageReceived(message);
        }

    }

    public void onExit(){

    }

    public void sendMessageToServer(String message){
        socketHandler.sendMessage(message);
    }
}
