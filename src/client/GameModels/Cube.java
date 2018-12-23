package client.GameModels;

import client.CubeFaces;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.FileNotFoundException;

public class Cube extends XGroup {

    public static final boolean FORWARD = true;
    public static final boolean BACKWARD = false;

    private double rotateStep;

    private XRectangle[] faces;
    private SimpleBooleanProperty isRotating;
    private Highlighter highlighter;

    public Cube(double size, double rotateStep, double posX, double posY, double posZ) throws FileNotFoundException {
        this.faces = new XRectangle[6];
        this.faces[0] = new XRectangle(0, CubeFaces.getImageAt(1), size, posX + -0.5 * size, posY + -0.5 * size, posZ + 0.5 * size);
        this.faces[1] = new XRectangle(1, CubeFaces.getImageAt(2), size, posX + -0.5 * size, posY + 0, posZ + 0);
        this.faces[2] = new XRectangle(2, CubeFaces.getImageAt(3), size, posX + -1 * size, posY + -0.5 * size, posZ + 0);
        this.faces[3] = new XRectangle(3, CubeFaces.getImageAt(4), size, posX + 0, posY + -0.5 * size, posZ + 0);
        this.faces[4] = new XRectangle(4, CubeFaces.getImageAt(5), size, posX + -0.5 * size, posY + -1 * size, posZ + 0);
        this.faces[5] = new XRectangle(5, CubeFaces.getImageAt(6), size, posX + -0.5 * size, posY + -0.5 * size, posZ + -0.5 * size);
//        this.faces[0] = new XRectangle(0, XRectangle.IMG0_URL, size, posX + -0.5 * size, posY + -0.5 * size, posZ + 0.5 * size);
//        this.faces[1] = new XRectangle(1, XRectangle.IMG1_URL, size, posX + -0.5 * size, posY + 0, posZ + 0);
//        this.faces[2] = new XRectangle(2, XRectangle.IMG2_URL, size, posX + -1 * size, posY + -0.5 * size, posZ + 0);
//        this.faces[3] = new XRectangle(3, XRectangle.IMG3_URL, size, posX + 0, posY + -0.5 * size, posZ + 0);
//        this.faces[4] = new XRectangle(4, XRectangle.IMG4_URL, size, posX + -0.5 * size, posY + -1 * size, posZ + 0);
//        this.faces[5] = new XRectangle(5, XRectangle.IMG5_URL, size, posX + -0.5 * size, posY + -0.5 * size, posZ + -0.5 * size);

        this.faces[1].setRotationAxis(Rotate.X_AXIS);
        this.faces[1].setRotate(90);

        this.faces[2].setRotationAxis(Rotate.Y_AXIS);
        this.faces[2].setRotate(90);

        this.faces[3].setRotationAxis(Rotate.Y_AXIS);
        this.faces[3].setRotate(90);

        this.faces[4].setRotationAxis(Rotate.X_AXIS);
        this.faces[4].setRotate(90);

        getChildren().addAll(faces[0], faces[1], faces[2], faces[3], faces[4], faces[5]);

        this.isRotating = new SimpleBooleanProperty(false);
        this.highlighter = new Highlighter(faces);
        this.highlighter.updateInFront();

        this.rotateStep = rotateStep;
    }

    public boolean isRotating() {
        return isRotating.get();
    }

    public void rotate1() {
        if (!isRotating.get()) {
            isRotating.set(true);

            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
                        double i = 0;

                        @Override
                        public void handle(ActionEvent event) {
                            if (i < 90) {
                                rotate(rotateStep, Rotate.Z_AXIS);
                            } else {
                                timeline.stop();
                                isRotating.set(false);
                                highlighter.updateInFront();
                            }

                            i += rotateStep;
                        }
                    }));
            timeline.play();
        }
    }

    public void rotate2() {
        if (!isRotating.get()) {
            isRotating.set(true);

            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
                        double i = 0;

                        @Override
                        public void handle(ActionEvent event) {
                            if (i < 90) {
                                rotate(-rotateStep, Rotate.Z_AXIS);
                            } else {
                                timeline.stop();
                                isRotating.set(false);
                                highlighter.updateInFront();
                            }

                            i += rotateStep;
                        }
                    }));
            timeline.play();
        }
    }

    public void rotate3() {
        if (!isRotating.get()) {
            isRotating.set(true);
            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
                        double i = 0;

                        @Override
                        public void handle(ActionEvent event) {
                            if (i < 90) {
                                rotate(rotateStep, Rotate.Y_AXIS);
                            } else {
                                timeline.stop();
                                isRotating.set(false);
                                highlighter.updateInFront();
                            }

                            i += rotateStep;
                        }

                    }));
            timeline.play();
        }
    }

    public void rotate4() {
        if (!isRotating.get()) {
            isRotating.set(true);
            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);

            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
                        double i = 0;

                        @Override
                        public void handle(ActionEvent event) {
                            if (i < 90) {
                                rotate(-rotateStep, Rotate.Y_AXIS);
                            } else {
                                timeline.stop();
                                isRotating.set(false);
                                highlighter.updateInFront();
                            }
                            i += rotateStep;
                        }

                    }));
            timeline.play();
        }
    }

    public void highlight(boolean direction) {
        if (direction) {
            highlighter.changeHLIndex(false);
            highlighter.highlight();
        }
        else {
            highlighter.changeHLIndex(true);
            highlighter.highlight();
        }
    }

    public Image selectFace() {
        return faces[highlighter.getSelectedFace()].getFaceImage();
    }

    public void updateFrontFaces() {
        highlighter.updateInFront();
    }

    public XRectangle[] getFaces() {
        return faces;
    }
}
