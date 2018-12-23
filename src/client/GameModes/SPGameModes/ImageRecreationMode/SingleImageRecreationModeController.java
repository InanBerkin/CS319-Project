package client.GameModes.SPGameModes.ImageRecreationMode;

import client.GameModes.GameInstance;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import client.GameModels.*;
import java.io.FileNotFoundException;

public class SingleImageRecreationModeController extends GameInstance implements  TimerSignable
{
    ///**********************FOR_IMAGE_RECREATION************************************
    private final int SIZE = 600;

    private Image img;
    private BufferedImage buffImg;

    private ArrayList<Image> imgParts;
    private BufferedImage[] buffImgParts;

    private List<Integer> remainingList;

    private String img_path = "assets/recImage.jpg";

    @FXML
    private Label gameStatusLabel;


    @Override
    public void initializeGameMode() {
        gameTimer.startTimer();

        //initialize values
        try {
            this.img = new Image(new FileInputStream(img_path), SIZE, SIZE, false, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //javafx.image.Image to BufferedImage to split parts
        this.buffImg = SwingFXUtils.fromFXImage(img, null);

        remainingList = new ArrayList<>();
        for( int i = 0 ; i < gridDimension * gridDimension ; i++)
            remainingList.add(i);
        Collections.shuffle(remainingList);

        //image parts save in bufferedimagearray
        buffImgParts = new BufferedImage[ gridDimension * gridDimension ];
        buffImgParts = splitImage(buffImg, gridDimension);

        this.imgParts = new ArrayList<>();
        this.imgParts = convertBuffToImage(buffImgParts, gridDimension);

        board = new GameBoard(gridDimension, this, null);
        pattern = new Pattern(gridDimension);
        pattern.setImagesToCreatePattern(this.imgParts.toArray(new Image[this.imgParts.size()]));
        this.imageRec();
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

    @Override
    public boolean submit(){
        boolean isPatternTrue = super.submit();
        if( isPatternTrue) {
            this.gameStatusLabel.setText("You solved the pattern!");
            this.gameTimer.stopTimer();
        }
        else {
            this.gameStatusLabel.setText("Wrong Pattern. Think Again!");
        }

        return isPatternTrue;
    }

    public ArrayList<Image> convertBuffToImage(BufferedImage[] images, int dimension ){

        ArrayList<Image> temp = new ArrayList<>();

        for ( int i = 0 ; i < dimension * dimension ; i++){
            temp.add(SwingFXUtils.toFXImage(images[i], null));
        }
        return temp;

    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public void setBuffImg(BufferedImage buffImg) {
        this.buffImg = buffImg;
    }

    public ArrayList<Image> getImgParts() {
        return imgParts;
    }

    public void setImgParts(ArrayList<Image>  imgParts) {
        this.imgParts = imgParts;
    }

    public BufferedImage[] getBuffImgParts() {
        return buffImgParts;
    }

    public void setBuffImgParts(BufferedImage[] buffImgParts) {
        this.buffImgParts = buffImgParts;
    }

    public int getDimension() {
        return gridDimension;
    }

    public void setDimension(int dimension) {
        gridDimension = dimension;
    }

    public List<Integer> getRemainingList() {
        return remainingList;
    }

    public void setRemainingList(List<Integer> remainingList) {
        this.remainingList = remainingList;
    }

    ///**********************FOR_IMAGE_RECREATION************************************









//*************************************************************FURTHER IMPLEMENTATION***********************************************************
//    public List<Integer> removeSameParts(List<Integer> list) throws  IOException{
//
//        List<Integer> tempList = new ArrayList();
//        for( Integer value : list)
//            tempList.add(value);
//
//
//        for( int i = 0 ; i < list.size()-1 ; i++){
//            for( int j = i+1 ; j < list.size() ; j++ ){
//                if ( isImagesSame(buffImgParts[list.get(i)], buffImgParts[list.get(j)]) && compareHist(createHistogram(buffImgParts[list.get(i)]),createHistogram(buffImgParts[list.get(j)])) > 0.50 ) {
//                    System.out.println(list.get(i) + " " + list.get(j));
//                    double [] temp  = findSimilarity(buffImgParts[list.get(i)], buffImgParts[list.get(j)]);
//                    System.out.println(temp[0]+" "+temp[1]+" "+temp[2]+" "+temp[3]);
//                    tempList.remove(list.get(i));
//                }
//            }
//        }
//
//        return tempList;
//
//    }
//    public void addToList(int value) throws ArrayIndexOutOfBoundsException{
//        if( value < this.dimension * this.dimension) {
//            this.remainingList.add(value);
//            Collections.shuffle(this.remainingList);
//        }
//        else{
//            throw new ArrayIndexOutOfBoundsException("The value should be in range of array length");
//        }
//    }
//
//    public void shuffleParts(){
//        Collections.shuffle(remainingList);
//    }
//
//    public double[][][] findHistograms(BufferedImage simImg) throws IOException{
//
//        BufferedImage[] temp = splitImage(simImg, 2 );
//        for( int i = 0 ; i < 4 ; i++)
//            ImageIO.write(temp[i], "jpg", new File("img" + ((i * i) +30) + ".jpg"));
//
//        double[][] hist1 = createHistogram(temp[0]);
//        double[][] hist2 = createHistogram(temp[1]);
//        double[][] hist3 = createHistogram(temp[2]);
//        double[][] hist4 = createHistogram(temp[3]);
//
//        double[][][] histograms = new double[4][3][256];
//
//        histograms[0] = hist1;
//        histograms[1] = hist2;
//        histograms[2] = hist3;
//        histograms[3] = hist4;
//        return  histograms ;
//
//    }
//
//    public int findSimilarityState(BufferedImage img1) throws  IOException{
//        double[][][] histograms = findHistograms(img1);
//
//        double leftTopRightBottom = compareHist(histograms[0],histograms[3]);
//        double rightTopLeftBottom = compareHist(histograms[1], histograms[2]);
//
//        //0 for there is no symmetry, 1 for left-half symmetry, 2 for right-half symmetry, 3 for full symettry
//        if ( leftTopRightBottom >= 0.85 && rightTopLeftBottom >= 0.85 ){
//            return 3;
//        }
//        else if ( leftTopRightBottom >= 0.85 ){
//            return 1;
//        }
//        else if ( rightTopLeftBottom >= 0.85) {
//            return 2;
//        }
//        else {
//            return 0;
//        }
//
//    }
//
//    public double[] findSimilarity(BufferedImage img1, BufferedImage img2 ) throws  IOException{
//
//        double[][][] histograms1 = findHistograms(img1);
//        double[][][] histograms2 = findHistograms(img2);
//        double sim1 = compareHist(histograms1[0], histograms2[0]);
//        double sim2 = compareHist(histograms1[1], histograms2[1]);
//        double sim3 = compareHist(histograms1[2], histograms2[2]);
//        double sim4 = compareHist(histograms1[3], histograms2[3]);
//
//        double[] similarities = { sim1, sim2, sim3, sim4 };
//
//        return similarities ;
//
//    }
//
//    public boolean isImagesSame(BufferedImage img1, BufferedImage img2) throws  IOException{
//
//        double[] similarity = findSimilarity(img1, img2);
//        if ( (similarity[1] > 0.75 && similarity[2] > 0.75 )  || (similarity[0] > 0.75 && similarity[3] > 0.75))
//            return true;
//        else
//            return false;
//
//    }
//
//
//    public double[][] createHistogram(BufferedImage imageHist) {
//
//
//
//        double[][] histogram = new double[3][];
//        //histogram[0] = new double[100];
//        histogram[0]= new double[256];
//        histogram[1] = new double[256];
//        histogram[2] = new double[256];
//        //fill with zero
//        for (int i = 0; i < histogram.length; i++){
//            Arrays.fill(histogram[i], 0.0);
//        }
//        Raster raster = imageHist.getRaster();
//
//        for( int i = 0 ; i < imageHist.getWidth() ; i++){
//            for( int j = 0 ; j < imageHist.getHeight() ; j++){
////                int[] temp = RGBtoLAB(raster.getSample(i,j,0),raster.getSample(i,j,1),raster.getSample(i,j,2));
////                if( (temp[0] - 1 ) < 0 )
////                    temp[0] = 1;
////                histogram[0][ temp[0] - 1  ] ++;
////                histogram[1][ temp[1]+128 ] ++;
////                histogram[2][ temp[2]+128 ] ++;
//                histogram[0][raster.getSample(i,j,0)]++;
//                histogram[1][raster.getSample(i,j,1)]++;
//                histogram[2][raster.getSample(i,j,2)]++;
//
//            }
//        }
//        for( int i = 0 ; i < 3 ; i++){
//            for( int j = 0 ; (i == 0 && j < 256 ) || ( i != 0 && j < 256 ) ; j++) {
//                histogram[i][j] = histogram[i][j] / (imageHist.getWidth() * imageHist.getHeight());
//            }
//        }
//
//        return histogram;
//    }
//
//    public double compareHist(double[][] hist1, double[][] hist2) {
//
//        double similarityR = 0;
//        double similarityG = 0;
//        double similarityB = 0;
////        double[] histFlat1 = new double[ 3 * 256 ];
////        double[] histFlat2 = new double[ 3 * 256 ];
////
////        for( int i = 0 ; i < 3 ; i++){
////            for( int j = 0 ; j < 256 ; j++){
////                histFlat1[i * 256 + j ] = hist1[i][j];
////                histFlat2[i * 256 + j ] = hist2[i][j];
////
////            }
////        }
////
////        similarity = corrCoef(histFlat1, histFlat2);
//
//
////
//
////
//        similarityR = corrCoef(hist1[0], hist2[0]);
//        similarityG = corrCoef(hist1[1], hist2[1]);
//        similarityB = corrCoef(hist1[2], hist2[2]);
////
////
//        System.out.println((similarityG + similarityB + similarityR)/3);
//
//        return (similarityG + similarityB + similarityR)/3;
//    }
//
//
//    public double chiSquare(double[] X, double[] Y){
//        double dist = 0;
//
//
//        for ( int i = 0 ; i < X.length ; i++ ){
//            double a = X[i];
//            double b = Y[i];
//            dist += 0.5 *( ((a - b ) *( a - b ))/(a+b+1e-18));
//            //System.out.println(dist);
//        }
//
//        return dist;
//    }
//
//    public double corrCoef(double[] X, double[] Y){
//
//        double sum_X = 0, sum_Y = 0, sum_XY = 0;
//        double squareSum_X = 0, squareSum_Y = 0;
//
//        for (int i = 0; i < X.length; i++)
//        {
//            // sum of elements of array X.
//            sum_X = sum_X + X[i];
//
//            // sum of elements of array Y.
//            sum_Y = sum_Y + Y[i];
//
//            // sum of X[i] * Y[i].
//            sum_XY = sum_XY + X[i] * Y[i];
//
//            // sum of square of array elements.
//            squareSum_X = squareSum_X + X[i] * X[i];
//            squareSum_Y = squareSum_Y + Y[i] * Y[i];
//        }
//
//
//        // use formula for calculating correlation coefficient.
//        double corr = (X.length * sum_XY - sum_X * sum_Y)
//                / Math.sqrt((X.length * squareSum_X - sum_X * sum_X)
//                * (X.length * squareSum_Y - sum_Y * sum_Y));
//
//        return corr;
//    }
//
//
//
//
//    public void removeFromList(int value ) throws Exception {
//
//        if ( remainingList.size() > 6 && remainingList.contains(value)){
//            this.remainingList.remove(value);
//            Collections.shuffle(this.remainingList);
//        }
//        else if ( remainingList.contains(value) && remainingList.size() <= 6 ){
//            System.out.println("The value can not be deleted because size is equal 6");
//        }
//        else{
//            throw new  Exception("The list does not contain value : "+  value);
//        }
//
//    }
//
//
//    /**
//     * Convert RGB to XYZ
//     * @param R
//     * @param G
//     * @param B
//     * @return XYZ in double array.
//     */
//    public double[] RGBtoXYZ(int R, int G, int B) {
//        double[][] M   = {{0.4124, 0.3576,  0.1805},
//                {0.2126, 0.7152,  0.0722},
//                {0.0193, 0.1192,  0.9505}};
//        double[] result = new double[3];
//
//        // convert 0..255 into 0..1
//        double r = R / 255.0;
//        double g = G / 255.0;
//        double b = B / 255.0;
//
//        // assume sRGB
//        if (r <= 0.04045) {
//            r = r / 12.92;
//        }
//        else {
//            r = Math.pow(((r + 0.055) / 1.055), 2.4);
//        }
//        if (g <= 0.04045) {
//            g = g / 12.92;
//        }
//        else {
//            g = Math.pow(((g + 0.055) / 1.055), 2.4);
//        }
//        if (b <= 0.04045) {
//            b = b / 12.92;
//        }
//        else {
//            b = Math.pow(((b + 0.055) / 1.055), 2.4);
//        }
//
//        r *= 100.0;
//        g *= 100.0;
//        b *= 100.0;
//
//        // [X Y Z] = [r g b][M]
//        result[0] = (r * M[0][0]) + (g * M[0][1]) + (b * M[0][2]);
//        result[1] = (r * M[1][0]) + (g * M[1][1]) + (b * M[1][2]);
//        result[2] = (r * M[2][0]) + (g * M[2][1]) + (b * M[2][2]);
//
//        return result;
//    }
//
//    /**
//     * Convert XYZ to LAB.
//     * @param X
//     * @param Y
//     * @param Z
//     * @return Lab values
//     */
//    public double[] XYZtoLAB(double X, double Y, double Z) {
//
//        double[] whitePoint = {95.0429, 100.0, 108.8900};
//
//        double x = X / whitePoint[0];
//        double y = Y / whitePoint[1];
//        double z = Z / whitePoint[2];
//
//        if (x > 0.008856) {
//            x = Math.pow(x, 1.0 / 3.0);
//        }
//        else {
//            x = (7.787 * x) + (16.0 / 116.0);
//        }
//        if (y > 0.008856) {
//            y = Math.pow(y, 1.0 / 3.0);
//        }
//        else {
//            y = (7.787 * y) + (16.0 / 116.0);
//        }
//        if (z > 0.008856) {
//            z = Math.pow(z, 1.0 / 3.0);
//        }
//        else {
//            z = (7.787 * z) + (16.0 / 116.0);
//        }
//
//        double[] result = new double[3];
//
//        result[0] = (116.0 * y) - 16.0;
//        result[1] = 500.0 * (x - y);
//        result[2] = 200.0 * (y - z);
//
//        return result;
//    }
//
//    public  int[] RGBtoLAB( int r, int g, int b){
//        double[] tempXYZ = RGBtoXYZ(r, g, b);
//        double[] tempLab = XYZtoLAB(tempXYZ[0], tempXYZ[1], tempXYZ[2]);
//
//        int[] Lab = { (int)Math.round(tempLab[0]),  (int)Math.round(tempLab[1]),  (int)Math.round(tempLab[2])};
//        return Lab;
//    }
//
//    public static void main( String [] args){
//        try {
//            RaceModeController img = new RaceModeController("yildiz.jpg", 4);
//
//
//            //double a = img.compareHist(hist1, hist2);
//            System.out.println(img.findSimilarityState(img.getBuffImgParts()[15]));
//
//        }catch (IOException error){
//            System.out.println("emptys");
//        }
//
//    }

}
