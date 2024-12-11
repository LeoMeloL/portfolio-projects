package WumpusGame;



public class Flecha extends Itens{
    
    private Monster monster;
    private Player player;
    private int valor;
    private KnightMonster knightMonster;
    
    
    public Flecha(int Qtd, Player player, Monster monster, KnightMonster monster2){
        super(Qtd);
        this.monster = monster;
        this.player = player;
        this.knightMonster = monster2;
    }
    
    public boolean killMonster() {
    int playerX = player.getXp();
    int playerY = player.getYp();
    int monsterX = monster.getXp();
    int monsterY = monster.getYp();

    int[] possibleMoves = { -1, 1 };

    valor = super.getQtd();

    if (valor > 0) {
        for (int deltaX : possibleMoves) {
            int newX = playerX + deltaX;
            int newY = playerY;

            // Verifique se o monstro está na posição adjacente à esquerda ou à direita
            if (monsterX == newX && monsterY == newY) {
                //super.setQtd(valor - 1);
                return true; // O monstro está a uma casa de distância na horizontal
            }

            newX = playerX;
            newY = playerY + deltaX;

            // Verifique se o monstro está na posição adjacente acima ou abaixo
            if (monsterX == newX && monsterY == newY) {
                //super.setQtd(valor - 1);
                return true; // O monstro está a uma casa de distância na vertical
            }
        }
    } else {
        System.out.println("Flechas insuficientes");
    }

    return false; // O monstro não está a uma casa de distância na horizontal ou vertical
}
    
    public boolean killKnightMonster(){
    int playerX = player.getXp();
    int playerY = player.getYp();
    int monsterX = knightMonster.getXp();
    int monsterY = knightMonster.getYp();

    int[] possibleMoves = { -1, 1 };

    valor = super.getQtd();

    if (valor > 0) {
        for (int deltaX : possibleMoves) {
            int newX = playerX + deltaX;
            int newY = playerY;

            // Verifique se o monstro está na posição adjacente à esquerda ou à direita
            if (monsterX == newX && monsterY == newY) {
                super.setQtd(valor - 1);
                return true; // O monstro está a uma casa de distância na horizontal
            }

            newX = playerX;
            newY = playerY + deltaX;

            // Verifique se o monstro está na posição adjacente acima ou abaixo
            if (monsterX == newX && monsterY == newY) {
                super.setQtd(valor - 1);
                return true; // O monstro está a uma casa de distância na vertical
            }
        }
    } else {
        System.out.println("Flechas insuficientes");
    }

    return false; // O monstro não está a uma casa de distância na horizontal ou vertical
}
    
}
