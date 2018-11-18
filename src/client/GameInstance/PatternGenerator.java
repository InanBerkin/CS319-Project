package client.GameInstance;

public class PatternGenerator {
    private static int gridDimension;
    private static int[][] gridMatrix;
    private static int png;
    private static int state;

    public PatternGenerator(int gridDimension)
    {
        this.gridDimension = gridDimension;
        gridMatrix = new int[gridDimension*gridDimension][2];
    }
    public int[][] generatePattern(boolean hard)
    {
        if(!hard) {
            if (gridDimension == 3) {
                for (int i = 0; i < 2; i++) {
                    png = (int) ((Math.random() * 6) + 1);
                    state = (int) ((Math.random() * 3));
                    for (int j = 0; j < 2; j++) {
                        gridMatrix[j + (i * 2)][0] = png;
                        gridMatrix[j + (i * 2)][1] = state + 3;
                        gridMatrix[j + (i * 2) + 5][0] = png;
                        gridMatrix[j + (i * 2) + 5][1] = state + 3;
                    }
                }
                png = (int) ((Math.random() * 6) + 1);
                state = (int) ((Math.random() * 3));
                gridMatrix[4][0] = png;
                gridMatrix[4][1] = state;

            } else if (gridDimension == 4) {
                int[][] tmp = new int[4][2];

                for (int i = 0; i < 4; i++) {
                    png = (int) ((Math.random() * 6) + 1);
                    state = (int) ((Math.random() * 3));

                    tmp[i][0] = png;
                    tmp[i][1] = state;
                }
                for (int i = 0; i < 2; i++)
                    for (int j = 0; j < 2; j++) {
                        gridMatrix[i + (j * 4)][0] = tmp[j + (i * 2)][0];
                        gridMatrix[i + (j * 4)][1] = tmp[j + (i * 2)][1];

                        gridMatrix[i + (j * 4) + 10][0] = tmp[j + (i * 2)][0];
                        gridMatrix[i + (j * 4) + 10][1] = tmp[j + (i * 2)][1];

                        gridMatrix[j + (i * 4) + 2][0] = tmp[j + (i * 2)][0];
                        gridMatrix[j + (i * 4) + 2][1] = tmp[j + (i * 2)][1]+ 3;

                        gridMatrix[j + (i * 4) + 8][0] = tmp[j + (i * 2)][0];
                        gridMatrix[j + (i * 4) + 8][1] = tmp[j + (i * 2)][1]+ 3;
                    }

            } else {
                int[][] tmp = new int[6][2];

                for (int i = 0; i < 6; i++) {
                    png = (int) ((Math.random() * 6) + 1);
                    state = (int) ((Math.random() * 3));

                    tmp[i][0] = png;
                    tmp[i][1] = state;
                }
                for (int i = 0; i < 2; i++)
                    for (int j = 0; j < 3; j++) {
                        gridMatrix[i + (j * 5)][0] = tmp[j + (i * 2)][0];
                        gridMatrix[i + (j * 5)][1] = tmp[j + (i * 2)][1];
                        gridMatrix[i + (j * 5) + 13][0] = tmp[j + (i * 2)][0];
                        gridMatrix[i + (j * 5) + 13][1] = tmp[j + (i * 2)][1];
                        gridMatrix[j + (i * 5) + 2][0] = tmp[j + (i * 2)][0];
                        gridMatrix[j + (i * 5) + 2][1] = tmp[j + (i * 2)][1] + 3;
                        gridMatrix[j + (i * 5) + 15][0] = tmp[j + (i * 2)][0];
                        gridMatrix[j + (i * 5) + 15][1] = tmp[j + (i * 2)][1] + 3;
                    }
                png = (int) ((Math.random() * 6) + 1);
                state = (int) ((Math.random() * 3));
                gridMatrix[12][0] = png;
                gridMatrix[12][1] = state;
            }
        }
        else
        {
            for(int i = 0; i< gridDimension*gridDimension; i++)
            {
                png = (int) ((Math.random() * 6) + 1);
                state = (int) ((Math.random() * 3));
                gridMatrix[i][0] = png;
                gridMatrix[i][1] = state;
            }
        }

        return gridMatrix;
    }

}
