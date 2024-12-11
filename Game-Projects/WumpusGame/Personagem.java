/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WumpusGame;

import java.awt.image.BufferedImage;

/**
 *
 * @author Leonardo
 */
public class Personagem {
    protected int xp;
    protected int yp;
    protected boolean isFree;
    protected BufferedImage playerImage;
    protected TabuleiroComImagens tabuleiro;
    protected int hp;

    public Personagem(int xp, int yp, BufferedImage playerImage) {
        this.xp = xp;
        this.yp = yp;
        this.isFree = true;
        this.playerImage = playerImage;
        this.hp = 100; // Defina os pontos de vida iniciais aqui
    }
    
    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getYp() {
        return yp;
    }

    public void setYp(int yp) {
        this.yp = yp;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean isFree) {
        this.isFree = isFree;
    }

    public BufferedImage getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(BufferedImage playerImage) {
        this.playerImage = playerImage;
    }

    public TabuleiroComImagens getTabuleiro() {
        return tabuleiro;
    }

    public void setTabuleiro(TabuleiroComImagens tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}

    
   
