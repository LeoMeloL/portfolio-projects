/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WumpusGame;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Player {
    private int xp;
    private int yp;
    private boolean isFree;
    private boolean isTurn;
    private BufferedImage playerImage; // Adicione a imagem do jogador
    private TabuleiroComImagens tabuleiro;
    private Monster monster;
    private int hp;
    private Poco poco;
    private BufferedImage playerPocoRightImage;
    private BufferedImage playerPocoUpImage;
    private BufferedImage playerPocoDownImage;
    private BufferedImage playerPocoLeftImage;
    private BufferedImage playerEsqImage;
    private BufferedImage playerUpImage;
    private BufferedImage playerDownImage;
    private BufferedImage playerDirImage;

    public Player(int xp, int yp, BufferedImage playerImage) {
    this.xp = xp;
    this.yp = yp;
    this.isFree = true;
    this.playerImage = playerImage; // Inicialize a imagem do jogador
    this.tabuleiro = tabuleiro;
    this.monster = monster;
    this.isTurn = true;
    this.hp = 100; // Defina os pontos de vida iniciais
    this.poco = poco;
    
    
        try {
            
            
            File playerCimaPocoImageFile = new File("img/indocimapoco.png");
            playerPocoUpImage = ImageIO.read(playerCimaPocoImageFile);
            
            File playerDownPocoImageFile = new File("img/indocimapoco.png");
            playerPocoDownImage = ImageIO.read(playerDownPocoImageFile);
            
            File playerLeftPocoImageFile = new File("img/indocimapoco.png");
            playerPocoLeftImage = ImageIO.read(playerLeftPocoImageFile);
            
            File playerRightPocoImageFile = new File("img/indocimapoco.png");
            playerPocoRightImage = ImageIO.read(playerRightPocoImageFile);
            
            File playerDirImageFile = new File("img/indodireita.png");
            playerDirImage = ImageIO.read(playerDirImageFile);
            
            File playerCimaImageFile = new File("img/indobaixo.png");
            playerUpImage = ImageIO.read(playerCimaImageFile);
            
            File playerEsqImageFile = new File("img/indoesquerda.png");
            playerEsqImage = ImageIO.read(playerEsqImageFile);
            
            File playerBaixoImageFile = new File("img/indocima.png");
            playerDownImage = ImageIO.read(playerBaixoImageFile);
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
}

 public void move(int newXp, int newYp) {
    if (isFree && isTurn) {
        xp = newXp;
        yp = newYp;

        // Chama o método do tabuleiro para revelar o quadrado atual
        if (tabuleiro != null) {
            tabuleiro.revealSquare(xp, yp);

            // Verifique se há barras de ouro ou poços nas posições reveladas pelo jogador
            tabuleiro.checkGoldCollection(newXp, newYp);
            if (tabuleiro.hasPoco(newXp, newYp)) {
                // Lidar com o comportamento quando o jogador encontra um poço (por exemplo, jogador perde o jogo)
            }
            
            if (tabuleiro.hasPlayerMetMonster()) {
    
            hp = 0;
                tabuleiro.revealEntireBoard();
                System.out.println("game over");
                GameOverDialog gameOverDialog = new GameOverDialog(null, tabuleiro);
                gameOverDialog.setVisible(true);
}
        }
    }
 }
 
 public void LanternX(int Xp, int Yp){
     while (Xp < 14){
     tabuleiro.revealSquare(Xp, Yp);
     ++Xp;
     }
     
 }
 
 public void LanternY(int Xp, int Yp){
     while(Yp > 0){
         tabuleiro.revealSquare(Xp, Yp);
         ++Yp;
     }
     
 }
 
 public void closePoco(){
     if (tabuleiro.getWood() > 0) {
         if (tabuleiro.hasPoco(xp,yp)) {     
             poco.setClosed();      
         }
     }
 }
 
   public void setTabuleiro(TabuleiroComImagens tabuleiro) {
        this.tabuleiro = tabuleiro; // Defina a referência ao tabuleiro
    }

    public int getXp() {
        return xp;
    }

    public int getYp() {
        return yp;
    }
    
    public int getHp(){
        return hp;
    }
    public void setHp(int valor){
        hp = hp - valor;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public BufferedImage getPlayerImage() {
        return playerImage;
    }
    public int getHP() {
    return hp;
}
    public void setInitialPosition(int xp, int yp) {
        this.xp = xp;
        this.yp = yp;
    }
    
    public BufferedImage getImageCimaPoco(){
        return playerPocoUpImage;
    }
    public BufferedImage getImageBaixoPoco(){
        return playerPocoDownImage;
    }
    public BufferedImage getImageEsquerdaPoco(){
        return playerPocoLeftImage;
    }
    public BufferedImage getImageDireitaPoco(){
        return playerPocoRightImage;
    }
    
    public BufferedImage getImageCima(){
        return playerUpImage;
    }
    public BufferedImage getImageBaixo(){
        return playerDownImage;
    }
    public BufferedImage getImageEsquerda(){
        return playerEsqImage;
    }
    public BufferedImage getImageDireita(){
        return playerDirImage;
    }
    
    

public void setHP(int hp) {
    this.hp = hp;
}
}