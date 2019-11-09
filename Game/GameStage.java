package Game;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import Game.Data.UI.UI;
import Game.Data.UI.UI.*;
import org.newdawn.slick.opengl.Texture;
import Game.Entity.GameTiles.Tile.*;
import Game.Entity.Enemy.*;
import Game.GameMenu.ManageTheMenu;
import Game.Entity.GameTiles.Tower.*;

import static Game.Data.Tools.Artist.*;

//gameStage class sẽ lo mọi thứ động trên màn hình như các wave của enemy, tương tác giữa người dùng để đặt tháp các kiểu
public class GameStage {
    private TileGrid  grid;
    private Player    player;
    private MoreWaves moreWaves;
    private UI        gameInterface;
    private Menu      pickTowerMenu;
    private Texture   metal;
    private Enemy[] enemyType;
    private int x, y;

    public GameStage(TileGrid grid) {
        this.grid = grid;
        this.x = 0;
        this.y = 0;
        for (int i = 0; i < grid.getTileHigh(); i++) {
            if (grid.map[0][i].getType().equals(TileType.Dirt)) y = i;
        }
        enemyType = new Enemy[2];
        enemyType[0] = new NormalEnemy(0, y, grid);
        enemyType[1] = new TankerEnemy(0, y, grid);
        //moreWaves = new MoreWaves(new Enemy(quickLoad("enemy2"), grid.getTile(0, 2), grid, TILE_SIZE, TILE_SIZE, 100, 50), 3,2);
        moreWaves = new MoreWaves(enemyType, 3, 2, grid);
        player = new Player(grid, moreWaves);
        player.setting();
        this.metal = quickLoad("metal");
        setTheUI();
    }

    private void setTheUI() {
        gameInterface = new UI();
        //pickTower.addButton("SniperTower", "gun1", 0, 0);
        //pickTower.addButton("NormalTower", "gun2", 64, 0);
        //pickTower.addButton("ParalizedTower", "zapgun", 128, 0);
        gameInterface.createMenu("pickTower", 1280, 150, 320, 900,1, 0); //tạo menu ở bên phải sân chơi
        //thêm các ô tháp để chọn vào menu
        pickTowerMenu = gameInterface.getMenu("pickTower");
        pickTowerMenu.quickAccess("NormalTower", "towernormal");
        pickTowerMenu.quickAccess("SniperTower", "towersniper");;
        pickTowerMenu.quickAccess("ParalizedTower","towerparalized");
    }

    private void updateUI() {
        gameInterface.draw();
        gameInterface.drawText(1420, 220, "50 $");
        gameInterface.drawText(1420, 340, "100 $");
        gameInterface.drawText(1420, 460, "200 $");
        gameInterface.drawText(1380, 700, "Lives: " + Player.lives);
        gameInterface.drawText(1380, 750, "Money: " + Player.currency + " $");
        gameInterface.drawText(1380, 800, "Wave: " + moreWaves.getWaveNumber());
        gameInterface.drawText(0, 0, "Press ESC to return to the main menu");
        gameInterface.drawText(0,870, ManageTheMenu.frameLastSec + " fps");
        if (Mouse.next()) {
            boolean clicked = Mouse.isButtonDown(0);
            if (clicked) {
                if (pickTowerMenu.isButtonClicked("SniperTower"))
                    player.picker(new SniperTower(TowerType.SniperTower, grid.getTile(0, 0), moreWaves.getCurrentWave().getEnemyList()));
                if (pickTowerMenu.isButtonClicked("NormalTower"))
                    player.picker(new NormalTower(TowerType.NormalTower, grid.getTile(0, 0), moreWaves.getCurrentWave().getEnemyList()));
                if (pickTowerMenu.isButtonClicked("ParalizedTower"))
                    player.picker(new ParalizedTower(TowerType.ParalizedTower, grid.getTile(0, 0), moreWaves.getCurrentWave().getEnemyList()));
            }
        }
    }

    public void update() {
        drawQuadTex(metal, 1280, 0, 320, 900); //vẽ menu chọn tháp
        grid.draw(); //vẽ map
        moreWaves.update(); //vẽ wave
        player.update();
        updateUI();
        if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
            ManageTheMenu.setGameCondition(ManageTheMenu.GameState.MAINMENU);
            drawQuadTex(metal, 1280, 0, 320, 900); //vẽ menu chọn tháp
            grid.draw(); //vẽ map
            moreWaves.update(); //vẽ wave
            player.update();
            updateUI();
        }
    }
}
