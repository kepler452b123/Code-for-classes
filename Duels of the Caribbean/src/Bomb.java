import javafx.scene.canvas.GraphicsContext;

public class Bomb extends Projectile
{
    private int count;
    private int xToAppear;
    private int yToAppear;
    private Ship target;
    public Bomb(int width, int height, int speed, int dmg, String image, Ship s)
    {
        super(width, height, speed, dmg, "fireball.png");
        count = 0;
        xToAppear = s.getY();
        yToAppear = s.getX();
        super.setX(s.getX());
        super.setY(s.getY());
        target = s;
    }

    public Bomb(Bomb other)
    {
        super(other.getWidth(), other.getHeight(), other.getSpeed(), other.getDamage(), other.getImgUrl());
        count = 0;
        super.setX(other.getX());
        super.setY(other.getY());
        target = other.getTarget();
        xToAppear = target.getX();
        yToAppear = target.getY();
    }
    
    public void draw(GraphicsContext gc)
    {
        gc.fillRect(200, 500, 100, 100);
    }
    //@override
    public void move(GraphicsContext gc)
    {
        count++;
        if (count >= 600)
        {
            super.setX(xToAppear);
            super.setY(yToAppear);
        }
        // if (count > 1800)
        // {
        //     super.setX(-100);
        //     super.setY(-100);
        // }
    }   
    //@override
    public Projectile copy()
    {
        return new Bomb(this);
    }

    public int getXToAppear()
    {
        return xToAppear;
    }

    public int getYToAppear()
    {
        return yToAppear;
    }

    public Ship getTarget()
    {
        return target;
    }
}
