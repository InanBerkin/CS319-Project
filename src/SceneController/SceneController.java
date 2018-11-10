package SceneController;

import Qbitz.QbitzApplication;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SceneController {

    private Stage stage;

    public SceneController(Stage stage){
        this.stage = stage;
    }

    private Parent replaceSceneContent(String fxml) throws Exception {
        Parent page = (Parent) FXMLLoader.load(QbitzApplication.class.getResource(fxml), null, new JavaFXBuilderFactory());
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
}
