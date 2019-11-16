package Game.Entity.GameTiles.Tower;

import java.util.concurrent.CopyOnWriteArrayList;
import Game.Entity.Enemy.Enemy;
import Game.Entity.Bullet.ZapBullet;
import Game.Entity.GameTiles.Tile.Tile;

public class ParalizedTower extends Tower {
    public ParalizedTower(TowerType type, Tile towerTile, CopyOnWriteArrayList<Enemy> enemies) {
        super(type, towerTile, enemies);
    }

    @Override
    public void shoot(Enemy target) {
        super.predictBulletDirections.add(new ZapBullet(super.type.bulletType, super.target, super.getX(), super.getY(), 32, 32));
        super.target.reduceCheck(super.type.bulletType.damage);
    }
}
