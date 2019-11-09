package Game.GameMenu;

import Game.Data.Tools.Clock;
import org.lwjgl.opengl.Display;
//import Game.Data.Audio.AudioPlayer;
//import Game.GameMenu.ManageTheMenu;

import static Game.Data.Tools.Artist.*;

public class Main {
    //private AudioPlayer music;
    public Main() {
        beginSession();
        //music = new AudioPlayer("/res/Music/backgound.mp3");
        //music.play();

        while (!Display.isCloseRequested()){
            Clock.update();
            ManageTheMenu.update();
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }

    public static void main(String[] args) {
        new Main();
    }
}