import javax.swing.*;

public class Frame extends JFrame{
    GameStage gameStage;

    public Frame(){
        this.setExtendedState(MAXIMIZED_BOTH);

        setTitle("Tower Defense");
        GameStage gameStage = new GameStage(this);
        this.add(gameStage);
        this.addKeyListener(new KeyHandler(gameStage));
        this.addMouseListener(new MouseHandler(gameStage));
        this.addMouseMotionListener(new MouseHandler(gameStage));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        gameStage.start();
    }


    public static void main(String[] args) {
        Frame frame = new Frame();
        //frame.startGame();
    }

}
