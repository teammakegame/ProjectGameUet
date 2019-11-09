package Game.Entity.GameTiles.Tower;

import org.newdawn.slick.opengl.Texture;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import Game.Entity.GameEntity;
import Game.Entity.Enemy.Enemy;
import Game.Entity.Predict_BulletDirection;
import Game.Entity.GameTiles.Tile.Tile;

import static Game.Data.Tools.Artist.*;
import static Game.Data.Tools.Clock.Delta;

public abstract class Tower implements GameEntity {
    private float                       x, y, timeSinceLastShot, firingSpeed, angle, closestDistance;
    private int                         width, height, range, cost;
    public Enemy                        target;
    private Texture[]                   textures;
    private CopyOnWriteArrayList<Enemy> enemies;
    public ArrayList<Predict_BulletDirection> predictBulletDirections;
    private boolean                     targeted;
    public TowerType                    type;

    public Tower(TowerType type, Tile towerTile, CopyOnWriteArrayList<Enemy> enemies) {
        this.type =              type;
        this.textures =          type.textures;
        this.range =             type.range;
        this.cost =              type.cost;
        this.x =                 towerTile.getX();
        this.y =                 towerTile.getY();
        this.width =             towerTile.getWidth();
        this.height =            towerTile.getHeight();
        this.enemies =           enemies;
        this.targeted =          false;
        this.timeSinceLastShot = 0f;
        this.predictBulletDirections =       new ArrayList<Predict_BulletDirection>();
        this.firingSpeed =       type.firingSpeed;
        this.angle =             0f;
    }

    private Enemy lockOnTarget() {
        Enemy closest = null; //enemy gần nhất
        closestDistance = 1000; //range súng mặc định
        for (Enemy e: enemies) {
            if (isInRange(e) && findDistance(e) < closestDistance && e.getCheck() > 0) {
                closestDistance = findDistance(e);
                closest = e;
            }
        }
        if (closest != null)
            targeted = true;
        return closest;
    }

    //kiểm tra enemy có trong tầm ngắm không
    private boolean isInRange(Enemy e) {
        float distanceX = Math.abs(e.getX() - x);
        float distanceY = Math.abs(e.getY() - y);
        if (distanceX < range && distanceY < range) return true;
        return false;
    }

    //tính khaorng cách giữa tháp và enemy
    private float findDistance(Enemy e) {
        float distanceX = Math.abs(e.getX() - x);
        float distanceY = Math.abs(e.getY() - y);
        return distanceX + distanceY;
    }

    //xoay súng theo enemy sử dụng toạ độ cực
    private float calculateAngle() {
        double temp = Math.atan2(target.getY() - y, target.getX() - x);
        return (float) Math.toDegrees(temp) - 90;
    }

    public abstract void shoot(Enemy target);

    public void updateEnemy(CopyOnWriteArrayList<Enemy> l) {
        enemies = l;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    public Enemy getTarget() { return target; }

    public int getCost() { return cost; }

    @Override
    public void update() {
        if (!targeted /*|| target.getCheck() < 0*/) {
            target = lockOnTarget();
        } else {
            angle = calculateAngle();
            if (timeSinceLastShot > firingSpeed) {
                shoot(target);
                timeSinceLastShot = 0;
            }
        }

        if (target == null || target.isAlive() == false) targeted = false;

        timeSinceLastShot += Delta();
        for (Predict_BulletDirection p: predictBulletDirections) {
            p.update();
        }
        draw();
    }

    @Override
    public void draw() {
        //vẽ bệ
        drawTexture(textures[0], x, y, width, height);
        if (textures.length > 1) {
            //vẽ súng xoay được
            for (int i = 1; i < textures.length; i++)
                drawRotation(textures[i], x, y, width, height, angle);
        }
    }
}
