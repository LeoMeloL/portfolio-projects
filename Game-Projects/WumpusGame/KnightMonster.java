package WumpusGame;

import java.awt.image.BufferedImage;

public class KnightMonster extends Monster {
    public KnightMonster(int xp, int yp, BufferedImage monsterImage) {
        super(xp, yp, monsterImage);
    }

    @Override
    public void moveRandomly(int rows, int cols) {
        int[] knightXMove = { 2, 1, -1, -2, -2, -1, 1, 2 };
        int[] knightYMove = { 1, 2, 2, 1, -1, -2, -2, -1 };

        int randomMove = getRandom().nextInt(8);

        int newXp = getXp() + knightXMove[randomMove];
        int newYp = getYp() + knightYMove[randomMove];

        if (isValidMove(newXp, newYp, rows, cols)) {
            setXp(newXp);
            setYp(newYp);
            System.out.println("Knight Monster moved");
        }
    }

    private boolean isValidMove(int x, int y, int rows, int cols) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }
    
    public void setInitialPosition(int xp, int yp) {
        setXp(xp);
        setYp(yp);
    }
}
