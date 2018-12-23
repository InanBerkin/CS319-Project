package client.GameModes.MPGameModes.MultiplayerImageRecreationMode;

import client.GameModels.GameBoard;
import client.GameModels.Pattern;
import client.GameModes.GameInstance;
import client.QBitzApplication;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class MultiplayerImageRecreationModeController extends GameInstance {
    @FXML
    private HBox playersBar;

    @FXML
    private Button submitButton;

    private final int SIZE = 640;

    private Image img;
    private BufferedImage buffImg;

    private ArrayList<Image> imgParts;
    private BufferedImage[] buffImgParts;

    private List<Integer> remainingList;


    @Override
    public void onMessageReceived(String message) {
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("submit")){
            Platform.runLater(() -> {
                updatePlayers(responseJSON.getJSONArray("finishList"));
                if(responseJSON.getBoolean("isGameFinished")){
                    QBitzApplication.getSceneController().gotoMenu("PostGameMenu", responseJSON);
                }
            });
        }
    }
    @Override
    public void initializeGameMode() {
        gameTimer.startTimer();
        String encoded = payload.getString("encodedImage");
        //initialize values
        byte[] outputArr = Base64.getDecoder().decode(encoded.getBytes());

        InputStream in = new ByteArrayInputStream(outputArr);
        try {
            buffImg = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = SwingFXUtils.toFXImage(buffImg,null);

        remainingList = new ArrayList<>();
        for( int i = 0 ; i < gridDimension * gridDimension ; i++)
            remainingList.add(i);
        Collections.shuffle(remainingList);

        //image parts save in bufferedimagearray
        buffImgParts = new BufferedImage[ gridDimension * gridDimension ];
        buffImgParts = splitImage(buffImg, gridDimension);

        this.imgParts = new ArrayList<>();
        this.imgParts = convertBuffToImage(buffImgParts, gridDimension);

        board = new GameBoard(gridDimension,null,this);
        pattern = new Pattern(gridDimension);
        pattern.setImagesToCreatePattern(this.imgParts.toArray(new Image[this.imgParts.size()]));
        this.imageRec();
        updatePlayers(payload.getJSONArray("userList"));
    }

    @Override
    public boolean submit(){
        boolean isPatternTrue = super.submit();
        if(isPatternTrue){
            JSONObject submitJSON = new JSONObject();
            submitJSON.put("requestType", "submit");
            submitJSON.put("roomID", payload.getInt("roomID"));
            submitJSON.put("finishTime", gameTimer.getGameTime().getValue());
            QBitzApplication.getSceneController().sendMessageToServer(submitJSON);
            submitButton.setDisable(true);
            return true;
        }
        else{
            return false;
        }
    }


    private void updatePlayers(JSONArray playersList){
        playersBar.getChildren().clear();
        int players = playersList.length();
        MultiplayerImageRecreationModeController.Player player;
        VBox vBox;
        int id;
        String finishTime = "Solving...";
        int rank = 0;
        String name;
        for (int i = 0; i < players; i++){
            JSONObject playerJSON = (JSONObject) playersList.get(i);
            id = playerJSON.getInt("id");
            name = playerJSON.getString("name");
            if(playerJSON.has("finishTime")){
                finishTime = playerJSON.getString("finishTime");
            }
            if(playerJSON.has("rank")){
                rank = playerJSON.getInt("rank");
            }
            player = new MultiplayerImageRecreationModeController.Player(id,name, finishTime, rank);
            vBox = new VBox(20);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(new Label(player.getName()));
            vBox.getChildren().add(new Label(player.getFinishTime()));
            if(player.getRank() != 0){
                vBox.getChildren().add(new Label(player.getRank() + ""));
            }
            vBox.setStyle("-fx-background-color: #000");
            vBox.setStyle("-fx-background-radius: 5");
            vBox.setStyle("-fx-padding: 20 20;");
            playersBar.getChildren().add(vBox);
        }
    }

    private class Player{
        private int id;
        private String name;
        private String finishTime;
        private int rank;

        public Player(int id, String name, String finishTime, int rank) {
            this.id = id;
            this.name = name;
            this.finishTime = finishTime;
            this.rank = rank;
        }

        public Player(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getFinishTime() {
            return finishTime;
        }

        public String getName() {
            return name;
        }

        public void setFinishTime(String finishTime) {
            this.finishTime = finishTime;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }
    ///**********************FOR_IMAGE_RECREATION************************************
    public Image[] getCubeFaces(){

        Image[] cubeFaces = new Image[6];
        //shuffle get random faces
        Collections.shuffle(this.imgParts);
        for( int i = 0 ; i < 6 ; i++) {
            cubeFaces[i] = this.imgParts.get(i);
        }

        return cubeFaces ;
    }
    public void imageRec()
    {
        Image[] tmp = getCubeFaces();
        for(int i = 0; i < 6; i++) {
            cube.getFaces()[i].setFill(new ImagePattern(tmp[i]));
            cube.getFaces()[i].setFaceImage(tmp[i]);
        }
    }
    public void addFace(Image newImg){
        if( this.imgParts.size() <= gridDimension * gridDimension  - 1 && !this.imgParts.contains(newImg) ){
            this.imgParts.add(newImg);
        }
        else {
            System.out.println("Face List exceed, you cannot add image.");
        }
    }

    public void removeFace(Image delImg){
        if( this.imgParts.size() >= 7  && this.imgParts.contains(delImg)){
            this.imgParts.remove(delImg);
        }
        else {
            System.out.println("Minimal number of faces left. You cannot remove it ");
        }

    }

    public BufferedImage[] splitImage(BufferedImage image, int dimension){

        int chunkWidth = (int)(image.getHeight() / dimension);
        int chunkHeight = (int)(image.getWidth() / dimension);

        BufferedImage[] empty = new BufferedImage[ dimension * dimension ];

        int count = 0 ;
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                //Initialize the image array with image chunks
                empty[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                // draws the image chunk
                Graphics2D gr = empty[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                gr.dispose();

            }
        }

        return empty;

    }
    public ArrayList<Image> convertBuffToImage(BufferedImage[] images, int dimension ){

        ArrayList<Image> temp = new ArrayList<>();

        for ( int i = 0 ; i < dimension * dimension ; i++){
            temp.add(SwingFXUtils.toFXImage(images[i], null));
        }
        return temp;

    }
}
