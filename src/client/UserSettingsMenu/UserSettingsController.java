package client.UserSettingsMenu;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.Popup;

import java.net.URL;
import java.util.ResourceBundle;

public class UserSettingsController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    public boolean checkUsername(String newUserName){
        return true;
    }

    public boolean checkMail(String newMail ){
        return true;
    }

    public boolean isPassMatch(String passFirst, String passSecond){
        return true;
    }
    public  boolean checkOldPass(String oldPass ){
        return true;
    }

    public void ensureDelProg(){
        //popup wants to password
    }
    public void ensureDelUser(){
        //popup wants to password
    }
    public void updateInfo(ActionEvent actionEvent) {
    }

    public void updatePass(ActionEvent actionEvent) {
    }

    public void delUserProg(ActionEvent actionEvent) {
    }

    public void delUser(ActionEvent actionEvent) {
    }
}
