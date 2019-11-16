package Game.GameMenu;

import Game.Data.UI.UserInterface;
import Game.GameStage;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import static Game.GameMenu.ManageTheMenu.*;
import static Game.Data.Tools.Artist.*;

public class GameOver {
    private Texture background;
    private UserInterface userInterface;

    public GameOver() {
        background = quickLoad("gameover");
        userInterface = new UserInterface();
    }

    public void draw() {
        userInterface.drawText(1, 1, "Press R to restart your gameStage, ESC to return to main menu");
    }

    public void update() {
        drawTexture(background, 0, 0, WIDTH, HEIGHT);
        draw();
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_R) {
                ManageTheMenu.setGameCondition(ManageTheMenu.GameState.GAME);
                gameStage = new GameStage(map);
                gameStage.update();
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                ManageTheMenu.setGameCondition(ManageTheMenu.GameState.MAINMENU);
                mainMenu = new MainMenu();
                mainMenu.update();
                gameStage = new GameStage(map);
                gameStage.update();
            }
        }
    }
}
