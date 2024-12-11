package WumpusGame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TabuleiroComImagens extends JPanel {
    private BufferedImage image;
    private Player player;
    private Monster monster;
    private BufferedImage monsterImage;
    private boolean[][] revealed; // Matriz de revelação de quadrados
    private GoldBar goldBar;
    private List<Wood> wood;
    private int woodQtd = 0;
    private BufferedImage goldImage;
    private int gold = 0;
    Random random = new Random();
    int numeroAleatorioX = random.nextInt(14);
    int numeroAleatorioY = random.nextInt(14);
    boolean isMonsterTurn = false;
    private Random random2 = new Random();
    private List<Poco> poco;
    private BufferedImage pocoImage;
    private Botoes botao;
    private BufferedImage knightMonsterImage;
    private boolean isPlayerTurn = false;
    private KnightMonster knightMonster;
    private int hits = 100;
    private boolean isMonsterAlive = true;
    private boolean isKnightMonsterAlive = true;
    private Flecha flecha;
    private int v = 10;
    private GameMenu menu;
    private BufferedImage monsterWarningImage;
    private boolean[][] monsterWarning;
    private boolean[][] goldWarning;
    private boolean[][] pocoWarning;
    private boolean isAimingUp = false;
    private boolean isAimingDown = false;
    private boolean isAimingLeft = false;
    private boolean isAimingRight = true;
    private BufferedImage PlayerEsq;
    private BufferedImage PlayerUp;
    private BufferedImage PlayerDown;
    private BufferedImage PlayerRight;  
    private BufferedImage monsterWarningLeftImage;
    private BufferedImage monsterWarningUpImage;
    private BufferedImage monsterWarningDownImage;
    private BufferedImage ventoDireitaImage;
    private BufferedImage ventoEsquerdaImage;
    private BufferedImage ventoCimaImage;
    private BufferedImage ventoBaixoImage;
    private BufferedImage goldShineImage;
    private BufferedImage woodImage;
    private BufferedImage pocoCloseImage;
    private boolean HARD = false;

    
    

    public TabuleiroComImagens(int screenWidth, int screenHeight) {
        
        setPreferredSize(new Dimension(screenWidth, screenHeight - 100));

        poco = new ArrayList<>();
        wood = new ArrayList<>();
        menu = new GameMenu();
        
                
        
            int aleatorioX = random2.nextInt(14);
            int aleatorioY = random2.nextInt(14);
            goldBar = new GoldBar(aleatorioX, aleatorioY);


        for (int i = 0; i < random2.nextInt(10) + 2; ++i) {
            aleatorioX = random2.nextInt(14);
            aleatorioY = random2.nextInt(14);
            poco.add(new Poco(aleatorioX, aleatorioY));
        }
        
        for (int i = 0; i < random2.nextInt(5) + 1; ++i) {
            aleatorioX = random2.nextInt(14);
            aleatorioY = random2.nextInt(14);
            wood.add(new Wood(1,aleatorioX, aleatorioY));
        }

        try {
            // Carregue a imagem do sistema de arquivos usando um caminho absoluto
            File imageFile = new File("img/chao.png");
            image = ImageIO.read(imageFile);
            
            File knightMonsterImageFile = new File("img/monstro2chao.png");
            knightMonsterImage = ImageIO.read(knightMonsterImageFile);


            File monsterImageFILE = new File("img/monstrochao.png");
            monsterImage = ImageIO.read(monsterImageFILE);

            File goldImageFile = new File("img/ourochao.png");
            goldImage = ImageIO.read(goldImageFile);

            File pocoImageFile = new File("img/pocofechado.png");
            pocoImage = ImageIO.read(pocoImageFile);


            File monsterWarningImageFile = new File("img/indodireitabafo.png");
            monsterWarningImage = ImageIO.read(monsterWarningImageFile);
            
            File monsterWarningLeftImageFile = new File("img/indoesquerdabafo.png");
            monsterWarningLeftImage = ImageIO.read(monsterWarningLeftImageFile);
            
            File monsterWarningUpImageFile = new File("img/indobaixobafo.png");
            monsterWarningUpImage = ImageIO.read(monsterWarningUpImageFile);
            
            File monsterWarningDownImageFile = new File("img/indocimabafo.png");
            monsterWarningDownImage = ImageIO.read(monsterWarningDownImageFile);
            
            File ventoDirImageFile = new File("img/indodireitavento.png");
            ventoDireitaImage = ImageIO.read(ventoDirImageFile);
            
            File ventoEsqImageFile = new File("img/indoesquerdavento.png");
            ventoEsquerdaImage = ImageIO.read(ventoEsqImageFile);
            
            File ventoCimaImageFile = new File("img/indocimavento.png");
            ventoCimaImage = ImageIO.read(ventoCimaImageFile);
            
            File ventoBaixoImageFile = new File("img/indobaixovento.png");
            ventoBaixoImage = ImageIO.read(ventoBaixoImageFile);
            
            File goldShineImageFile = new File("img/chaovento.png");
            goldShineImage = ImageIO.read(goldShineImageFile);
            
            File woodImageFile = new File("img/madeirachao.png");
            woodImage = ImageIO.read(woodImageFile);
            
            File pocoClosedImageFile = new File("img/pocoaberto.png");
            pocoCloseImage = ImageIO.read(pocoClosedImageFile);
            
            
            
            
            
            
            
            monsterWarning = new boolean[15][15]; // Inicialize a matriz de aviso
            goldWarning = new boolean[15][15];
            pocoWarning = new boolean[15][15];
          
            
            

            player = new Player(0, 14, PlayerRight);
            if (numeroAleatorioX == 0) {
                numeroAleatorioX = random.nextInt(13);
            }
            if (numeroAleatorioY == 14) {
                numeroAleatorioY = random.nextInt(13);
            }
            monster = new Monster(numeroAleatorioX, numeroAleatorioY, monsterImage);
            numeroAleatorioX = random.nextInt(13);
            numeroAleatorioY = random.nextInt(13);
            
            if (numeroAleatorioX == 0) {
                numeroAleatorioX = random.nextInt(13);
            }
            if (numeroAleatorioY == 14) {
                numeroAleatorioY = random.nextInt(13);
            }
            
            knightMonster = new KnightMonster(numeroAleatorioX, numeroAleatorioY, monsterImage);
            // Defina a referência ao tabuleiro
            player.setTabuleiro(this);
            monster.setTabuleiro(this);

            int cols = 15;
            int rows = 15;
            revealed = new boolean[cols][rows];
           
            revealed[player.getXp()][player.getYp()] = true;
            
            flecha = new Flecha(v, player, monster, knightMonster);
            
            PlayerEsq = player.getImageEsquerda();      
            PlayerUp = player.getImageCima();
            PlayerDown = player.getImageBaixo();
            PlayerRight = player.getImageDireita();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int rows = 15;
    int cols = 15;
    int squareSize = Math.min(getWidth() / cols, getHeight() / rows);

    for (int y = 0; y < rows; ++y) {
        for (int x = 0; x < cols; ++x) {
            if (revealed[x][y]) { // Se o quadrado estiver revelado
                if ((x + y) % 2 == 0) {
                    g.setColor(Color.black);
                } else {
                    g.setColor(Color.black);
                }
                

                
                    g.drawImage(image, x * squareSize, y * squareSize, squareSize, squareSize, null);
 

                // Desenhe elementos (ouro, poços, monstro) nas posições reveladas
                
                for (int xz = 0; xz < cols; ++xz) {
                    for (int yz = 0; yz < rows; ++yz) {
                        if (monsterWarning[xz][yz] && isAimingRight) {
                            g.drawImage(monsterWarningImage, xz * squareSize, yz * squareSize, squareSize, squareSize, null);
                        }
                        
                        if (monsterWarning[xz][yz] && isAimingLeft) {
                            g.drawImage(monsterWarningLeftImage, xz * squareSize, yz * squareSize, squareSize, squareSize, null);
                        }
                        
                        if (monsterWarning[xz][yz] && isAimingUp) {
                            g.drawImage(monsterWarningUpImage, xz * squareSize, yz * squareSize, squareSize, squareSize, null);
                        }
                        
                        if (monsterWarning[xz][yz] && isAimingDown) {
                            g.drawImage(monsterWarningDownImage, xz * squareSize, yz * squareSize, squareSize, squareSize, null);
                        }
                    }
                }
                
                for (int xz = 0; xz < cols; ++xz) {
                    for (int yz = 0; yz < rows; ++yz) {
                        
                        if(goldWarning[xz][yz] && isAimingRight) {
                            g.drawImage(ventoDireitaImage, xz * squareSize, yz * squareSize, squareSize, squareSize, null);

                        }
                        
                        if(goldWarning[xz][yz] && isAimingLeft) {
                            g.drawImage(ventoEsquerdaImage, xz * squareSize, yz * squareSize, squareSize, squareSize, null);

                        }
                        
                        if(goldWarning[xz][yz] && isAimingUp) {
                            g.drawImage(ventoCimaImage, xz * squareSize, yz * squareSize, squareSize, squareSize, null);

                        }
                        
                        if(goldWarning[xz][yz] && isAimingDown) {
                            g.drawImage(ventoBaixoImage, xz * squareSize, yz * squareSize, squareSize, squareSize, null);

                        }
                    }
                }
                    if (goldBar.getX() == x && goldBar.getY() == y) {
      
                        g.drawImage(goldImage, x * squareSize, y * squareSize, squareSize, squareSize, null);
                    }
              
                    
                    for (Poco pocoAtual : poco) {
                        for (int xz = 0; xz < cols; ++xz) {
                            for (int yz = 0; yz < rows; ++yz) {
                                
                                if (pocoWarning[xz][yz] && isAimingRight) {
                                    g.drawImage(ventoDireitaImage, xz * squareSize, yz * squareSize, squareSize, squareSize, null);

                                }
                                
                                if (pocoWarning[xz][yz] && isAimingLeft) {
                                    g.drawImage(ventoEsquerdaImage, xz * squareSize, yz * squareSize, squareSize, squareSize, null);

                                }
                                
                                if (pocoWarning[xz][yz] && isAimingUp) {
                                    g.drawImage(ventoCimaImage, xz * squareSize, yz * squareSize, squareSize, squareSize, null);

                                }
                                
                                if (pocoWarning[xz][yz] && isAimingDown) {
                                    g.drawImage(ventoBaixoImage, xz * squareSize, yz * squareSize, squareSize, squareSize, null);

                                }
                            }
                    }
              }
                
                    
                    
                for (Poco pocoAtual : poco) {
                    if (pocoAtual.getXp() == x && pocoAtual.getYp() == y && !pocoAtual.isClosed()) {
                        g.drawImage(pocoImage, x * squareSize, y * squareSize, squareSize, squareSize, null);
                    }
                    
                    if (pocoAtual.getXp() == x && pocoAtual.getYp() == y && pocoAtual.isClosed()){
                        g.drawImage(pocoCloseImage, x * squareSize, y * squareSize, squareSize, squareSize, null);
                    }
                }
                
                for (Wood woodAtual : wood) {
                    if (woodAtual.getX() == x && woodAtual.getY() == y){
                        g.drawImage(woodImage, x * squareSize, y * squareSize, squareSize, squareSize, null);
                    }
                }

                    if (isMonsterAlive) {
                        int monsterX = monster.getXp();
                        int monsterY = monster.getYp();
                        if (monsterX == x && monsterY == y) {
                             g.drawImage(monsterImage, x * squareSize, y * squareSize, squareSize, squareSize, null);
                         }
                }
                    if (isKnightMonsterAlive){
                int knightMonsterX = knightMonster.getXp();
                int knightMonsterY = knightMonster.getYp();
                if (knightMonsterX == x && knightMonsterY == y) {
                    g.drawImage(knightMonsterImage, x * squareSize, y * squareSize, squareSize, squareSize, null);
                }
                    }
                
            } else {
                g.setColor(Color.black);
                g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
            }

            int playerX = player.getXp();
            int playerY = player.getYp();
            if (playerX == x && playerY == y && isAimingRight) {
                g.drawImage(PlayerRight, x * squareSize, y * squareSize, squareSize, squareSize, null);
            }
            
            if (playerX == x && playerY == y && isAimingLeft) {
                g.drawImage(PlayerEsq, x * squareSize, y * squareSize, squareSize, squareSize, null);
            }
            if (playerX == x && playerY == y && isAimingUp) {
                g.drawImage(PlayerUp, x * squareSize, y * squareSize, squareSize, squareSize, null);
            }
            if (playerX == x && playerY == y && isAimingDown) {
                g.drawImage(PlayerDown, x * squareSize, y * squareSize, squareSize, squareSize, null);
            }
        }
    }
}

    // Método para revelar GoldBar e Poco quando o jogador passar por eles
        private void revealGoldBarsAndPocos(int x, int y) { 
            if (!goldBar.isCollected() && goldBar.getX() == x && goldBar.getY() == y) {
                goldBar.collect();
                gold += 1;
                if (gold == 3) {
                    System.out.println("Finished game");
                }
                System.out.println("gold: " + gold);
                revealed[x][y] = true; // Marque a posição como revelada
                repaint();
            }

        for (Poco pocoAtual : poco) {
            if (pocoAtual.getXp() == x && pocoAtual.getYp() == y) {
                // Lide com o comportamento quando o jogador encontrar um poço (por exemplo, jogador perde o jogo)
                revealed[x][y] = true; // Marque a posição como revelada
                repaint();
            }
        }
     
    }
        

    // Método para revelar o monstro quando o jogador se aproximar dele
    public void revealMonster() {
        int playerX = player.getXp();
        int playerY = player.getYp();
        int monsterX = monster.getXp();
        int monsterY = monster.getYp();
        
            revealed[monsterX][monsterY] = true; // Marque a posição do monstro como revelada
            repaint();
        }        
  
   public void checkGoldCollection(int x, int y) {

 
        if (!goldBar.isCollected() && goldBar.getX() == x && goldBar.getY() == y) {
            this.setGold(1);
            goldBar.collect();
            repaint();
        }
        for (Wood woodAtual : wood){
            
            if (!woodAtual.isCollected() && woodAtual.getX() == x && woodAtual.getY() == y) {
                setWood(woodQtd++);
                woodAtual.collect();
                woodQtd++;
                System.out.println("wood + 1");
                repaint();
            }
        }

}

    public Player getPlayer() {
        return player;
    }
    public Monster getMonster() {
        return monster;
    }
    
public void revealSquare(int x, int y) {
    if (x >= 0 && x < revealed.length && y >= 0 && y < revealed[0].length) {
        revealed[x][y] = true;

        // Verifique se o jogador está atualmente na posição (x, y)
        int playerX = player.getXp();
        int playerY = player.getYp();
        
        if (playerX == x && playerY == y) {
            // Verifique se há barras de ouro ou poços nas posições reveladas pelo jogador

                if (!goldBar.isCollected() && goldBar.getX() == x && goldBar.getY() == y) {
                    goldBar.collect();
                    this.setGold(1);// Coleta a barra de ouro
         
            }

            for (Poco pocoAtual : poco) {
                if (pocoAtual.getXp() == x && pocoAtual.getYp() == y) {
                    // Lide com o comportamento quando o jogador encontrar um poço (por exemplo, jogador perde o jogo)
                }
            }
        }

        repaint(); // Redesenha o tabuleiro com base no novo estado de revelação
    }
}
    
    public void revealEntireBoard() {
    int cols = 15;
    int rows = 15;

    // Marque todas as células como reveladas e revele ouro, poços e monstros
    for (int x = 0; x < cols; ++x) {
        for (int y = 0; y < rows; ++y) {
            revealed[x][y] = true;

            // Verifique se há barras de ouro nas posições reveladas 
                if (!goldBar.isCollected() && goldBar.getX() == x && goldBar.getY() == y) {
                    goldBar.collect();
                
            }

            // Verifique se há poços nas posições reveladas
            for (Poco pocoAtual : poco) {
                if (pocoAtual.getXp() == x && pocoAtual.getYp() == y) {
                    // Lide com o comportamento quando o jogador encontrar um poço (por exemplo, jogador perde o jogo)
                }
            }
            
            for (Wood woodAtual : wood){
                
            }

            // Verifique se o monstro está na posição revelada
            int monsterX = monster.getXp();
            int monsterY = monster.getYp();
            if (monsterX == x && monsterY == y) {
                // Lide com o comportamento quando o monstro for revelado
            }
        }
    }

    repaint(); // Redesenha o tabuleiro para refletir as mudanças
}
    
  public boolean hasPoco(int x, int y) {
    for (Poco pocoAtual : poco) {
        if (pocoAtual.getXp() == x && pocoAtual.getYp() == y && !pocoAtual.isClosed()) {
            return true;
        }
    }
    return false; // Não encontrou nenhum poço na posição
}
  
  public int getGold(){
      return gold;
  }
  
  public void setGold(int valor){
      gold = valor;
  }
  
  public void setWood(int valor){
      woodQtd = valor;
  }
  
  public int getWood(){
      return woodQtd;
  }
  
  public void checkWin(){
      if (!isMonsterAlive && !isKnightMonsterAlive || gold == 1 && player.getXp() == 0 && player.getYp() == 14) {
          
          System.out.println("game over");
          GameOverDialog gameOverDialog = new GameOverDialog(null,this);
          gameOverDialog.setVisible(true);
          
      }
  }
  
  public void alternarTurnos() {
    if (isPlayerTurn) {
        

        isPlayerTurn = false;
        checkMonsterProximity();
        checkGoldCollection(player.getXp(), player.getYp());

        monster.moveRandomly(15, 15);
        knightMonster.moveRandomly(15,15);
        checkMonsterProximity();
        checkGoldProximity();
        checkPocoProximity();
        checkWin();
        if (hasPlayerMetMonster() && isMonsterAlive) {
            System.out.println("game over");
            GameOverDialog gameOverDialog = new GameOverDialog(null, this);
            gameOverDialog.setVisible(true);
        }
        
        if (hasPlayerMetKnightMonster()&& isKnightMonsterAlive){
            System.out.println("Tomou feio");
            player.setHp(hits - 50);
        }
        
        if (player.getHp() <= 0 && isMonsterAlive){
            System.out.println("game over");
            GameOverDialog gameOverDialog = new GameOverDialog(null, this);
            gameOverDialog.setVisible(true);
            
        }
    } else {
       
        isPlayerTurn = true;
        checkMonsterProximity();
        checkGoldCollection(player.getXp(), player.getYp());
        monster.moveRandomly(15, 15);
        knightMonster.moveRandomly(15,15);
        checkMonsterProximity();
        checkGoldProximity();
        checkPocoProximity();
        checkWin();
        if (hasPlayerMetMonster()) {
            System.out.println("game over");
            GameOverDialog gameOverDialog = new GameOverDialog(null,this);
            gameOverDialog.setVisible(true);
        }
        
        if (hasPlayerMetKnightMonster() && isKnightMonsterAlive){
            System.out.println("Tomou feio");
            player.setHp(hits - 50);
        } 
        
        if (player.getHp() <= 0 && isMonsterAlive){
            System.out.println("game over");
            GameOverDialog gameOverDialog = new GameOverDialog(null, this);
            gameOverDialog.setVisible(true);
            
        }
    }
}
  
  public boolean hasPlayerMetMonster() {
    int playerX = player.getXp();
    int playerY = player.getYp();
    int monsterX = monster.getXp();
    int monsterY = monster.getYp();

    // Verifique se o jogador está na mesma posição que o monstro
    return (playerX == monsterX && playerY == monsterY);
}
  public boolean hasPlayerMetKnightMonster() {
    int playerX = player.getXp();
    int playerY = player.getYp();
    int knightMonsterX = knightMonster.getXp();
    int knightMonsterY = knightMonster.getYp();
    int[] knightXMove = { 2, 1, -1, -2, -2, -1, 1, 2 };
    int[] knightYMove = { 1, 2, 2, 1, -1, -2, -2, -1 };

    // Verifique cada possível movimento do monstro em forma de L
    for (int i = 0; i < knightXMove.length; i++) {
        int newX = knightMonsterX + knightXMove[i];
        int newY = knightMonsterY + knightYMove[i];

        // Verifique se a posição intermediária coincide com a posição atual do jogador
        if (newX == playerX && newY == playerY) {
            return true; // O monstro passou pelo jogador
        }
    }

    return false; // O monstro não passou pelo jogador
}
  
public void monsterDeath(){
    
    if (flecha.killMonster() && monster.getXp() > player.getXp() && isAimingRight && monster.getYp() == player.getYp()){
        isMonsterAlive = false;
        monsterImage = null; 
        flecha.setQtd(v-1);
        repaint();
    }
    
    if (flecha.killMonster() && monster.getXp() < player.getXp() && isAimingLeft && monster.getYp() == player.getYp()) {
        isMonsterAlive = false;
        monsterImage = null; 
        flecha.setQtd(v-1);
        repaint();
    }
    
    if (flecha.killMonster() && monster.getYp() > player.getYp() && isAimingDown && monster.getXp() == player.getXp()) {
        isMonsterAlive = false;
        monsterImage = null; 
        flecha.setQtd(v-1);
        repaint();
    }
    
    if (flecha.killMonster() && monster.getYp() < player.getYp() && isAimingUp && monster.getXp() == player.getXp()) {
        isMonsterAlive = false;
        monsterImage = null; 
        flecha.setQtd(v-1);
        repaint();
    }
    
     
    if (flecha.killKnightMonster() && knightMonster.getXp() > player.getXp() && isAimingRight && knightMonster.getYp() == player.getYp()) {
    isKnightMonsterAlive = false;
    knightMonsterImage = null;
    flecha.setQtd(v-1);
    repaint();
    }
    
    if (flecha.killKnightMonster() && knightMonster.getXp() < player.getXp() && isAimingLeft && knightMonster.getYp() == player.getYp()) {
    isKnightMonsterAlive = false;
    knightMonsterImage = null;
    flecha.setQtd(v-1);
    repaint();
    }
    
    if (flecha.killKnightMonster() && knightMonster.getYp() > player.getYp() && isAimingDown && knightMonster.getXp() == player.getXp()) {
    isKnightMonsterAlive = false;
    knightMonsterImage = null;
    flecha.setQtd(v-1);
    repaint();
    }
    
    if (flecha.killKnightMonster() && knightMonster.getYp() < player.getYp() && isAimingUp && knightMonster.getXp() == player.getXp()) {
    isKnightMonsterAlive = false;
    knightMonsterImage = null;
    flecha.setQtd(v-1);
    repaint();
    }
    
    if (!flecha.killKnightMonster() && !flecha.killMonster() && flecha.getQtd() > 0) {
        flecha.setQtd(v - 1);
        System.out.println("errou flecha");
    }
}
  
public int getHp2(){
    return player.getHp();
}

public void checkMonsterProximity() {
    int playerX = player.getXp();
    int playerY = player.getYp();
    int monsterX = monster.getXp();
    int monsterY = monster.getYp();
    int knightMonsterX = knightMonster.getXp();
    int knightMonsterY = knightMonster.getYp();

    // Limpe o aviso anterior
    for (int x = 0; x < monsterWarning.length; x++) {
        for (int y = 0; y < monsterWarning[x].length; y++) {
            monsterWarning[x][y] = false;
        }
    }

    // Verifique a proximidade do jogador em relação ao monstro nas quatro direções
    int[] dx = { -1, 1, 0, 0 }; // Esquerda, direita, cima, baixo
    int[] dy = { 0, 0, -1, 1 };

    for (int i = 0; i < 4; i++) {
        int newX = monsterX + dx[i];
        int newY = monsterY + dy[i];

        if (playerX == newX && playerY == newY && isMonsterAlive) {
            // O jogador está próximo a essa posição do monstro, defina o aviso como verdadeiro
            monsterWarning[newX][newY] = true;
        }
    }

    // Verifique a proximidade do jogador em relação ao knightMonster nas quatro direções
    for (int i = 0; i < 4; i++) {
        int newX = knightMonsterX + dx[i];
        int newY = knightMonsterY + dy[i];

        if (playerX == newX && playerY == newY && isKnightMonsterAlive) {
            // O jogador está próximo a essa posição do knightMonster, defina o aviso como verdadeiro
            monsterWarning[newX][newY] = true;
        }
    }

    repaint(); // Redesenha o tabuleiro para refletir as mudanças no aviso
}

public void setAim(int direcao){
    if (direcao == 1) {
        isAimingUp = true;
        isAimingDown = false;
        isAimingLeft = false;
        isAimingRight = false;
    }
    
    if (direcao == 2) {
        isAimingUp = false;
        isAimingDown = false;
        isAimingLeft = true;
        isAimingRight = false;
    }
    
    if (direcao == 3) {
        isAimingUp = false;
        isAimingDown = true;
        isAimingLeft = false;
        isAimingRight = false;
    }
    
    if (direcao == 4) {
        isAimingUp = false;
        isAimingDown = false;
        isAimingLeft = false;
        isAimingRight = true;
    }
}

public void restartGame() {
    // Limpe todas as listas de elementos do jogo
    poco.clear();
    this.setGold(0);

    // Recoloque barras de ouro e poços em posições aleatórias
    for (int i = 0; i < 1; ++i) {
        int aleatorioX = random2.nextInt(14);
        int aleatorioY = random2.nextInt(14);
        goldBar = new GoldBar(aleatorioX,aleatorioY);
    }

    for (int i = 0; i < 5; ++i) {
        int aleatorioX = random2.nextInt(14);
        int aleatorioY = random2.nextInt(14);
        poco.add(new Poco(aleatorioX, aleatorioY));
    }

    // Reinicie a posição do jogador e dos monstros
    player = new Player(0,14, player.getPlayerImage());
    player.setTabuleiro(this);
    monster = new Monster(numeroAleatorioX, numeroAleatorioY, monsterImage);
    monster.setTabuleiro(this);
    knightMonster = new KnightMonster(numeroAleatorioX, numeroAleatorioY, monsterImage);

    // Reinicie as variáveis de estado do jogo
    isMonsterAlive = true;
    isKnightMonsterAlive = true;

    // Reinicie o estado de revelação
    for (int x = 0; x < revealed.length; x++) {
        for (int y = 0; y < revealed[x].length; y++) {
            revealed[x][y] = false;
        }
    }
    
    revealed[0][14] = true;

    // Redesenhe o tabuleiro
    repaint();
}

public void checkGoldProximity() {
    int playerX = player.getXp();
    int playerY = player.getYp();
    int goldX = goldBar.getX();
    int goldY = goldBar.getY();
    

    // Limpe o aviso anterior
    for (int x = 0; x < goldWarning.length; x++) {
        for (int y = 0; y < goldWarning[x].length; y++) {
            goldWarning[x][y] = false;
        }
    }

    // Verifique a proximidade do jogador em relação ao monstro nas quatro direções
    int[] dx = { -1, 1, 0, 0 }; // Esquerda, direita, cima, baixo
    int[] dy = { 0, 0, -1, 1 };

    for (int i = 0; i < 4; i++) {
        int newX = goldX + dx[i];
        int newY = goldY + dy[i];

        if (playerX == newX && playerY == newY && isMonsterAlive) {
            // O jogador está próximo a essa posição do monstro, defina o aviso como verdadeiro
            goldWarning[newX][newY] = true;
        }
    }


    repaint(); // Redesenha o tabuleiro para refletir as mudanças no aviso
}

public void checkPocoProximity() {
    int playerX = player.getXp();
    int playerY = player.getYp();
    
    for (Poco pocoAtual : poco) {
        for (int x = 0; x < pocoWarning.length; x++) {
            for (int y = 0; y < pocoWarning[x].length; y++) {
                pocoWarning[x][y] = false;
            }
        } // Este loop for estava faltando fechar
    }
        int[] dx = { -1, 1, 0, 0 }; // Esquerda, direita, cima, baixo
        int[] dy = { 0, 0, -1, 1 };
    
        for (Poco pocoAtual : poco) {
            for (int i = 0; i < 4; i++) {
                int newX = pocoAtual.getXp() + dx[i];
                int newY = pocoAtual.getYp() + dy[i];
            
                if (playerX == newX && playerY == newY) {
                    pocoWarning[newX][newY] = true;
                }
            }
        }

}

public void CriaFlecha(){
    
    flecha.setQtd(flecha.getQtd() + 1);
}

public int getArrow(){
    return flecha.getQtd();
}


public void closePoco(){
    
    int playerX = player.getXp();
    int playerY = player.getYp();
    
    int[] dx = { -1, 1, 0, 0 }; // Esquerda, direita, cima, baixo
    int[] dy = { 0, 0, -1, 1 };
    
    for (Poco pocoAtual : poco){
        for (int i = 0; i < 4; ++i){
            int newX = pocoAtual.getXp() + dx[i];
            int newY = pocoAtual.getYp() + dy[i];
            
            if (playerX == newX && playerY == newY && woodQtd > 0){
                pocoAtual.setClosed();
                System.out.println("poco fechado");
                woodQtd--;
            }
        }
       
        
    }
}

public void useLantern(){
    int playerX = player.getXp();
    int playerY = player.getYp();
   
    if (isAimingUp) {
        for (int i = 0; i < 15 && i != playerY; ++i)
        revealSquare(playerX, i);
    }
    
    if (isAimingDown) {     
        for (int i = 15; i != playerY; --i)
        revealSquare(playerX, i);
    }
    
    if (isAimingRight) {     
        for (int i = 15; i != playerX; --i)
        revealSquare(i, playerY);
    }
    
    if (isAimingLeft){
        for (int i = 0; i < 15 && i != playerX; ++i)
            revealSquare(i,playerY);
    }
}

public void HARD(){
    HARD = !HARD;
}







}

