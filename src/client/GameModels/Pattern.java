package client.GameModels;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;


import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.FileInputStream;
import java.io.IOException;

public class Pattern {

    private static final int BOARD_LENGTH = 500;
    private static final int BOARD_DEPTH = 5;
    private static final int BOARD_WIDTH = 5;
    private static final int SIZE = 128;
    private static PhongMaterial[] gridMat;
    private static int gridDimension;
    private static Group patternGroup;
    private static Box[] gridCell;
    private static int[][] gridMatrix;
    private static final Transform t = new Rotate();
    private ImageView[] patternImageViews;
    private Image[] imagesToCreatePattern = null;
    private GaussianBlur gaussianBlur = new GaussianBlur();

    public Pattern(int gridDimension)
    {
        this.gridDimension = gridDimension;

        gaussianBlur.setRadius(2);

        gridMat = new PhongMaterial[gridDimension*gridDimension];
        patternImageViews =  new ImageView[gridDimension*gridDimension];

        gridCell = new Box[(gridDimension) * (gridDimension)];
        gridMatrix = (new PatternGenerator(gridDimension)).generatePattern(false);

    }


    public void setImagesToCreatePattern(Image[] imagesToCreatePattern){
        this.imagesToCreatePattern = imagesToCreatePattern;
    }

    public void setGivenPattern(int[][] givenPattern){
        gridMatrix = givenPattern;
    }
    public Group createPatternGroup()
    {
        patternGroup = createPattern();
        return patternGroup;
    }

    private Group createPattern() {
        Group boardGroupInst = new Group();
        int gridIndex;



        for (int i = 0; i < gridDimension; i++) {
            for (int j = 0; j < gridDimension; j++) {

                gridIndex = i * gridDimension + j;

                gridCell[gridIndex] = new Box(BOARD_LENGTH / gridDimension, BOARD_LENGTH / gridDimension, 0);
                gridMat[gridIndex] = new PhongMaterial();
                gridCell[gridIndex].setId(Integer.toString(gridIndex));
//                System.out.println(
//                        gridMatrix[gridIndex][0] +" " +gridMatrix[gridIndex][1]);

                if(imagesToCreatePattern == null){
                    String png = Integer.toString(gridMatrix[gridIndex][0]) + ".png";

                    try {
                        //gridMat[gridIndex].setDiffuseMap(new Image(new FileInputStream("assets/CubeFaces/" + png), SIZE, SIZE, true, false));
                        //gridCell[gridIndex].getTransforms().add(new Rotate(90*gridMatrix[gridIndex][1], Rotate.Z_AXIS));
                        patternImageViews[gridIndex] = new ImageView(new Image(new FileInputStream("assets/CubeFaces/" + png), SIZE, SIZE, true, false));
                        patternImageViews[gridIndex].setRotate( 90*gridMatrix[gridIndex][1]);
                        this.smoothImageView(patternImageViews[gridIndex]);
                        gridMat[gridIndex].setDiffuseMap(patternImageViews[gridIndex].snapshot(null,null));
                    } catch (Exception e) {
                        System.out.println("File not found");
                    }
                }
                else {
                    try {
                        gridMat[gridIndex].setDiffuseMap(imagesToCreatePattern[gridIndex]);

                        patternImageViews[gridIndex] = new ImageView(gridMat[gridIndex].getDiffuseMap());
                        this.smoothImageView(patternImageViews[gridIndex]);
                    } catch (Exception e) {
                        System.out.println("File not found");
                    }
                }


                gridCell[gridIndex].setMaterial(gridMat[gridIndex]);

                gridCell[gridIndex].translateXProperty().set(((BOARD_LENGTH / gridDimension) * (j + 0.5)) + (-BOARD_LENGTH / 2));
                gridCell[gridIndex].translateYProperty().set(((BOARD_LENGTH / gridDimension) * (i + 0.5)) + (-BOARD_LENGTH / 2));
                boardGroupInst.getChildren().add(gridCell[gridIndex]);
            }
        }
        System.out.println(boardGroupInst);
        return boardGroupInst;
    }

    public boolean checkPattern(ImageView[] boardImages){

        if( boardImages.length != patternImageViews.length)
            return false;


        for( int i = 0 ; i <  boardImages.length && i < patternImageViews.length; i++ ) {

            this.smoothImageView(boardImages[i]);
            BufferedImage boardImage = SwingFXUtils.fromFXImage(boardImages[i].snapshot(null, null), null);
            BufferedImage patternImage = SwingFXUtils.fromFXImage(patternImageViews[i].snapshot(null, null), null);

            Raster rasterBoard = boardImage.getRaster();
            Raster rasterPattern = patternImage.getRaster();


            for (int x = 0; x < boardImage.getWidth() && x < patternImage.getWidth(); x++) {
                for (int y = 0; y < boardImage.getHeight() && y < patternImage.getHeight(); y++) {
                    int boardR = rasterBoard.getSample(x, y, 0);
                    int boardG = rasterBoard.getSample(x, y, 1);
                    int boardB = rasterBoard.getSample(x, y, 2);

                    int patternR = rasterPattern.getSample(x, y, 0);
                    int patternG = rasterPattern.getSample(x, y, 1);
                    int patternB = rasterPattern.getSample(x, y, 2);


                    if (boardR != patternR || boardG != patternG || boardB != patternB) {
                        System.out.println("Wrong one: " + i);
                        System.out.println("x: " + x + " y: " + y);
                        System.out.println(patternImageViews[i].getRotate() + " rot" + boardImages[i].getRotate());
                        return false;
                    }
                }
            }

        }
                return true;

    }

    public void setMatQuestMark() throws IOException
    {
        for(int i = 0; i < gridDimension*gridDimension; i++)
        {
            gridMat[i].setDiffuseMap(new Image(new FileInputStream("assets/questionMark.png")));
            gridCell[i].setMaterial(gridMat[i]);
        }
    }

    public void showPattern(){
        for(int i = 0; i < gridDimension*gridDimension; i++)
        {
            gridMat[i].setDiffuseMap(patternImageViews[i].snapshot(null, null));
            gridCell[i].setMaterial(gridMat[i]);
        }
    }
    public void smoothImageView(ImageView imageView){

        imageView.setEffect(gaussianBlur);
        imageView.setFitWidth(128);
    }

    public static int[][] getGridMatrix() {
        return gridMatrix;
    }
}
