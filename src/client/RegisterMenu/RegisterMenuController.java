package client.RegisterMenu;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterMenuController implements Initializable {

    @FXML
    public TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField confirmPasswordTextField;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    @FXML
    public void register(ActionEvent event){
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String email = emailTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();

        if(username.isEmpty() || password.isEmpty() || email.isEmpty() || confirmPassword.isEmpty() ){
            errorLabel.setText("Please fill all the fields");
            return;
        }
        else if(!password.equals(confirmPassword)){
            errorLabel.setText("Passwords do not match!");
            return;
        }
    }

    @FXML
    public void gotoLogin(ActionEvent event){
        Client.getSceneController().gotoLogin();
    }
}
