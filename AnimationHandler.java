
import javafx.animation.AnimationTimer;

import java.io.IOException;
import java.util.ArrayList;

public class AnimationHandler
{
    private long lastUpdate = 0;

    public void handle() {

            // update player movement if the player has moved
            Player.move();

            if(!Player.getJumping() && !Player.checkLocationIsValid(Player.getX(), Player.getY()+12))
            {
                if(Keyboard.getSpace())
                {
                    Player.jump();
                }
            }

            // gravity
            Player.gravity();

            // check if player interacted with radius tile
            if(Main.getGameInstance().getGameState()) {
                TileManager.checkPlayerInteractWithRadiusTile();
            }
            try {
                Main.getGameInstance().drawGame();
            } catch (IOException ioe) {}

    }
}
