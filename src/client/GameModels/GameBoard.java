package client.GameModels;


import client.GameModes.SPGameModes.ImageRecreation.ImageRecreationMode;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

import java.io.FileInputStream;

public class GameBoard {


    private static final int BOARD_LENGTH = 500;
    private static final int BOARD_DEPTH = 5;
    private static final int BOARD_WIDTH = 5;
    private static int gridDimension;
    private static PhongMaterial mainMat;
    private static PhongMaterial frameMat;
    private static PhongMaterial previewMat;
    private static PhongMaterial selectedFaceMat;
    private static Group boardGroup;
    private static Boolean[] statusOfGridCells;
    private static ImageView[] boardImageViews;
    private static ImageRecreationMode imageRecreationMode;

    public GameBoard(int gridDimension, ImageRecreationMode imageRecreationMode)
    {
        this.gridDimension = gridDimension;

        this.imageRecreationMode = imageRecreationMode;

        statusOfGridCells = new Boolean[gridDimension*gridDimension];

        boardImageViews = new ImageView[gridDimension*gridDimension];

        mainMat = new PhongMaterial();
        try {
            mainMat.setDiffuseMap(new Image(new FileInputStream("assets/CubeFaces/boardImage.png")));
        }
        catch (Exception e){
            System.out.println("No file ");
        }


        frameMat = new PhongMaterial();

        try {
            frameMat.setDiffuseMap(new Image(new FileInputStream("assets/black.jpg")));
        }
        catch (Exception e){
            System.out.println("No file ");
        }
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
        Box frameRight = new Box(BOARD_WIDTH, BOARD_LENGTH + 10, BOARD_DEPTH + 10);
        Box frameLeft = new Box(BOARD_WIDTH, BOARD_LENGTH + 10, BOARD_DEPTH + 10);
        Box frameTop = new Box(BOARD_LENGTH + 10, BOARD_WIDTH, BOARD_DEPTH + 10);
        Box frameBottom = new Box(BOARD_LENGTH + 10, BOARD_WIDTH, BOARD_DEPTH + 10);


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
                boardImageViews[gridIndex] = new ImageView();
                boardImageViews[gridIndex].setImage(mainMat.getDiffuseMap());

                placeAndRotate(gridCell[gridIndex]);


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
                if( imageRecreationMode != null)
                    imageRecreationMode.imageRec();
                if (!statusOfGridCells[cellId]&& selectedFaceMat.getDiffuseMap() != mainMat.getDiffuseMap()) {
                    PhongMaterial tmp = new PhongMaterial();
                    tmp.setDiffuseMap(selectedFaceMat.getDiffuseMap());

                    boardImageViews[cellId].setImage(selectedFaceMat.getDiffuseMap());
                    if( imageRecreationMode != null)
                        imageRecreationMode.removeFace(selectedFaceMat.getDiffuseMap());


                    gridCell.setMaterial(tmp);
                    selectedFaceMat.setDiffuseMap(mainMat.getDiffuseMap());

                    previewMat.setDiffuseMap(mainMat.getDiffuseMap());
                    statusOfGridCells[cellId] = Boolean.TRUE;
                } else {
                    statusOfGridCells[cellId] = Boolean.FALSE;

                    if( imageRecreationMode != null)
                        imageRecreationMode.addFace(boardImageViews[cellId].getImage());

                    gridCell.setMaterial(mainMat);

                }
            }
            else if( event.getButton() == MouseButton.SECONDARY)
            {
                System.out.println("right");
                if (statusOfGridCells[cellId]) {

                    boardImageViews[cellId].setRotate((boardImageViews[cellId].getRotate() + 90)%360);

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
    public void setSelectedFaceMat(Image selectedFace)
    {
        selectedFaceMat.setDiffuseMap(selectedFace);
        previewMat.setDiffuseMap(selectedFace);
    }

    public ImageView[] getBoardImageViews()
    {
        return boardImageViews;
    }

}