
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyReleased implements EventHandler<KeyEvent>
{
    public void handle(KeyEvent event) {
        
        if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP)
        {
            Keyboard.setW(false);
        }

        if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT)
        {
            Keyboard.setA(false);
        }

        if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN)
        {
            Keyboard.setS(false);
        }

        if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT)
        {
            Keyboard.setD(false);
        }

        if (event.getCode() == KeyCode.SPACE)
        {
            Keyboard.setSpace(false);
        }
    }
}
