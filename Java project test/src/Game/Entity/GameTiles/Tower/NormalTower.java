package Game.Entity.GameTiles.Tower;

import java.util.concurrent.CopyOnWriteArrayList;
import Game.Entity.Enemy.Enemy;
import Game.Entity.Bullet.BasicBullet;
import Game.Entity.GameTiles.Tile.Tile;

public class NormalTower extends Tower {
    public NormalTower(TowerType type, Tile towerTile, CopyOnWriteArrayList<Enemy> enemies) {
        super (type, towerTile, enemies);
    }

    @Override
    public void shoot(Enemy target) {
        super.predictBulletDirections.add(new BasicBullet(super.type.bulletType, super.target, super.getX(), super.getY(), 32, 32));
        super.target.reduceCheck(super.type.bulletType.damage);
    }
}
