package Game.Data.Tools;

import Game.Entity.GameTiles.Tile.Tile;
import Game.Entity.GameTiles.Tile.TileGrid;
import Game.Entity.GameTiles.Tile.TileType;

import java.io.*;

public class Level {
    public static void save(String mapName, TileGrid grid) {
        String mapData = "";
        for (int i = 0; i < grid.getTileWide(); i++) {
            for (int j = 0; j < grid.getTileHigh(); j++) {
                mapData += getTileID(grid.getTile(i, j));
            }
        }
        try {
            File file = new File(mapName);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(mapData);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TileGrid load(String mapName) {
        TileGrid grid = new TileGrid();
        try {
            File file;
            BufferedReader br = new BufferedReader(new FileReader(mapName));
            String data = br.readLine();
            for (int i = 0; i < grid.getTileWide(); i++) {
                for (int j = 0; j < grid.getTileHigh(); j++) {
                    grid.setTile(i, j, getTileType(data.substring(i * grid.getTileHigh() + j, i * grid.getTileHigh() + j + 1)));
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grid;
    }

    public static TileType getTileType(String ID) {
        TileType type = TileType.NULL;
        switch (ID) {
            case "0":
                type = TileType.Grass;
                break;
            case "1":
                type = TileType.Dirt;
                break;
            case "2":
                type = TileType.Water;
                break;
            case "3":
                type = TileType.NULL;
                break;
        }
        return type;
    }

    public static String getTileID(Tile t) {
        String ID = "ERROR!";
        switch (t.getType()) {
            case Grass:
                ID = "0";
                break;
            case Dirt:
                ID = "1";
                break;
            case Water:
                ID = "2";
                break;
            case NULL:
                ID = "3";
                break;
        }
        return ID;
    }
}
