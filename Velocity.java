

//Velocity singleton Class for increasing and decreasing the player's speed based on when the tile is hit

public class Velocity extends Feature{
   // private static Player player;
    public static boolean speedBoosted = false;
    private long elapsedTime = 0; //ElapsedTime variable for delaying when the velocity tile runs again
    private boolean canActivate = true;

    double radius;
    double velocityX;
    double velocityY;
    boolean on = true;

    public Velocity(double radius, double velocityX, double velocityY)
    {
        this.radius = radius;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void doThing()
    {

        /*
        long currentTime = System.currentTimeMillis();
        if(currentTime - elapsedTime > 1000){ //Make the cool down one second
            canActivate = true;
        }

        if(canActivate) { //make sure velocity tile has a cooldown
            if (speedBoosted) { //decrease player speed
                Player.playerSpeed /= 2;
                speedBoosted = false;
            } else { //increase player speed
                Player.playerSpeed *= 2;
                speedBoosted = true;
            }
            elapsedTime = System.currentTimeMillis();
            canActivate = false;
        }*/
    }

    public double getRadius()
    {
        return radius;
    }
    public double getVelocityX()
    {
        return velocityX;
    }
    public double getVelocityY()
    {
        return velocityY;
    }

    public boolean getOn()
    {
        return on;
    }

}
