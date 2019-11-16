package Game.Entity.GameTiles.Tile;

import Game.Data.UI.UserInterface;
import Game.Data.UI.UserInterface.*;
import Game.GameMenu.ManageTheMenu;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import static Game.Data.Tools.Artist.*;
import static Game.Data.Tools.Level.*;


//chế độ sáng tạo, tạo map theo ý thích của người chơi
public class Editor {
    private TileGrid      grid;
    private int           index;
    private TileType[]    types;
    private UserInterface gameInterface;
    private Menu          pickTerrainMenu;
    private Texture       metal;

    public Editor() {
        this.grid =     load ("map");
        this.index =    0;
        this.types =    new TileType[5];
        this.types[0] = TileType.FreshGrass;
        this.types[1] = TileType.FadedGrass;
        this.types[2] = TileType.Sand;
        this.types[3] = TileType.Route;
        this.types[4] = TileType.Water;
        this.metal =    quickLoad("metal");
        setTheUI();
    }

    private void setTheUI() {
        gameInterface = new UserInterface();
        gameInterface.createMenu("pickTerrain",1150, 150, 580, 700,1, 0);
        pickTerrainMenu = gameInterface.getMenu("pickTerrain");
        pickTerrainMenu.quickAccess("Grass1", "grass");
        pickTerrainMenu.quickAccess("Grass2", "grass4");
        pickTerrainMenu.quickAccess("Sand", "grass5");
        pickTerrainMenu.quickAccess("Route", "road12");
        pickTerrainMenu.quickAccess("Water", "water2");
    }

    public void update() {
        draw();
        if (Mouse.next()) {
            boolean clicked = Mouse.isButtonDown(0);
            if (clicked) {
                if (pickTerrainMenu.isButtonClicked("Grass1")) {
                    index = 0;
                } else if (pickTerrainMenu.isButtonClicked("Grass2")) {
                    index = 1;
                } else if (pickTerrainMenu.isButtonClicked("Sand")) {
                    index = 2;
                } else if (pickTerrainMenu.isButtonClicked("Route")) {
                    index = 3;
                } else if (pickTerrainMenu.isButtonClicked("Water")) {
                    index = 4;
                } else setTile();
            }
        }
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) {
                moveIndex();
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.getEventKeyState()) {
                save("map", grid);
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
                ManageTheMenu.setGameCondition(ManageTheMenu.GameState.MAINMENU);
            }
        }
    }

    private void draw() {
        drawTexture(metal, 1280, 0, 320, 900);
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
