import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;

/**
 * Projectile class 
 * @author Vachan Arora, Kashif Bhuiyan, Nikhil Venugopal 
 * @version 5/15/22
 */
public class Projectile {
    private int speed;
    private int x;
    private int y;
    private int height;
    private int width;
    private int dmg;
    private String color;
    Image img;
    public Projectile(int x, int y, int width, int height, int speed, int dmg, String image)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.dmg = dmg;
        img = new Image(image);
    }

    public Projectile(int width, int height, int speed, int dmg, String image)
    {
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.dmg = dmg;
        img = new Image(image);
        x = 0;
        y = 0;
    }

    public Projectile(Projectile other)
    {
        this.x = other.getX();
        this.y = other.getY();
        this.width = other.getWidth();
        this.height = other.getHeight();
        this.speed = other.getSpeed();
        this.dmg = 10;
        img = new Image(other.getImgUrl());
    }

    public void draw(GraphicsContext gc)
    {
        gc.drawImage(img, x, y);
    }

    public void move(GraphicsContext gc)
    {
        gc.clearRect(x , y, width, height);
        y += speed;
    }
    
    public void removeSelf(GraphicsContext gc)
    {
        gc.clearRect(x, y, width, height);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getDamage()
    {
        return dmg;
    }

    public int getSpeed()
    {
        return speed;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setX(int val)
    {
        x = val;
    }

    public void setY(int val)
    {
        y = val;
    }

    public String getImgUrl()
    {
        return img.getUrl();
    }

    public void setDamage(int damage)
    {
        dmg = damage;
    }

    public void setImage(Image image)
    {
        img = image;
    }

    public Projectile copy()
    {
        return new Projectile(this);
    }
}
