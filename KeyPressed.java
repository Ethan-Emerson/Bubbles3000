
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyPressed implements EventHandler<KeyEvent>
{
    public void handle(KeyEvent event) {

        if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP)
        {
            Keyboard.setW(true);
        }

        if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT)
        {
            Keyboard.setA(true);
        }

        if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN)
        {
            Keyboard.setS(true);
        }

        if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT)
        {
            Keyboard.setD(true);
        }

        if (event.getCode() == KeyCode.SPACE)
        {
            Keyboard.setSpace(true);
        }
    }
}
