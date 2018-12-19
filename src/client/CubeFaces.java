package client;

import javafx.scene.image.Image;

public class CubeFaces {

    Image[] faces;
    String[] paths;
    public CubeFaces() {
        paths = new String[6];
        faces = new Image[6];

        initPaths();
        initImages();
    }

    public void initPaths() {
        String base = "assets/";
        for(int i = 1; i <=6; i++) {
            paths[i-1] = base + i + ".jpg";
        }
    }

    public void initImages() {
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
}
