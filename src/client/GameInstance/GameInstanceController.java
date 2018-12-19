package client.GameInstance;

import client.ImageRecreation.ImageRecreation;
import client.MenuController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import java.io.FileNotFoundException;
import javafx.util.Duration;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameInstanceController extends MenuController {

    private final Group root = new Group();
    private final Group mainGroup = new Group();

    @FXML
    private GridPane gridPane;

    @FXML
    private Label timerLabel;

    private GameBoard board;
    private Pattern pattern;

    private static final double CAMERA_INITIAL_DISTANCE = -1000;
    private static final double CAMERA_INITIAL_X_ANGLE = -30.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 60.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static ImageRecreation imageRecreation = null;

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private int gridDimension;
    private int gameMode;

    Cube cube;
    final XGroup cameraHolder = new XGroup();
    final PerspectiveCamera camera = new PerspectiveCamera(true);

    final XGroup cameraHolderBoard = new XGroup();
    final PerspectiveCamera cameraBoard = new PerspectiveCamera(true);

    private GameTimer gameTimer;

    @Override
    public void onMessageReceived(String message) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            cube = new Cube(100, 4.5, 0, 0, 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        root.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        try {
            buildBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            gridDimension = payload.getInt("boardSize");
            gameMode = payload.getInt("gameMode");
            System.out.println(gridDimension + " " + gameMode);

            if(gameMode == 1) {
            try {
            imageRecreation = new ImageRecreation("assets/recImage.jpg", gridDimension, rect);
            } catch (IOException e) {
            e.printStackTrace();
            }
            board = new GameBoard(gridDimension, imageRecreation);
            pattern = new Pattern(gridDimension, imageRecreation.getImgParts().toArray(new Image[imageRecreation.getImgParts().size()]));
            imageRecreation.imageRec();
            } else if(gameMode == 0)
            {
            board = new GameBoard(gridDimension, null);
            pattern = new Pattern(gridDimension,null);
            }

            //pattern.setMatQuestMark();

            Group boardGroup = board.createBoardGroup();
            Group patternGroup = pattern.createPatternGroup();

            patternGroup.setTranslateX(patternGroup.getTranslateX()+ WIDTH/5);
            patternGroup.translateZProperty().set(0);


            boardGroup.translateZProperty().set(0);
            boardGroup.getTransforms().add(new Rotate(-40, Rotate.X_AXIS));

            boardGroup.setTranslateX(boardGroup.getTranslateX()-WIDTH/5);
            boardGroup.getChildren().add(new AmbientLight());

            Group mainGroup = new Group();
            mainGroup.getChildren().addAll(boardGroup,patternGroup);




            SubScene scene = new SubScene(root, 200, 500, true, SceneAntialiasing.BALANCED);
            scene.setCamera(camera);
            scene.setFill(Color.WHITE);

            SubScene boardScene = new SubScene(mainGroup, WIDTH, HEIGHT , true, SceneAntialiasing.BALANCED);
            boardScene.setCamera(cameraBoard);
            boardScene.setFill(Color.WHITE);

            gridPane.add(scene, 0,2);
            gridPane.add(boardScene, 1, 2);
            gameTimer = new GameTimer();
            gameTimer.setGameLabel(timerLabel);
            gameTimer.startTimer(0);

            handleKeys(gridPane);
        });
    }

    private void buildCamera() {
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);

        cameraHolder.getChildren().add(camera);

        cameraHolder.rotate(CAMERA_INITIAL_X_ANGLE, Rotate.X_AXIS);
        cameraHolder.rotate(CAMERA_INITIAL_Y_ANGLE, Rotate.Y_AXIS);

        root.getChildren().add(cameraHolder);

        cameraBoard.setNearClip(CAMERA_NEAR_CLIP);
        cameraBoard.setFarClip(CAMERA_FAR_CLIP);
        cameraBoard.setTranslateZ(CAMERA_INITIAL_DISTANCE);

        cameraHolderBoard.getChildren().add(cameraBoard);
//
//        cameraHolderBoard.rotate(CAMERA_INITIAL_X_ANGLE, Rotate.X_AXIS);
//        cameraHolderBoard.rotate(CAMERA_INITIAL_Y_ANGLE, Rotate.Y_AXIS);

        mainGroup.getChildren().add(cameraHolderBoard);

    }

    private void buildBody() throws Exception {
        root.getChildren().add(cube);
        cube.updateFrontFaces();
        // buildAxes();
    }

    private void buildAxes() {
        for (int i = -2000; i <= 2000; i += 10) {
            PhongMaterial redMaterial = new PhongMaterial();
            redMaterial.setDiffuseColor(Color.DARKRED);
            redMaterial.setSpecularColor(Color.RED);

            Sphere sphereX = new Sphere();
            sphereX.setRadius(3);
            sphereX.setTranslateX(i);
            sphereX.setTranslateY(0);
            sphereX.setTranslateZ(0);
            sphereX.setMaterial(redMaterial);

            root.getChildren().addAll(sphereX);

            PhongMaterial greenMaterial = new PhongMaterial();
            greenMaterial.setDiffuseColor(Color.DARKGREEN);
            greenMaterial.setSpecularColor(Color.GREEN);

            Sphere sphereY = new Sphere();
            sphereY.setRadius(3);
            sphereY.setTranslateX(0);
            sphereY.setTranslateY(i);
            sphereY.setTranslateZ(0);
            sphereY.setMaterial(greenMaterial);

            root.getChildren().addAll(sphereY);

            PhongMaterial blueMaterial = new PhongMaterial();
            blueMaterial.setDiffuseColor(Color.DARKBLUE);
            blueMaterial.setSpecularColor(Color.BLUE);

            Sphere sphereZ = new Sphere();
            sphereZ.setRadius(3);
            sphereZ.setTranslateX(0);
            sphereZ.setTranslateY(0);
            sphereZ.setTranslateZ(i);
            sphereZ.setMaterial(blueMaterial);

            root.getChildren().addAll(sphereZ);
        }
    }

    private void handleKeys(GridPane gridPane) {
        gridPane.addEventFilter(KeyEvent.KEY_PRESSED, event-> {
            event.consume();
            if (event.getCode() == KeyCode.W) {
                cube.rotate1();
            } else if (event.getCode() == KeyCode.S) {
                cube.rotate2();
            } else if (event.getCode() == KeyCode.A) {
                cube.rotate3();
            } else if (event.getCode() == KeyCode.D) {
                cube.rotate4();
            } else if (event.getCode() == KeyCode.Q) {
               cube.highlight(Cube.BACKWARD);
            } else if (event.getCode() == KeyCode.E) {
                cube.highlight(Cube.FORWARD);
            } else if (event.getCode() == KeyCode.SPACE) {
                board.setSelectedFaceMat(cube.selectFace());
            }
        });
    }

    public void foo() {
        System.out.println("Is pattern correct? : " + pattern.checkPattern(board.getBoardImageViews()));
        System.out.println("Submit Button!");
    }

}