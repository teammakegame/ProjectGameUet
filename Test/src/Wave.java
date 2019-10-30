import javafx.stage.Screen;

import java.util.Random;

public class Wave {
    public GameStage gameStage;
    public int waveNumber = 0;
    public int allEnemiesInOneRound = 0;
    public int pointsThisRound;
    boolean spawning;
    public int countDelay;
    public int timeDelay = 100;

    public int currentPoint;

    public Wave(GameStage gameStage){
        this.gameStage = gameStage;

    }

    public void nextWave(){
        this.waveNumber++;
        this.pointsThisRound = this.waveNumber * 10;
        this.currentPoint = 0;
        this.allEnemiesInOneRound = 0;
        this.spawning = true;

        System.out.println("coming");
        for(int i = 0; i < this.gameStage.enemyMap.length; i++){
            this.gameStage.enemyMap[i] = null;
        }
    }

    public void spawnEnemy(){
        if(this.currentPoint < this.pointsThisRound){
            if(countDelay < timeDelay){
                countDelay++;
            } else {
                countDelay = 0;

                System.out.println("enemy has spawned");

                int[] enemiesSpawnableID = new int[Enemy.enemyList.length];
                int enemiesSpawnable = 0;

                for(int i = 0; i < Enemy.enemyList.length; i++){
                    if(Enemy.enemyList[i] != null) {
                        if (Enemy.enemyList[i].point + currentPoint <= this.pointsThisRound && Enemy.enemyList[i].point <= this.waveNumber) {
                            enemiesSpawnableID[enemiesSpawnable] = Enemy.enemyList[i].id;
                            enemiesSpawnable++;
                        }
                    }
                }
                int enemyID = new Random().nextInt(enemiesSpawnable);
                this.currentPoint += Enemy.enemyList[enemyID].point;
                this.gameStage.spawnEnemy(enemiesSpawnableID[enemyID]);
            }
        } else {
            this.spawning = false;
        }
    }
}
