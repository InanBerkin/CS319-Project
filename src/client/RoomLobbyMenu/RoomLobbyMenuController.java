package client.RoomLobbyMenu;

import client.MenuController;
import javafx.application.Platform;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomLobbyMenuController extends MenuController {
    @Override
    public void onMessageReceived(String message) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() ->{
            System.out.println(payload.getInt("id"));
        });
    }

    private void addPlayers(){

    }
}
