package client.PatternChecker;

public class PatternChecker {

    private int[][] board;
    private int[][] pattern;
    private int gridDimension;

    public PatternChecker(int[][] board, int[][] pattern, int gridDimension){
           this.board = board;
           this.pattern = pattern;
           this.gridDimension = gridDimension;
    }

    public  boolean checkPattern() {
        for (int row = 0; row < gridDimension*gridDimension; row++) {
            System.out.println("Board: " + board[row][0]);
            System.out.println("Pattern: " + pattern[row][0]);
                if (board[row][0] != pattern[row][0]) {
                    return false;
                }
                if (board[row][0] >= 5 && board[row][1] != pattern[row][1]){
                    System.out.println("state " + row);
                    return false;
                }
        }
        return true;
    }

}
