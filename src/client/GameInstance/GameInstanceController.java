package client.GameInstance;

import client.MenuController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class GameInstanceController extends MenuController {
    @FXML
    private final Group root = new Group();

    @FXML
    private GridPane gridPane;

    @FXML
    private Label timerLabel;

    private static final double CAMERA_INITIAL_DISTANCE = -1000;
    private static final double CAMERA_INITIAL_X_ANGLE = -30.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 60.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double KEY_ROTATION_STEP = 4.5;

    final XGroup cube = new XGroup();
    final XGroup cameraHolder = new XGroup();
    final PerspectiveCamera camera = new PerspectiveCamera(true);

    final BooleanProperty isRotating = new SimpleBooleanProperty(false);

    private XRectangle[] rect;

    private Highlighter highlighter;

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private GameTimer gameTimer;

    @Override
    public void onMessageReceived(String message) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setDepthTest(DepthTest.ENABLE);

        buildCamera();
        try {
            buildBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SubScene scene = new SubScene(root, 200, 500, true, SceneAntialiasing.BALANCED);
        scene.setCamera(camera);
        scene.setFill(Color.GREY);
        handleKeys(gridPane);

        gridPane.add(scene, 0,2);

        /*gameTimer = new GameTimer();
        gameTimer.setGameLabel(timerLabel);
        gameTimer.startTimer(0);*/
    }

    private void buildCamera() {
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);

        cameraHolder.getChildren().add(camera);

        cameraHolder.rotate(CAMERA_INITIAL_X_ANGLE, Rotate.X_AXIS);
        cameraHolder.rotate(CAMERA_INITIAL_Y_ANGLE, Rotate.Y_AXIS);

        root.getChildren().add(cameraHolder);
    }

    private void buildBody() throws Exception {
        double size = 100;

        rect = new XRectangle[6];
        rect[0] = new XRectangle(0, XRectangle.IMG0_URL, size, -0.5 * size, -0.5 * size, 0.5 * size);
        rect[1] = new XRectangle(1, XRectangle.IMG1_URL, size, -0.5 * size, 0, 0);
        rect[2] = new XRectangle(2, XRectangle.IMG2_URL, size, -1 * size, -0.5 * size, 0);
        rect[3] = new XRectangle(3, XRectangle.IMG3_URL, size, 0, -0.5 * size, 0);
        rect[4] = new XRectangle(4, XRectangle.IMG4_URL, size, -0.5 * size, -1 * size, 0);
        rect[5] = new XRectangle(5, XRectangle.IMG5_URL, size, -0.5 * size, -0.5 * size, -0.5 * size);

        rect[1].setRotationAxis(Rotate.X_AXIS);
        rect[1].setRotate(90);

        rect[2].setRotationAxis(Rotate.Y_AXIS);
        rect[2].setRotate(90);

        rect[3].setRotationAxis(Rotate.Y_AXIS);
        rect[3].setRotate(90);

        rect[4].setRotationAxis(Rotate.X_AXIS);
        rect[4].setRotate(90);

        cube.getChildren().addAll(rect[0], rect[1], rect[2], rect[3], rect[4], rect[5]);
        root.getChildren().add(cube);

        highlighter = new Highlighter(rect);

        highlighter.updateInFront();

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
        gridPane.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    if (!isRotating.get()) {
                        isRotating.set(true);
                        Timer animTimer = new Timer();
                        animTimer.scheduleAtFixedRate(new TimerTask() {
                            double i = 0;

                            @Override
                            public void run() {
                                if (i < 90) {
                                    cube.rotate(KEY_ROTATION_STEP, Rotate.Z_AXIS);
                                } else {
                                    animTimer.cancel();
                                    isRotating.set(false);
                                    highlighter.updateInFront();
                                }
                                i += KEY_ROTATION_STEP;
                            }
                        }, 0, 25);
                    }
                    break;
                case S:
                    if (!isRotating.get()) {
                        isRotating.set(true);
                        Timer animTimer = new Timer();
                        animTimer.scheduleAtFixedRate(new TimerTask() {
                            double i = 0;

                            @Override
                            public void run() {
                                if (i < 90) {
                                    cube.rotate(-KEY_ROTATION_STEP, Rotate.Z_AXIS);
                                } else {
                                    animTimer.cancel();
                                    isRotating.set(false);
                                    highlighter.updateInFront();
                                }
                                i += KEY_ROTATION_STEP;
                            }
                        }, 0, 25);
                    }
                    break;
                case A:
                    if (!isRotating.get()) {
                        isRotating.set(true);
                        Timer animTimer = new Timer();
                        animTimer.scheduleAtFixedRate(new TimerTask() {
                            double i = 0;

                            @Override
                            public void run() {
                                if (i < 90) {
                                    cube.rotate(KEY_ROTATION_STEP, Rotate.Y_AXIS);
                                } else {
                                    animTimer.cancel();
                                    isRotating.set(false);
                                    highlighter.updateInFront();
                                }
                                i += KEY_ROTATION_STEP;
                            }
                        }, 0, 25);
                    }
                    break;
                case D:
                    if (!isRotating.get()) {
                        isRotating.set(true);
                        Timer animTimer = new Timer();
                        animTimer.scheduleAtFixedRate(new TimerTask() {
                            double i = 0;

                            @Override
                            public void run() {
                                if (i < 90) {
                                    cube.rotate(-KEY_ROTATION_STEP, Rotate.Y_AXIS);
                                } else {
                                    animTimer.cancel();
                                    isRotating.set(false);
                                    highlighter.updateInFront();
                                }
                                i += KEY_ROTATION_STEP;
                            }
                        }, 0, 25);
                    }
                    break;
                case Q:
                    highlighter.changeHLIndex(false);
                    highlighter.highlight();
                    break;
                case E:
                    highlighter.changeHLIndex(true);
                    highlighter.highlight();
                    break;
                case SPACE:
                    System.out.println("Face: " + highlighter.getSelectedFace());
                    break;
            }
        });
    }

}