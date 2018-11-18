package client.GameInstance;

import client.MenuController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GameInstanceController extends MenuController {
    @FXML
    private final Group root = new Group();

    @FXML
    private GridPane gridPane;

    final XformWorld world = new XformWorld();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final XformCamera cameraXform = new XformCamera();
    private static final double CAMERA_INITIAL_DISTANCE = -1000;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    double mousePosX, mousePosY, mouseOldX, mouseOldY, mouseDeltaX, mouseDeltaY;
    double mouseFactorX, mouseFactorY;


    private static final String img3url = "1.jpg";
    private static final String img2url = "2.jpg";
    private static final String img4url = "3.jpg";
    private static final String img6url = "4.jpg";
    private static final String img5url = "5.jpg";
    private static final String img1url = "6.jpg";

    private Image img1;
    private Image img2;
    private Image img3;
    private Image img4;
    private Image img5;
    private Image img6;
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static GameBoard board;

    @Override
    public void onMessageReceived(String message) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        board = new GameBoard(3);
        Pattern pattern = new Pattern(3);
        //pattern.setMatQuestMark();

        Group boardGroup = board.createBoardGroup();
        Group patternGroup = pattern.createPatternGroup();

        patternGroup.translateXProperty().set((WIDTH)*0.70);
        patternGroup.translateYProperty().set(HEIGHT/2);
        patternGroup.translateZProperty().set(0);

        boardGroup.translateXProperty().set((WIDTH)*0.25);
        boardGroup.translateYProperty().set((HEIGHT)/2);
        boardGroup.translateZProperty().set(0);

        Group mainGroup = new Group();
        mainGroup.getChildren().add(boardGroup);
        mainGroup.getChildren().add(patternGroup);

        buildCamera();
        buildBodySystem();
        Camera camera2 = new PerspectiveCamera();
        SubScene scene2 = new SubScene(mainGroup, WIDTH, HEIGHT , true, SceneAntialiasing.BALANCED);

        scene2.setFill(Color.GREY);
        scene2.setCamera(camera2);

        Group sceneGroup = new Group();
        sceneGroup.getChildren().add(scene2);

        SubScene scene = new SubScene(world, 300, HEIGHT, true,SceneAntialiasing.BALANCED);
        scene.setCamera(camera);
        scene.setFill(Color.GREY);
        handleMouse(scene);


        sceneGroup.getChildren().add(scene);

        gridPane.add(scene, 0,1);
        gridPane.add(scene2,1,1);

        mouseFactorX = 180.0 / scene.getWidth();
        mouseFactorY = 180.0 / scene.getHeight();
    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(camera);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
    }

    private void buildBodySystem() {

        img1 = new Image(getClass().getResourceAsStream("1.jpg"));
        img2 = new Image(getClass().getResourceAsStream("2.jpg"));
        img3 = new Image(getClass().getResourceAsStream("3.jpg"));
        img4 = new Image(getClass().getResourceAsStream("4.jpg"));
        img5 = new Image(getClass().getResourceAsStream("5.jpg"));
        img6 = new Image(getClass().getResourceAsStream("6.jpg"));
        Group cube = new Group();
        //size of the cube
        double size = 100;
        //set color for the cube
        Rectangle[] rect = new Rectangle[6];
        for ( int i = 0 ; i < 6 ; i++){
            rect[i] = new Rectangle();
            rect[i].setHeight(size);
            rect[i].setWidth(size);


        }

        rect[0].setTranslateX(-0.5 * size);
        rect[0].setTranslateY(-0.5 * size);
        rect[0].setTranslateZ(0.5 * size);
        rect[0].setFill(new ImagePattern(img1));
        rect[0].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        System.out.println("1");
                        board.setSelectedFaceMat(1);
                    }
                }
            }
        });

        rect[1].setFill(new ImagePattern(img2));

        rect[1].setTranslateX(-0.5 * size);
        rect[1].setTranslateY(0);
        rect[1].setRotationAxis(Rotate.X_AXIS);
        rect[1].setRotate(90);
        rect[1].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        System.out.println("2");
                        board.setSelectedFaceMat(2);
                    }
                }
            }
        });

        rect[2].setFill(new ImagePattern(img3));
        rect[2].setTranslateX(-1 * size);
        rect[2].setTranslateY(-0.5 * size);
        rect[2].setRotationAxis(Rotate.Y_AXIS);
        rect[2].setRotate(90);
        rect[2].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        System.out.println("3");
                        board.setSelectedFaceMat(3);
                    }
                }
            }
        });
        rect[3].setFill(new ImagePattern(img4));

        rect[3].setTranslateX(0);
        rect[3].setTranslateY(-0.5 * size);
        rect[3].setRotationAxis(Rotate.Y_AXIS);
        rect[3].setRotate(90);
        rect[3].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        System.out.println("4");
                        board.setSelectedFaceMat(4);
                    }
                }
            }
        });

        rect[4].setFill(new ImagePattern(img5));
        rect[4].setTranslateX(-0.5 * size);
        rect[4].setTranslateY(-1 * size);
        rect[4].setRotationAxis(Rotate.X_AXIS);
        rect[4].setRotate(90);
        rect[4].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        System.out.println("5");
                        board.setSelectedFaceMat(5);
                    }
                }
            }
        });

        rect[5].setFill(new ImagePattern(img6));
        rect[5].setTranslateX(-0.5 * size);
        rect[5].setTranslateY(-0.5 * size);
        rect[5].setTranslateZ(-0.5 * size);
        rect[5].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        System.out.println("6");
                        board.setSelectedFaceMat(6);
                    }
                }
            }
        });


        cube.getChildren().addAll(
                rect[0],rect[1],rect[2],rect[3],rect[4],rect[5]);

        world.getChildren().addAll(cube);
    }

    private void handleMouse(SubScene scene) {
        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);
            if (me.isPrimaryButtonDown()) {
                cameraXform.ry(mouseDeltaX * 180.0 / scene.getWidth());
                cameraXform.rx(-mouseDeltaY * 180.0 / scene.getHeight());
            } else if (me.isSecondaryButtonDown()) {
                camera.setTranslateZ(camera.getTranslateZ() + mouseDeltaY);
            }
        });
    }
}





class XformWorld extends Group {
    final Translate t = new Translate(0.0, 0.0, 0.0);
    final Rotate rx = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
    final Rotate ry = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);

    public XformWorld() {
        super();
        this.getTransforms().addAll(t, rx, ry, rz);
    }
}

class XformCamera extends Group {
    Point3D px = new Point3D(1.0, 0.0, 0.0);
    Point3D py = new Point3D(0.0, 1.0, 0.0);
    Rotate r;
    Transform t = new Rotate();

    public XformCamera() {
        super();
    }

    public void rx(double angle) {
        r = new Rotate(angle, px);
        this.t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

    public void ry(double angle) {
        r = new Rotate(angle, py);
        this.t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }
}