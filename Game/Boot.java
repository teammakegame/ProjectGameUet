package Game;

import Game.Data.Tools.Clock;
import org.lwjgl.opengl.Display;
import Game.Data.Audio.AudioPlayer;
import Game.GameMenu.*;

import static Game.Data.Tools.Artist.*;

public class Boot {
    private AudioPlayer music;

    public Boot() {
        beginSession();
        music = new AudioPlayer("/res/Music/backgound.mp3");
        music.play();

        //TileGrid grid = new TileGrid(map);
        //Wave wave = new Wave(20, e);
        //Player player = new Player(grid);
        //NormalTower tower = new NormalTower(quickLoad("cannon5"), grid.getTile(4, 1), 10);

        //GameStage gameStage = new GameStage(map);
        while (!Display.isCloseRequested()){
            Clock.update();
            //grid.draw();
            //wave.Update();
            //player.update();
            //tower.update();
            //gameStage.update();
            ManageTheMenu.update();
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }

    public static void main(String[] args) {
        new Boot();
    }
}
