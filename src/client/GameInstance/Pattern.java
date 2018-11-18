package client.GameInstance;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class Pattern {

    private static final int BOARD_LENGTH = 400;
    private static final int BOARD_DEPTH = 5;
    private static final int BOARD_WIDTH = 5;
    private static PhongMaterial[] gridMat;
    private static int gridDimension;
    private static Group patternGroup;
    private static Box[] gridCell;
    private static int[][] gridMatrix;
    private static Rotate r;
    private static final Transform t = new Rotate();

    public Pattern(int gridDimension)
    {
        this.gridDimension = gridDimension;

        gridMatrix = (new PatternGenerator(gridDimension)).generatePattern(false);

        gridMat = new PhongMaterial[gridDimension*gridDimension];
        //gridMat.setDiffuseMap( new Image(getClass().getResourceAsStream("questionMark.png")));
        gridCell = new Box[(gridDimension) * (gridDimension)];
        patternGroup = createPattern();
    }
    public Group createPatternGroup()
    {
        return patternGroup;
    }

    private Group createPattern()
    {
        Group boardGroupInst = new Group();
        int gridIndex;

        for (int i = 0; i < gridDimension; i++) {
            for (int j = 0; j < gridDimension; j++) {

                gridIndex = i * gridDimension + j;

                gridCell[gridIndex] = new Box(BOARD_LENGTH / gridDimension, BOARD_LENGTH / gridDimension, 0);
                gridMat[gridIndex] = new PhongMaterial();
                gridCell[gridIndex].setId(Integer.toString(gridIndex));
                System.out.println(gridMatrix[gridIndex][0]);
                String png = Integer.toString(gridMatrix[gridIndex][0]) + ".jpg";
                System.out.println(png+ " " + gridMatrix[gridIndex][1]);
                gridMat[gridIndex].setDiffuseMap(new Image(getClass().getResourceAsStream(png)));
                gridCell[gridIndex].setMaterial(gridMat[gridIndex]);
                gridCell[gridIndex].getTransforms().add(new Rotate(90*gridMatrix[gridIndex][1], Rotate.Z_AXIS));

                gridCell[gridIndex].translateXProperty().set(((BOARD_LENGTH / gridDimension) * (j + 0.5)) + (-BOARD_LENGTH / 2));
                gridCell[gridIndex].translateYProperty().set(((BOARD_LENGTH / gridDimension) * (i + 0.5)) + (-BOARD_LENGTH / 2));
                boardGroupInst.getChildren().add(gridCell[gridIndex]);
            }
        }
        System.out.println(boardGroupInst);
        return boardGroupInst;
    }
    public void setMatQuestMark()
    {
        for(int i = 0; i < gridDimension*gridDimension; i++)
        {
            gridMat[i].setDiffuseMap(new Image(getClass().getResourceAsStream("questionMark.png")));
            gridCell[i].setMaterial(gridMat[i]);
        }
    }

}
