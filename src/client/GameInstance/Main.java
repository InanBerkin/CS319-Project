package client.GameInstance;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * @author Halil Şahiner
 * @version 1.0
 *
 * http://www.genuinecoder.com
 */
public class Main extends Application {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static Camera camera;
    private static Scene scene;

    @Override
    public void start(Stage primaryStage) {

        GameBoard board = new GameBoard(3);
        Pattern pattern = new Pattern(4);
        //pattern.setMatQuestMark();
        Group boardGroup = board.createBoardGroup();
        Group patternGroup = pattern.createPatternGroup();

        boardGroup.translateXProperty().set(WIDTH/2);
        boardGroup.translateYProperty().set(HEIGHT/2);
        boardGroup.translateZProperty().set(-1000);

        patternGroup.translateXProperty().set((WIDTH / 5)*2);
        patternGroup.translateYProperty().set((HEIGHT / 5)*2);
        patternGroup.translateZProperty().set(-1050);

        Group mainGroup = new Group();
        mainGroup.getChildren().add(boardGroup);
        mainGroup.getChildren().add(patternGroup);

        camera = new PerspectiveCamera();
        scene = new Scene(mainGroup, WIDTH, HEIGHT, true);
        scene.setFill(Color.PINK);
        scene.setCamera(camera);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Board");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
