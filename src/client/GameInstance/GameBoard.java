package client.GameInstance;


import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class GameBoard {


    private static final int BOARD_LENGTH = 400;
    private static final int BOARD_DEPTH = 5;
    private static final int BOARD_WIDTH = 5;
    private static int gridDimension;
    private static PhongMaterial mainMat;
    private static PhongMaterial frameMat;
    private static PhongMaterial previewMat;
    private static PhongMaterial selectedFaceMat;
    private static Group boardGroup;
    private static Boolean[] statusOfGridCells;
    private static int[][] faceStates;
    private static int filledFaces = 0;
    private static int faceName = 0;

    public GameBoard(int gridDimension)
    {
        this.gridDimension = gridDimension;


        statusOfGridCells = new Boolean[gridDimension*gridDimension];

        faceStates = new int[gridDimension*gridDimension][2];

        mainMat = new PhongMaterial();
        mainMat.setDiffuseMap(new Image(getClass().getResourceAsStream("CubeFaces/2.jpg")));

        frameMat = new PhongMaterial();
        frameMat.setDiffuseMap(new Image(getClass().getResourceAsStream("black.jpg")));
        frameMat.setSpecularColor(Color.BLACK);

        previewMat = new PhongMaterial();
        previewMat.setDiffuseMap(mainMat.getDiffuseMap());


        selectedFaceMat = new PhongMaterial();
        selectedFaceMat.setDiffuseMap(mainMat.getDiffuseMap());

        boardGroup = createBoard();

    }
    public Group createBoardGroup()
    {
        return boardGroup;
    }

    private Group createBoard() {
        Group boardGroupInst = new Group();

        Box mainBoard = new Box(BOARD_LENGTH, BOARD_LENGTH, BOARD_DEPTH );
        Box frameRight = new Box(BOARD_WIDTH, BOARD_LENGTH + 20, BOARD_DEPTH + 10);
        Box frameLeft = new Box(BOARD_WIDTH, BOARD_LENGTH + 20, BOARD_DEPTH + 10);
        Box frameTop = new Box(BOARD_LENGTH + 20, BOARD_WIDTH, BOARD_DEPTH + 10);
        Box frameBottom = new Box(BOARD_LENGTH + 20, BOARD_WIDTH, BOARD_DEPTH + 10);

        Box[] gridCell = new Box[(gridDimension) * (gridDimension)];

        mainBoard.setMaterial(mainMat);
        frameRight.setMaterial(frameMat);
        frameLeft.setMaterial(frameMat);
        frameTop.setMaterial(frameMat);
        frameBottom.setMaterial(frameMat);

        frameRight.translateXProperty().set(BOARD_LENGTH / 2 + BOARD_WIDTH / 2);
        frameRight.translateZProperty().set(-2.5);

        frameLeft.translateXProperty().set(-BOARD_LENGTH / 2 - BOARD_WIDTH / 2);
        frameLeft.translateZProperty().set(-2.5);

        frameBottom.translateYProperty().set(-BOARD_LENGTH / 2 - BOARD_WIDTH / 2);
        frameBottom.translateZProperty().set(-2.5);

        frameTop.translateYProperty().set(BOARD_LENGTH / 2 + BOARD_WIDTH / 2);
        frameTop.translateZProperty().set(-2.5);

        boardGroupInst.getChildren().add(frameRight);
        boardGroupInst.getChildren().add(frameLeft);
        boardGroupInst.getChildren().add(frameBottom);
        boardGroupInst.getChildren().add(frameTop);
        boardGroupInst.getChildren().add(mainBoard);

        int gridIndex;
        for (int i = 0; i < gridDimension; i++)
            for (int j = 0; j < gridDimension; j++) {

                gridIndex = i * gridDimension + j;
                statusOfGridCells[gridIndex] = Boolean.FALSE;

                gridCell[gridIndex] = new Box(BOARD_LENGTH / gridDimension, BOARD_LENGTH / gridDimension, BOARD_DEPTH + 10);
                gridCell[gridIndex].setId(Integer.toString(gridIndex));
                gridCell[gridIndex].setMaterial(mainMat);


                placeAndRotate(gridCell[gridIndex]);

                /*gridCell[gridIndex].setOnMouseEntered(event -> {
                    int cellId = Integer.parseInt(event.getPickResult().getIntersectedNode().getId());
                    if(previewMat.getDiffuseMap() != mainMat.getDiffuseMap())
                        previewMat.setDiffuseColor(new Color(0.5,0.5,0.5,0.8));
                    else
                        previewMat.setDiffuseColor(new Color(1,1,1,1));
                    for(int r = 0; r < gridDimension*gridDimension; r++)
                    {
                        if(r != cellId) {
                            if (gridCell[r].getMaterial() == previewMat) {
                                gridCell[r].setMaterial(mainMat);
                            }
                        }
                        else {
                            if(gridCell[r].getMaterial() == mainMat){

                                gridCell[r].setMaterial(previewMat);
                            }
                        }
                    }
                });*/
                previewSelectedFace(gridCell,gridIndex);
                gridCell[gridIndex].translateXProperty().set(((BOARD_LENGTH / gridDimension) * (j + 0.5)) + (-BOARD_LENGTH / 2));
                gridCell[gridIndex].translateYProperty().set(((BOARD_LENGTH / gridDimension) * (i + 0.5)) + (-BOARD_LENGTH / 2));
                boardGroupInst.getChildren().add(gridCell[gridIndex]);
            }
        return boardGroupInst;
    }
    private void placeAndRotate(Box gridCell)
    {
        gridCell.setOnMouseClicked(event -> {
            int cellId = Integer.parseInt(event.getPickResult().getIntersectedNode().getId());

            if(event.getButton() == MouseButton.PRIMARY) {
                if (!statusOfGridCells[cellId]&& selectedFaceMat.getDiffuseMap() != mainMat.getDiffuseMap()) {
                    PhongMaterial tmp = new PhongMaterial();
                    tmp.setDiffuseMap(selectedFaceMat.getDiffuseMap());
                    faceStates[cellId][0] = faceName;
                    faceStates[cellId][1] = 0;
                    gridCell.setMaterial(tmp);
                    selectedFaceMat.setDiffuseMap(mainMat.getDiffuseMap());
                    filledFaces++;
                    previewMat.setDiffuseMap(mainMat.getDiffuseMap());
                    statusOfGridCells[cellId] = Boolean.TRUE;
                    for(int m = 0; m < gridDimension*gridDimension; m++)
                    {
                        System.out.println("face name: " + faceStates[m][0]);
                        System.out.println("face state: " + faceStates[m][1]);
                    }
                } else {
                    statusOfGridCells[cellId] = Boolean.FALSE;
                    gridCell.setMaterial(mainMat);
                    faceStates[cellId][0] = 0;
                    faceStates[cellId][1] = 0;
                    filledFaces--;
                }
            }
            else if( event.getButton() == MouseButton.SECONDARY)
            {
                System.out.println("right");
                if (statusOfGridCells[cellId]) {
                    faceStates[cellId][1] = (faceStates[cellId][1] + 1)%4;
                    System.out.println(faceStates[cellId][1]);

                    gridCell.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));
                }
            }
        });
    }
    private void previewSelectedFace(Box[] gridCell, int gridIndex)
    {
        gridCell[gridIndex].setOnMouseEntered(event -> {
            int cellId = Integer.parseInt(event.getPickResult().getIntersectedNode().getId());
            if(previewMat.getDiffuseMap() != mainMat.getDiffuseMap())
                previewMat.setDiffuseColor(new Color(0.5,0.5,0.5,0.8));
            else
                previewMat.setDiffuseColor(new Color(1,1,1,1));
            for(int r = 0; r < gridDimension*gridDimension; r++)
            {
                if(r != cellId) {
                    if (gridCell[r].getMaterial() == previewMat) {
                        gridCell[r].setMaterial(mainMat);
                    }
                }
                else {
                    if(gridCell[r].getMaterial() == mainMat){

                        gridCell[r].setMaterial(previewMat);
                    }
                }
            }
        });
    }
    public void setSelectedFaceMat( int faceName)
    {
        this.faceName = faceName;
        selectedFaceMat.setDiffuseMap(new Image(getClass().getResourceAsStream(Integer.toString(faceName) + ".jpg")));
        previewMat.setDiffuseMap(selectedFaceMat.getDiffuseMap());
    }


    public static int[][] getFaceStates() {
        return faceStates;
    }

    public static int getFilledFaces() {
        return filledFaces;
    }
}
