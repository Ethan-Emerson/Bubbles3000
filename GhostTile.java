
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class GhostTile {
    private static GhostTile instance;
    private String path;
    private boolean active = false;
    private TileType tileType;

    public static GhostTile getInstance() {
        if (instance == null) {
            instance = new GhostTile();
        }
        return instance;
    }

    public void setPath(String path) {
        this.path = path;
        active = true;
    }

    public void setTileType(TileType tileType)
    {
        this.tileType = tileType;
    }

    public TileType getTileType()
    {
        return tileType;
    }

    public void inactive() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void drawMe(GraphicsContext gc, double x, double y) {
        if (active && path != null) {
            gc.save();  // Save the current state of the graphics context
            gc.setGlobalAlpha(0.5);  // Set opacity to 50%
            gc.drawImage(new Image(path), x, y, 30, 30);  // Draw with transparency
            gc.restore();  // Restore the original state of the graphics context
        }
    }
}
