package client;

import javafx.fxml.Initializable;

public abstract class MenuController implements Initializable {

    public abstract void onMessageReceived(String message);

}
