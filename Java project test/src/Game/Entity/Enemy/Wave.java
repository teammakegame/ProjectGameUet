package Game.Entity.Enemy;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static Game.Data.Tools.Artist.*;
import static Game.Data.Tools.Clock.*;

public class Wave {
    private float                       timeSinceLastSpawn, spawnTime; //thời gian từ lần cuối sinh ra và thời gian giữa các lần sinh
    private Enemy[]                     enemyType; //kiểu enemy
    private CopyOnWriteArrayList<Enemy> enemyList; //list bao gồm các enemy
    private int                         enemiesPerWave, enemiesSpawn; //số lượng enemy một wave và số lượng enemy trong list
    private boolean                     waveCompleted; //kiểm tra wave đã kết thúc chưa

    public Wave(Enemy[] enemyType, float spawnTime, int enemiesPerWave){
        this.enemyType =          enemyType;
        this.enemiesPerWave =     enemiesPerWave;
        this.enemiesSpawn =       0;
        this.spawnTime =          spawnTime;
        this.timeSinceLastSpawn = 0;
        this.enemyList =          new CopyOnWriteArrayList<Enemy>();
        this.waveCompleted =      false;
        spawn(); //sinh ra enemy đầu tiên luôn
    }

    public void update() {
        boolean allEnemiesDead = true;
        //nếu số lượng enemy hiện tại chưa bằng số lượng enemy một wave đc định trước thì vẫn thức hiện spawn
        if (enemiesSpawn < enemiesPerWave) {
            //cộng tgian theo milisec vào thời gian từ lần spawn trước, nếu lớn hơn tgian giữa các lần spawn đc định từ trước thì sinh enemy
            timeSinceLastSpawn += Delta();
            if (timeSinceLastSpawn > spawnTime) {
                spawn();
                timeSinceLastSpawn = 0;
            }
        }
        //cho enemy chạy lần lượt
        for (Enemy e: enemyList) {
            if (e.isAlive()) {
                //nếu sống thì mới vẽ
                allEnemiesDead = false;
                e.update();
                e.draw();
            } else
                //nếu chết thì loại bỏ enemy khỏi list
                enemyList.remove(e);
        }
        //nếu tất cả enemy đều chết thì kết thúc wave
        if (allEnemiesDead)
            waveCompleted = true;
    }

    private void spawn() {
        int theChosenOne = 0;
        Random random = new Random();
        theChosenOne = random.nextInt(enemyType.length);
        //thêm enemy vào list
        enemyList.add(new Enemy(enemyType[theChosenOne].getTexture(),
                enemyType[theChosenOne].geteTile(),
                enemyType[theChosenOne].getTileGrid(), TILE_SIZE, TILE_SIZE,
                enemyType[theChosenOne].getHealth(),
                enemyType[theChosenOne].getSpeed()));
        enemiesSpawn++;
    }

    public boolean isWaveCompleted() {return waveCompleted;}

    public CopyOnWriteArrayList<Enemy> getEnemyList() {return enemyList;}
}
