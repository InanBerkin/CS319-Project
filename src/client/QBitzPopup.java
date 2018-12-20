package client;

import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.Optional;

public class QBitzPopup extends Dialog {

    public QBitzPopup(){
        setWidth(300);
        setHeight(300);
        initStyle(StageStyle.UNDECORATED);
        initModality(Modality.NONE);
        initOwner(QBitzApplication.getSceneController().getWindow());
    }

}
