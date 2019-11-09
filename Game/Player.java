package Game;

import Game.Data.Tools.Clock;
import Game.Entity.GameTiles.Tile.*;
import Game.Entity.Enemy.*;
import Game.Entity.GameTiles.Tower.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

import static Game.Data.Tools.Artist.*;

public class Player {
    private TileGrid         grid;
    private TileType[]       types;
    private MoreWaves        moreWaves;
    private ArrayList<Tower> towerList;
    private boolean          leftClicked, rightClicked, holdClicked;
    private Tower temp;
    public static int currency, lives;

    public Player(TileGrid grid, MoreWaves moreWaves){
        this.grid =        grid;
        this.types =       new TileType[3];
        this.types[0] =    TileType.Grass;
        this.types[1] =    TileType.Dirt;
        this.types[2] =    TileType.Water;
        this.moreWaves =   moreWaves;
        this.towerList =   new ArrayList<Tower>();
        this.leftClicked = false;
        this.rightClicked = false;
        this.holdClicked = false;
        this.temp = null;
        currency = 0;
        lives = 0;
    }

    public void setting() {
        currency = 100;
        lives = 20;
    }

    public static void setTheLives(int amount) {
        lives += amount;
    }

    public static boolean setTheMoney(int amount) {
        if (currency + amount >= 0) {
            currency += amount;
            System.out.println(currency);
            return true;
        }
        System.out.println(currency);
        return false;
    }

    public void update() {
        if (holdClicked) {
            temp.setX(getMouse().getX());
            temp.setY(getMouse().getY());
            temp.draw();
        }
        for (Tower t: towerList) {
            t.update();
            t.draw();
            t.updateEnemy(moreWaves.getCurrentWave().getEnemyList());
        }

        if (Mouse.isButtonDown(0) && !leftClicked) {
            placeTheTower();
        }

        if (Mouse.isButtonDown(1) && !rightClicked) {
            System.out.println("Nothing happened");
        }

        leftClicked = Mouse.isButtonDown(0);
        rightClicked = Mouse.isButtonDown(1);

        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) {
                Clock.changeMultiplier(0.2f);
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()) {
                Clock.changeMultiplier(-0.2f);
            }
         }
    }

    private void placeTheTower() {
        Tile tile = getMouse();
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

    private Tile getMouse() {
        return grid.getTile(Mouse.getX() / TILE_SIZE, (HEIGHT - Mouse.getY() - 1) / TILE_SIZE);
    }
}
