
import javafx.event.EventHandler;
import javafx.scene.input.*;

public class MouseEvents implements EventHandler<MouseEvent> {

    private Editor editor = Editor.getInstance();

    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if(!Main.getGameInstance().getGameState())
                editor.clicked(event.getX(), event.getY());
        } else if (event.getEventType() == MouseEvent.MOUSE_MOVED || event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            editor.mouseMoved(event.getX(), event.getY());
        }
    }
}