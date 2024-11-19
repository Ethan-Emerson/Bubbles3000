
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.HashMap;

public class TileManager {
    public static boolean playerBeingMovedByTile;
    public static boolean playerMovedByPull = false;

    // called when a player interacts or walks over a tile
    // executes all features corresponding with the tile
    public static void playerInteractWithTile(Tile t) {
        TileType type = t.getTileType();

        for (int j = 0; j < type.getFeatures().size(); j++) {
            Feature f = type.getFeatures().get(j);
            // for broken tile functionality
            if (f instanceof Break) {
                Break b = (Break) f;
                double time = b.getTime();

                AnimationTimer timer = new AnimationTimer() {
                    private long lastUpdate = 0;
                    long duration = 0;

                    @Override
                    public void handle(long now) {
                        double durationInSeconds = duration / 1_000_000_000.0;

                        if (durationInSeconds >= time) {
                            t.setIsEnabled(false);
                            this.stop();
                        }
                        if (lastUpdate != 0) { // Ensure we don't update on the first frame
                            duration += now - lastUpdate; // Accumulate elapsed time in nanoseconds
                        }
                        lastUpdate = now;
                    }
                };

                // Start the jump timer to handle the jump animation
                timer.start();
            }
        }
        if(t.getIsEnabled())
            type.doThing();
    }

    static int time = 0;

    static HashMap<Tile, Integer> pushTimers = new HashMap<>();

    // for tile interactions that have a radius like push/pull
    public static void checkPlayerInteractWithRadiusTile() {
        ArrayList<Tile> onScreenTiles = Main.getGameInstance().getTheLevel().getOnScreenTiles();

        playerBeingMovedByTile = false;
        playerMovedByPull = false;
        Player.playerXSpeed = 2;
        Player.playerYSpeed = 2;

        for (int i = 0; i < onScreenTiles.size(); i++) {
            Tile t = onScreenTiles.get(i);
            TileType type = t.getTileType();

            // skip this tile because features are disabled
            if(!t.getFeaturesEnabled())
                continue;

            for (int j = 0; j < type.getFeatures().size(); j++) {
                Feature f = type.getFeatures().get(j);
                if (f instanceof Push) {
                    Push p = (Push) f;

                    if (p.getOn()) {
                        double radius = p.getRadius() * 30;
                        double amount = p.getAmount();
                        double tileX = t.getX() * 30 + 30;
                        double tileY = t.getY() * 30 + 30;

                        double deltaX = Player.getX() - tileX;
                        double deltaY = Player.getY() - tileY;
                        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                        // check if player is in radius
                        if (distance < radius) {

                            if(pushTimers.containsKey(t))
                            {
                                int time = pushTimers.get(t) + 1;
                                pushTimers.replace(t, time);
                            }
                            else
                            {
                                pushTimers.put(t, 1);
                            }

                            if(pushTimers.get(t) > 5)
                            {
                                playerBeingMovedByTile = true;

                                double speed = 2 - (distance / radius) * (2 - amount);
                                double normX = deltaX / distance;
                                double normY = deltaY / distance;

                                Player.move(speed * normX, speed * normY);
                            }
                        }
                        else
                        {
                            pushTimers.replace(t, 0);
                        }
                    }
                } else if (f instanceof Pull) {
                    Pull p = (Pull) f;

                    if (p.getOn()) {
                        double radius = p.getRadius() * 30;
                        double amount = p.getAmount();
                        double tileX = t.getX() * 30 + 30;
                        double tileY = t.getY() * 30 + 30;

                        double deltaX = tileX - Player.getX();
                        double deltaY = tileY - Player.getY();
                        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                        // Check if the player is within the circular radius
                        if (distance < radius) {
                            playerBeingMovedByTile = true;
                            playerMovedByPull = true;

                            // Inverse speed
                            double speed = 2 - (distance / radius) * (2 - amount);

                            double normX = deltaX / distance;
                            double normY = deltaY / distance;

                            if (Player.getJumping())
                                Player.move(speed * normX, speed * normY);
                            else
                                Player.move(speed * normX, 0);
                        }
                    }
                } else if (f instanceof Velocity) {

                    Velocity v = (Velocity) f;
                    double radius = v.getRadius() * 30;
                    double tileX = t.getX() * 30 + 30;
                    double tileY = t.getY() * 30 + 30;

                    double velocityX = v.getVelocityX();
                    double velocityY = v.getVelocityY();

                    double deltaX = Player.getX() - tileX;
                    double deltaY = Player.getY() - tileY;
                    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                    if (distance < radius) {
                        double speedFactor = 1 - (distance / radius);  // Inverse relationship:

                        Player.playerXSpeed += velocityX * speedFactor;
                        Player.playerYSpeed += velocityY * speedFactor;
                    }

                } else if(f instanceof Timer){
                    TimerHandler timerHandler = Main.getGameInstance().getTheLevel().getTimerHandler(t);
                    if(!timerHandler.getTimerOn() && !timerHandler.getFirstRun()){
                        timerHandler.resetAdjacentTiles();
                        timerHandler.setFirstRun(true);
                    }
                } else if(f instanceof Activate a){
                    double tileSize = Main.getGameInstance().getTileSize();
                    double tileX = (t.getX() * tileSize) + tileSize;
                    double tileY = (t.getY() * tileSize) + tileSize;
                    double tileXbound = tileX + tileSize;
                    double tileYbound = tileY + tileSize;

                    if(!Game.collideCheck(tileX,tileY,tileXbound,tileYbound,Player.getY()+12,Player.getX(),Player.getSize()-1)){
                        a.setActivate(true);
                    }

                }
            }
        }
    }
}