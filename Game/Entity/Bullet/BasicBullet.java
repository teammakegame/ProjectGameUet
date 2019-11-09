package Game.Entity.Bullet;

import Game.Entity.Projectile;
import Game.Entity.Enemy.Enemy;

public class BasicBullet extends Projectile {
    public BasicBullet(BulletType type, Enemy target, float x, float y, int width, int height) {
        super(type, target, x, y, width, height);
    }

    @Override
    public void power() {
        super.power();
    }
}
