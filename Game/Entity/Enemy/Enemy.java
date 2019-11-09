package Game.Entity.Enemy;

import org.newdawn.slick.opengl.Texture;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import Game.Player;
import Game.Entity.GameEntity;
import Game.Entity.GameTiles.Tile.*;
import static Game.Data.Tools.Artist.*;
import static Game.Data.Tools.Clock.*;


public class Enemy implements GameEntity {
    private int                   width, height, currentCheckpoint; //chiều rộng, chiều cao, máu và điểm rẽ hiện tại
    private float                 speed, x, y, health, totalHealth, check; // tốc độ và toạ độ của enemy
    private Tile                  eTile; //ô vuông nơi enemy đang đứng
    private Texture               texture, healthBackgound, healthForeground, healthBorder; //texture enemy
    private boolean               first, alive; //enemy đầu tiên và còn sống
    private TileGrid              grid;
    private ArrayList<Checkpoint> checkpoints; //list các góc để enemy rẽ hướng
    private int[] directions; //mảng chứa hướng di chuyển của enemy

    public Enemy(int x, int y, TileGrid grid) {
        this.texture = quickLoad("enemy2");
        this.healthBackgound =   quickLoad("health1");
        this.healthForeground =  quickLoad("health2");
        this.healthBorder =      quickLoad("health3");
        this.eTile = grid.getTile(x, y);
        this.x =                 eTile.getX();
        this.y =                 eTile.getY();
        this.width =             TILE_SIZE;
        this.height =            TILE_SIZE;
        this.health =            50;
        this.totalHealth =       health;
        this.check =             health;
        this.speed =             100;
        this.grid =              grid;
        this.first =             true;
        this.alive =             true;
        this.checkpoints =       new ArrayList<Checkpoint>();
        this.directions =        new int[2];
        this.directions[0] =     0; //hướng của enemy theo x
        this.directions[1] =     0; //hướng của enemy theo y
        this.directions =        findNextDirection(eTile); //tìm hướng đi tiếp theo của enemy
        this.currentCheckpoint = 0;
        populateCheckpointList();
    }

    public Enemy(Texture texture, Tile eTile, TileGrid grid, int width, int height, float health, float speed){
        this.texture =           texture;
        this.healthBackgound =   quickLoad("health1");
        this.healthForeground =  quickLoad("health2");
        this.healthBorder =      quickLoad("health3");
        this.eTile =             eTile;
        this.x =                 eTile.getX();
        this.y =                 eTile.getY();
        this.width =             width;
        this.height =            height;
        this.health =            health;
        this.totalHealth =       health;
        this.check =             health;
        this.speed =             speed;
        this.grid =              grid;
        this.first =             true;
        this.alive =             true;
        this.checkpoints =       new ArrayList<Checkpoint>();
        this.directions =        new int[2];
        this.directions[0] =     0; //hướng của enemy theo x
        this.directions[1] =     0; //hướng của enemy theo y
        this.directions =        findNextDirection(eTile); //tìm hướng đi tiếp theo của enemy
        this.currentCheckpoint = 0;
        populateCheckpointList();
    }

    //nhận damage từ tháp
    public void takingHit(int amount) {
        health -= amount;
        if (health <= 0) {
            die();
            Player.setTheMoney(10);
        }
    }

    private void die() {
        alive = false;
    }

    public void update() {
        //kiểm tra xem có phải lần update đầu tiên không
        if (first) first = false;
        else {
            //nếu như đến điểm rẽ
            if (checkPointReached()) {
                if (currentCheckpoint + 1 == checkpoints.size()) {
                    endOfMap(); //end đường thì die
                }
                else {
                    currentCheckpoint++;
                }
            }
            else {
                //nếu chưa đến điểm rẽ, di chuyển enemy theo toạ độ của điểm rẽ tiếp theo
                x += Delta() * checkpoints.get(currentCheckpoint).getDirectionX() * speed;
                y += Delta() * checkpoints.get(currentCheckpoint).getDirectionY() * speed;
            }
        }
    }

    //khi enemy đến điểm rẽ cuối cùng
    private void endOfMap() {
        Player.setTheLives(-1);
        die();
    }

    private boolean checkPointReached() {
        boolean reached = false;
        Tile t = checkpoints.get(currentCheckpoint).getTile();
        //kiểm tra xem nếu toạ độ enemy lớn hơn toạ độ điểm rẽ - 1 và nhỏ hơn toạ độ điểm rẽ + 1
        if (x > t.getX() - 3 && x < t.getX() + 3 && y > t.getY() - 3 && y < t.getY() + 3) {
            reached = true;
            x = t.getX();
            y = t.getY();
        }
        return reached;
    }

