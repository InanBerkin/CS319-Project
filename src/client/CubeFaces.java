package client;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;

public class CubeFaces {

    public static Image[] faces;
    public static String[] paths;
    public static String[] newPaths;
    public static int colorOffset = 0;
    String homeDir = System.getProperty("user.home");
    String folderPath = homeDir + File.separator + "qbitz_configs";

    public void initPaths() {
        paths = new String[6];
        String base = "assets/";
        for(int i = 1; i <=6; i++) {
            paths[i-1] = base + i + ".jpg";
        }
    }

    public void initImages() {
        faces = new Image[6];
        for(int i = 0; i<6; i++) {
            faces[i] = new Image(paths[i]);
        }
    }

    public Image getImageAt(int index) {
        if(index >= 0 && index < 6 ) {
            return faces[index];
        }
        return null;
    }

    public void setImageAt(int index, Image imageToSet) {
        if(index >= 0 && index < 6 ) {
            faces[index] = imageToSet;
        }
    }

    public void changeColor(int offset) {
        int realOffset = colorOffset - offset;

        ColorAdjust colorAdjust = new ColorAdjust();

        colorAdjust.setHue(colorAdjust.getHue() + realOffset);
        for(int i = 0; i < 6; i++) {
            ImageView tempView = new ImageView(faces[i]);
            tempView.setEffect(colorAdjust);
            faces[i] = tempView.snapshot(null,null);
        }
    }

    public void saveFaces() {

        try {

            // write object to file
            FileOutputStream fos = new FileOutputStream(folderPath + File.separator + "faces.qbitz");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(faces);
            oos.close();


            //System.out.println("One:" + result.getOne() + ", Two:" + result.getTwo());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadFaces() {
        FileInputStream fis = null;
        try {
            // read object from file
            fis = new FileInputStream(folderPath + File.separator + "faces.qbitz");
            ObjectInputStream ois = new ObjectInputStream(fis);
            faces = (Image[]) ois.readObject();
            ois.close();
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
