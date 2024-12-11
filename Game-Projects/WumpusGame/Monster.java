package WumpusGame;

import java.awt.image.BufferedImage;
import java.util.Random;


public class Monster {
    
    private int xp;
    private int yp;
    private BufferedImage monsterImage;
    private Random random;
    private TabuleiroComImagens tabuleiro;
    
    public Monster(int xp, int yp, BufferedImage monsterImage) {
        this.xp = xp;
        this.yp = yp;
        this.monsterImage = monsterImage;
        this.random = new Random();
        this.tabuleiro = tabuleiro;
    }
    
        public void moveRandomly(int rows, int cols) {

        int direction = random.nextInt(4);

        
        switch (direction) {
            case 0: // Mover para cima
                if (yp > 0) {
                    yp--;
                    System.out.println("Monster moved");
                }
                break;
            case 1: // Mover para baixo
                if (yp < rows - 1) {
                    yp++;
                    System.out.println("Monster moved");
                }
                break;
            case 2: // Mover para a esquerda
                if (xp > 0) {
                    xp--;
                    System.out.println("Monster moved");
                }
                break;
            case 3: // Mover para a direita
                if (xp < cols - 1) {
                    xp++;
                    System.out.println("Monster moved");
                }
                break;
        }
        
    }
    
    public int getXp() {
        return xp;
    }

    public int getYp() {
        return yp;
    }
    
    public void setXp(int valor){
        xp = valor;
    }
    
    public void setYp(int valor){
        yp = valor;
    }
    public Random getRandom(){
        return random;
    }
    
    public void setInitialPosition(int xp, int yp) {
        this.xp = xp;
        this.yp = yp;
    }
    
    public BufferedImage getMonsterImage() {
        return monsterImage;
    }
    
    public void setTabuleiro(TabuleiroComImagens tabuleiro) {
        this.tabuleiro = tabuleiro; // Defina a referência ao tabuleiro
    }
    
}
