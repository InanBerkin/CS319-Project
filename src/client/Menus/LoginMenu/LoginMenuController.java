package client.Menus.LoginMenu;

import client.Menus.MenuController;
import client.QBitzApplication;
import client.UserConfiguration;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.json.JSONObject;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenuController extends MenuController {

    @FXML
    public TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;
    @FXML
    private ProgressIndicator progressIcon;

    private TranslateTransition translate;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        //Instantiating TranslateTransition class
        translate = new TranslateTransition(Duration.millis(100));
        translate.setFromX(0);
        translate.setToX(-5);
        translate.setCycleCount(4);
        translate.setAutoReverse(true);
        translate.setNode(loginButton);
    }

    @FXML
    public void login(ActionEvent event){
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if(username.isEmpty() || password.isEmpty() ){
            errorLabel.setText("Please fill all the fields");
            translate.play();
            return;
        }
        JSONObject loginJSON = new JSONObject();
        loginJSON.put("requestType", "login");
        if(isValidEMail(username)){
            loginJSON.put("email", username);
        }
        else {
            loginJSON.put("username", username);
        }
        loginJSON.put("password", password);
        progressIcon.setVisible(true);
        QBitzApplication.getSceneController().sendMessageToServer(loginJSON);
    }

    @FXML
    public void gotoRegister(){
        QBitzApplication.getSceneController().gotoMenu("RegisterMenu");
    }
    public void gotoMainMenu(){
        QBitzApplication.getSceneController().gotoMenu("MainMenu");
    }

    @Override
    public void onMessageReceived(String message){
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("login")){
            if (responseJSON.getBoolean("result")){
                Platform.runLater(() -> {
                    progressIcon.setVisible(false);
                    UserConfiguration.isLoggedIn = true;
                    QBitzApplication.getSceneController().gotoMenu("RoomMenu");
                });
            }
        }
    }

    private boolean isValidEMail(String email) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }


}
