package Game.Entity.Enemy;
import Game.Entity.GameTiles.Tile.TileGrid;
import Game.GameMenu.ManageTheMenu;
import static Game.GameMenu.ManageTheMenu.*;

public class MoreWaves {
    private float timeSinceLastWave, timeBetweenEnemies; //thời gian từ lần spawn trước và thời gian giữa các enemy
    private int enemiesPerWave; //số lượng enemy một wave
    private Enemy[] enemyType; //kiểu enemy
    private Wave  currentWave; //wave hiện tại
    private TileGrid grid; //bản đồ
    public static int waveNumber; //số lượng wave

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
        waveNumber++; //số lượng wave tăng lên 1, nễu bằng 21 thì thoát gameStage, người chơi thắng
        if (waveNumber == 21) {
            ManageTheMenu.setGameCondition(GameState.GAMEWIN);
        }
        enemiesPerWave += 3; //mỗi lượt thêm 3 enemy
    }

    public Wave getCurrentWave() {return currentWave;}

    public int getWaveNumber() { return waveNumber; }
}
