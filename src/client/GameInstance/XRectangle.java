package client.GameInstance;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class XRectangle extends Rectangle {

    public static final String IMG0_URL = "assets/CubeFaces/1.png";
    public static final String IMG1_URL = "assets/CubeFaces/2.png";
    public static final String IMG2_URL = "assets/CubeFaces/3.png";
    public static final String IMG3_URL = "assets/CubeFaces/4.png";
    public static final String IMG4_URL = "assets/CubeFaces/5.png";
    public static final String IMG5_URL = "assets/CubeFaces/6.png";
    private static final int SIZE = 128;

    private final String imgUrl;
    private final int id;

    private Image faceImage;
    private Image faceImagePad;

    public XRectangle(int id, String imgUrl, double size, double posX, double posY, double posZ) throws FileNotFoundException {
        super();

        this.id = id;
        this.imgUrl = imgUrl;
        this.faceImage = new Image(new FileInputStream(imgUrl),SIZE,SIZE,true,false);
        System.out.println(faceImage.getHeight());
        this.faceImagePad = addBorders(this.faceImage);
        System.out.println(faceImagePad.getHeight());
        setFill(new ImagePattern(faceImagePad));

        setHeight(size);
        setWidth(size);

        setTranslateX(posX);
        setTranslateY(posY);
        setTranslateZ(posZ);
    }



    public void hl(boolean isHL) throws FileNotFoundException {
        if (isHL) {
            setFill(new ImagePattern(changeImageColor()));
        }
        else {
            setFill(new ImagePattern(faceImagePad));
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


    private Image addBorders( Image img ){

        BufferedImage buffImg =  SwingFXUtils.fromFXImage(img, null);

        int widthPad = (int) ( img.getWidth() * 6) / 100;
        int heightPad = (int) ( img.getHeight() * 6) / 100;
        BufferedImage newImage = new BufferedImage(buffImg.getWidth()+ widthPad, buffImg.getHeight() + heightPad, buffImg.getType());

        Graphics g = newImage.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, buffImg.getWidth() + widthPad, buffImg.getHeight() + heightPad);
        g.drawImage(buffImg, (int)widthPad/2, (int)heightPad/ 2 , null);
        g.dispose();

        Image tempImg = SwingFXUtils.toFXImage(newImage, null );
        ImageView tempView = new ImageView(tempImg);
        tempView.setFitWidth(img.getWidth());
        tempView.setPreserveRatio(true);

        return  tempView.snapshot(null, null);


    }

    private Image changeImageColor() {
        BufferedImage base  =  SwingFXUtils.fromFXImage(this.faceImage, null);
            try {

            BufferedImage overlay = SwingFXUtils.fromFXImage(new Image(new FileInputStream("img/selected.png")),null);

            Graphics2D g2d = base.createGraphics();
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.7f));
            int x = (base.getWidth() - overlay.getWidth()) / 2;
            int y = (base.getHeight() - overlay.getHeight()) / 2;
            g2d.drawImage(overlay, x, y, null);
            g2d.dispose();


        } catch (IOException e) {
            e.printStackTrace();
        }
        // Raster raster = result.getRaster();

//        for (int x = 0; x < result.getWidth(); x++) {
//            for (int y = 0; y < result.getHeight(); y++) {
//                int R = raster.getSample(x, y, 0);
//                int G = raster.getSample(x, y, 1);
//                int B = raster.getSample(x, y, 2);
//
//
//                if (R < 75 && G < 75 && B < 75) {
//                    R = 255;
//                }
//                else if (R > 175 && G > 175 && B > 175) {
//                    R = (int) (R * 0.75);
//                }
//
//                Color color = new Color(R, G, B);
//                int RGB = color.getRGB();
//                result.setRGB(x, y, RGB);
//            }
//        }
        Image tempImg = SwingFXUtils.toFXImage(base, null);
        ImageView temp = new ImageView(tempImg);
        temp.setFitWidth(this.faceImage.getWidth());
        temp.setPreserveRatio(true);
        return temp.snapshot(null, null);
    }

    public Image getFaceImage() {
        return this.faceImage;
    }

    public void setFaceImage(Image img){
        this.faceImage = img;
        this.faceImagePad = addBorders(this.faceImage);

    }

}