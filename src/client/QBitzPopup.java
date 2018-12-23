package client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.*;

import java.util.Optional;

public class QBitzPopup extends Dialog {

    static boolean answer;

    public static boolean display(Stage primaryStage, String title, String message) {


        Stage window = new Stage();
        window.setMinWidth(500);
        // block event for other window
        window.initModality(Modality.APPLICATION_MODAL);
        window.initOwner(primaryStage);
        window.setTitle(title);
        // window.setMinWidth(500);

        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("Yes");
        yesButton.setId("yes");
        Button noButton = new Button("No");
        noButton.setId("no");

        yesButton.setOnAction(e -> {
            answer = true;
//            root.getChildren().remove(shade);
            window.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(50);
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
//        layout.setBorder(new Border(
//                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(5))));
        Scene scene = new Scene(layout, 500, 400);
        window.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("styles/Popup.css");
        window.setScene(scene);

        window.initStyle(StageStyle.UNDECORATED);
        window.setY( primaryStage.getHeight()/2 - scene.getHeight()/2);
        window.setX( primaryStage.getWidth()/2 - scene.getWidth()/2);
        window.showAndWait();

        return answer;

    }

}