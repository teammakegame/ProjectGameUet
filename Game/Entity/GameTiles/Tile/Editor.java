package Game.Entity.GameTiles.Tile;

import Game.Data.UI.UI;
import Game.Data.UI.UI.*;
import Game.GameMenu.ManageTheMenu;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import static Game.Data.Tools.Artist.*;
import static Game.Data.Tools.Level.*;


public class Editor {
    private TileGrid   grid;
    private int        index;
    private TileType[] types;
    private UI gameInterface;
    private Menu pickTerrainMenu;
    private Texture metal;

    public Editor() {
        this.grid =     load ("map");
        this.index =    0;
        this.types =    new TileType[3];
        this.types[0] = TileType.Grass;
        this.types[1] = TileType.Dirt;
        this.types[2] = TileType.Water;
        this.metal = quickLoad("metal");
        setTheUI();
    }

    private void setTheUI() {
        gameInterface = new UI();
        gameInterface.createMenu("pickTerrain",1280, 350, 320, 900,1, 0);
        pickTerrainMenu = gameInterface.getMenu("pickTerrain");
        pickTerrainMenu.quickAccess("Grass", "grass5");
        pickTerrainMenu.quickAccess("Dirt", "road3");
        pickTerrainMenu.quickAccess("Water", "water");
    }

    public void update() {
        //grid.draw();
        draw();
        if (Mouse.next()) {
            boolean clicked = Mouse.isButtonDown(0);
            if (clicked) {
                if (pickTerrainMenu.isButtonClicked("Grass")) {
                    index = 0;
                } else if (pickTerrainMenu.isButtonClicked("Dirt")) {
                    index = 1;
                } else if (pickTerrainMenu.isButtonClicked("Water")) {
                    index = 2;
                } else setTile();
            }
        }
        //handle keyboard input
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) {
                moveIndex();
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.getEventKeyState()) {
                save("map", grid);
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState())
                ManageTheMenu.setGameCondition(ManageTheMenu.GameState.MAINMENU);
        }
    }

    private void draw() {
        drawQuadTex(metal, 1280, 0, 320, 900);
        grid.draw();
        gameInterface.draw();
        gameInterface.drawText(0, 0, "Press S to save your map, ESC to return to main menu");
    }

    private void setTile() {
        grid.setTile((int) Math.floor(Mouse.getX() / TILE_SIZE), (int) Math.floor((HEIGHT - Mouse.getY() - 1) / TILE_SIZE), types[index]);
    }

    private void moveIndex() {
        index++;
        if (index > types.length - 1) {
            index = 0;
        }
    }
}
