public class EnemyMove {
    public Enemy enemy;
    public SpawnPoint spawnPoint;

    double xPos;
    double yPos;

    int routePosX;
    int routePosY;

    int health;
    boolean attack;

    public EnemyMove(Enemy enemy, SpawnPoint spawnPoint){
        this.enemy = enemy;
        this.routePosX = spawnPoint.x;
        this.routePosY = spawnPoint.y;
        this.xPos = spawnPoint.x * 55;
        this.yPos = spawnPoint.y * 55;
        //System.out.println(spawnPoint.x);

        this.health = enemy.health;
        attack = false;
    }

    public EnemyMove update(){
        EnemyMove currentEnemy = this;
        if(currentEnemy.health <= 0){
            GameStage.player.money += 10;
            return null;
        } else if(this.routePosX == EnemyAuto.baseposX && this.routePosY == EnemyAuto.baseposY){
            GameStage.player.health -= 10;

            return null;
        }

        return currentEnemy;
    }
}
