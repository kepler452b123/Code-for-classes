import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.media.*;

/**
 * Ship class 
 * @author Vachan Arora, Kashif Bhuiyan, Nikhil Venugopal 
 * @version 5/15/22
 */
public class Ship {
    private int hp;
    private final int MAXHP;
    private int xPos;
    private int yPos;
    private int gold;
    private int velX = 8;
    private int velY = 8;
    private int speed;
    private ArrayList<Powerup> shop;
    private ArrayList<Turret> turrets = new ArrayList<Turret>();
    private Stack<Projectile> ammo;
    private int lBound;
    private int uBound;
    private Image img;
    private int height;
    private int width;
    private int ammoLeft = 20;
    private boolean top = true;
    private LinkedList<ArrayList<Integer>> turretsPos;
    private int count;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    public Ship(int x, int y, int width, int height, int lBound, int uBound, Projectile p, String image)
    {
        hp = 100;
        MAXHP = hp;
        speed = 40;
        xPos = x;
        yPos = y;
        gold = 10000;
        this.width = width;
        this.height = height;
        shop = new ArrayList<Powerup>();
        ammo =  new Stack<Projectile>();
        ammo.push(p);
        this.lBound = lBound;                                               
        this.uBound = uBound;
        img = new Image(image);
        turretsPos = new LinkedList<ArrayList<Integer>>();
        count = -1;
        if (yPos > 375)
        {
            top = false;
        }
        ArrayList<Integer> pos1 = new ArrayList<Integer>();
        ArrayList<Integer> pos2 = new ArrayList<Integer>();
        ArrayList<Integer> pos3 = new ArrayList<Integer>();
        ArrayList<Integer> pos4 = new ArrayList<Integer>();
        if (top)
        {
            pos1.add(0);
            pos1.add(0);
            pos2.add(375);
            pos2.add(0);
            pos3.add(1500 - 375 - width);
            pos3.add(0);
            pos4.add(1500 - width);
            pos4.add(0);
        }
        else
        {
            pos1.add(0);
            pos1.add(750 - 113);
            pos2.add(375);
            pos2.add(750 - 113);
            pos3.add(1500 - 375 - width);
            pos3.add(750 - 113);
            pos4.add(1500 - width);
            pos4.add(750 - 113);
        }
        turretsPos.add(pos1);
        turretsPos.add(pos2);
        turretsPos.add(pos3);
        turretsPos.add(pos4);
    }

    public Ship(int x, int y, int width, int height, Projectile p, String image)
    {
        xPos = x;
        yPos = y;
        hp = 100;
        MAXHP = hp;
        this.width = width;
        this.height = height;
        ammo = new Stack<Projectile>();
        ammo.push(p);
        img = new Image(image);
    }

    public void moveLeft()
    {
        if (xPos - velX >= -50)
        {
            xPos -= velX;
        }
    }

    public void moveRight()
    {

        if (xPos + velX + width <= 1550)
        {
            xPos += velX;
        }
    }

    public void moveUp()
    {
        if(yPos - velY >= uBound)
            yPos -= velY;
    }

    public void moveDown()
    {
        if(yPos + velY + height <= lBound)
            yPos += velY;
    }

    public void draw(GraphicsContext gc)
    {
        gc.drawImage(img, xPos, yPos);
    }

    public void drawHealth(GraphicsContext gc)
    {
        int hOfBar = 25;
        int wOfBar = 3 * hp;
        int wOfMaxBar = 3 * MAXHP;
        if (top)
        {
            gc.fillRect(750 - wOfMaxBar / 2, 0, wOfBar, hOfBar);
        }
        else
        {
            gc.fillRect(750 - wOfMaxBar / 2, 750 - hOfBar, wOfBar, hOfBar);
        }
    }
    
    public void removeSelf(GraphicsContext gc)
    {
        gc.clearRect(xPos, yPos, width, height + 25);
    }

    public boolean isAlive()
    {
        return hp > 0;
    }

    public Projectile shoot()
    {
        if (!turrets.isEmpty())
        {
            for (Turret t : turrets)
            {
                t.addCount();
                
            }
        }
        if (ammoLeft == 0 && ammo.size() > 1)
        {
            ammo.pop();
            ammoLeft = 0;
        }
        else
        {
            ammoLeft--;
        }
        Projectile currentP = ammo.peek();
        
        if (!top)
        {
            currentP.setX(xPos + (width / 2 - ammo.peek().getWidth() / 2));
            currentP.setY(yPos - ammo.peek().getHeight());
            return currentP.copy();
            //new Projectile(xPos + (width / 2 - ammo.peek().getWidth() / 2), yPos - ammo.peek().getHeight(), ammo.peek().getWidth(), ammo.peek().getHeight(), ammo.peek().getSpeed(), ammo.peek().getDamage(), ammo.peek().getImgUrl());
        }
        currentP.setX(xPos + (width / 2 - ammo.peek().getWidth() / 2));
        currentP.setY(yPos + height + 25);
        return currentP.copy();
        //new Projectile(xPos + (width / 2 - ammo.peek().getWidth() / 2), yPos + height + 25, ammo.peek().getWidth(), ammo.peek().getHeight(), ammo.peek().getSpeed(), ammo.peek().getDamage(), ammo.peek().getImgUrl());
    }

