package WumpusGame;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class GameOverDialog extends JDialog {
    private JButton newGameButton;
    private JButton exitButton;
    TabuleiroComImagens tabuleiro;
    WumpusGame wumpus = new WumpusGame();

    public GameOverDialog(Frame parent, TabuleiroComImagens tabuleiro) {
        super(parent, "FIM", true);
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(parent);
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();
        
        
        JPanel panel = new JPanel();
        JLabel label = new JLabel("O jogo terminou!");
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(label);

        newGameButton = new JButton("Começar Novo Jogo");
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabuleiro.restartGame();
                dispose();
            }
        });

        exitButton = new JButton("Encerrar Jogo");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Encerre o jogo
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newGameButton);
        buttonPanel.add(exitButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Exemplo de Game Over Dialog");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 300);
                frame.setVisible(true);

                //GameOverDialog dialog = new GameOverDialog(frame);
               // dialog.setVisible(true);
            }
        });
    }
}