    private void populateCheckpointList() {
        //thêm điểm rẽ đầu tiên vào mảng checkpoints
        checkpoints.add(findNextCheckpoint(eTile, directions = findNextDirection(eTile)));
        int count = 0;
        boolean cont = true;
        while (cont){
            //tìm hướng đi tiếp theo từ điểm rẽ hiện tại rồi add vào mảng currentDirection
            int[] currentDirection = findNextDirection(checkpoints.get(count).getTile());
            // kiểm tra xem điểm rẽ tiếp theo có tồn tại không, dừng lại sau 20 lần đếm
            if (currentDirection[0] == 100 || count == 20) {
                cont = false;
            }
            else {
                checkpoints.add(findNextCheckpoint(checkpoints.get(count).getTile(), directions = findNextDirection(checkpoints.get(count).getTile())));
            }
            count++;
        }
    }

    private Checkpoint findNextCheckpoint (Tile start, int[] direc){
        Tile next = null;
        Checkpoint c = null;
        boolean found = false; //xem có tìm thấy ô rẽ tiếp theo không
        int count = 1; //tăng mỗi vòng lặp
        while (!found) {
            //nếu ô của enemy giống
            //nếu ô của enemy khác vs những ô tiếp theo theo hướng đi của enemy thì thực hiện lệnh điều kiện
            if (start.getPlaceX() + direc[0] * count == grid.getTileWide() || start.getPlaceY() + direc[1] * count == grid.getTileHigh() || start.getType() != grid.getTile(start.getPlaceX() + direc[0] * count, start.getPlaceY() + direc[1] * count).getType()) {
                found = true; //đã tìm thấy
                count -= 1; //trở lại ô trước ô khác ô của enemy
                next = grid.getTile(start.getPlaceX() + direc[0] * count , start.getPlaceY() + direc[1] * count); //và next sẽ = ô đó
            }
            count++;
        }
        c = new Checkpoint(next, direc[0], direc[1]); //trả về điểm rẽ
        return c;
    }

    private int[] findNextDirection(Tile start) {
        int[] direc = new int[2];
        Tile up = grid.getTile(start.getPlaceX(), start.getPlaceY() - 1); //ô trên enemy
        Tile right = grid.getTile(start.getPlaceX() + 1, start.getPlaceY()); //ô bên phải enemy
        Tile down = grid.getTile(start.getPlaceX(), start.getPlaceY() + 1); //ô dưới enemy
        Tile left = grid.getTile(start.getPlaceX() - 1, start.getPlaceY()); //ô bên trái enemy
        if (start.getType() == up.getType() && directions[1] != 1) { //nếu ô trên enemy giống ô của enemy và đang không đi xuống
            direc[0] = 0;
            direc[1] = -1;
        }
        else if (start.getType() == right.getType() && directions[0] != -1) { //nếu ô bên phải enemy giống ô của enemy và đang không sang trái
            direc[0] = 1;
            direc[1] = 0;
        }
        else if (start.getType() == down.getType() && directions[1] != -1) { //nếu ô dưới enemy giống ô của enemy và đang không đi lên
            direc[0] = 0;
            direc[1] = 1;
        }
        else if (start.getType() == left.getType() && directions[0] != 1) { //nếu ô bên trái enemy giống ô của enemy và đang không sang phải
            direc[0] = -1;
            direc[1] = 0;
        }
        else {  //không đi hướng nào
            direc[0] = 100;
            direc[1] = 100;
        }
        return direc;
    }

    //vẽ enemy
    public void draw() {
        float percent = health / totalHealth;
        drawQuadTex (texture, x, y, width, height);
        drawQuadTex(healthBackgound, x, y - 16, width, 8);
        drawQuadTex(healthForeground, x, y - 16, TILE_SIZE * percent, 8);
        drawQuadTex(healthBorder, x, y - 16, width, 8);
    }

    public void reduceCheck(float amount) {
        check -= amount;
    }

    public float getCheck() { return check; }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Tile geteTile() {
        return eTile;
    }

    public void seteTile(Tile eTile) {
        this.eTile = eTile;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public TileGrid getTileGrid() {
        return grid;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setTexture(String name) {
        this.texture = quickLoad(name);
    }
}
