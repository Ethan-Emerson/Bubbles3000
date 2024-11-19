
import javafx.scene.image.Image;

public class Parallax
{
    double x;
    double y;

    public Parallax(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getInitX()
    {
        return x;
    }

    public double getInitY()
    {
        return y;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }
}
