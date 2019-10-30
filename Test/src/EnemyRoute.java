public class EnemyRoute {
    Level level;
    int[][] route = new int[26][13];
    int[][] route_pointsWorth = new int[26][13];
    int baseBlock = 3;
    public Base base;
    int xPos, yPos;

    int lastPos = - 1;

    int RIGHT = 1;
    int DOWN = 2;
    int LEFT = 3;
    int UP = 4;

    public EnemyRoute(Level level){
        this.level = level;

        this.xPos = this.level.findSpawnPoint().x;
        this.yPos = this.level.findSpawnPoint().y;

        calculateRoute();
    }

    public void calculateRoute(){
        while (base == null){
            calculateNextPos();
        }

    }

    public void calculateNextPos(){
        for(int i = 1; i < 5; i++){
            if(i != lastPos){
                if(yPos > 0 && i == UP){
                    if(level.map[xPos][yPos - 1] == 1){
                        setPointWorth();
                        lastPos = DOWN;
                        route[xPos][yPos] = UP;

                        yPos--;
                        break;
                    }else if(level.map[xPos][yPos - 1] == baseBlock){
                        base = new Base(xPos, yPos - 1);
                        lastPos = DOWN;
                        route[xPos][yPos] = UP;
                        break;
                    }
                }


                if(i == DOWN && yPos < 12){
                    if(level.map[xPos][yPos + 1] == 1){
                        setPointWorth();
                        lastPos = UP;
                        route[xPos][yPos] = DOWN;

                        yPos++;
                        break;
                    }else if(level.map[xPos][yPos + 1] == baseBlock){
                        base = new Base(xPos, yPos + 1);
                        lastPos = UP;
                        route[xPos][yPos] = DOWN;
                        break;
                    }
                }

                if(i == RIGHT && xPos < 25){
                    if(level.map[xPos + 1][yPos] == 1){
                        setPointWorth();
                        lastPos = LEFT;
                        route[xPos][yPos] = RIGHT;

                        xPos++;
                        break;
                    }else if(level.map[xPos + 1][yPos] == baseBlock){
                        base = new Base(xPos + 1, yPos);
                        lastPos = LEFT;
                        route[xPos][yPos] = RIGHT;
                        break;
                    }
                }

                if(xPos > 0 && i == LEFT){
                    if(level.map[xPos - 1][yPos] == 1){
                        setPointWorth();
                        lastPos = RIGHT;
                        route[xPos][yPos] = LEFT;

                        xPos--;
                        break;
                    }else if(level.map[xPos - 1][yPos] == baseBlock){
                        base = new Base(xPos - 1, yPos);
                        lastPos = RIGHT;
                        route[xPos][yPos] = LEFT;
                        break;
                    }
                }
            }
        }
    }

    public void setPointWorth(){
        if(lastPos == UP){
            route_pointsWorth[xPos][yPos] = route_pointsWorth[xPos][yPos - 1] + 1;
        }
        if(lastPos == DOWN){
            route_pointsWorth[xPos][yPos] = route_pointsWorth[xPos][yPos + 1] + 1;
        }
        if(lastPos == RIGHT){
            route_pointsWorth[xPos][yPos] = route_pointsWorth[xPos + 1][yPos] + 1;
        }
        if(lastPos == LEFT){
            route_pointsWorth[xPos][yPos] = route_pointsWorth[xPos - 1][yPos] + 1;
        }
        if(lastPos == -1){
            route_pointsWorth[xPos][yPos] = 1;
        }
    }

    public int getPointsWorth(int x, int y){
        return route_pointsWorth[x][y];
    }
}

