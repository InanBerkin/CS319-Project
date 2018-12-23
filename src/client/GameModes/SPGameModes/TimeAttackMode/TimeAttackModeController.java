package client.GameModes.SPGameModes.TimeAttackMode;


import client.GameModes.GameInstance;
import client.GameModels.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class TimeAttackModeController extends GameInstance{

    Socket socket;
    DataInputStream getFromServer = null;

    @FXML
    public Label labelAI;
    @Override
    public void initializeGameMode() {
        gameTimer.startTimer();
        board = new GameBoard(gridDimension, null);
        pattern = new Pattern(gridDimension);

        try {
            socket = new Socket("localhost", 2004);
            getFromServer = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Runnable r = new Runnable() {
//            public void run() {
//
//                    do {
//                        try {
//
//                            final String msg= getFromServer.readUTF(); // <--- freeze GUI
//                            Platform.runLater(() -> rotatex(msg));
//                            try {
//                                Thread.sleep(100);
//                            } catch (InterruptedException ex) {
//                                break;
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    } while (true);
//                }
//
//        };
//
//        new Thread(r).start();

        Task task = new Task<Void>() {
            @Override public Void call() {
                try{

                    while(true) {
                        int message = Integer.parseInt(getFromServer.readUTF());
                        System.out.println(message);
                        if (message == 3) {
                            cube.rotate2();

                        }
                        else if ( message == 4) {
                            cube.rotate4();
                        }
                        else if ( message == 2 ) {
                            cube.rotate3();
                        }
                        else if (message == 1 ){
                            cube.rotate1();

                        }
                        else {
                            board.setSelectedFaceMat(cube.selectFace());
                            System.out.println("inside"+message+"x");
                        }


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
        cube.rotateProperty().bind(task.progressProperty());
        //labelAI.textProperty().bind(task.progressProperty());
        new Thread(task).start();

    }


    @FXML
    public void rotatex(String message){
    //        labelAI.setText(message);
        System.out.println(message.getClass().getTypeName());
        if (message == "4") {
            cube.rotate4();
            System.out.println("inside 4 ");
        }
        else if ( message == "3") {
            cube.rotate3();
        }
        else if ( message == "2" ) {
            cube.rotate2();
        }
        else {
            cube.rotate2();
            System.out.println("inside 4 ");
        }

    }
    @Override
    public boolean submit(){





 //       return true;
       boolean isPatternTrue = super.submit();
        System.out.println("Is pattern true: " + isPatternTrue);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isPatternTrue;
    }
}