    public int getX()
    {
        return xPos;
    }

    public int getY()
    {
        return yPos;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getHealth()
    {
        return hp;
    }

    public boolean checkCollision(Projectile p, GraphicsContext gc)
    {
        int leftX = p.getX();
        int rightX = p.getX() + p.getWidth();
        int topY = p.getY();
        int botY = p.getY() + p.getHeight();
        if (leftX > xPos && leftX < xPos + width && topY > yPos && topY < yPos + height)
        {
            setHealth(p.getDamage());
            p.removeSelf(gc);
            return true;
        }
        else if(rightX > xPos && rightX < xPos + width && topY > yPos && topY < yPos + height)
        {
            setHealth(p.getDamage());
            p.removeSelf(gc);
            return true;
        }
        else if(leftX > xPos && leftX < xPos + width && botY > yPos && botY < yPos + height)
        {
            setHealth(p.getDamage());
            p.removeSelf(gc);
            return true;
        }
        else if(rightX > xPos && rightX < xPos + width && botY > yPos && botY < yPos + height)
        {
            setHealth(p.getDamage());
            p.removeSelf(gc);
            return true;
        }
        return false;
    
    }

    public boolean checkHealth()
    {
        return (hp<=0);
    }

    public void setHealth( int change )
    {
        hp -= change;
    }

    public Projectile getProj()
    {
        return ammo.peek();
    }

    public void setProj(Projectile p)
    {
        ammo.push(p);
    }

    public int getSpeed()
    {
        return speed;
    }

    public void setSpeed(int s)
    {
        speed = s;
    }

    public void setImage(Image i)
    {
        img = i;
    }
    
    public int getGold()
    {
        return gold;
    }

    public void setGold(int g)
    {
        gold = g;
    }

    public int getMax()
    {
        return MAXHP;
    }

    public void addAmmo(Projectile p)
    {
        ammo.add(p);
        setAmmoLeft(20);
    }

    public void setAmmoLeft(int a)
    {
        ammoLeft = a;
    }

    public void displayGold(GraphicsContext gc)
    {
        String s = "GOLD :" + gold + "";
        if (!top)
        {
            gc.fillText(s, 20, 730);
        }
        gc.fillText(s, 20, 20);
    }

    public void addTurret(Turret turret)
    {
        turrets.add(turret);
    }

    public void buyItem(int index)
    {
        
        if (shop.get(index).getCost() <= gold)
        {
            shop.get(index).activate(this);
            gold -= shop.get(index).getCost();
        }
    }

    public void addGold()
    {
        gold++;
    }

    public void createShop(ArrayList<Powerup> items)
    {
        for (Powerup p : items)
        {
            shop.add(p);
        }
    }
    
    public ArrayList<Turret> getTurrets()
    {
        return turrets;
    }

    public ArrayList<Integer> getTurretsPos()
    {
        return turretsPos.get(0);
    }

    public void removeTurretPos()
    {
        turretsPos.remove();
    }

    public void setVelX(int vel)
    {
        velX = vel;
    }

    public void setVelY(int vel)
    {
        velY = vel;
    }

    public int getCount()
    {
        return count;
    }

    public boolean getTop()
    {
        return top;
    }

    public boolean getDown()
    {
        return down;
    }

    public boolean getUp()
    {
        return up;
    }

    public void setUp(boolean val)
    {
        up = val;
    }

    public void setDown(boolean val)
    {
        down = val;
    }

    public boolean getLeft()
    {
        return left;
    }

    public void setLeft(boolean val)
    {
        left = val;
    }

    public boolean getRight()
    {
        return right;
    }

    public void setRight(boolean val)
    {
        right = val;
    }

    public void resetShip()
    {
        hp = 100;
        gold = 0;
        xPos = 750 - width;
        if (top)
        {
            yPos = 150;
        }
        else
        {
            yPos = 450;
        }
    }

    public void clearTurrets()
    {
        turrets.clear();
        turretsPos.clear();
        turretsPos = new LinkedList<ArrayList<Integer>>(); 
        ArrayList<Integer> pos1 = new ArrayList<Integer>();
        ArrayList<Integer> pos2 = new ArrayList<Integer>();
        ArrayList<Integer> pos3 = new ArrayList<Integer>();
        ArrayList<Integer> pos4 = new ArrayList<Integer>();
        if (top)
        {
            pos1.add(0);
            pos1.add(0);
            pos2.add(375);
            pos2.add(0);
            pos3.add(1500 - 375 - width);
            pos3.add(0);
            pos4.add(1500 - width);
            pos4.add(0);
        }
        else
        {
            pos1.add(0);
            pos1.add(750 - 113);
            pos2.add(375);
            pos2.add(750 - 113);
            pos3.add(1500 - 375 - width);
            pos3.add(750 - 113);
            pos4.add(1500 - width);
            pos4.add(750 - 113);
        }
        turretsPos.add(pos1);
        turretsPos.add(pos2);
        turretsPos.add(pos3);
        turretsPos.add(pos4);
    }
}