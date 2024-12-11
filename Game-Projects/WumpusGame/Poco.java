/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WumpusGame;

public class Poco {
    
    private int x;
    private int y;
    boolean isClosed;
    
    public Poco(int x, int y) {
        this.x = x;
        this.y = y;
        this.isClosed = false;
    }
    
 public int getXp() {
        return x;
    }

    public int getYp() {
        return y;
    }
    
public boolean isClosed(){
    return isClosed;
    }
    
    public void setClosed(){
        isClosed = true;
    }
    
    
    
}
