
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import java.io.*;
import java.util.*;

// Selector class created by Mitch Woolley
// on 18 October 2024

// test line

// This class will be in charge of the selector inside the editor
public class Selector {

    // ArrayList of SelectableTile objects
    private ArrayList <SelectableTile> tiles;
    private int posSelected = 99;

    public Selector() {
        tiles = new ArrayList<>();
    }

    // Method to draw the selector
    public void drawSelector(GraphicsContext gc) throws IOException {
        gc.setFill(Color.BLACK);
        gc.fillRect(720, 50, 65, 185);

        // Two loops to draw the "blank" squares to add the different tiles
        gc.setFill(Color.DARKGRAY);
        for (int width = 720; width < 780; width += 30) {
            for (int height = 50; height < 230; height += 30) {
                gc.fillRect(width + 2, height + 2, 26, 26);
            }
        }

        // Now call the method to draw the two arrows
        drawArrows(gc);

        // Read the tiles from the tileTypes.txt file and add them into the selector
        if (tiles.isEmpty()) {
            readTiles();
        }

        // Draw the tiles
        drawTiles(gc);
    }

    private void drawArrows(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        // Up arrow on the left hand side
        gc.fillPolygon(new double[]{725, 735, 745, 740, 740, 730, 730}, new double[]{213, 203, 213, 213, 223, 223, 213}, 7);
        // Down arrow on the right hand side
        gc.fillPolygon(new double[]{755, 765, 775, 770, 770, 760, 760}, new double[]{217, 227, 217, 217, 207, 207, 217}, 7);
    }

    private void readTiles() throws IOException {
        Scanner read = new Scanner(new File("level/tileTypes.txt"));
        int numberoftiles = 0;
        while (read.hasNext()) {
            // Read in the tile name
            String name = read.next();
            TileType tileType = Main.getGameInstance().getTheLevel().getTileTypeFromString(name);

            // Read in the tile image path and add it to the ArrayList of tile paths
            String next = read.next();
            tiles.add(new SelectableTile(numberoftiles, tileType, next));

            // throw away rest of line
            read.nextLine();

            numberoftiles++;
        }
    }

    public void drawTiles(GraphicsContext gc) {
        // Draw the tiles
        int positionBeingDrawn = 0;
        // Find the start position of the arraylist to draw (it will be different from index 0 if it's scrolled)
        int start = 0;
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getPosition() == 0) {
                start = i;
            }
        }
        // Find the end position of the arraylist to draw (it could be at index 9 if the blocks end at or past the size of the
        // selector, or the blocks could end before the end of the selector)
        int end = Math.min(start + 10, tiles.size());
        for (int i = start; i < end; i++) {
            SelectableTile tile = tiles.get(i);
            if (tile.getPosition() == posSelected) {
                tile.drawMeSelected(gc, xCoordinateOf(positionBeingDrawn), yCoordinateOf(positionBeingDrawn));
            } else {
                tile.drawMe(gc, xCoordinateOf(positionBeingDrawn), yCoordinateOf(positionBeingDrawn));
            }
            positionBeingDrawn++;
        }
    }

    /*
    THE FOLLOWING METHODS WILL TAKE IN A POSITION AND RETURN THE
    STARTING X AND Y COORDINATES OF THAT POSITION. FOR THIS METHOD
    "POSITION" IS DEFINED AS THE NUMERIC VALUE ASSOCIATED WITH THE
    DIFFERENT EMPTY SQUARES IN THE SELECTOR. POSITION 1 IS TOP LEFT,
    POSITION 2 IS TOP RIGHT, POSITION 3 IS SECOND DOWN ON THE LEFT, ETC.
     */

    private double xCoordinateOf(int position) {
        if (position % 2 == 0) {
            return 722;
        }
        return 752;
    }
    private double yCoordinateOf(int position) {
        if (position == 0 || position == 1) {
            return 52;
        } else if (position == 2 || position == 3) {
            return 82;
        } else if (position == 4 || position == 5) {
            return 112;
        } else if (position == 6 || position == 7) {
            return 142;
        } else {
            return 172;
        }
    }

    public void clicked(double x, double y) {
        // Variables to set location of click
        int row = 0;
        int positionSelected = 0;

        if (y >= 52 && y <= 78) {
        } else if (y >= 82 && y <= 108) {
            row = 1;
        } else if (y >= 112 && y <= 138) {
            row = 2;
        } else if (y >= 142 && y <= 168) {
            row = 3;
        } else if (y >= 172 && y <= 198) {
            row = 4;
        } else if (y >= 202 && y <= 228) {
            row = 5;
        } else {
            // "Bad click" row
            row = 6;
        }
        // Figure out what was clicked
        if (x >= 722 && x <= 748) {
            if (row == 0) {
            } else if (row == 1) {
                positionSelected = 2;
            } else if (row == 2) {
                positionSelected = 4;
            } else if (row == 3) {
                positionSelected = 6;
            } else if (row == 4) {
                positionSelected = 8;
            } else if (row == 5) {
                positionSelected = 10;
            }
        } else if (x >= 752 && x <= 782) {
            if (row == 0) {
                positionSelected = 1;
            } else if (row == 1) {
                positionSelected = 3;
            } else if (row == 2) {
                positionSelected = 5;
            } else if (row == 3) {
                positionSelected = 7;
            } else if (row == 4) {
                positionSelected = 9;
            } else if (row == 5) {
                positionSelected = 11;
            }
        }
        if (positionSelected != 10 && positionSelected != 11) {
            posSelected = positionSelected;
        }
        setTileSelected(positionSelected);
    }

    private void setTileSelected(int positionSelected) {
        // Set the path for the ghost tile image to the tile clicked
        if (positionSelected < 10) {
            boolean canSelect = true;
            // Activate the ghost tile by setting the path of the image
            // to that of the tile selected (will also set the ghost tile to
            // active

            for (int i = 0; i < tiles.size(); i++) {// For loop to find the tile selected
                if (tiles.get(i).getPosition() == positionSelected) {
                    // Is it a start or a goal tile?
                    if (tiles.get(i).getTileType().getTypeName().equals("start")) {
                        // If it is, can you click it?
                        if (Start.getInstance().getIfDeployed()) {
                            // If it's already out there, then you can't deploy it
                            canSelect = false;
                        }
                    } else if (tiles.get(i).getTileType().getTypeName().equals("goal")) {
                        // If it is, can you click it?
                        if (Goal.getInstance().getIfDeployed()) {
                            // If it's already out there, then you can't deploy it
                            canSelect = false;
                        }
                    }
                    if (canSelect) {
                        GhostTile.getInstance().setPath(tiles.get(i).getPath());
                        GhostTile.getInstance().setTileType(tiles.get(i).getTileType());
                    }
                }
            }
        }
        // If an arrow was clicked ...
        if (positionSelected == 10) {
            // If you're not at the top of the list
            if (tiles.get(0).getPosition() < 0) {
                // Set the position of each block an offset of 2
                for (int i = 0; i < tiles.size(); i++) {
                    tiles.get(i).setPosition(tiles.get(i).getPosition() + 2);
                }
                posSelected += 2;
            }

        } else if (positionSelected == 11) {
            // If there are more blocks to view
            if (tiles.get(tiles.size() - 1).getPosition() > 9) {
                // Set the position of each block an offset of 2
                for (int i = 0; i < tiles.size(); i++) {
                    tiles.get(i).setPosition(tiles.get(i).getPosition() - 2);
                }
                posSelected -= 2;
            }
        }
    }
}