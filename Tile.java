
public class Tile
{
    private int x;
    private int y;
    private TileType tileType;
    private boolean isEnabled = true;
    private boolean featuresEnabled = true;

    public Tile(int x, int y, TileType tileType)
    {
        this.x = x;
        this.y = y;
        this.tileType = tileType;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public TileType getTileType()
    {
        return tileType;
    }

    public String toString(){
        String toReturn = "";
        toReturn += tileType.getName() +" "+x+" "+y+"\n";
        return toReturn;
    }

    public void setIsEnabled(boolean state)
    {
        isEnabled = state;
    }
    public boolean getIsEnabled()
    {
        return isEnabled;
    }

    public void setFeaturesEnabled(boolean state)
    {
        featuresEnabled = state;
    }
    public boolean getFeaturesEnabled()
    {
        return featuresEnabled;
    }
}
