
import javafx.animation.AnimationTimer;

import java.util.ArrayList;

public class TimerHandler extends AnimationTimer
{
    private long lastUpdate = 0;
    final private long seconds;
    private Tile tile;
    private boolean running;
    private boolean firstRun = false;
    private boolean timerOn = false;

    public TimerHandler(Tile tile, double seconds)
    {
        this.tile = tile;
        this.seconds = (long)(seconds * 1_000_000_000L);
    }

    public void handle(long now)
    {
        if(firstRun){
            timerOn = true;
            lastUpdate = now;
            firstRun = false;
        }
        if(timerOn) {
            if (now - lastUpdate >= seconds) {
                ArrayList<Tile> onScreenTiles = Main.getGameInstance().getTheLevel().getOnScreenTiles();

                int timerX = tile.getX();
                int timerY = tile.getY();
                for (int k = 0; k < onScreenTiles.size(); k++) {
                    Tile currentTile = onScreenTiles.get(k);
                    int tileX = currentTile.getX();
                    int tileY = currentTile.getY();

                    // Check if tile is adjacent (including diagonals)
                    boolean isAdjacent = Math.abs(tileX - timerX) <= 1 &&
                            Math.abs(tileY - timerY) <= 1 &&
                            !(tileX == timerX && tileY == timerY); // Exclude the timer tile itself

                    if (isAdjacent) {
                        // check if tile is activate
                        ArrayList<Feature> features = currentTile.getTileType().getFeatures();

                        for (int i = 0; i < features.size(); i++) {
                            Feature feature = features.get(i);
                            if (feature instanceof Activate) {
                                ((Activate) feature).setActivate(true);
                                feature.doThing();
                                ((Activate) feature).setActivate(true);
                                break;
                            }
                        }

                        boolean featuresEnabled = currentTile.getFeaturesEnabled();
                        currentTile.setFeaturesEnabled(!featuresEnabled);
                    }
                }
                lastUpdate = now;
            }
        }
    }

    @Override
    public void start()
    {
        super.start();
        running = true;
    }

    @Override
    public void stop()
    {
        super.stop();
        running = false;
    }

    public boolean getRunning(){return running;}

    public void resetAdjacentTiles(){
        ArrayList<Tile> onScreenTiles = Main.getGameInstance().getTheLevel().getOnScreenTiles();

        int timerX = tile.getX();
        int timerY = tile.getY();
        for (int k = 0; k < onScreenTiles.size(); k++)
        {
            Tile currentTile = onScreenTiles.get(k);
            int tileX = currentTile.getX();
            int tileY = currentTile.getY();

            // Check if tile is adjacent (including diagonals)
            boolean isAdjacent = Math.abs(tileX - timerX) <= 1 &&
                    Math.abs(tileY - timerY) <= 1 &&
                    !(tileX == timerX && tileY == timerY); // Exclude the timer tile itself

            if (isAdjacent)
            {
                // check if tile is activate
                ArrayList<Feature> features = currentTile.getTileType().getFeatures();

                for(int i = 0; i < features.size(); i++)
                {
                    Feature feature = features.get(i);
                    if(feature instanceof Activate)
                    {
                        ((Activate) feature).setActivate(true);
                        feature.doThing();
                        ((Activate) feature).setActivate(true);
                        break;
                    }
                }
                currentTile.setFeaturesEnabled(true);
            }
        }
    }

    public void setFirstRun(boolean firstRun){this.firstRun = firstRun;}

    public void setTimerOn(boolean timerOn){this.timerOn = timerOn;}

    public boolean getFirstRun(){return firstRun;}

    public boolean getTimerOn(){return timerOn;}
}
