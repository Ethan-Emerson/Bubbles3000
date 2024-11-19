import java.util.ArrayList;

public class Activate extends Feature{

    private String feature; //feature to activate
    private boolean canActivate = true;

    public Activate(String feature) {
        this.feature = feature;
    }

    public void doThing(){
        if(canActivate){
            ArrayList<Tile> onScreenTiles = Main.getGameInstance().getTheLevel().getOnScreenTiles();
            for(int i = 0; i < onScreenTiles.size(); i++)
            {
                Tile t = onScreenTiles.get(i);

                ArrayList<Feature> tileFeatures = t.getTileType().getFeatures();
                for (int j = 0; j < tileFeatures.size(); j++) {
                    Feature tempF = tileFeatures.get(j);


                    //Do the comparisons
                    if (tempF instanceof Start && feature.equals("start")) {
                        t.setIsEnabled(!t.getIsEnabled());
                    } else if (tempF instanceof Goal && feature.equals("goal")) {
                        t.setIsEnabled(!t.getIsEnabled());
                    } else if (tempF instanceof Break && feature.equals("broken")) {
                        t.setIsEnabled(!t.getIsEnabled());
                    } else if (tempF instanceof Death && feature.equals("death")) {
                        t.setIsEnabled(!t.getIsEnabled());
                    } else if (tempF instanceof Pull && feature.equals("pull")) {
                        t.setIsEnabled(!t.getIsEnabled());
                    } else if (tempF instanceof Push && feature.equals("push")) {
                        t.setIsEnabled(!t.getIsEnabled());
                    } else if (tempF instanceof Timer && feature.equals("timer")) {
                        t.setIsEnabled(!t.getIsEnabled());
                        Main.getGameInstance().getTheLevel().toggleTimer(t);
                    } else if (tempF instanceof Velocity && feature.equals("velocity")) {
                        t.setIsEnabled(!t.getIsEnabled());
                    } else if (tempF instanceof Activate && feature.equals("activate")) {
                        t.setIsEnabled(!t.getIsEnabled());
                    }
                }
            }
            canActivate = false;
        }
    }

    public void setActivate(boolean state)
    {
        canActivate = state;
    }
}