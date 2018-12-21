package client.GameModels;

public class PatternGenerator {
    private int gridDimension;
    private static int[][] gridMatrix;
    private static int png;
    private static int state;
    private static int numOfPng = 6;
    private static int numOfStates = 4;


    public PatternGenerator(int gridDimension)
    {
        this.gridDimension = gridDimension;
        gridMatrix = new int[gridDimension*gridDimension][2];
    }
    public int[][] generatePattern(boolean hard)
    {
        if(!hard) {
            if (gridDimension == 3) {
                int[][] tmp = new int[2][2];

                for (int i = 0; i < 2; i++) {
                    png = (int) ((Math.random() * numOfPng) + 1);
                    state = (int) ((Math.random() * numOfStates));

                    tmp[i][0] = png;
                    tmp[i][1] = state;
                }
                int patternRandom  = (int) ((Math.random() * 1 ) + 1);
                int patternRandom2  = (int) ((Math.random() * 1 ) + 1);
                if( patternRandom == 1 && patternRandom2 == 1)
                    patternRandom = 0;
                for(int i = 0 ; i< 2 ; i++) {
                    int multiplier = 0;
                    if (i == 0) {
                        multiplier = numOfPng;
                        gridMatrix[0][i] = ((tmp[0][i] + i+patternRandom2) % multiplier)+1;
                        gridMatrix[1][i] = ((tmp[1][i] + i+patternRandom)  % multiplier)+1;
                        gridMatrix[2][i] = ((tmp[0][i] + i+patternRandom)  % multiplier)+1;
                        gridMatrix[3][i] = ((tmp[1][i] + i+patternRandom2) % multiplier)+1;

                        gridMatrix[5][i] = ((tmp[1][i] + i+patternRandom2) % multiplier)+1;
                        gridMatrix[6][i] = ((tmp[0][i] + i+patternRandom)  % multiplier)+1;
                        gridMatrix[7][i] = ((tmp[1][i] + i+patternRandom)  % multiplier)+1;
                        gridMatrix[8][i] = ((tmp[0][i] + i+patternRandom2) % multiplier)+1;



                    } else {
                        multiplier = numOfStates;
                        gridMatrix[0][i] = (tmp[0][i] + 0) % multiplier;
                        gridMatrix[1][i] = (tmp[1][i] + 1) % multiplier;
                        gridMatrix[2][i] = (tmp[0][i] + 1) % multiplier;
                        gridMatrix[3][i] = (tmp[1][i] + 0) % multiplier;

                        gridMatrix[5][i] = (tmp[1][i] + 2) % multiplier;
                        gridMatrix[6][i] = (tmp[0][i] + 3) % multiplier;
                        gridMatrix[7][i] = (tmp[1][i] + 3) % multiplier;
                        gridMatrix[8][i] = (tmp[0][i] + 2) % multiplier;


                    }
                }


                png = (int) ((Math.random() * numOfPng) + 1);
                state = (int) ((Math.random() * numOfStates));
                gridMatrix[4][0] = png;
                gridMatrix[4][1] = state;

            } else if (gridDimension == 4) {
                int[][] tmp = new int[4][2];

                for (int i = 0; i < 4; i++) {
                    png = (int) ((Math.random() * numOfPng) + 1);
                    state = (int) ((Math.random() * numOfStates));

                    tmp[i][0] = png;
                    tmp[i][1] = state;
                }
                int patternRandom  = (int) ((Math.random() * 1 ) + 1);
                int patternRandom2  = (int) ((Math.random() * 1 ) + 1);
                if( patternRandom == 1 && patternRandom2 == 1)
                    patternRandom = 0;

                for(int i = 0 ; i< 2 ; i++) {
                    int multiplier = 0;
                    if (i == 0) {
                        multiplier = numOfPng;
                        gridMatrix[0][i]  = ((tmp[0][i] + i+patternRandom2)  % multiplier)+1;
                        gridMatrix[1][i]  = ((tmp[1][i] + i+patternRandom2)  % multiplier)+1;
                        gridMatrix[2][i]  = ((tmp[2][i] + i+patternRandom)   % multiplier)+1;
                        gridMatrix[3][i]  = ((tmp[0][i] + i+patternRandom)   % multiplier)+1;
                        gridMatrix[4][i]  = ((tmp[2][i] + i+patternRandom2)  % multiplier)+1;
                        gridMatrix[5][i]  = ((tmp[3][i] + i+patternRandom2)  % multiplier)+1;
                        gridMatrix[6][i]  = ((tmp[3][i] + i+patternRandom)   % multiplier)+1;
                        gridMatrix[7][i]  = ((tmp[1][i] + i+patternRandom)   % multiplier)+1;
                        gridMatrix[8][i]  = ((tmp[1][i] + i+patternRandom)  % multiplier)+1;
                        gridMatrix[9][i]  = ((tmp[3][i] + i+patternRandom)  % multiplier)+1;
                        gridMatrix[10][i] = ((tmp[3][i] + i+patternRandom2) % multiplier)+1;
                        gridMatrix[11][i] = ((tmp[2][i] + i+patternRandom2) % multiplier)+1;
                        gridMatrix[12][i] = ((tmp[0][i] + i+patternRandom)  % multiplier)+1;
                        gridMatrix[13][i] = ((tmp[2][i] + i+patternRandom)  % multiplier)+1;
                        gridMatrix[14][i] = ((tmp[1][i] + i+patternRandom2) % multiplier)+1;
                        gridMatrix[15][i] = ((tmp[0][i] + i+patternRandom2) % multiplier)+1;

                    } else {
                        multiplier = numOfStates;
                        gridMatrix[0][i] = (tmp[0][i] + 0) % multiplier;
                        gridMatrix[1][i] = (tmp[1][i] + 0) % multiplier;
                        gridMatrix[2][i] = (tmp[2][i] + 1) % multiplier;
                        gridMatrix[3][i] = (tmp[0][i] + 1) % multiplier;

                        gridMatrix[4][i] = (tmp[2][i] + 0) % multiplier;
                        gridMatrix[5][i] = (tmp[3][i] + 0) % multiplier;
                        gridMatrix[6][i] = (tmp[3][i] + 1) % multiplier;
                        gridMatrix[7][i] = (tmp[1][i] + 1) % multiplier;

                        gridMatrix[8][i] = (tmp[1][i] + 3) % multiplier;
                        gridMatrix[9][i] = (tmp[3][i] + 3) % multiplier;
                        gridMatrix[10][i] = (tmp[3][i] + 2) % multiplier;
                        gridMatrix[11][i] = (tmp[2][i] + 2) % multiplier;

                        gridMatrix[12][i] = (tmp[0][i] + 3) % multiplier;
                        gridMatrix[13][i] = (tmp[2][i] + 3) % multiplier;
                        gridMatrix[14][i] = (tmp[1][i] + 2) % multiplier;
                        gridMatrix[15][i] = (tmp[0][i] + 2) % multiplier;
                    }
                }

            } else {
                int[][] tmp = new int[6][2];

                for (int i = 0; i < 6; i++) {
                    png = (int) ((Math.random() * numOfPng) + 1);
                    state = (int) ((Math.random() * numOfStates));

                    tmp[i][0] = png;
                    tmp[i][1] = state;
                }
                int patternRandom  = (int) ((Math.random() * 1 ) + 1);
                int patternRandom2  = (int) ((Math.random() * 1 ) + 1);
                if( patternRandom == 1 && patternRandom2 == 1)
                    patternRandom = 0;
                for(int i = 0 ; i< 2 ; i++) {
                    int multiplier = 0;
                    if (i == 0) {
                        multiplier = numOfPng;
                        gridMatrix[0][i]  = ((tmp[0][i] + i+patternRandom2 ) % multiplier)+1;
                        gridMatrix[1][i]  = ((tmp[1][i] + i+patternRandom2)  % multiplier)+1;
                        gridMatrix[2][i]  = ((tmp[4][i] + i+patternRandom)   % multiplier)+1;
                        gridMatrix[3][i]  = ((tmp[2][i] + i+patternRandom)   % multiplier)+1;
                        gridMatrix[4][i]  = ((tmp[0][i] + i+patternRandom)   % multiplier)+1;
                        gridMatrix[5][i]  = ((tmp[2][i] + i+patternRandom2)  % multiplier)+1;
                        gridMatrix[6][i]  = ((tmp[3][i] + i+patternRandom2)  % multiplier)+1;
                        gridMatrix[7][i]  = ((tmp[5][i] + i+patternRandom)   % multiplier)+1;
                        gridMatrix[8][i]  = ((tmp[3][i] + i+patternRandom)   % multiplier)+1;
                        gridMatrix[9][i]  = ((tmp[1][i] + i+patternRandom)   % multiplier)+1;
                        gridMatrix[10][i] = ((tmp[4][i] + i+patternRandom2)  % multiplier)+1;
                        gridMatrix[11][i] = ((tmp[5][i] + i+patternRandom2)  % multiplier)+1;
                        gridMatrix[13][i] = ((tmp[5][i] + i+patternRandom2)  % multiplier)+1;
                        gridMatrix[14][i] = ((tmp[4][i] + i+patternRandom2)  % multiplier)+1;
                        gridMatrix[15][i] = ((tmp[1][i] + +patternRandom)    % multiplier)+1;
                        gridMatrix[16][i] = ((tmp[3][i] + i+patternRandom)   % multiplier)+1;

                        gridMatrix[17][i] = ((tmp[5][i] + i+patternRandom)  % multiplier)+1;
                        gridMatrix[18][i] = ((tmp[3][i] + i+patternRandom2) % multiplier)+1;
                        gridMatrix[19][i] = ((tmp[2][i] + i+patternRandom2) % multiplier)+1;
                        gridMatrix[20][i] = ((tmp[0][i] + i+patternRandom)  % multiplier)+1;
                        gridMatrix[21][i] = ((tmp[2][i] + i+patternRandom)  % multiplier)+1;
                        gridMatrix[22][i] = ((tmp[4][i] + i+patternRandom)  % multiplier)+1;
                        gridMatrix[23][i] = ((tmp[1][i] + i+patternRandom2) % multiplier)+1;
                        gridMatrix[24][i] = ((tmp[0][i] + i+patternRandom2) % multiplier)+1;



                    } else {
                        multiplier = numOfStates;
                        gridMatrix[0][i] = (tmp[0][i] + 0) % multiplier;
                        gridMatrix[1][i] = (tmp[1][i] + 0) % multiplier;
                        gridMatrix[2][i] = (tmp[4][i] + 1) % multiplier;
                        gridMatrix[3][i] = (tmp[2][i] + 1) % multiplier;

                        gridMatrix[4][i] = (tmp[0][i] + 1) % multiplier;
                        gridMatrix[5][i] = (tmp[2][i] + 0) % multiplier;
                        gridMatrix[6][i] = (tmp[3][i] + 0) % multiplier;
                        gridMatrix[7][i] = (tmp[5][i] + 1) % multiplier;

                        gridMatrix[8][i] = (tmp[3][i] + 1) % multiplier;
                        gridMatrix[9][i] = (tmp[1][i] + 1) % multiplier;
                        gridMatrix[10][i] = (tmp[4][i] + 0) % multiplier;
                        gridMatrix[11][i] = (tmp[5][i] + 0) % multiplier;

                        gridMatrix[13][i] = (tmp[5][i] + 2) % multiplier;
                        gridMatrix[14][i] = (tmp[4][i] + 2) % multiplier;
                        gridMatrix[15][i] = (tmp[1][i] + 3) % multiplier;
                        gridMatrix[16][i] = (tmp[3][i] + 3) % multiplier;

                        gridMatrix[17][i] = (tmp[5][i] + 3) % multiplier;
                        gridMatrix[18][i] = (tmp[3][i] + 2) % multiplier;
                        gridMatrix[19][i] = (tmp[2][i] + 2) % multiplier;
                        gridMatrix[20][i] = (tmp[0][i] + 3) % multiplier;

                        gridMatrix[21][i] = (tmp[2][i] + 3) % multiplier;
                        gridMatrix[22][i] = (tmp[4][i] + 3) % multiplier;
                        gridMatrix[23][i] = (tmp[1][i] + 2) % multiplier;
                        gridMatrix[24][i] = (tmp[0][i] + 2) % multiplier;


                    }
                }
                png = (int) ((Math.random() * numOfPng) + 1);
                state = (int) ((Math.random() * numOfStates));
                gridMatrix[12][0] = png;
                gridMatrix[12][1] = state;
            }
        }
        else
        {
            for(int i = 0; i< gridDimension*gridDimension; i++)
            {
                png = (int) ((Math.random() * numOfPng) + 1);
                state = (int) ((Math.random() * numOfStates));
                gridMatrix[i][0] = png;
                gridMatrix[i][1] = state;
            }
        }

        return gridMatrix;
    }

}
