package Game.GameMenu;

import Game.Data.UI.UserInterface;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;
import static Game.Data.Tools.Artist.*;

public class MainMenu {
    private Texture background; //hình nền
    private UserInterface menuUI;

    public MainMenu() {
        background = quickLoad("mainmenu");
        menuUI =     new UserInterface();
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
        drawTexture(background, 0, 0, WIDTH, HEIGHT);
        menuUI.draw();
        updateButton();
    }
}
