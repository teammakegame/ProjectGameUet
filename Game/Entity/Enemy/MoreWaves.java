package Game.Entity.Enemy;
import Game.Entity.GameTiles.Tile.TileGrid;

public class MoreWaves {
    private float timeSinceLastWave, timeBetweenEnemies; //thời gian từ lần spawn trước và thời gian giữa các enemy
    private int   waveNumber, enemiesPerWave; //số lượng wave và số lượng enemy một wave
    private Enemy[] enemyType;
    private Wave  currentWave; //wave hiện tại
    private int check = 1;
    private TileGrid grid;

    public MoreWaves(Enemy[] enemyType, float timeBetweenEnemies, int enemiesPerWave, TileGrid grid) {
        this.enemyType =          enemyType;
        this.timeBetweenEnemies = timeBetweenEnemies;
        this.enemiesPerWave =     enemiesPerWave;
        this.timeSinceLastWave =  0;
        this.waveNumber =         0;
        this.currentWave =        null;
        this.grid = grid;
        newWave(); //wave đầu
    }

    public void update() {
        //vẽ wave chừng nào vẫn còn enemy
        if (!currentWave.isWaveCompleted()) {
            currentWave.update();
        }
        //nếu không thì tạo wave mới, bắt đầu wave tiếp theo
        else newWave();
    }

    public void newWave() {
        currentWave = new Wave(enemyType, timeBetweenEnemies, enemiesPerWave); //tạo một wave
        waveNumber++;
        enemiesPerWave += 2;
        System.out.println("Beginning wave " + waveNumber);
    }

    public Wave getCurrentWave() {return currentWave;}

    public int getWaveNumber() { return waveNumber; }
}
