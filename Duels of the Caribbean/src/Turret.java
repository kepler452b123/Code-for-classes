import javafx.scene.image.*;
/**
 * Turret class 
 * @author Vachan Arora, Kashif Bhuiyan, Nikhil Venugopal 
 * @version 5/15/22
 */
import javafx.scene.canvas.*;
 public class Turret extends Ship
{
    int XPOS;
    int YPOS;
    Image img;
    private int count;
    public Turret(int x, int y, int w, int h, Projectile p, String i)
    {
        super(x, y, w, h, p, i);
        XPOS = x;
        YPOS = y;
        img = new Image(i);
        count = 0;
    } 

    // public void draw(GraphicsContext gc)
    // {
    //     gc.drawImage(img, XPOS, YPOS);
    // }

    public void moveLeft(){return;}

    public void moveRight(){return;}

    public void moveUp(){return;}

    public void moveDown(){return;}

    public int getCount()
    {
        return count;
    }

    public void addCount()
    {
        count++;
    }

}
