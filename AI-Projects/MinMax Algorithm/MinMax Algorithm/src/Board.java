import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JFrame {
    private String[][] board;
    private JButton[][] buttons;
    private MinMax minMaxAlgorithm;

    public Board() {
        board = new String[3][3];
        initBoard();
        minMaxAlgorithm = new MinMax();

        buttons = new JButton[3][3];
        initializeButtons();

        setTitle("Jogo da Velha");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 3));

        addButtonsToBoard();
    }

    private void initBoard() {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                board[i][j] = "-";
            }
        }
    }

    private void initializeButtons() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
                buttons[i][j].setText(board[i][j]);
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        buttonClicked(row, col);
                    }
                });
            }
        }
    }

    private void addButtonsToBoard() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                add(buttons[i][j]);
            }
        }
    }

    private void buttonClicked(int row, int col) {
        if (board[row][col].equals("-")) {
            board[row][col] = "X";
            buttons[row][col].setText("X");  
            if (checkWin("player")){
                JOptionPane.showMessageDialog(this, "Jogador X venceu!");
                resetGame();
            } else {
                if (!isBoardFull()){
                    computerMove();
                    if (checkWin("computer")){
                        resetGame();
                    }
                }
            }
        }     
    }

    protected boolean checkWin(String player) {
        String x;
        if (player.equals("player")){
            x = "X";
        } else {
            x = "O";
        }
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && board[i][0].equals(x)) {
                return true;
            }
        }
    

        for (int j = 0; j < 3; j++) {
            if (board[0][j].equals(board[1][j]) && board[0][j].equals(board[2][j]) && board[0][j].equals(x)) {
                return true;
            }
        }
    

        if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && board[0][0].equals(x)) {
            return true;
        }
        if (board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]) && board[0][2].equals(x)) {
            return true;
        }
    
        return false; 
    }

    private void resetGame(){
        initBoard();
        for (int i = 0; i < board.length; ++i){
            for (int j = 0; j < board.length; ++j){
                buttons[i][j].setText("-");
            }
        }
        
    }

    protected boolean isBoardFull(){
        for (int i = 0; i < board.length; ++i){
            for (int j = 0; j < board.length; ++j){
                if (board[i][j] == "-"){
                    return false;
                }
            }
        }

        return true;
    }

    private void computerMove() {
        int[] bestMove = minMaxAlgorithm.minimax(this, 5, true);
        int row = bestMove[0];
        int col = bestMove[1];
    
        board[row][col] = "O";
        buttons[row][col].setText("O");
    
        if (checkWin("computer")) {
            JOptionPane.showMessageDialog(this, "Jogador O venceu!");
            resetGame();
        }
    }

    public String[][] getBoard() {
        return board;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Board().setVisible(true);
            }
        });
    }
}
