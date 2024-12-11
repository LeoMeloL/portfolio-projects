
package WumpusGame;

import javax.swing.JFrame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.*;
import java.awt.*;

public class WumpusGame extends JPanel{
    
    static JFrame frame;
    private JLabel titleLabel;
    private JButton startButton;
    private JButton exitButton;
    private JButton howToPlayButton;
    private JButton settingsButton;
    private JButton highScoresButton;
    private JButton creditsButton;
    
    
    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();
        
        TabuleiroComImagens tabuleiro = new TabuleiroComImagens(screenWidth, screenHeight);
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        
        
        
        Botoes botoes = new Botoes(tabuleiro);
       // Menu menu = new Menu();
        
        tabuleiro.setFocusable(true);
        tabuleiro.requestFocusInWindow();
        frame.setLayout(new BorderLayout());
        frame.add(tabuleiro, BorderLayout.CENTER);
        //frame.add(menu,BorderLayout.NORTH);
        


        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int playerX = tabuleiro.getPlayer().getXp();
        int playerY = tabuleiro.getPlayer().getYp();
        
        if (keyCode == KeyEvent.VK_LEFT && playerX > 0) {
            if (!tabuleiro.hasPoco(playerX - 1, playerY)) {
                tabuleiro.getPlayer().move(playerX - 1, playerY); 
                tabuleiro.alternarTurnos();
            }
        } else if (keyCode == KeyEvent.VK_RIGHT && playerX < 14) {
            if (!tabuleiro.hasPoco(playerX + 1, playerY)) {
                tabuleiro.getPlayer().move(playerX + 1, playerY);
                tabuleiro.alternarTurnos();
            }
        } else if (keyCode == KeyEvent.VK_UP && playerY > 0) {
            if (!tabuleiro.hasPoco(playerX, playerY - 1)) {
                tabuleiro.getPlayer().move(playerX, playerY - 1);
                tabuleiro.alternarTurnos();
            }
        } else if (keyCode == KeyEvent.VK_DOWN && playerY < 14) {
            if (!tabuleiro.hasPoco(playerX, playerY + 1)) {
                tabuleiro.getPlayer().move(playerX, playerY + 1);
                tabuleiro.alternarTurnos();
            }
        } else if (keyCode == KeyEvent.VK_W) {
            int aim = 1;
            tabuleiro.setAim(aim);
                 
        } else if (keyCode == KeyEvent.VK_A) {
            int aim = 2;
            tabuleiro.setAim(aim);
        } else if (keyCode == KeyEvent.VK_S) {
            int aim = 3;
            tabuleiro.setAim(aim);
        } else if (keyCode == KeyEvent.VK_D){
            int aim = 4;
            tabuleiro.setAim(aim);
                    }
        tabuleiro.repaint(); // Redesenha o tabuleiro após o movimento do jogador
    }       
           
            
            
        });

        frame.setFocusable(true);
        frame.requestFocus();
    }
    
    public void restartGame() {
        
    frame.dispose();
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    int screenWidth = gd.getDisplayMode().getWidth();
    int screenHeight = gd.getDisplayMode().getHeight();
    TabuleiroComImagens tabuleiro = new TabuleiroComImagens(screenWidth, screenHeight);
    JFrame newFrame = new JFrame();
    newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Botoes botoes = new Botoes(tabuleiro);

    tabuleiro.setFocusable(true);
    tabuleiro.requestFocusInWindow();
    newFrame.setLayout(new BorderLayout());
    newFrame.add(tabuleiro, BorderLayout.CENTER);

    newFrame.pack();
    newFrame.setLocationRelativeTo(null);
    newFrame.setVisible(true);
    
    
}
}



    
