
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class ParallaxManager {

    static Image s = new Image("file:images/stars1.png");
    static Image s2 = new Image("file:images/stars2.png");
    static ArrayList<Parallax> parallaxStars = new ArrayList<>();
    static ArrayList<Parallax> parallaxStars2 = new ArrayList<>();

    static Parallax starsBottomLeft = new Parallax(0, 0);
    static Parallax starsBottomRight = new Parallax(0+s.getWidth(), 0);
    static Parallax starsTopLeft = new Parallax(0, 0-s.getHeight());
    static Parallax starsTopRight = new Parallax(0 + s.getWidth(), 0-s.getHeight());

    static Parallax stars2BottomLeft = new Parallax(0, 0);
    static Parallax stars2BottomRight = new Parallax(0+s2.getWidth(), 0);
    static Parallax stars2TopLeft = new Parallax(0, 0-s2.getHeight());
    static Parallax stars2TopRight = new Parallax(0 + s2.getWidth(), 0-s2.getHeight());

    public static void init()
    {
        parallaxStars.add(starsBottomLeft);
        parallaxStars.add(starsBottomRight);
        parallaxStars.add(starsTopLeft);
        parallaxStars.add(starsTopRight);

        parallaxStars2.add(stars2BottomLeft);
        parallaxStars2.add(stars2BottomRight);
        parallaxStars2.add(stars2TopLeft);
        parallaxStars2.add(stars2TopRight);
    }

    public static void updateParallax1()
    {
        if(World.x * 1.05 < starsBottomLeft.getInitX())
        {
            // set the bottom right parallax to the new left
            starsBottomRight.setX(starsBottomLeft.getInitX() - s.getWidth());
            starsTopRight.setX(starsBottomLeft.getInitX() - s.getWidth());

            Parallax temp = starsBottomLeft;
            starsBottomLeft = starsBottomRight;
            starsBottomRight = temp;

            temp = starsTopLeft;
            starsTopLeft = starsTopRight;
            starsTopRight = temp;

        }

        else if (World.x * 1.05 > starsBottomRight.getInitX())
        {
            starsBottomLeft.setX(starsBottomRight.getInitX() + s.getWidth());
            starsTopLeft.setX(starsBottomRight.getInitX() + s.getWidth());

            Parallax temp = starsBottomRight;
            starsBottomRight = starsBottomLeft;
            starsBottomLeft = temp;

            temp = starsTopRight;
            starsTopRight = starsTopLeft;
            starsTopLeft = temp;
        }

        if(World.y * 1.05 < starsTopLeft.getInitY())
        {
            starsBottomLeft.setY(starsTopLeft.getInitY() - s.getHeight());
            starsBottomRight.setY(starsTopLeft.getInitY() - s.getHeight());

            Parallax temp = starsTopLeft;
            starsTopLeft = starsBottomLeft;
            starsBottomLeft = temp;

            temp = starsTopRight;
            starsTopRight = starsBottomRight;
            starsBottomRight = temp;
        }

        else if (World.y * 1.05 > starsBottomRight.getInitY())
        {
            starsTopLeft.setY(starsBottomRight.getInitY() + s.getHeight());
            starsTopRight.setY(starsBottomRight.getInitY() + s.getHeight());

            Parallax temp = starsTopRight;
            starsTopRight = starsTopLeft;
            starsTopLeft = temp;

            temp = starsBottomLeft;
            starsBottomLeft = starsBottomRight;
            starsBottomRight = temp;
        }
    }

    public static void updateParallax2()
    {
        if(World.x * 1.15 < stars2BottomLeft.getInitX())
        {
            // set the bottom right parallax to the new left
            stars2BottomRight.setX(stars2BottomLeft.getInitX() - s2.getWidth());
            stars2TopRight.setX(stars2BottomLeft.getInitX() - s2.getWidth());

            Parallax temp = stars2BottomLeft;
            stars2BottomLeft = stars2BottomRight;
            stars2BottomRight = temp;

            temp = stars2TopLeft;
            stars2TopLeft = stars2TopRight;
            stars2TopRight = temp;

        }

        else if (World.x * 1.15 > stars2BottomRight.getInitX())
        {
            stars2BottomLeft.setX(stars2BottomRight.getInitX() + s2.getWidth());
            stars2TopLeft.setX(stars2BottomRight.getInitX() + s2.getWidth());

            Parallax temp = stars2BottomRight;
            stars2BottomRight = stars2BottomLeft;
            stars2BottomLeft = temp;

            temp = stars2TopRight;
            stars2TopRight = stars2TopLeft;
            stars2TopLeft = temp;
        }

        if(World.y * 1.1 < stars2TopLeft.getInitY())
        {
            stars2BottomLeft.setY(stars2TopLeft.getInitY() - s2.getHeight());
            stars2BottomRight.setY(stars2TopLeft.getInitY() - s2.getHeight());

            Parallax temp = stars2TopLeft;
            stars2TopLeft = stars2BottomLeft;
            stars2BottomLeft = temp;

            temp = stars2TopRight;
            stars2TopRight = stars2BottomRight;
            stars2BottomRight = temp;

        }

        else if (World.y * 1.1 > stars2BottomRight.getInitY())
        {
            stars2TopLeft.setY(stars2BottomRight.getInitY() + s2.getHeight());
            stars2TopRight.setY(stars2BottomRight.getInitY() + s2.getHeight());

            Parallax temp = stars2TopRight;
            stars2TopRight = stars2TopLeft;
            stars2TopLeft = temp;

            temp = stars2BottomLeft;
            stars2BottomLeft = stars2BottomRight;
            stars2BottomRight = temp;
        }
    }

    public static void drawParallax(GraphicsContext gc)
    {
        updateParallax1();
        updateParallax2();

        for(int i = 0; i < parallaxStars.size(); i++)
        {
            Parallax p = parallaxStars.get(i);

            gc.drawImage(s, (World.x * -1.05) + p.getInitX(), (World.y * -1.05) + p.getInitY());
        }

        for(int i = 0; i < parallaxStars2.size(); i++)
        {
            Parallax p = parallaxStars2.get(i);
            gc.drawImage(s2, (World.x * -1.15) + p.getInitX(), (World.y * -1.1) + p.getInitY());
        }
    }
}
