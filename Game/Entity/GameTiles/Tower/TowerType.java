package Game.Entity.GameTiles.Tower;

import org.newdawn.slick.opengl.Texture;

import static Game.Data.Tools.Artist.*;

import Game.Entity.Bullet.BulletType;

//kiểu liệt kê, liệt kê các loại tháp
public enum TowerType {

    NormalTower(new Texture[]{quickLoad("towernormalbase"), quickLoad("towernormalgun")}, BulletType.NormalBullet, 10, 500, 1, 50),
    SniperTower(new Texture[]{quickLoad("towersniperbase"), quickLoad("towersnipergun")}, BulletType.NormalBullet, 30, 1000, 3, 100),
    ParalizedTower(new Texture[]{quickLoad("towerzapbase"), quickLoad("towerzapgun")}, BulletType.ParalizedBullet, 0, 200, -100, 200);

    Texture[] textures;
    BulletType bulletType;
    int damage, range, cost;
    float firingSpeed;

    TowerType(Texture[] textures, BulletType bulletType, int damage, int range, float firingSpeed, int cost) {
        this.textures =    textures;
        this.bulletType =  bulletType;
        this.damage =      damage;
        this.range =       range;
        this.cost =        cost;
        this.firingSpeed = firingSpeed;
    }
}
