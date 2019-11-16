package Game;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import Game.Data.UI.UserInterface;
import Game.Data.UI.UserInterface.*;
import org.newdawn.slick.opengl.Texture;
import Game.Entity.GameTiles.Tile.*;
import Game.Entity.Enemy.*;
import Game.GameMenu.ManageTheMenu;
import Game.Entity.GameTiles.Tower.*;
import static Game.GameMenu.ManageTheMenu.gameStage;
import static Game.GameMenu.ManageTheMenu.map;

import static Game.Data.Tools.Artist.*;

//gameStage class sẽ lo mọi thứ động trên màn hình như các wave của enemy, tương tác giữa người dùng để đặt tháp các kiểu
public class GameStage {
    private TileGrid      grid; //bản đồ
    private PlayerControllerAndProperties playerControllerAndProperties; //biến dùng để tương tác với người chơi
    private MoreWaves     moreWaves; //quản lý wave
    private UserInterface gameInterface; //quản lý giao diện trong gameStage
    private Menu          pickTowerMenu; //menu chọn tháp
    private Texture       metal; //hình nền chọn tháp
    private Enemy[]       enemyType; //kiểu enemy
    private int           x, y; //toạ độ xuất phát của enemy

    public GameStage(TileGrid grid) {
        this.grid = grid;
        this.x = 0;
        this.y = 0;
        //enemy sẽ tự động xuất phát tại ô đường
        for (int i = 0; i < grid.getTileHigh(); i++) {
            if (grid.map[0][i].getType().equals(TileType.Route)) {
                y = i;
            }
        }
        enemyType = new Enemy[4];
        enemyType[0] = new NormalEnemy(0, y, grid);
        enemyType[1] = new TankerEnemy(0, y, grid);
        enemyType[2] = new SmallerEnemy(0, y, grid);
        enemyType[3] = new BOSS(0, y, grid);
        moreWaves = new MoreWaves(enemyType, 2, 2, grid);
        playerControllerAndProperties = new PlayerControllerAndProperties(grid, moreWaves);
        playerControllerAndProperties.setting();
        this.metal = quickLoad("metal");
        setTheUI();
    }

    private void setTheUI() {
        gameInterface = new UserInterface();
        gameInterface.createMenu("pickTower", 1280, 150, 320, 900,1, 0); //tạo menu ở bên phải sân chơi
        //thêm các nút chọn tháp
        pickTowerMenu = gameInterface.getMenu("pickTower");
        pickTowerMenu.quickAccess("NormalTower", "towernormal");
        pickTowerMenu.quickAccess("SniperTower", "towersniper");
        pickTowerMenu.quickAccess("MachineGun", "towermachinegun");
        pickTowerMenu.quickAccess("ParalizedTower","towerparalized");
    }

    private void updateUI() {
        gameInterface.draw();
        gameInterface.drawText(1420, 220, "50 $");
        gameInterface.drawText(1420, 350, "100 $");
        gameInterface.drawText(1420, 470, "200 $");
        gameInterface.drawText(1420, 590, "300 $");
        gameInterface.drawText(1380, 700, "Lives: " + PlayerControllerAndProperties.lives);
        gameInterface.drawText(1380, 750, "Money: " + PlayerControllerAndProperties.currency + " $");
        gameInterface.drawText(1380, 800, "Wave: " + moreWaves.getWaveNumber() + " / 20");
        gameInterface.drawText(0, 0, "Press ESC to return to the main menu");
        gameInterface.drawText(0,870, ManageTheMenu.frameLastSec + " fps");
        if (Mouse.next()) {
            boolean clicked = Mouse.isButtonDown(0);
            if (clicked) {
                if (pickTowerMenu.isButtonClicked("SniperTower"))
                    playerControllerAndProperties.picker(new SniperTower(TowerType.SniperTower, grid.getTile(0, 0), moreWaves.getCurrentWave().getEnemyList()));
                if (pickTowerMenu.isButtonClicked("NormalTower"))
                    playerControllerAndProperties.picker(new NormalTower(TowerType.NormalTower, grid.getTile(0, 0), moreWaves.getCurrentWave().getEnemyList()));
                if (pickTowerMenu.isButtonClicked("MachineGun"))
                    playerControllerAndProperties.picker(new NormalTower(TowerType.MachineGun, grid.getTile(0, 0), moreWaves.getCurrentWave().getEnemyList()));
                if (pickTowerMenu.isButtonClicked("ParalizedTower"))
                    playerControllerAndProperties.picker(new ParalizedTower(TowerType.ParalizedTower, grid.getTile(0, 0), moreWaves.getCurrentWave().getEnemyList()));
            }
        }
    }

    public void update() {
        drawTexture(metal, 1280, 0, 320, 900); //vẽ menu chọn tháp
        grid.draw(); //vẽ map
        moreWaves.update(); //vẽ wave
        playerControllerAndProperties.update();
        updateUI();
        //nếu nhấn esc thì quay lại main menu và restart lại gameStage
        if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
            ManageTheMenu.setGameCondition(ManageTheMenu.GameState.MAINMENU);
            gameStage = new GameStage(map);
        }
    }
}
