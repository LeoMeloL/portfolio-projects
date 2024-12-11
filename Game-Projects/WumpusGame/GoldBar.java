/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WumpusGame;


public class GoldBar {
    private int x;
    private int y;
    private boolean collected;
    public int gold;

    public GoldBar(int x, int y) {
        this.x = x;
        this.y = y;
        this.collected = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isCollected() {
        return collected;
    }
   

    public void collect() {
        collected = true;
        gold++;
        System.out.println("Gold: " + gold);
    }
}
