
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Level
{
    private static HashMap<Pair<Integer, Integer>, Tile> world = new HashMap<>();
    private static ArrayList<TileType> tileTypes = new ArrayList<>();
    private Start start = Start.getInstance(); //start singleton
    private Goal goal = Goal.getInstance(); //goal singleton
    private static ArrayList<ShootingStar> shootingStars = new ArrayList<>();

    private static HashMap<Tile, TimerHandler> timers = new HashMap<>();

    public Level()
    {
        ParallaxManager.init();
        for (int i = 0; i < 5; i++) {
            shootingStars.add(new ShootingStar(new Image("file:images/shootingstar.png")));
        }
        for (int i = 0; i < 5; i++) {
            shootingStars.add(new ShootingStar(new Image("file:images/shootingstar2.png")));
        }
        for (int i = 0; i < 5; i++) {
            shootingStars.add(new ShootingStar(new Image("file:images/shootingstar3.png")));
        }

    }

    public void drawLevel(GraphicsContext gc)
    {
        tileTypes.clear();
        ParallaxManager.drawParallax(gc);
        for (int i = 0; i < shootingStars.size(); i++) {
            shootingStars.get(i).drawShootingStar(gc);
        }
        drawSquares(gc);
    }


    public void loadLevel()
    {
        double timerSeconds = 0;
        resetTimers();

        try
        {
            File file = new File( "level/tileTypes.txt");
            Scanner scanner = new Scanner(file);

            while(scanner.hasNext())
            {
                String tileName = scanner.next();
                String fileName = scanner.next();
                double backgroundRed = scanner.nextDouble();
                double backgroundGreen = scanner.nextDouble();
                double backgroundBlue = scanner.nextDouble();
                double backgroundAlpha = scanner.nextDouble();
                double borderRed = scanner.nextDouble();
                double borderGreen = scanner.nextDouble();
                double borderBlue = scanner.nextDouble();
                double borderAlpha = scanner.nextDouble();
                boolean collide = scanner.nextBoolean();

                int featureCount = scanner.nextInt();
                ArrayList<Feature> features = new ArrayList<>();
                for(int i = 0; i< featureCount; i++)
                {
                    String feature = scanner.next();

                    if(feature.equals("start")) {
                        features.add(start);
                    }
                    else if(feature.equals("goal")) {
                        features.add(goal);
                    } else if(feature.equals("death")) {
                        features.add(new Death());
                    }
                    else if(feature.equals("velocity")){
                        double radius = scanner.nextDouble();
                        double velocityX = scanner.nextDouble();
                        double velocityY = scanner.nextDouble();
                        features.add(new Velocity(radius, velocityX, velocityY));
                    }
                    else if(feature.equals("break")){
                        double time = scanner.nextDouble();
                        features.add(new Break(time));
                    }
                    else if(feature.equals("push")){
                        double radius = scanner.nextDouble();
                        double amount = scanner.nextDouble();
                        boolean on = scanner.nextBoolean();
                        features.add(new Push(radius, amount, on));
                    }
                    else if(feature.equals("pull")){
                        double radius = scanner.nextDouble();
                        double amount = scanner.nextDouble();
                        boolean on = scanner.nextBoolean();
                        features.add(new Pull(radius, amount, on));
                    }
                    else if(feature.equals("timer")){
                        timerSeconds = scanner.nextDouble();
                        features.add(new Timer());
                    }
                    else if(feature.equals("activate")){
                        String featureName = scanner.next();
                        features.add(new Activate(featureName));
                    }
                }

                tileTypes.add(new TileType(tileName, fileName, Color.color(backgroundRed, backgroundGreen, backgroundBlue, backgroundAlpha), Color.color(borderRed, borderGreen, borderBlue, borderAlpha), collide, features));
            }

        }catch(Exception e)
        {
            System.out.println("There was an issue with your tileTypes.txt file format. Please use correct format and try again.");
            System.exit(0);
        }

        try
        {
            File file = new File("level/tileLocations.txt");
            Scanner scanner = new Scanner(file);

            while(scanner.hasNext()) {
                String tileType = scanner.next();
                int x = scanner.nextInt();
                int y = scanner.nextInt();

                TileType type = getTileTypeFromString(tileType);

                Tile tile = new Tile(x, y, type); //to help initialize player start
                Pair<Integer, Integer> tilePos = new Pair<>(x, y);

                world.put(tilePos, tile);

                //Set the start value
                if (tileType.equals("start")) {
                    start.setStartTilePosition(tile);
                    start.setDeployedAs(true);
                } else if (tileType.equals("goal")) {
                    goal.setDeployedAs(true);
                }
                else if(tileType.equals("timer"))
                {
                    TimerHandler timerHandler = new TimerHandler(tile, timerSeconds);
                    timers.put(tile, timerHandler);
                }
            }

        }catch(FileNotFoundException e)
        {
            System.out.println("There was an issue with your tileLocations.txt file format. Please use correct format and try again.");
            System.exit(0);
        }
    }

    public void saveLevel()
    {
        //Use the file writer
        try {
            FileWriter myWriter = new FileWriter(new File("level/tileLocations.txt"));

            for (Tile value : world.values())
            {
                myWriter.write(value.toString());
            }

            myWriter.close();
        }
        catch(IOException io){
            System.out.println("ERROR NO FILE");
        }
    }

    public void drawSquares(GraphicsContext gc){ //This is to just draw the squares for the loader
        int tileSize = Main.getGameInstance().getTileSize();

        // only draws onscreen tiles
        ArrayList<Tile> onScreenTiles = getOnScreenTiles();

        for(int i = 0; i < onScreenTiles.size(); i++) {
            boolean draw = true;
            Tile pos = onScreenTiles.get(i);

            if (Main.getGameInstance().getGameState() && pos.getTileType().getTypeName().equals("start")) {
                draw = false;
            }

            //loop over the features to make draw not good for broken tile
            if(Main.getGameInstance().getGameState() && !pos.getIsEnabled()) {
                ArrayList<Feature> features = pos.getTileType().getFeatures();
                for (int f = 0; f < features.size(); f++) {
                    if (features.get(f) instanceof Break) {
                        draw = false;
                        break;
                    }
                }
            }

            if (draw && pos.getIsEnabled()) {
                int x = pos.getX();
                int y = pos.getY();
                Color bg = pos.getTileType().getBackground();
                Color border = pos.getTileType().getBorder();
                Image bgImage = pos.getTileType().getBgImage();

                gc.setFill(bg);
                gc.setStroke(border);

                // draw background
                gc.fillRect((x * tileSize) + (World.x * -1), (y * tileSize) + (World.y * -1), tileSize, tileSize);

                gc.drawImage(bgImage, (x * tileSize) + (World.x * -1), (y * tileSize) + (World.y * -1));
                gc.setLineWidth(1.5);
                gc.strokeRect((x * tileSize) + (World.x * -1), (y * tileSize) + (World.y * -1), tileSize, tileSize);
                gc.setLineWidth(1);
            }
            else if(draw && !pos.getIsEnabled()){
                int x = pos.getX();
                int y = pos.getY();
                Color bg = pos.getTileType().getBackground();
                Color border = pos.getTileType().getBorder();
                Image bgImage = pos.getTileType().getBgImage();

                // Set the fill and stroke colors with 20% opacity (alpha = 0.2)
                Color semiTransparentBg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 0.3);
                Color semiTransparentBorder = new Color(border.getRed(), border.getGreen(), border.getBlue(), 0.3);

                gc.setFill(semiTransparentBg);
                gc.setStroke(semiTransparentBorder);

                // Set global alpha for transparency
                gc.setGlobalAlpha(0.3); // 30% opacity

                // Draw the background with opacity
                gc.fillRect((x * tileSize) + (World.x * -1), (y * tileSize) + (World.y * -1), tileSize, tileSize);

                // Draw the image with reduced opacity
                gc.drawImage(bgImage, (x * tileSize) + (World.x * -1), (y * tileSize) + (World.y * -1));

                gc.setLineWidth(1.5);
                gc.strokeRect((x * tileSize) + (World.x * -1), (y * tileSize) + (World.y * -1), tileSize, tileSize);
                gc.setLineWidth(1);
                gc.setGlobalAlpha(1); //set opacity back
            }
        }
    }

    public ArrayList<Tile> getOnScreenTiles()
    {
        ArrayList<Pair<Integer, Integer>> tilePositions = new ArrayList<>();
        ArrayList<Tile> toReturn = new ArrayList<>();

        double x = World.x - 60;
        double y = World.y - 60;
        double boundX = World.x + Main.getWidth() + 60;
        double boundY = World.y + Main.getHeight() + 60;

        for(double d = x; d < boundX; d+=30)
        {
            for(double k = y; k < boundY; k+=30)
            {
                int posX = (int) (Math.floor(d / 30) * 30)/30;
                int posY = (int) (Math.floor(k / 30) * 30)/30;
                tilePositions.add(new Pair<>(posX, posY));
            }
        }

        for(int i = 0; i < tilePositions.size(); i++)
        {
            if(world.containsKey(tilePositions.get(i)))
            {
                toReturn.add(world.get(tilePositions.get(i)));
            }
        }

        return toReturn;
    }

    public HashMap<Pair<Integer, Integer>, Tile> getWorld()
    {
        return world;
    }

    public TileType getTileTypeFromString(String tileType)
    {
        TileType type = null;
        for(int i = 0; i <tileTypes.size(); i++)
        {
            if (tileTypes.get(i).getName().equals(tileType))
            {
                type = tileTypes.get(i);
                return type;
            }
        }
        return type;
    }

    public void activateAllTiles()
    {
        for (Tile value : world.values())
        {
            value.setIsEnabled(true);
        }
    }

    public void startTimers()
    {
        for (TimerHandler value : timers.values())
        {
            value.start();
        }
    }

    public void stopTimers()
    {
        for (TimerHandler value : timers.values())
        {
            value.stop();
        }
    }

    public void resetTimers()
    {
        for (TimerHandler value : timers.values())
        {
            value.stop();
        }
        timers.clear();
    }

    public void toggleTimer(Tile t)
    {
        if(timers.containsKey(t))
        {
            if(timers.get(t).getRunning())
            {
                timers.get(t).stop();
            }
            else
            {
                timers.get(t).start();
            }
        }
    }

    public TimerHandler getTimerHandler(Tile t){

        return timers.get(t);
    }

    public void resetTimerHandlers(){
        for (TimerHandler value : timers.values())
        {
            value.setTimerOn(false);
            value.setFirstRun(false);
        }
    }

    public ArrayList<ShootingStar> getShootingStars()
    {
        return shootingStars;
    }
}