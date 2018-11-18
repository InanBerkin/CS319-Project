package client.SceneController;

import client.ClientSocketHandler;
import client.MenuController;
import client.QBitzApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SceneController {

    private Stage stage;
    private FXMLLoader loader;
    private ClientSocketHandler socketHandler;
    private final String BASE_URL = "../";

    public SceneController(Stage stage){
        loader = new FXMLLoader();
        this.stage = stage;
    }

    private Parent replaceSceneContent(String fxml) throws Exception {
        loader = new FXMLLoader(getClass().getResource(fxml));
        Parent page = (Parent) loader.load();
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(page);
        }
        return page;
    }

    public void changeScene( String sceneName ){
        try {
            stage.setTitle(sceneName);
            replaceSceneContent(BASE_URL + sceneName + "/"+ sceneName + "View.fxml");
        } catch (Exception ex) {
            Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changeScene(String sceneName, JSONObject payload){
        try {
            stage.setTitle(sceneName);
            replaceSceneContent(BASE_URL + sceneName + "/"+ sceneName + "View.fxml");
            getController().setPayload(payload);
        } catch (Exception ex) {
            Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
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
            getController().onMessageReceived(message);
        }

    }

    public void onExit(){

    }

    public void sendMessageToServer(String message){
        socketHandler.sendMessage(message);
    }
}
