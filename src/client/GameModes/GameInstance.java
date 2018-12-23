package client.GameModes;



import client.GameModes.SPGameModes.MemoryMode.MemoryModeController;
import client.Menus.MenuController;
import client.GameModels.*;
import client.SoundController;
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
import org.json.JSONArray;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class initalize the all the game instance properties of the game for the modes
 * author : Halil Şahiner
 */

public abstract class GameInstance extends MenuController implements TimerSignable {

    private final Group root = new Group();
    private Group boardGroup = new Group();
    public Group patternGroup = new Group();

    @FXML
    private VBox vBox;

    @FXML
    private HBox sceneHbox;

    @FXML
    private HBox labelHbox;

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

    public Cube cube;
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
        SoundController.playGameSound();
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

            setQuestMark();

            boardGroup.getChildren().add(new AmbientLight());
            patternGroup.getChildren().add(new AmbientLight());

            SubScene cubeScene = new SubScene(root, 400, 400, true, SceneAntialiasing.BALANCED);
            cubeScene.setCamera(camera);
            cubeScene.setFill(Color.WHITE);


            SubScene boardScene = new SubScene(boardGroup, 400, 400 , true, SceneAntialiasing.BALANCED);
            boardScene.setCamera(cameraBoard);
            boardScene.setFill(Color.WHITE);

            SubScene patternScene = new SubScene(patternGroup, 400, 400 , true, SceneAntialiasing.BALANCED);
            patternScene.setCamera(cameraPattern);
            patternScene.setFill(Color.WHITE);

            labelHbox.getChildren().addAll(new Label("Cube"), new Label("Board"), new Label("Pattern"));
            labelHbox.setSpacing(350);
            labelHbox.setAlignment(Pos.CENTER);

            sceneHbox.setId("sceneBox");
            sceneHbox.setSpacing(40);
            sceneHbox.setAlignment(Pos.CENTER);
            sceneHbox.getChildren().addAll(cubeScene, boardScene, patternScene);

            handleKeys(vBox);
        });
    }

    /**
     * This method contains the game mode functionality for the modes
     */
    public abstract void initializeGameMode();

    /**
     * this method will be filled to set question mark for memory mode of the multiplayer game
     */
    public void setQuestMark(){

    }

    /**
     * Build the cameras for the game instance scenes.
     */
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

    /**
     * Build the body of the game instance
     */
    private void buildBody() {
        root.getChildren().add(cube);
        cube.updateFrontFaces();
        // buildAxes();
    }


    /**
     * Handle the keys for the cube and the board of the game instance to make it functional
     * @param vBox is the fxml object to get the parts of the game instance to activate the keys.
     */
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

    /**
     * This method is submit the created pattern on the board to check and handle the functionality
     * @return
     */
    public boolean submit() {
        return pattern.checkPattern(board.getBoardImageViews());
    }

    /**
     * This method handles the json array which is given by the server to take the generated pattern
     * @param array JSONArray to pass the generated pattern
     * @param dimension board dimension
     * @return
     */
    public int[][] jsonArrayToMatrix(JSONArray array, int dimension) {
        int size = dimension * dimension;
        int[][] result = new int[size][2];

        int cur1 = 0;
        for (Object iterator1 : array) {
            int cur2 = 0;
            for (Object iterator2 : (JSONArray) iterator1) {
                result[cur1][cur2] = (int) iterator2;
                cur2++;
            }
            cur1++;
        }

        return result;
    }


}