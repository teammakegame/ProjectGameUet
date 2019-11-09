package Game.GameMenu;

import Game.Data.UI.UI;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;
import static Game.Data.Tools.Artist.*;
import Game.GameMenu.*;

public class MainMenu {
    private Texture background;
    private UI      menuUI;

    public MainMenu() {
        background = quickLoad("mainmenu");
        menuUI =     new UI();
        menuUI.addButton("Play", "playbutton", WIDTH / 2 - 500, (int) (HEIGHT * 0.45f));
        menuUI.addButton("Create", "createmaps", WIDTH / 2 - 480, (int) (HEIGHT * 0.65f));
        menuUI.addButton("Quit", "quitbutton", WIDTH / 2 - 480, (int) (HEIGHT * 0.85f));
    }

    private void updateButton() {
        if (Mouse.isButtonDown(0)) {
            if (menuUI.isButtonClicked("Play"))
                ManageTheMenu.setGameCondition(ManageTheMenu.GameState.GAME);
            if (menuUI.isButtonClicked("Create"))
                ManageTheMenu.setGameCondition(ManageTheMenu.GameState.EDITOR);
            if (menuUI.isButtonClicked("Quit"))
                System.exit(0);
        }
    }

    public void update() {
        drawQuadTex(background, 0, 0, 1600, 900);
        menuUI.draw();
        updateButton();
    }
}
