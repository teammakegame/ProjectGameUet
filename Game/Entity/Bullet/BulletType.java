package Game.Entity.Bullet;

import org.newdawn.slick.opengl.Texture;

import static Game.Data.Tools.Artist.*;

public enum BulletType {

    NormalBullet(quickLoad("bullet"), 20, 500),
    ParalizedBullet(quickLoad("towerzapbullet"), 0, 450);

    public Texture texture;
    public int damage;
    public float speed;

    BulletType(Texture texture, int damage, float speed) {
        this.texture = texture;
        this.damage = damage;
        this.speed = speed;
    }
}
