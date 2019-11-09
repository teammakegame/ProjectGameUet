package Game.GameMenu;

import Game.Entity.GameTiles.Tile.*;
import Game.GameStage;

import static Game.Data.Tools.Level.load;

public class ManageTheMenu {
    public static enum GameState {
        MAINMENU, GAME, EDITOR
    }

    public static GameState gameCondition = GameState.MAINMENU;
    public static MainMenu mainMenu;
    public static GameStage gameStage;
    public static Editor editor;
    public static long sec = System.currentTimeMillis() + 1000;
    public static int frameLastSec = 0;
    public static int frameThisSec = 0;

    static TileGrid map = load("map");

    public static void update() {
        switch (gameCondition) {
            case MAINMENU:
                if (mainMenu == null)
                    mainMenu = new MainMenu();
                mainMenu.update();
                break;
            case GAME:
                if (gameStage == null)
                    gameStage = new GameStage(map);
                gameStage.update();
                break;
            case EDITOR:
                if (editor == null)
                    editor = new Editor();
                editor.update();
                break;
        }

        long time = System.currentTimeMillis();
        if (time > sec) {
            sec += 1000;
            frameLastSec = frameThisSec;
            frameThisSec = 0;
            System.out.println(frameLastSec + " fps");
        }
        frameThisSec++;
    }

    public static void setGameCondition(GameState newState)  {
        gameCondition = newState;
    }
}
