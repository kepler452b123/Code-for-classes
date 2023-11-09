import javafx.scene.image.*;
/**
 * Powerup class 
 * @author Vachan Arora, Kashif Bhuiyan, Nikhil Venugopal   
 * @version 5/16/22
 */
public class Powerup {
    private int cost;
    private final int AMOUNT = 20;
    private String type;
    private Image img;
    public Powerup(int cost, String type, String image)
    {
        this.cost = cost;
        this.type = type;
        img = new Image(image);
    }
    
    public Powerup(int cost, String type)
    {
        this.cost = cost;
        this.type = type;
    }
    
    public void activate(Ship ship)
    {
        if (type.equals("damage"))
        {
            Projectile sp = ship.getProj();
            Projectile p;
            if (!ship.getTop())
            {
                p = new Projectile(sp.getWidth(), sp.getHeight(), sp.getSpeed(), sp.getDamage() + 10, "bluefireball.png");
            }
            else
            {
                p = new Projectile(sp.getWidth(), sp.getHeight(), sp.getSpeed(), sp.getDamage() + 10, "fireball.png");
            }
            ship.addAmmo(p);
        }
 
        if (type.equals("speed"))
        {
            ship.setSpeed(ship.getSpeed() * 2);
        }

        if (type.equals("health"))
        {
            if (ship.getHealth() + 50 > ship.getMax())
            {
                ship.setHealth(ship.getHealth() - ship.getMax());
            }
            else
            {
                ship.setHealth(-50);
            }
        }

        if (type.equals("turret"))
        {
            Projectile sp = ship.getProj();
            int xPos = ship.getTurretsPos().get(0);
            int yPos = ship.getTurretsPos().get(1);
            if (!ship.getTop())
            {
                ship.addTurret(new Turret(xPos, yPos, 75, 113, new Projectile(sp.getWidth(), sp.getHeight(), 
                    sp.getSpeed(), sp.getDamage() + 10, sp.getImgUrl()), "blueturret.png"));
            }
            else
            {
                ship.addTurret(new Turret(xPos, yPos, 75, 113, new Projectile(sp.getWidth(), sp.getHeight(), 
                    sp.getSpeed(), sp.getDamage() + 10, sp.getImgUrl()), "redturret.png"));
            }
            ship.removeTurretPos();
        }
    }
    
     public boolean isBuyable(Ship ship)
     {
         if (ship.getGold() >= cost)
         {
            activate(ship);
            return true;
         }
         return false;
     }

     public int getCost()
     {
         return cost;
     }
}
