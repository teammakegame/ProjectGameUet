package Game.Entity;

import org.newdawn.slick.opengl.Texture;
import Game.Entity.Enemy.*;
import Game.Entity.Bullet.*;
import static Game.Data.Tools.Artist.*;
import static Game.Data.Tools.Clock.*;


public abstract class Projectile implements GameEntity {
    private Texture texture;
    private float   x, y, speed, velocityX, velocityY;
    private int     damage, width, height;
    private Enemy   target;
    private boolean alive;

    public Projectile(BulletType type, Enemy target, float x, float y, int width, int height) {
        this.texture =   type.texture;
        this.x =         x;
        this.y =         y;
        this.width =     width;
        this.height =    height;
        this.speed =     type.speed;
        this.damage =    type.damage;
        this.target =    target;
        this.alive =     true;
        this.velocityX = 0f;
        this.velocityY = 0f;
        calculateDirection();
    }

    private void calculateDirection() {
        float totalAllowedMovement = 1.0f;
        float xToTarget = Math.abs(target.getX() - x - TILE_SIZE / 4 + TILE_SIZE / 2);
        float yToTarget = Math.abs(target.getY() - y - TILE_SIZE / 4 + TILE_SIZE / 2);
        float totalDistanceFromTarget = xToTarget + yToTarget;
        float percentX = xToTarget / totalDistanceFromTarget;
        velocityX = percentX;
        velocityY = totalAllowedMovement - percentX;
        if (target.getX() < x) velocityX *= -1;
        if (target.getY() < y) velocityY *= -1;
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

    public void power() {
        if (target.isAlive()) {
            target.takingHit(damage);
            alive = false;
        }
    }

    public void setAlive(boolean status) {
        alive = status;
    }

    public void update() {
        if (alive) {
            calculateDirection();
            x += velocityX * speed * Delta();
            y += velocityY * speed * Delta();
            if (isCollided(x, y, width, height, target.getX(), target.getY(), target.getWidth(), target.getHeight())) {
                power();
            }
            draw();
        }
    }

    public void draw() {
        drawQuadTex (texture, x, y, 32, 32);
    }


}
