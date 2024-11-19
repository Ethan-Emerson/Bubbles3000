

//Singleton Start Class to limit where the player starts to the last tile selected
//Esentially keep one coopy of it

public class Start extends Feature {
    private static Start instance;
    private static Player player = new Player();
    private static Tile tp = null;
    private static boolean deployed = false;

    private Start() {
    }

    public static Start getInstance() {
        if (instance == null) {
            synchronized (Start.class) {
                if (instance == null) {
                    instance = new Start();
                }
            }
        }
        return instance;
    }

    public void doThing()
    {
    }

    public void setStartTilePosition(Tile tp){
        this.tp = tp;
    }

    public int getStartX(){return tp.getX();}
    public int getStartY(){return tp.getY();}

    public boolean getIfDeployed(){return deployed;}

    public void setDeployedAs(boolean deployed){
        this.deployed = deployed;
    }
}

