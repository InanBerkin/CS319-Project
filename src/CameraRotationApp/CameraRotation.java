package CameraRotationApp;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;

public class CameraRotation extends Application {
    public Rotate yRotate;
    public Rotate xRotate;
    public Rotate zRotate;
    public Box box;
    public boolean rotating = false;

    private Parent createContent() throws Exception {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final PhongMaterial goldenrod = new PhongMaterial();
        goldenrod.setDiffuseColor(Color.DARKGOLDENROD);
        goldenrod.setSpecularColor(Color.GOLDENROD);


        final Box xAxis = new Box(300, 0.2, 0.2);
        final Box yAxis = new Box(0.2, 300, 0.2);
        final Box zAxis = new Box(0.2, 0.2, 300);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        ////////////////////////////////

        Image diffuseMap = new Image(new FileInputStream("./assets/1.png"));

        box = new Box(5, 5, 5);
        goldenrod.setDiffuseMap(diffuseMap);
        box.setMaterial(goldenrod);

        Translate pivot = new Translate();
        yRotate = new Rotate(0, Rotate.Y_AXIS);
        zRotate = new Rotate(0, Rotate.Z_AXIS);
        xRotate = new Rotate(0, Rotate.X_AXIS);

        // Create and position camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        box.getTransforms().addAll(xRotate, yRotate, zRotate);

        camera.getTransforms().addAll (
                pivot,
                new Rotate(0, Rotate.Y_AXIS),
                new Rotate(-20, Rotate.X_AXIS),
//                yRotate,
//                xRotate,
                new Translate(0, 0, -50)
        );



        // Build the Scene Graph
        Group root = new Group();
        root.getChildren().add(camera);
        root.getChildren().add(box);
        root.getChildren().addAll(xAxis, yAxis, zAxis);


        // set the pivot for the camera position animation base upon mouse clicks on objects
        root.getChildren().stream()
                .filter(node -> !(node instanceof Camera))
                .forEach(node ->
                        node.setOnMouseClicked(event -> {
                            pivot.setX(node.getTranslateX());
                            pivot.setY(node.getTranslateY());
                            pivot.setZ(node.getTranslateZ());
                        })
                );

        // Use a SubScene
        SubScene subScene = new SubScene(
                root,
                300,300,
                true,
                SceneAntialiasing.BALANCED
        );

        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);

        return group;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        Scene scene = new Scene(createContent());
        stage.setScene(scene);
        stage.show();
        scene.setOnKeyPressed(event -> {
            if(!rotating) {
                switch (event.getCode()) {
                    case UP:
                        System.out.println("UP");
                        //animate(xRotate, false);
                        matrixRotateNode(0,90,0);
                        break;
                    case DOWN:
                        System.out.println("DOWN");
                        animate(xRotate, true);
                        break;
                    case LEFT:
                        System.out.println("LEFT");
                        animate(yRotate, false);
                        break;
                    case RIGHT:
                        System.out.println("RIGHT");
                        animate(yRotate, true);
                        break;
                    case Q:
                        System.out.println("Q");
                        animate(zRotate, false);
                        break;
                    case E:
                        System.out.println("E");
                        animate(zRotate, true);
                        break;
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);

    }


    public void animate(Rotate rotate, boolean reverse){
        // animate the camera position.
        DoubleProperty angle = rotate.angleProperty();
        int x = angle.intValue();
        Timeline timeline;
        if(reverse){
            timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0),
                            new KeyValue(rotate.angleProperty(), x)
                    ),
                    new KeyFrame(
                            Duration.seconds(0.5),
                            new KeyValue(rotate.angleProperty(), x-90)
                    )
            );
        }
        else {
            timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0),
                            new KeyValue(rotate.angleProperty(), x)
                    ),
                    new KeyFrame(
                            Duration.seconds(0.5),
                            new KeyValue(rotate.angleProperty(), x+90)
                    )
            );
        }
        rotating = true;
        timeline.setOnFinished(event -> {
            System.out.println(rotate.angleProperty());
            rotating = false;
        });
        timeline.play();
    }

    private void matrixRotateNode(double alf, double bet, double gam){
        double A11=Math.cos(alf)*Math.cos(gam);
        double A12=Math.cos(bet)*Math.sin(alf)+Math.cos(alf)*Math.sin(bet)*Math.sin(gam);
        double A13=Math.sin(alf)*Math.sin(bet)-Math.cos(alf)*Math.cos(bet)*Math.sin(gam);
        double A21=-Math.cos(gam)*Math.sin(alf);
        double A22=Math.cos(alf)*Math.cos(bet)-Math.sin(alf)*Math.sin(bet)*Math.sin(gam);
        double A23=Math.cos(alf)*Math.sin(bet)+Math.cos(bet)*Math.sin(alf)*Math.sin(gam);
        double A31=Math.sin(gam);
        double A32=-Math.cos(gam)*Math.sin(bet);
        double A33=Math.cos(bet)*Math.cos(gam);

        double d = Math.acos((A11+A22+A33-1d)/2d);
        if(d!=0d){
            double den = 2d*Math.sin(d);
            Point3D p = new Point3D((A32-A23)/den,(A13-A31)/den,(A21-A12)/den);
            System.out.println(p);
            box.setRotationAxis(p);
            box.setRotate(Math.toDegrees(d));
        }
    }

}