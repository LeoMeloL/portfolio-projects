/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WumpusGame;


public class Wood extends Itens{
   
    private int x;
    private int y;
    private boolean isCollected;
    
    public Wood(int Qtd, int x, int y){
        super(Qtd);
        this.isCollected = false;
        this.x = x;
        this.y = y;
        
        
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isCollected() {
        return isCollected;
    }
    
    public void collect() {
        isCollected = true;
        int v = 0;
        super.setQtd(v + 1);
        System.out.println("Wood: " + super.getQtd());
    }
    
    
    
    
    
    
    
}
