/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WumpusGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel {
    private JLabel titleLabel;
    private JButton startButton;
    private JButton exitButton;
    private JButton howToPlayButton;
    private JButton settingsButton;
    private JButton highScoresButton;
    private JButton creditsButton;
    private WumpusGame game;

    public Menu() {
        // Configurar o layout para organizar os componentes verticalmente
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.game = game;

        // Configurar o título
        titleLabel = new JLabel("O MUNDO DE WUMPUS");
        titleLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Criar os botões
        startButton = createButton("Iniciar Jogo");
        exitButton = createButton("Sair");
        howToPlayButton = createButton("Tabuleiro é revelado ?");
        settingsButton = createButton("Configurações");
        highScoresButton = createButton("Placar");
        creditsButton = createButton("Créditos");

        // Adicionar os componentes ao painel
        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 30))); // Espaço em branco
        add(startButton);
        add(howToPlayButton);
        add(settingsButton);
        add(highScoresButton);
        add(creditsButton);
        add(exitButton);
        add(Box.createVerticalGlue());

        // Configurar ações dos botões
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para iniciar o jogo
                game.restartGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        howToPlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
            }
        });
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Menu Principal");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new GameMenu());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}