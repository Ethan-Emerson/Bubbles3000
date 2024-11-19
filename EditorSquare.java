
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EditorSquare {
    private double x, y;

    public EditorSquare(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean containsPoint(double x, double y) {
        // Tile covers a 30x30 area, so the bottom-right corner is (x + 30, y + 30)
        return x >= this.x && x < (this.x + 30) && y >= this.y && y < (this.y + 30);
    }

    public void drawMe(GraphicsContext gc) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x, y, 30, 30);
        gc.setFill(Color.GRAY);
        gc.fillRect(x + 1, y + 1, 28, 28);
    }
}