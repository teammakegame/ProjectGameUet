import javax.swing.*;
import java.awt.*;

public class Enemy {
    public static Enemy[] enemyList = new Enemy[100];

    public static Enemy normalEnemy = new NormalEnemy(0, 30, 100, 20, 1.7, 10, 1).getTextureFile("enemy");
    public static Enemy normalEnemy1 = new NormalEnemy(1, 40, 120, 20, 1.7, 10, 2).getTextureFile("enemy2");

    public int dam;
    public int id;
    public int priceReward;
    public int health;
    public double speed;
    public double armor;
    public int point;
    public String textureFile = "";
    public Image texture = null;

    public Enemy(int id, int priceReward, int health, double speed, double armor, int dam, int point){
        if(enemyList[id] == null){
            enemyList[id] = this;
            this.id = id;
            this.health = health;
            this.priceReward = priceReward;
            this.speed = speed;
            this.armor = armor;
            this.dam = dam;
            this.point = point;
        }
    }

    public Enemy getTextureFile(String str){
        this.textureFile = str;
        this.texture = new ImageIcon("character/enemy/" + textureFile + ".png").getImage();
        return this;
    }
}
