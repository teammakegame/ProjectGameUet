public class Player {
    private GameStage gameStage;
    int health;
    int money;

    public Player(GameStage gameStage){
        this.gameStage = gameStage;
        health = 100;
        money = 1000;
    }
}
