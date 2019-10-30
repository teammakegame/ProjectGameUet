import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

public class GameStage extends JPanel implements Runnable {
    private Thread thread;
    private Frame frame;
    boolean IsRunning;
    private int fps;
    private int scene;

    private int hand = 0;
    private int handX;
    private int handY;

    private int[][] map = new int[26][13];
    private Tower[][] towerMap = new Tower[26][13];
    private Image[] terrain = new Image[100];

    public Tower selectedTower;

    SpawnPoint spawnPoint;

    public EnemyMove[] enemyMap = new EnemyMove[100];
    public static Bullet[] missiles = new Bullet[10];
    public int enemy = 0;
    Wave wave;

    LevelFile levelFile;
    Level level;
    public static EnemyAuto enemyAI;

    public static Player player;

    public GameStage(Frame frame) {
        this.frame = frame;
        this.frame.addKeyListener(new KeyHandler(this));
        fps = 0;
        scene = 0;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            IsRunning = true;
            thread.start();
        }
    }

    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
        if(scene == 0){
            g.setColor(Color.GREEN);
            g.fillRect(0,0, frame.getWidth(), frame.getHeight());

        } else if (scene == 1) {
            g.setColor(Color.ORANGE);
            g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

            g.setColor(Color.RED);
            //g.drawRect(40, 40, 40, 40);
            for (int i = 0; i <= 25; i++) {
                for (int j = 0; j <= 12; j++) {
                    g.drawRect(55 + i * 55, 220 + j * 55, 55, 55);
                }
            }

            //grid
            g.setColor(Color.GRAY);
            for(int i = 0; i <= 25; i++){
                for(int j = 0; j <= 12; j++){
                    if(map[i][j] == 1){
                        g.drawImage(new ImageIcon("character/terrain/road.png").getImage(),  i* 55 + 55, 220 + j * 55, 55, 55, null);
                    }else if(map[i][j] == 0){
                        g.drawImage(new ImageIcon("character/terrain/grass.png").getImage(),  i* 55 + 55, 220 + j * 55, 55, 55, null);
                    }else if(map[i][j] == 3){
                        g.drawImage(new ImageIcon("character/terrain/base1.png").getImage(),  i* 55 + 55, 220 + j * 55, 55, 55, null);
                    }else if(map[i][j] == 2){
                        g.drawImage(new ImageIcon("character/terrain/road2.png").getImage(),  i* 55 + 55, 220 + j * 55, 55, 55, null);
                    }
                    g.drawRect(55 + 55 * i, 55 * 4 + 55 * j, 55, 55);
                }
            }


            //Enemy
            for(int i = 0; i < enemyMap.length; i++){
                if(enemyMap[i] != null){
                    g.drawImage(enemyMap[i].enemy.texture, (int) enemyMap[i].xPos + 55, (int) enemyMap[i].yPos + 220, 55, 55, null);
                    g.setColor(Color.GRAY);
                    g.drawRect((int)enemyMap[i].xPos + 55, (int) enemyMap[i].yPos + 200, 55, 10);
                    g.setColor(Color.RED);
                    g.drawRect((int)enemyMap[i].xPos + 55, (int) enemyMap[i].yPos + 200, 55 * enemyMap[i].health / 100, 10);
                    g.fillRect((int)enemyMap[i].xPos + 55, (int) enemyMap[i].yPos + 200, 55 * enemyMap[i].health / 100, 10);
                }
            }

            //Health and Money
            g.drawRect(55, 55, 55 * 5, 55 * 2);
            g.drawRect(55, 55, 55 * 5, 55);
            String health = "Health: " + player.health;
            g.setFont(new Font(health, Font.PLAIN, 20));
            g.drawString(health, 55 + 55 / 2, 55 + 35);

            String money = "Money: " + player.money;
            g.setFont(new Font(money, Font.PLAIN, 20));
            g.drawString(money, 55 + 55 / 2, 110 + 35);

            //skills
            for(int i = 0; i < 4; i++){
                g.drawRect(55 * 8 + (55 * 2 + 55 / 2) * i, 55, 55 * 2 + 55 / 2, 110);
            }

            //tower status
            g.drawRect(55* 30, 55, 55 * 2, 55 * 2);
            if(selectedTower != null){
                g.drawImage(selectedTower.image, 55* 30, 55, 55 * 2, 55 * 2, null);
            }

            //button
            g.drawImage(new ImageIcon("character/terrain/playbutton.png").getImage(), 55 * 8, 55, 55 * 2 + 55 / 2, 55 * 2, null);

            // tower list

            g.drawRect(55 * 30, 55 * 4, 55 * 2, 55 * 2);
            g.drawImage(Tower.towerList[0].image, 55 * 30, 55 * 4, null);
            g.drawImage(Tower.towerList[1].image, 55 * 30, 55 * 7, null);
            g.setColor(new Color(220, 0 , 0, 100));
            //System.out.println(Tower.towerList[0].cost);
            if(Tower.towerList[0].cost > player.money){
                g.fillRect(55 * 30, 55 * 4, 55 * 2, 55 * 2);
            }

            g.drawRect(55 * 30, 55 * 7, 55 * 2, 55 * 2);
            g.drawRect(55 * 30, 55 * 10, 55 * 2, 55 * 2);
            g.drawRect(55 * 30, 55 * 13, 55 * 2, 55 * 2);
            g.drawRect(55 * 30, 55 * 16, 55 * 2, 55 * 2);

            //missiles
            Graphics2D g2d = (Graphics2D) g;
            for(int i = 0; i < missiles.length; i++){
                if(missiles[i] != null){
                    g2d.rotate(missiles[i].direction + Math.toRadians(90), (int) missiles[i].x, (int) missiles[i].y);
                    g.drawImage(missiles[i].texture, (int)missiles[i].x, (int)missiles[i].y, 10, 20, null);
                    g2d.rotate(-missiles[i].direction + Math.toRadians(-90), (int) missiles[i].x, (int) missiles[i].y);
                }
            }
            //hand
            if(hand != 0 && Tower.towerList[hand - 1] != null){
                g.drawImage(Tower.towerList[hand - 1].image, this.handX - 55 / 2, this.handY - 90 / 2, 55, 55, null);
            }

            //display tower and range
            for(int i = 0; i <= 25; i++){
                for(int j = 0; j <= 12; j++){
                    if(towerMap[i][j] != null) {
                        if(selectedTower == towerMap[i][j]) {
                            g.setColor(Color.GRAY);
                            g.drawOval(55 + 55 * i - (towerMap[i][j].range * 2 * 55 + 55) / 2 + 55 / 2, 55 * 4 + 55 * j - (towerMap[i][j].range * 2 * 55 + 55) / 2 + 55 / 2, towerMap[i][j].range * 2 * 55 + 55, towerMap[i][j].range * 2 * 55 + 55);
                            g.setColor(new Color(50, 50, 50, 50));
                            g.fillOval(55 + 55 * i - (towerMap[i][j].range * 2 * 55 + 55) / 2 + 55 / 2, 55 * 4 + 55 * j - (towerMap[i][j].range * 2 * 55 + 55) / 2 + 55 / 2, towerMap[i][j].range * 2 * 55 + 55, towerMap[i][j].range * 2 * 55 + 55);
                        }
                        g.drawImage(Tower.towerList[towerMap[i][j].id].image, 55 + 55 * i, 55 * 4 + 55 * j, 55, 55, null);
                        if(towerMap[i][j].target != null){
                            if(towerMap[i][j] instanceof  WarriorTower) {
                                g.setColor(Color.BLACK);
                                g.drawLine(55 + 55 * i + 55 / 2, 55 * 4 + 55 * j + 55 / 2, 55 + (int) towerMap[i][j].target.xPos + 55 / 2, 55 * 4 + (int) towerMap[i][j].target.yPos + 55 / 2);
                            }

                        }
                    }
                }
            }
        }
    }



    public void loadGame(){
        IsRunning = true;
        levelFile = new LevelFile();
        wave = new Wave(this);

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++) {
                terrain[j + (10 * i)] = new ImageIcon("character/" + "terrain.png").getImage();
                terrain[j + 10 * i] = createImage(new FilteredImageSource(terrain[j + 10 * i].getSource(), new CropImageFilter(j * 25, i * 25 , 25, 25)));
            }
        }

    }


    public void startGame(){
        scene = 1;
        this.level = levelFile.getLevel();
        //this.level.findSpawnPoint();
        this.map = this.level.map;

        player = new Player(this);
        this.wave.waveNumber = 0;
        this.enemyAI = new EnemyAuto(this.level);
    }

    public void run() {
        loadGame();

        fps = 60;
        long period = 1000 * 1000000 / fps;
        long beginTime;
        long sleepTime;
        beginTime = System.nanoTime();
        while (IsRunning) {
            repaint();
            long deltaTime = System.nanoTime() - beginTime;
            sleepTime = period - deltaTime;
            update();
            try {
                if (sleepTime > 0)
                    Thread.sleep(sleepTime / 1000000);
                else {
                    Thread.sleep(period / 2000000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            beginTime = System.nanoTime();
        }

        System.out.println(scene);
        System.exit(0);
    }

    public class keyInput{
        public void ESC(){
            IsRunning = false;
        }
        public void Enter(){
            wave.nextWave();
        }
        public void Space(){
            startGame();
        }
    }

    public void enemyUpdate(){
        for(int i = 0; i < enemyMap.length; i++){
            if(enemyMap[i] != null){
                //System.out.println(i);
                if(!enemyMap[i].attack){
                    EnemyAuto.moveAI.move(enemyMap[i]);
                }

                enemyMap[i] = enemyMap[i].update();
                if(GameStage.player.health <= 0){
                   IsRunning = false;
                }
            }
        }
    }

    public void towerUpdate(){
        for(int i = 0; i < 26; i++){
            for(int j = 0; j < 13; j++){
                if(towerMap[i][j] != null){
                    towerAttack(i, j);
                }
            }
        }
    }

    public void towerAttack(int x, int y){
        if(this.towerMap[x][y].target == null){
            //find target
            if(this.towerMap[x][y].attackTimeDelay > this.towerMap[x][y].maxAttackTimeDelay){
                EnemyMove currentEnemy = this.towerMap[x][y].calculateEnemy(enemyMap, x, y);

                if(currentEnemy != null){
                    this.towerMap[x][y].towerAttack(x, y, currentEnemy);

                    this.towerMap[x][y].target = currentEnemy;
                    this.towerMap[x][y].attackTime = 0;
                    this.towerMap[x][y].attackTimeDelay = 0;

                    System.out.println("enemy attacked: health:" + currentEnemy.health + " x:" + x + " y:" + y);
                }
            }else {
                this.towerMap[x][y].attackTimeDelay += 1;
            }

        }else {
            if(this.towerMap[x][y].attackTime < this.towerMap[x][y].maxAttackTime){
                this.towerMap[x][y].attackTime += 1;
            }else {
                this.towerMap[x][y].target = null;
            }
        }
    }

    public void missileUpdate(){
        for(int i = 0; i < missiles.length; i++){
            if(missiles[i] != null){
                missiles[i].update();

                if(missiles[i].target == null){
                    missiles[i] = null;
                }
            }
        }
    }

    public void update(){
        enemyUpdate();
        towerUpdate();
        missileUpdate();

        if(wave.spawning){
            wave.spawnEnemy();
        }
    }

    public void spawnEnemy(int enemyID){
        spawnPoint = level.findSpawnPoint();
        for(int i = 0; i < enemyMap.length; i++){
            if(enemyMap[i] == null){
                enemyMap[i] = new EnemyMove(Enemy.enemyList[enemyID], spawnPoint);
                break;
            }
        }
    }

    public void placeTower(int x, int y){
        int xPos = (x ) / 55;
        int yPos = (y + 55 / 2) / 55;
        yPos -= 4;
        //System.out.println(x);
        //System.out.println(y);
        if(xPos < 26 && yPos < 13) {

            xPos -= 1;
            yPos -= 1;
            if (towerMap[xPos][yPos] == null && map[xPos][yPos] == 0) {
                player.money -= Tower.towerList[hand - 1].cost;
                towerMap[xPos][yPos] = Tower.towerList[hand - 1];

                towerMap[xPos][yPos] = (Tower) Tower.towerList[hand - 1].clone();
                selectedTower = towerMap[xPos][yPos];
            }
        }
    }

    public void selectTower(int x, int y){
        int xPos = (x ) / 55;
        int yPos = (y + 55 / 2 ) / 55;
        yPos -= 4;

        //System.out.println(x);
        //System.out.println(y);

        if(xPos > 0 && xPos < 26 && yPos > 0 && yPos < 13) {
            xPos -= 1;
            yPos -= 1;

            selectedTower = towerMap[xPos][yPos];
        }else {
            selectedTower = null;
        }
    }

    public void startButton(int x, int y){
        if(x >= 55* 8 && x <= 55 * 10 + 55 / 2){
            if(y >= 55 && y <= 55 * 3){
                wave.nextWave();
            }
        }
    }


    public class Mouse{
        boolean mouseprs = false;
        public void mousePrs(MouseEvent e){
            mouseprs = true;

            if(hand != 0){
                System.out.println(e.getX());
                System.out.println(e.getY());
                placeTower(e.getXOnScreen(), e.getYOnScreen());
                hand = 0;
            }
            else {

                selectTower(e.getXOnScreen(), e.getYOnScreen());
                startButton(e.getX(), e.getY());
            }
            updateMouse(e);
        }

        public void mouseMove(MouseEvent e) {
            handX = e.getXOnScreen();
            handY = e.getYOnScreen();
        }

        public void updateMouse(MouseEvent e){
            if(scene == 1){
                if(hand == 0 && mouseprs) {
                    if (e.getXOnScreen() >= 55 * 30 && e.getXOnScreen() <= 55 * 32) {
                        if (e.getYOnScreen() >= 55 * 4 && e.getYOnScreen() <= 55 * 6 + 55 / 2) {
                            if (player.money > Tower.towerList[0].cost) {
                                hand = 1;
                            }
                        }
                    }

                    if (e.getXOnScreen() >= 55 * 30 && e.getXOnScreen() <= 55 * 32) {
                        if (e.getYOnScreen() >= 55 * 7 && e.getYOnScreen() <= 55 * 9 + 55 / 2) {
                            if (player.money > Tower.towerList[0].cost) {
                                hand = 2;
                            }
                        }
                    }

                    if (e.getXOnScreen() >= 55 * 30 && e.getXOnScreen() <= 55 * 32) {
                        if (e.getYOnScreen() >= 55 * 10 && e.getYOnScreen() <= 55 * 12+ 55 /2) {
                            if (player.money > Tower.towerList[0].cost) {
                                hand = 3;
                            }
                        }
                    }
                }
            }
        }
    }
}
