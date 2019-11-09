package Game.Entity.Bullet;
import Game.Entity.Projectile;
import Game.Entity.Enemy.Enemy;

public class ZapBullet extends Projectile {
    public ZapBullet(BulletType type, Enemy target, float x, float y, int width, int height) {
        super(type, target, x, y, width, height);
    }

    @Override
    public void power() {
        super.getTarget().setSpeed(4f);
        super.power();
    }
}
