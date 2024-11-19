
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;

// Created by Mitch Woolley
// on 18 October 2024

// test line

// This class will be in charge of the editor UI functionality
public class Editor {
    // private variables to hold the tiles placed down to eventually write to the file
    ArrayList <EditorSquare> world = new ArrayList<>();
    private Selector selector;
    private static Editor instance;

    //Variables to load in the the previous tile

    // Mouse variables to be implimented by the ghost tile
    private double ghostTileX = -1;
    private double ghostTileY = -1;
    private boolean isGhostTilePositioned = false;

    // Singleton stuff
    public static Editor getInstance() {
        if (instance == null) {
            instance = new Editor();
        }
        return instance;
    }
    private Editor() {
        selector = new Selector();
    }

    // Draw method
    public void drawEditor(GraphicsContext gc) throws IOException {

        // Draw the grey squares and selector
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, 1000, 1000);
        drawSquares(gc);
        Main.getGameInstance().getTheLevel().drawSquares(Main.getCanvas().getGraphicsContext2D());

        selector.drawSelector(gc);

        // Get selector bounds (for this example, we assume the selector is static)
        double selectorXStart = 720;
        double selectorXEnd = 785;
        double selectorYStart = 30;
        double selectorYEnd = 235;

        // Draw the ghost tile only if active, positioned, and outside the selector bounds
        GhostTile ghostTile = GhostTile.getInstance();
        if (ghostTile.isActive() && isGhostTilePositioned) {
            // Check if the ghost tile is outside the selector's bounds
            if (!(ghostTileX >= selectorXStart && ghostTileX <= selectorXEnd &&
                    ghostTileY >= selectorYStart && ghostTileY <= selectorYEnd)) {
                // Only draw if it's not within the selector's area
                ghostTile.drawMe(gc, ghostTileX, ghostTileY);
            }
        }
    }

    public void drawSquares(GraphicsContext gc) {

        // For loop to draw the 16 squares that will display
        for (int height = 0; height < 450; height += 30) {
            for (int width = 0; width < 800; width += 30) {
                EditorSquare square = new EditorSquare(width, height);
                world.add(square);
                square.drawMe(gc);
            }
        }
    }

    public void clicked(double x, double y) {
        // If the click was inside the selector
        if (x >= 720 && x <= 785 && y >= 50 && y <= 235) {
            // Run the method in selector for a mouse click
            //Make sure the code doesn't break when selecting a ghost tile
            try {
                selector.clicked(x, y);
            }
            catch (IndexOutOfBoundsException ioobe){

            }
        } else {
            // ... code here for click inside editor, outside of selector ...
            int coordX = getTileFromCoordX(x);
            int coordY = getTileFromCoordY(y);

            GhostTile ghostTile = GhostTile.getInstance();

            if (ghostTile.isActive())
            {
                if(ghostTile.getTileType().getTypeName().equals("erase")) {
                    removeTile(coordX, coordY);
                } else {
                    addTile(coordX, coordY, ghostTile.getTileType());
                }
            }

        }
    }
    // Method to handle mouse movement
    public void mouseMoved(double mouseX, double mouseY) {
        GhostTile ghostTile = GhostTile.getInstance();
        // Check to see if the ghostTile is a start tile
        if (ghostTile.isActive()) {
            if (ghostTile.getTileType().getTypeName().equals("start")) {
                // Check to see if the start tile is deployed
                if (Start.getInstance().getIfDeployed()) {
                    // Get rid of the ghostTile Start if you can't place it
                    ghostTile.inactive();
                }
            } else if (ghostTile.getTileType().getTypeName().equals("goal")) {
                // Check to see if the goal tile is deployed
                if (Goal.getInstance().getIfDeployed()) {
                    // Get rid of the ghostTile Goal if you can't place it
                    ghostTile.inactive();
                }
            }
        }

        //getTileFromCoord(mouseX, mouseY);

        if (ghostTile.isActive()) {
            // Snap the ghost tile only if mouse has moved

            ghostTileX = Math.floor(mouseX / 30) * 30;
            ghostTileY = Math.floor(mouseY / 30) * 30;

            isGhostTilePositioned = true;  // Mark the ghost tile as positioned
        }
    }


    public void resetGhostTile() {
        isGhostTilePositioned = false;  // Reset the flag when a new tile is selected
    }

    public int getTileFromCoordX(double x)
    {
        return (int) (((Math.floor(x / 30) * 30)/30) + (World.x/30));
    }

    public int getTileFromCoordY(double y)
    {
        return (int) (((Math.floor(y / 30) * 30)/30) + (World.y/30));
    }

    public void addTile(int coordX, int coordY, TileType tileType)
    {
        removeTile(coordX, coordY);

        Tile tile = new Tile(coordX, coordY, tileType);

        if (tileType.getTypeName().equals("start")) {
            Start s = Start.getInstance();
            s.setDeployedAs(true);
            s.setStartTilePosition(tile);
            ghostTileX = -1;
            ghostTileY = -1;
        } else if (tileType.getTypeName().equals("goal")) {
            Goal g = Goal.getInstance();
            g.setDeployedAs(true);
            ghostTileX = -1;
            ghostTileY = -1;
        }

        Pair<Integer, Integer> tilePos = new Pair<>(coordX, coordY);

        Main.getGameInstance().getTheLevel().getWorld().put(tilePos, tile);
    }

    public void removeTile(int coordX, int coordY)
    {
        ArrayList<Tile> onScreenTiles = Main.getGameInstance().getTheLevel().getOnScreenTiles();

        for(int i = 0; i < onScreenTiles.size(); i++)
        {
            if(onScreenTiles.get(i).getX() == coordX && onScreenTiles.get(i).getY() == coordY)
            {
                Tile t = onScreenTiles.get(i);
                if (t.getTileType().getTypeName().equals("start")) {
                    Start s = Start.getInstance();
                    s.setDeployedAs(false);
                } else if (t.getTileType().getTypeName().equals("goal")) {
                    Goal g = Goal.getInstance();
                    g.setDeployedAs(false);
                }
                Pair<Integer, Integer> tilePos = new Pair<>(onScreenTiles.get(i).getX(), onScreenTiles.get(i).getY());
                Main.getGameInstance().getTheLevel().getWorld().remove(tilePos);
                break;
            }
        }
    }
}