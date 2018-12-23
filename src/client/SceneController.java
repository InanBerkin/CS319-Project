package client;

import client.Network.ClientSocketHandler;
import client.Menus.MenuController;
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

    public static final boolean SP = true;
    public static final boolean MP = false;

    private Stage stage;
    private FXMLLoader loader;
    private ClientSocketHandler socketHandler;

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

    public void gotoMenu(String menuName) {
        try {
            stage.setTitle(menuName);
            replaceSceneContent("Menus/" + menuName + "/"+ menuName + "View.fxml");
        } catch (Exception ex) {
            Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoMenu(String menuName, JSONObject payload) {
        try {
            stage.setTitle(menuName);
            replaceSceneContent("Menus/" + menuName + "/"+ menuName + "View.fxml");
            getController().setPayload(payload);
        } catch (Exception ex) {
            Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoGameMode(boolean isSP, String gameMode) {
        try {
            stage.setTitle(gameMode);
            if (isSP)
                replaceSceneContent("GameModes/SPGameModes/" + gameMode + "/"+ gameMode + "View.fxml");
            else
                replaceSceneContent("GameModes/MPGameModes/" + gameMode + "/"+ gameMode + "View.fxml");
        } catch (Exception ex) {
            Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoGameMode(boolean isSP, String gameMode, JSONObject payload) {
        try {
            stage.setTitle(gameMode);
            if (isSP)
                replaceSceneContent("GameModes/SPGameModes/" + gameMode + "/"+ gameMode + "View.fxml");
            else
                replaceSceneContent("GameModes/MPGameModes/" + gameMode + "/"+ gameMode + "View.fxml");
            getController().setPayload(payload);
        } catch (Exception ex) {
            Logger.getLogger(QBitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public MenuController getController(){
        return loader.getController();
    }

    public Stage getWindow() {
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
