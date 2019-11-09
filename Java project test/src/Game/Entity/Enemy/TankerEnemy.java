package Game.Entity.Enemy;
import Game.Entity.GameTiles.Tile.*;

public class TankerEnemy extends Enemy {
    public TankerEnemy(int x, int y, TileGrid grid) {
        super(x, y, grid);
        this.setTexture("enemy");
        super.setSpeed(50);
        super.setHealth(120);
    }
}
