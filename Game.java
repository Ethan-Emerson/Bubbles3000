
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;

public class Game
{
    AnimationHandler ah = new AnimationHandler();
    private static Level theLevel = new Level();
    Editor theEditor = Editor.getInstance();
    final int tileSize = 30;
    private static  GameState state = GameState.getInstance(); //using the singleton class to change the

    public Game() {
        theLevel.loadLevel();
    }

    public void drawGame() throws IOException {
        if (!state.getState()) { //load in the editor
            // clear entire canvas
            Main.getCanvas().getGraphicsContext2D().clearRect(0, 0, 1000, 1000);
            theEditor.drawEditor(Main.getCanvas().getGraphicsContext2D());
        } else { //load in the player
            // clear entire canvas
            Main.getCanvas().getGraphicsContext2D().clearRect(0, 0, 1000, 1000);

            Main.getCanvas().getGraphicsContext2D().setFill(Color.BLACK);

            Main.getCanvas().getGraphicsContext2D().fillRect(0, 0, 1000, 1000);

            //Main.getCanvas().getGraphicsContext2D().fillRect(50+Player.getX() *-1, +Player.getY()*-1, 30, 30);
            theLevel.drawLevel(Main.getCanvas().getGraphicsContext2D());

            Player.draw(Main.getCanvas().getGraphicsContext2D());
        }
    }



    public static boolean collideCheck(double tileX, double tileY, double tileXbound, double tileYbound, double x, double y, double playerSize)
    {
        double xSize = x+playerSize;
        double ySize = y+playerSize;

        // loop through every pixel in the player, to see if any pixel is colliding with passed location
        for(double i = x; i <= xSize; i++)
        {
            for(double j = y; j <= ySize; j++)
            {
                // check if collide with tile
                if((tileX <= j && j <= tileXbound) && (tileY <= i && i <= tileYbound)) {
                    return true;
                }
                j++;
            }
            i++;
        }

        return false;
    }

    public AnimationHandler getAnimationHandler()
    {
        return ah;
    }

    public Level getTheLevel()
    {
        return theLevel;
    }

    public int getTileSize()
    {
        return tileSize;
    }

    public boolean getGameState(){return state.getState();}

    public void teleportToTile(double x, double y)
    {
        World.x = (x*30 - Main.getWidth()/2) + 30;
        World.y = (y*30 - Main.getHeight()/2) - 1;

        Player.setX((double) Main.getWidth() /2 + World.x);
        Player.setY((double) Main.getHeight() /2 + World.y);
    }

    public static boolean interactCheck(double tileX, double tileY, double tileXbound, double tileYbound, double x, double y, double playerSize) {

        double xSize = x+playerSize;
        double ySize = y+playerSize;
        double middleX = (tileX + tileXbound)/2;
        double middleY = (tileY + tileYbound)/2;

        // loop through every pixel in the player, to see if any pixel is colliding with passed location
        for(double i = x; i <= xSize; i++)
        {
            for(double j = y; j <= ySize; j++)
            {
                // check if collide with tile
                if(((middleX-1 <= j && j <= middleX+1) && (tileY <= i && i <= tileYbound))) { //middle of top/bottom of the tile
                    return true;
                }
                else if((tileX <= j && j <= tileXbound) && (middleY-1 <= i && i <= middleY+1)){ //middle of right/left of the tile
                    return true;
                }
                j++;
            }
            i++;
        }
        return false;
    }
}