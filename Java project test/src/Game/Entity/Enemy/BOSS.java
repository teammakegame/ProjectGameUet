package Game.Entity.Enemy;

import Game.Entity.GameTiles.Tile.TileGrid;

public class BOSS extends Enemy {
    public BOSS(int x, int y, TileGrid grid) {
        super(x, y, grid);
        super.setTexture("enemyboss");
        super.setSpeed(30);
        super.setHealth(1000);
    }
}
