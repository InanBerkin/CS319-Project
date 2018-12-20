package client.GameInstance;

import client.MenuController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.transform.Rotate;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;

public class MemoryMode extends MenuController implements TimerSignable {
    private int memoryTime;

    private Group root = new Group();
    private Group boardGroup = new Group();
    private Group patternGroup = new Group();

    @FXML
    private Button startButton;

    @FXML
    private VBox vBoxMemory;

    @FXML
    private HBox sceneHboxMemory;

    @FXML
    private Label timerLabelMemory;

    private GameBoard board;
    private Pattern pattern;

    private static final double CAMERA_INITIAL_DISTANCE = -1000;
    private static final double CAMERA_INITIAL_X_ANGLE = -30.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 60.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double KEY_ROTATION_STEP = 9;

    private int gridDimension;

    private Cube cube;
    private XGroup cameraHolder = new XGroup();
    private PerspectiveCamera camera = new PerspectiveCamera(true);

    private XGroup cameraHolderBoard = new XGroup();
    private PerspectiveCamera cameraBoard = new PerspectiveCamera(true);

    private XGroup cameraHolderPattern = new XGroup();
    private PerspectiveCamera cameraPattern = new PerspectiveCamera(true);

    private GameTimer gameTimer;

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
        gameTimer = new GameTimer(this, timerLabelMemory);

        root.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        try {
            buildBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            gridDimension = payload.getInt("boardSize");

            this.memoryTime = memoryTime(gridDimension);
            board = new GameBoard(gridDimension, null);
            pattern = new Pattern(gridDimension,null);

            try {
                pattern.setMatQuestMark();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

            sceneHboxMemory.setSpacing(40);
            sceneHboxMemory.setAlignment(Pos.CENTER);
            sceneHboxMemory.getChildren().addAll(cubeScene, boardScene, patternScene);
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

        boardGroup.getChildren().add(cameraHolderBoard);

        cameraPattern.setNearClip(CAMERA_NEAR_CLIP);
        cameraPattern.setFarClip(CAMERA_FAR_CLIP);
        cameraPattern.setTranslateZ(CAMERA_INITIAL_DISTANCE);

        cameraHolderPattern.getChildren().add(cameraPattern);

        patternGroup.getChildren().add(cameraHolderPattern);
    }

    private void buildBody()  {
        root.getChildren().add(cube);
        cube.updateFrontFaces();
    }

    private void handleKeys(VBox vBox) {
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
            } else if (event.getCode() == KeyCode.E) {
                cube.highlight(Cube.FORWARD);
            } else if (event.getCode() == KeyCode.SPACE) {
                board.setSelectedFaceMat(cube.selectFace());
            }
        });
    }

    @Override
    public void timerStopped() {
        handleKeys(vBoxMemory);

            try {
            pattern.setMatQuestMark();
        } catch (IOException e) {
            e.printStackTrace();
        }

        gameTimer = new GameTimer(this, timerLabelMemory);
        gameTimer.startTimer();
    }

    public int memoryTime(int dimension) {
        if (dimension == 3)
            return 5;
        else if (dimension == 4 )
            return 15;
        else
            return 30;
    }
    @FXML
    public boolean startShowPattern(){
        this.gameTimer.startTimer(this.memoryTime);
        this.pattern.showPattern();
        this.startButton.setVisible(false);

        return true;
    }
}
