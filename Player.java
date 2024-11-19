
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Player
{
    static int playerSize = 30;
    static double playerX = 0;//(double) Main.getWidth() /2;
    static double playerY = 0;// (double) Main.getHeight() /2;
    public static double playerSpeed = 30;
    public static double playerXSpeed = 30;
    public static double playerYSpeed = 30;
    static boolean freezePlayer = false;
    static boolean jumping = false;
    static double jumpPower;
    private static double gravityForce = 1; // starting gravity force
    private static final double maxGravity = 12; // max downward speed
    private static final double[] jumpForces = {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

    public static double getX()
    {
        return playerX;
    }

    public static double getY()
    {
        return playerY;
    }

    public static int getSize()
    {
        return playerSize;
    }

    public static void setX(double x)
    {
        playerX = x;
    }

    public static void setY(double y)
    {
        playerY = y;
    }

    public static boolean getJumping()
    {
        return jumping;
    }

    public static void setJumping(boolean state)
    {
        jumping = state;
    }

    public static void setFreezePlayer(boolean state)
    {
        freezePlayer = state;
    }
    public static boolean getFreezePlayer()
    {
        return freezePlayer;
    }

    public static void draw(GraphicsContext gc)
    {
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(((double) Main.getWidth() /2) - Player.getSize(),((double) Main.getHeight() /2) - Player.getSize(), playerSize, playerSize);
    }

    public static void move()
    {
        if(freezePlayer)
            return;

        double newWorldX = World.x;
        double newWorldY = World.y;

        double newPlayerX = playerX;
        double newPlayerY = playerY;

        if(Keyboard.getW())
        {
            if(!Main.getGameInstance().getGameState())
            {
                newWorldY = World.y - playerYSpeed;
                newPlayerY = playerY - playerYSpeed;
            }
        }
        if(Keyboard.getS())
        {
            if(!Main.getGameInstance().getGameState())
            {
                newWorldY = World.y + playerYSpeed;
                newPlayerY = playerY + playerYSpeed;
            }
        }
        if(Keyboard.getA())
        {
            newWorldX = World.x - playerXSpeed;
            newPlayerX = playerX - playerXSpeed;
        }
        if(Keyboard.getD())
        {
            newWorldX = World.x + playerXSpeed;
            newPlayerX = playerX + playerXSpeed;
        }

        // check if newX and newY is in any non passthroughable tile

        if(checkLocationIsValid(newPlayerX, newPlayerY))
        {
            //System.out.println("Player x: " +playerX);
            //System.out.println("Player Y: " +playerY);

            //update position
            World.x = newWorldX;
            World.y = newWorldY;

            playerX = newPlayerX;
            playerY = newPlayerY;
        }
    }

    // to adjust existing player location
    public static void move(double x, double y)
    {
        if(freezePlayer)
            return;

        double newWorldX = World.x + x;
        double newWorldY = World.y + y;

        double newPlayerX = playerX + x;
        double newPlayerY = playerY + y;

        // check if newX and newY is in any non passthroughable tile

        if(checkLocationIsValid(newPlayerX, newPlayerY))
        {
            //update position
            World.x = newWorldX;
            World.y = newWorldY;

            playerX = newPlayerX;
            playerY = newPlayerY;
        }
    }

    // collision checker
    public static boolean checkLocationIsValid(double x, double y)
    {
        if(!Main.getGameInstance().getGameState())
            return true;

        int tileSize = Main.getGameInstance().getTileSize();

        ArrayList<Tile> onScreenTiles = Main.getGameInstance().getTheLevel().getOnScreenTiles();

        // tile interactions
        for(int i = 0; i < onScreenTiles.size(); i++)
        {
            Tile p = onScreenTiles.get(i);
            if (!p.getTileType().getTypeName().equals("start")) {
                double tileX = (p.getX() * tileSize) + tileSize;
                double tileY = (p.getY() * tileSize) + tileSize;
                double tileXbound = tileX + tileSize;
                double tileYbound = tileY + tileSize;

                ArrayList<Feature> features = p.getTileType().getFeatures();
                for(int j = 0; j < features.size(); j++)
                {
                    Feature f = features.get(j);
                    // make death tile a pit
                    if(f instanceof Death)
                    {
                        tileY += tileSize;
                        tileYbound +=tileSize;
                    }
                }

                if (Game.interactCheck(tileX, tileY, tileXbound, tileYbound, y, x, playerSize - 1))
                {
                    TileManager.playerInteractWithTile(p);
                }
            }
        }

        for(int i = 0; i < onScreenTiles.size(); i++)
        {
            Tile p = onScreenTiles.get(i);

            if (!p.getTileType().getTypeName().equals("start")) {
                double tileX = (p.getX() * tileSize) + tileSize;
                double tileY = (p.getY() * tileSize) + tileSize;
                double tileXbound = tileX + tileSize;
                double tileYbound = tileY + tileSize;

                ArrayList<Feature> features = p.getTileType().getFeatures();
                for(int j = 0; j < features.size(); j++)
                {
                    Feature f = features.get(j);
                    // make death tile a pit
                    if(f instanceof Death)
                    {
                        tileY += tileSize;
                        tileYbound +=tileSize;
                    }
                }

                if (p.getIsEnabled() && Game.collideCheck(tileX, tileY, tileXbound, tileYbound, y, x, playerSize - 1)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void gravity() {

        if (!Main.getGameInstance().getGameState() || jumping || (TileManager.playerBeingMovedByTile && !TileManager.playerMovedByPull)){
            return; // skip gravity if game isn't active or player is mid-jump
        }

        // Increase gravity force until it hits maxGravity
        gravityForce = Math.min(gravityForce + 1, maxGravity);

        // Check if falling down is valid
        if (checkLocationIsValid(playerX, playerY + gravityForce)) {
            playerY += gravityForce;
            World.y += gravityForce;
        } else {
            gravityForce = 1; // reset when on ground
        }
    }

    public static void jump() {
        if (!Main.getGameInstance().getGameState() || jumping || !isOnGround()) {
            return; // only jump if game is active, player isn't already jumping, and is on the ground
        }

        jumping = true;
        AnimationTimer jumpTimer = new AnimationTimer() {
            private int jumpFrame = 0; // track frames within the jump

            @Override
            public void handle(long now) {
                if (jumpFrame < jumpForces.length) {
                    double jumpSpeed = jumpForces[jumpFrame++];

                    // Check for ceiling collision
                    if (checkLocationIsValid(playerX, playerY - jumpSpeed)) {
                        playerY -= jumpSpeed;
                        World.y -= jumpSpeed;
                    } else {
                        // Stop the jump if we hit a ceiling
                        stopJump();
                        return;
                    }
                } else {
                    stopJump();
                }
            }

            private void stopJump() {
                jumping = false;
                gravityForce = 1; // reset gravity force when jump ends
                this.stop();
            }
        };

        jumpTimer.start();
    }

    private static boolean isOnGround() {
        return !checkLocationIsValid(playerX, playerY + 2); // Check if there's a tile below the player
    }
}
