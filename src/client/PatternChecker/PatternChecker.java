package client.PatternChecker;

public class PatternChecker {

    public static boolean checkPattern(int[][] mat1, int[][]mat2) {
        for (int row = 0; row < 3; row ++) {
            for (int col = 0; col < 3; col++) {
                if (mat1[row][col] != mat2[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[][] pattern = new int[3][3];
        int[][] board = new int[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                pattern[row][col] = row * 3 + col;
                board[row][col] = row * 3 + col;
            }
        }
        System.out.println(checkPattern(pattern, board));
    }


}
