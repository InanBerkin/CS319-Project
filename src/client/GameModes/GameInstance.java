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
import org.json.JSONArray;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    private Label gameStatusLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Label submitButton;

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

            gameStatusLabel.setStyle("-fx-text-fill: #FEC601");

            handleKeys(vBox);
        });
    }

    public abstract void initializeGameMode();

    public void setQuestMark(){

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

    private void buildBody() throws Exception {
        root.getChildren().add(cube);
        cube.updateFrontFaces();
        // buildAxes();
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

    public boolean submit() {
        if(pattern.checkPattern(board.getBoardImageViews())){
            gameStatusLabel.setStyle("-fx-text-fill: #43d873");
            gameStatusLabel.setText("You solved the pattern!");
            submitButton.setDisable(true);
            return true;
        }
        else{
            gameStatusLabel.setStyle("-fx-text-fill: #FF1654");
            gameStatusLabel.setText("Wrong Pattern");
            return false;
        }
    }

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