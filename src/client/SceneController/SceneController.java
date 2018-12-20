package client.SceneController;

import client.ClientSocketHandler;
import client.MenuController;
import client.QBitzApplication;
import client.UserConfiguration;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.json.JSONArray;
import org.json.JSONException;
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

    public Window getWindow(){
        return stage;
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

    public void sendMessageToServer(JSONObject message){
        if(isJSONValid(message.toString())){
            socketHandler.sendMessage(message.toString());
        }

    }

    private boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
