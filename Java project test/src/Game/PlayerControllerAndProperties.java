package Game;

import Game.Data.Tools.Clock;
import Game.Entity.GameTiles.Tile.*;
import Game.Entity.Enemy.*;
import Game.Entity.GameTiles.Tower.*;

import Game.GameMenu.ManageTheMenu;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

import static Game.Data.Tools.Artist.*;

public class PlayerControllerAndProperties {
    private TileGrid         grid; //map
    private TileType[]       types; //kiểu ô
    private MoreWaves        moreWaves; //quản lý wave
    private ArrayList<Tower> towerList; //list tháp
    private boolean          leftClicked, rightClicked, holdClicked; //biến mô tả đang nhấn chuột trái, phải, hoặc giữ tháp khi chọn trong menu
    private Tower            temp; //tháp giả tạm thời
    public static int        currency, lives; //tiền và mạng

    public PlayerControllerAndProperties(TileGrid grid, MoreWaves moreWaves){
        this.grid =         grid;
        this.types =        new TileType[5];
        this.types[0] =     TileType.FreshGrass;
        this.types[1] =     TileType.FadedGrass;
        this.types[2] =     TileType.Sand;
        this.types[3] =     TileType.Route;
        this.types[4] =     TileType.Water;
        this.moreWaves =    moreWaves;
        this.towerList =    new ArrayList<Tower>();
        this.leftClicked =  false;
        this.rightClicked = false;
        this.holdClicked =  false;
        this.temp =         null;
        currency =          0;
        lives =             0;
    }

    //tiền và mạng mặc định
    public void setting() {
        currency = 100;
        lives = 10;
    }

    //trừ mạng, nếu mạng = 0 thì thoát ra main menu, người chơi thua
    public static void setTheLives(int amount) {
        lives += amount;
        if (lives == 0) {
            ManageTheMenu.setGameCondition(ManageTheMenu.GameState.GAMEOVER);
        }
    }

    public static boolean setTheMoney(int amount) {
        if (currency + amount >= 0) {
            currency += amount;
            return true;
        }
        return false;
    }

    public void update() {
        //nếu chưa đặt tháp thì vẽ tháp tạm thời theo con trỏ chuột
        if (holdClicked) {
            temp.setX(getMouse().getX());
            temp.setY(getMouse().getY());
            temp.draw();
        }
        //vẽ các tháp trong list
        for (Tower t: towerList) {
            t.update();
            t.draw();
            t.updateEnemy(moreWaves.getCurrentWave().getEnemyList());
        }

        //nhấn chuột trái -> đặt tháp, !leftClicked để tránh bị nhấn quá nhiều vì class Clock
        if (Mouse.isButtonDown(0) && !leftClicked) {
            placeTheTower();
        }

        if (Mouse.isButtonDown(1) && !rightClicked) {
            System.out.println("Nothing happened");
        }

        leftClicked = Mouse.isButtonDown(0);
        rightClicked = Mouse.isButtonDown(1);

        while (Keyboard.next()) {
            //tăng thời gian nhanh hơn 20%
            if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) {
                Clock.changeMultiplier(0.2f);
            }
            //giảm thời gian chậm chơn 20%
            if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()) {
                Clock.changeMultiplier(-0.2f);
            }
            //pause gameStage, chưa hoàn thiện
            if (Keyboard.getEventKey() == Keyboard.KEY_P && Keyboard.getEventKeyState()) {
                Clock.changeMultiplier(-1f);
            }
        }
    }

    private void placeTheTower() {
        Tile tile = getMouse(); //lấy toạ độ con trỏ
        if (holdClicked)
            if (!tile.getCaptured() && setTheMoney(-temp.getCost())) {
                towerList.add(temp);
                tile.setCaptured(true);
                holdClicked = false;
                temp = null;
            }
        //holdClicked = false;
        //temp = null;
    }

    public void picker(Tower t) {
        temp = t;
        holdClicked = true;
    }

    //lấy toạ độ của chuột
    private Tile getMouse() {
        return grid.getTile(Mouse.getX() / TILE_SIZE, (HEIGHT - Mouse.getY() - 1) / TILE_SIZE);
    }
}
