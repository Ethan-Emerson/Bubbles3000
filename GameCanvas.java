
import javafx.scene.canvas.Canvas;

import javafx.scene.canvas.*;

public class GameCanvas extends Canvas
{
   GraphicsContext gc;

   public GameCanvas()
   {
       setWidth(Main.getWidth());
       setHeight(Main.getHeight());
       gc = getGraphicsContext2D();
   }

   public void draw()
   {

   }



}
