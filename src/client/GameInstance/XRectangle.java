package client.GameInstance;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class XRectangle extends Rectangle {

    public static final String IMG0_URL = "assets/CubeFaces/1.jpg";
    public static final String IMG1_URL = "assets/CubeFaces/2.jpg";
    public static final String IMG2_URL = "assets/CubeFaces/3.jpg";
    public static final String IMG3_URL = "assets/CubeFaces/4.jpg";
    public static final String IMG4_URL = "assets/CubeFaces/5.jpg";
    public static final String IMG5_URL = "assets/CubeFaces/6.jpg";

    private final String imgUrl;
    private final int id;

    private Image faceImage;

    public XRectangle(int id, String imgUrl, double size, double posX, double posY, double posZ) throws FileNotFoundException {
        super();

        this.id = id;
        this.imgUrl = imgUrl;
        this.faceImage = new Image(new FileInputStream(imgUrl));

        setFill(new ImagePattern(faceImage));

        setHeight(size);
        setWidth(size);

        setTranslateX(posX);
        setTranslateY(posY);
        setTranslateZ(posZ);
    }

    public void hl(boolean isHL) throws FileNotFoundException {
        if (isHL) {
            setFill(new ImagePattern(changeImageColor(faceImage)));
        }
        else {
            setFill(new ImagePattern(faceImage));
        }
    }

    public boolean isInFront() {
        Bounds boundsInScene = localToScene(getBoundsInLocal());
        long X = Math.round(boundsInScene.getMaxX());
        long Y = Math.round(boundsInScene.getMaxY());
        long Z = Math.round(boundsInScene.getMaxZ());

        return X < 0 || Y < 0 || Z < 0;
    }

    public int getID() {
        return this.id;
    }

    private Image changeImageColor(Image image) {
        BufferedImage result  =  SwingFXUtils.fromFXImage(image, null);
        Raster raster = result.getRaster();

        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                int R = raster.getSample(x, y, 0);
                int G = raster.getSample(x, y, 1);
                int B = raster.getSample(x, y, 2);


                if (R < 75 && G < 75 && B < 75) {
                    R = 255;
                }
                else if (R > 175 && G > 175 && B > 175) {
                    R = (int) (R * 0.75);
                }

                Color color = new Color(R, G, B);
                int RGB = color.getRGB();
                result.setRGB(x, y, RGB);
            }
        }

        return SwingFXUtils.toFXImage(result, null);
    }

    public Image getFaceImage() {
        return this.faceImage;
    }
}