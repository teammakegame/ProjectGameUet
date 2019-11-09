package Game.Entity.GameTiles.Tile;

//enum là kiểu liệt kê
public enum TileType {

    Grass("grass5", true), Dirt("road3", false), Water("water", false), NULL("water", false);

    String  textureName;
    boolean buidable;

    //khai báo texture
    TileType (String textureName, boolean buidable){
        this.textureName = textureName;
        this.buidable = buidable;
    }
}
