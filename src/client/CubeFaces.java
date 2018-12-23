package client;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;

public class CubeFaces {

    public static Image[] faces;
    public static String[] paths;
    public static Image[] defaultFaces;
    public static double colorOffset = 50;
    static String homeDir = System.getProperty("user.home");
    static String folderPath = homeDir + File.separator + "qbitz_configs";
    static final int SIZE = 128;

    static double realOffset = 0;

    public static void initPaths() {
        paths = new String[6];
        String base = "assets/CubeFaces/";
        for(int i = 1; i <=6; i++) {
            System.out.println(base + i + ".png");
            paths[i-1] = base + i + ".png";
        }
    }

    public static void initImages() {
        faces = new Image[6];
        defaultFaces = new Image[6];
        for(int i = 0; i<6; i++) {
            try {
                faces[i] = new Image(new FileInputStream(paths[i]),SIZE,SIZE,true,false);
                defaultFaces[i] = new Image(new FileInputStream(paths[i]),SIZE,SIZE,true,false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public static Image[] getAllFaces() {
        return faces;
    }

    public static void setAllFaces() {
        ColorAdjust colorAdjust = new ColorAdjust();

        colorAdjust.setHue(realOffset);
        for(int i = 0; i < 6; i++) {
            ImageView tempView = new ImageView(defaultFaces[i]);
            tempView.setEffect(colorAdjust);
            faces[i] = tempView.snapshot(null,null);
        }
    }
    public static Image getImageAt(int index) {
        index--;
        if(index >= 0 && index < 6 ) {
            return faces[index];
        }
        return null;
    }

    public static void setImageAt(int index, Image imageToSet) {
        index--;
        if(index >= 0 && index < 6 ) {
            faces[index] = imageToSet;
        }
    }

    public static void changeColor(double offset) {
        colorOffset = offset;
        realOffset = ((offset - 50)*2.0/100);

        //System.out.println("Hue: "  + realOffset);

        ColorAdjust colorAdjust = new ColorAdjust();

        colorAdjust.setHue(realOffset);
        for(int i = 0; i < 6; i++) {
            ImageView tempView = new ImageView(defaultFaces[i]);
            tempView.setEffect(colorAdjust);
            faces[i] = tempView.snapshot(null,null);
        }
    }


    public static void saveFaces() {

        try {

            double writeableOffset = realOffset;
            // write object to file
            FileOutputStream fos = new FileOutputStream(folderPath + File.separator + "faces.qbitz");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(writeableOffset);
            oos.close();

            //System.out.println("One:" + result.getOne() + ", Two:" + result.getTwo());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadFaces() {
        FileInputStream fis = null;
        try {
            // read object from file
            fis = new FileInputStream(folderPath + File.separator + "faces.qbitz");
            ObjectInputStream ois = new ObjectInputStream(fis);
            realOffset = (double) ois.readObject();
            ois.close();
            CubeFaces.setAllFaces();
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(OptionsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(OptionsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(OptionsController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                //Logger.getLogger(OptionsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
