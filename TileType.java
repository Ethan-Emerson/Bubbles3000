
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class TileType
{
    private String name;
    private String file;
    private Color background;
    private Color border;
    private boolean collide;
    private ArrayList<Feature> features;

    private Image bgImage;

    public TileType(String name, String file, Color background, Color border, boolean collide, ArrayList<Feature> features)
    {
        this.name = name;
        this.file = file;
        this.background = background;
        this.border = border;
        this.collide = collide;
        this.features = features;

        bgImage = new Image(file, 30, 30, false, false);
    }


    public Color getBackground()
    {
        return background;
    }

    public Color getBorder()
    {
        return border;
    }

    public String getName()
    {
        return name;
    }

    public boolean getCollide()
    {
        return collide;
    }

    public Image getBgImage()
    {
        return bgImage;
    }

    public String getTypeName(){return name;}

    public void setCollide(boolean collide) { this.collide = collide; }

    public void doThing(){
        for(int i=0;i<features.size();i++){
            features.get(i).doThing();
        }
    }

    public boolean canDraw(){ //boolean used to determine of the tile can be drawn (right now jas only implementation for broken)
       if(!getCollide()) { //only make sure
           for (int i = 0; i < features.size(); i++) {
               Feature f = features.get(i);
               if (f instanceof Break) { //don't draw on broken
                   return false;
               }
           }
       }
        return true;
    }

    public ArrayList<Feature> getFeatures(){return features;}
}