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

    public void gotoLogin(){
        try {
            stage.setTitle("Login");
            replaceSceneContent("../LoginMenu/LoginMenuView.fxml");
        } catch (Exception ex) {
            Logger.getLogger(QbitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoMainMenu(){
        try {
            replaceSceneContent("../MainMenu/MainMenuView.fxml");
        } catch (Exception ex) {
            Logger.getLogger(QbitzApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoRegister(){
        try {
            replaceSceneContent("../RegisterMenu/RegisterMenuView.fxml");
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
