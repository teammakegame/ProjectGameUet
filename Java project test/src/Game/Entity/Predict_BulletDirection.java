package Game.Entity;

import org.newdawn.slick.opengl.Texture;
import Game.Entity.Enemy.*;
import Game.Entity.Bullet.*;
import static Game.Data.Tools.Artist.*;
import static Game.Data.Tools.Clock.*;


public abstract class Predict_BulletDirection implements GameEntity {
    private Texture texture; //texture của đạn
    private float   x, y, speed, velocityX, velocityY, angle; //toạ độ và hướng di chuyển
    private int     damage, width, height; //dam, chiều dài chiều rộng của đạn
    private Enemy   target; //mục tiêu
    private boolean alive;

    public Predict_BulletDirection(BulletType type, Enemy target, float x, float y, int width, int height) {
        this.texture =   type.texture;
        this.x =         x;
        this.y =         y;
        this.width =     width;
        this.height =    height;
        this.speed =     type.speed;
        this.damage =    type.damage;
        this.target =    target;
        this.alive =     true;
        this.velocityX = 0;
        this.velocityY = 0;
        this.angle =     calculateAngle();
        calculateDirection(); //tính toán hướng đi của đạn
    }

    private void calculateDirection() {
        //chuyển động tối đa
        float totalAllowedMovement = 1.0f;
        //khoảng cách từ đạn đến enemy
        //+TILE_SIZE / 2 để đạn bắt đầu ở vị trí góc phải dưới của tháp, trừ đi TILE_SIZE / 4 để dịch sang giữa
        float xToTarget = Math.abs(target.getX() - x - TILE_SIZE / 4 + TILE_SIZE / 2);
        float yToTarget = Math.abs(target.getY() - y - TILE_SIZE / 4 + TILE_SIZE / 2);
        //tổng khoảng cách
        velocityX = xToTarget / (xToTarget + yToTarget);
        velocityY = totalAllowedMovement - velocityX;
        //tổng hướng di chuyển của đạn theo x và y = 1 để tránh độ chính xác quá cao
        if (target.getX() < x) velocityX *= -1;
        if (target.getY() < y) velocityY *= -1;
    }

    private float calculateAngle() {
        double temp = Math.atan2(target.getY() - y, target.getX() - x);
        return (float) Math.toDegrees(temp) - 90;
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
            //calculateDirection();
            x += velocityX * speed * Delta();
            y += velocityY * speed * Delta();
            if (isCollided(x, y, width, height, target.getX(), target.getY(), target.getWidth(), target.getHeight())) {
                power();
            }
            draw();
        }
    }

    //vẽ đạn
    public void draw() {
        drawRotation (texture, x, y, 32, 32, angle);
    }


}
