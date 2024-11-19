
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

// Made by Mitch Woolley
// on 19 October 2024

// test line

// This class will hold information for the selectable tiles
// in the selector
public class SelectableTile {
    private int position;
    private String path;
    private TileType tileType;

    public SelectableTile(int position, TileType tileType, String path) {
        this.position = position;
        this.tileType = tileType;
        this.path = path;
    }

    public void drawMe(GraphicsContext gc, double x, double y) {
        if (tileType.getTypeName().equals("start")) {
            if (Start.getInstance().getIfDeployed()) {
                gc.setGlobalAlpha(0.5);
            }
        } else if (tileType.getTypeName().equals("goal")) {
            if (Goal.getInstance().getIfDeployed()) {
                gc.setGlobalAlpha(0.5);
            }
        }
        gc.drawImage(new Image(path, 26, 26, false, false), x, y);
        gc.setGlobalAlpha(1);
    }
    public void drawMeSelected(GraphicsContext gc, double x, double y) {
        if (tileType.getTypeName().equals("start")) {
            if (Start.getInstance().getIfDeployed()) {
                gc.setGlobalAlpha(0.5);
            }
        } else if (tileType.getTypeName().equals("goal")) {
            if (Goal.getInstance().getIfDeployed()) {
                gc.setGlobalAlpha(0.5);
            }
        }
        gc.setFill(Color.RED);
        gc.fillRect(x - 1, y - 1, 28, 28);
        gc.setGlobalAlpha(0.7);
        gc.drawImage(new Image(path, 26, 26, false, false), x, y);

        gc.setGlobalAlpha(1);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public TileType getTileType() {
        return tileType;
    }
}