package Game.Entity.Bullet;

import org.newdawn.slick.opengl.Texture;

import static Game.Data.Tools.Artist.*;

public enum BulletType {

    NormalBullet(quickLoad("towernormalbullet"), 25, 2000),
    SniperBullet(quickLoad("towersniperbullet"), 100, 2000),
    ParalizedBullet(quickLoad("towerzapbullet"), 0, 1000),
    MachineBullet(quickLoad("towermachinegunbullet"), 20, 2000);

    public Texture texture;
    public int damage;
    public float speed;

    BulletType(Texture texture, int damage, float speed) {
        this.texture = texture;
        this.damage =  damage;
        this.speed =   speed;
    }
}
