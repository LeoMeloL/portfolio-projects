public class MinMax {

    public int[] minimax(Board board, int depth, boolean maximizingPlayer) {
        if (checkWin(board, "X") == 10) {
            return new int[]{-1, -1, 10};
        } else if (checkWin(board, "O")== 10) {
            return new int[]{-1, -1, -10};
        } else if (isBoardFull(board) || depth == 0) {
            return new int[]{-1, -1, evaluateBoard(board)};
        }
    
        int[] bestMove = new int[]{-1, -1, maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE};
    
        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (board.getBoard()[i][j].equals("-")) {
                    board.getBoard()[i][j] = maximizingPlayer ? "X" : "O";
            
                    int[] currentMove = minimax(board, depth - 1, !maximizingPlayer);
            
                    board.getBoard()[i][j] = "-";
            
                    if ((maximizingPlayer && currentMove[2] > bestMove[2]) ||
                            (!maximizingPlayer && currentMove[2] < bestMove[2])) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestMove[2] = currentMove[2];
                    }
                }
            }
        }
    
        return bestMove;
    }
    
    private int evaluateBoard(Board board) {
        int score = 0;
    
        // Contagem de ameaças imediatas
        for (String player : new String[]{"X", "O"}) {
            for (int i = 0; i < board.getBoard().length; i++) {
                for (int j = 0; j < board.getBoard()[i].length; j++) {
                    if (board.getBoard()[i][j].equals(player)) {
                        // Linhas e colunas
                        if ((i == 0 || board.getBoard()[i - 1][j].equals(player)) &&
                                (i == board.getBoard().length - 1 || board.getBoard()[i + 1][j].equals(player))) {
                            score += player.equals("X") ? 1 : -1;
                        }
                        if ((j == 0 || board.getBoard()[i][j - 1].equals(player)) &&
                                (j == board.getBoard()[i].length - 1 || board.getBoard()[i][j + 1].equals(player))) {
                            score += player.equals("X") ? 1 : -1;
                        }
                        // Diagonais
                        if ((i == 0 || j == 0 || board.getBoard()[i - 1][j - 1].equals(player)) &&
                                (i == board.getBoard().length - 1 || j == board.getBoard()[i].length - 1 || board.getBoard()[i + 1][j + 1].equals(player))) {
                            score += player.equals("X") ? 1 : -1;
                        }
                        if ((i == 0 || j == board.getBoard()[i].length - 1 || board.getBoard()[i - 1][j + 1].equals(player)) &&
                                (i == board.getBoard().length - 1 || j == 0 || board.getBoard()[i + 1][j - 1].equals(player))) {
                            score += player.equals("X") ? 1 : -1;
                        }
                    }
                }
            }
        }
    
        // Controle do centro e ocupação dos cantos
        if (board.getBoard()[1][1].equals("X")) {
            score += 2;
        } else if (board.getBoard()[1][1].equals("O")) {
            score -= 2;
        }
        for (int i = 0; i < board.getBoard().length; i++) {
            if (board.getBoard()[i][0].equals("X") && board.getBoard()[i][board.getBoard().length - 1].equals("X")) {
                score += 1;
            } else if (board.getBoard()[i][0].equals("O") && board.getBoard()[i][board.getBoard().length - 1].equals("O")) {
                score -= 1;
            }
            if (board.getBoard()[0][i].equals("X") && board.getBoard()[board.getBoard().length - 1][i].equals("X")) {
                score += 1;
            } else if (board.getBoard()[0][i].equals("O") && board.getBoard()[board.getBoard().length - 1][i].equals("O")) {
                score -= 1;
            }
        }
    
        return score;
    }
    
    
    
    private static int checkWin(Board board, String player) {
        if (board.checkWin(player)) {
            return player.equals("X") ? 10 : -10;
        } else if (isBoardFull(board)) {
            return 0;
        }
        return -1;
    }
    
    

    private static boolean isBoardFull(Board board) {
        return board.isBoardFull();
    }
}
