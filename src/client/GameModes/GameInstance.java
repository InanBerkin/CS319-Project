package client.GameModes;



import client.GameModes.SPGameModes.MemoryMode.MemoryModeController;
import client.Menus.MenuController;
import client.GameModels.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class GameInstance extends MenuController implements TimerSignable {

    private final Group root = new Group();
    private Group boardGroup = new Group();
    private Group patternGroup = new Group();

    @FXML
    private VBox vBox;

    @FXML
    private HBox sceneHbox;

    @FXML
    private Label timerLabel;

    public GameBoard board;
    public Pattern pattern;

    private static final double CAMERA_INITIAL_DISTANCE = -1000;
    private static final double CAMERA_INITIAL_X_ANGLE = -30.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 60.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double KEY_ROTATION_STEP = 9;


    public int gridDimension;

    Cube cube;
    final XGroup cameraHolder = new XGroup();
    final PerspectiveCamera camera = new PerspectiveCamera(true);

    final XGroup cameraHolderBoard = new XGroup();
    final PerspectiveCamera cameraBoard = new PerspectiveCamera(true);

    final XGroup cameraHolderPattern = new XGroup();
    final PerspectiveCamera cameraPattern = new PerspectiveCamera(true);

    public GameTimer gameTimer;

    @Override
    public void onMessageReceived(String message) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            cube = new Cube(200, KEY_ROTATION_STEP, 0, 0, 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        gameTimer = new GameTimer(this,timerLabel);

        root.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        try {
            buildBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            gridDimension = payload.getInt("boardSize");

            initializeGameMode();

            boardGroup = board.createBoardGroup();
            patternGroup = pattern.createPatternGroup();
            patternGroup.translateZProperty().set(0);
            boardGroup.translateZProperty().set(0);

            boardGroup.getChildren().add(new AmbientLight());
            patternGroup.getChildren().add(new AmbientLight());

            SubScene cubeScene = new SubScene(root, 400, 400, true, SceneAntialiasing.BALANCED);
            cubeScene.setCamera(camera);
            cubeScene.setFill(Color.SPRINGGREEN);

            SubScene boardScene = new SubScene(boardGroup, 400, 400 , true, SceneAntialiasing.BALANCED);
            boardScene.setCamera(cameraBoard);
            boardScene.setFill(Color.WHITE);

            SubScene patternScene = new SubScene(patternGroup, 400, 400 , true, SceneAntialiasing.BALANCED);
            patternScene.setCamera(cameraPattern);
            patternScene.setFill(Color.WHITE);

            sceneHbox.setSpacing(40);
            sceneHbox.setAlignment(Pos.CENTER);
            sceneHbox.getChildren().addAll(cubeScene, boardScene, patternScene);

            handleKeys(vBox);
        });
    }

    public abstract void initializeGameMode();

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

        boardGroup.getChildren().add(cameraHolderBoard);

        cameraPattern.setNearClip(CAMERA_NEAR_CLIP);
        cameraPattern.setFarClip(CAMERA_FAR_CLIP);
        cameraPattern.setTranslateZ(CAMERA_INITIAL_DISTANCE);

        cameraHolderPattern.getChildren().add(cameraPattern);

        patternGroup.getChildren().add(cameraHolderPattern);
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

    public void handleKeys(VBox vBox) {

        vBox.addEventFilter(KeyEvent.KEY_PRESSED, event-> {
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
                board.setSelectedFaceMat(cube.selectFace());
            } else if (event.getCode() == KeyCode.E) {
                cube.highlight(Cube.FORWARD);
                board.setSelectedFaceMat(cube.selectFace());
            }

        });
    }

    @Override
    public void timerStopped() {

    }

    public void foo() {
        System.out.println("Is pattern correct? : " + pattern.checkPattern(board.getBoardImageViews()));
        System.out.println("Submit Button!");
    }

}