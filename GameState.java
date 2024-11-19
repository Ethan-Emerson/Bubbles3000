
// Created by Mitch Woolley
// on 17 October 2024

// This class implements the singleton principle to keep track
// of if the game will be in the editor state or the playable state
//Functionality includes: If the game returns true, then the game is in player mode, if not it is in editor mode


import java.util.ArrayList;

public class GameState {
    private static boolean editorState = false;                                     // Static boolean to keep track of if the game is in the editor state
    public boolean getState() {return editorState;}                                 // Method to return if the game is in the editor state
    private static Start start = Start.getInstance();
    private static GameState instance;

    private GameState(){} //singleton constructor
    public static GameState getInstance(){
        if (instance == null) {
            synchronized (GameState.class) {
                if (instance == null) {
                    instance = new GameState();
                }
            }
        }
        return instance;
    }
    public void editorMode(boolean editorState) {
        this.editorState = editorState;

        World.x = 0;
        World.y = 0;

        Player.setX((double) Main.getWidth() /2);
        Player.setY((double) Main.getHeight()/2);

        Main.getGameInstance().getTheLevel().activateAllTiles();

        if(!editorState)
        {
            Player.playerXSpeed = 30;
            Player.playerYSpeed = 30;
            Main.getGameInstance().getTheLevel().stopTimers();
        }
        else
        {
            Main.getGameInstance().getTheLevel().saveLevel();
            Main.getGameInstance().getTheLevel().loadLevel();

            Main.getGameInstance().teleportToTile(start.getStartX() - 0.1, start.getStartY() + 1);
            Player.playerXSpeed = 2;
            Player.playerYSpeed = 2;

            Main.getGameInstance().getTheLevel().startTimers();

            ArrayList<ShootingStar> shootingStars = Main.getGameInstance().getTheLevel().getShootingStars();

            // reset star position of all stars
            for(int i = 0; i < shootingStars.size(); i++)
            {
                shootingStars.get(i).resetStarPosition();
            }
        }

        Velocity.speedBoosted = false;
    }   // Method to set if the game is in the editor state
}
