package client.LoginMenu;

import client.MenuController;
import client.QbitzApplication;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;


import java.net.URL;
import java.util.ResourceBundle;

public class LoginMenuController extends MenuController {

    @FXML
    public TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;

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
        QbitzApplication.getSceneController().sendMessageToServer(password);
        QbitzApplication.getSceneController().gotoMainMenu();
        System.out.println("Login");
    }

    @FXML
    public void gotoRegister(ActionEvent event){
        QbitzApplication.getSceneController().gotoRegister();
    }

    @Override
    public void onMessageReceived(String message){
        usernameTextField.setText(message);
    }
}
