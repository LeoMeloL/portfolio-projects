package WumpusGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Botoes {
    
    private TabuleiroComImagens tabuleiro;
    private int gold;
    private JLabel hpLabel; // Adicione um JLabel para mostrar o HP
    private JLabel woodLabel;
    private JLabel arrowLabel;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    int screenWidth = gd.getDisplayMode().getWidth();
    int screenHeight = gd.getDisplayMode().getHeight();
    // Crie um Timer para atualizar o HP a cada segundo
    private Timer hpTimer;

    public Botoes(TabuleiroComImagens tabuleiro) {
        
        this.tabuleiro = tabuleiro;
        
        // Crie um JFrame para a interface gráfica
        JFrame frame = new JFrame("Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());
        gold = tabuleiro.getGold();
        
        hpLabel = new JLabel("HP: " + tabuleiro.getHp2()); // Inicialize o JLabel com o valor inicial de HP
        frame.add(hpLabel); // Adicione o JLabel à janela
        woodLabel = new JLabel("Wood: " + tabuleiro.getWood());
        frame.add(woodLabel);
        arrowLabel = new JLabel("Arrow: " + tabuleiro.getArrow());
        frame.add(arrowLabel);

        // Defina o layout do painel
        frame.setLayout(new FlowLayout());

        // Crie seis botões
        JButton button1 = new JButton("Debug");
        JButton button2 = new JButton("Lanterna");
        JButton button4 = new JButton("Colocar madeira");
        JButton button5 = new JButton("Atirar flecha");
        JButton button9 = new JButton("Criar flecha");
        JButton button3 = new JButton("Modo HARD ?");

        // Adicione ação aos botões
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabuleiro.revealEntireBoard();
                tabuleiro.setGold(1);
                System.out.println("gold coletado");
                System.out.println("botao pressionado");
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           tabuleiro.useLantern();
            }
        });
        
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
           tabuleiro.HARD();
            }
        });
            
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabuleiro.closePoco();
            }
        });

        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {               
                    tabuleiro.monsterDeath();

            }
         });
        
        button9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {               
                    if (tabuleiro.getWood() > 0) {
                        tabuleiro.CriaFlecha();
                        System.out.println("flecha criada");
                        tabuleiro.setWood(tabuleiro.getWood() - 1);
                    } else {
                        System.out.println("sem madeira suficiente");
                    }

            }
         });
                
                 
           

        // Adicione os botões à janela
        frame.add(button1);
        frame.add(button2);
        frame.add(button4);
        frame.add(button5);
        frame.add(button9);
        frame.add(button3);

        // Defina a janela como visível
        frame.setVisible(true);
        
        // Inicialize o Timer para atualizar o HP a cada segundo
        hpTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarStats();
            }
        });
        hpTimer.start(); // Inicie o Timer
    }

    // Método para atualizar o texto do JLabel com o HP atualizado
    public void atualizarStats() {
        int novoHp = tabuleiro.getHp2();
        hpLabel.setText("HP: " + novoHp + " //");
        int novoWood = tabuleiro.getWood();
        woodLabel.setText("Wood: " + novoWood + " //");
        int novoArrow = tabuleiro.getArrow();
        arrowLabel.setText("Arrow: " + novoArrow);
        
    }
}