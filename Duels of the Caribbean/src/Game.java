import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Group;
import javafx.scene.canvas.*;
import javafx.animation.AnimationTimer;
import javafx.animation.Animation;

import java.io.File;
import java.util.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.*;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.control.*;
import javafx.scene.media.*;
/**
 * Game class 
 * @author Vachan Arora, Kashif Bhuiyan, Nikhil Venugopal 
 * @version 5/15/22
 */
public class Game extends Application {
    ArrayList<Projectile> pList = new ArrayList<Projectile>();
    ArrayList<Projectile> pList2 = new ArrayList<Projectile>();
    private Ship ship = new Ship(750 - 41, 150, 92, 150, 
            375, 0, new Projectile(15, 15, 7, 10, "redball.png"), "ship1.png");
    private Ship ship2 = new Ship(750 - 41,450, 92, 150,
            750, 375, new Projectile(15, 15, -7, 10, "blueball.png"), "ship2.png");
    private Image background = new Image("background.png");
    public static void main(String[] args) {
        launch(args);
    }
    public void end(Ship ship, Stage primaryStage)
    {
        Stage secondaryStage = new Stage();
        Canvas canvas = new Canvas(1500, 750);
        Group root = new Group();
        root.getChildren().add(canvas);
        Scene endScreen = new Scene(root);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.fillText("L BOZO", 750, 375);
        Image i = new Image("endscreen.png");
        canvas.getGraphicsContext2D().drawImage(i, 0, 0);
        secondaryStage.setScene(endScreen);
        secondaryStage.show();
        Button btn  = new Button();
        btn.setOnAction(e -> {
            playAgain(primaryStage);
            secondaryStage.close();
         });
        root.getChildren().add(btn);
        btn.setLayoutX(580);
        btn.setLayoutY(510);
        btn.setPrefWidth(325);
        btn.setPrefHeight(100);
        btn.setText("Play Again");
        canvas.getGraphicsContext2D().drawImage(i, 0, 0);
    }
    @Override
    public void start(Stage primaryStage) {
        createGame(primaryStage);
    }
    public void createGame(Stage primaryStage)
    {
        Group root = new Group();
        Canvas canvas = new Canvas(1500, 750);
        //note: made height less than the actual image for a better hitbox. height is 25 less than it should be. thats why
        //you see +25 in some places like the switch case for inputs and shoot() method for ship
        // Ship ship = new Ship(750 - 41, 150, 92, 150, 
        //     375, 0, new Projectile(15, 15, 10, 10, "cannonball.png"), "ship1.png");
        // Ship ship2 = new Ship(750 - 41,450, 92, 150,
        //     750, 375, new Projectile(15, 15, -10, 10, "cannonball.png"), "ship2.png");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 1500, 750);
        ship.draw(gc);
        ship2.draw(gc);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Duels of the Caribbean");
        gc.drawImage(background, 0, 0);
        Image damage = new Image("damage.png");
        Image speed = new Image("speed.png");
        Image hp = new Image("health.png");
        Image turr = new Image("turret.png");
        gc.drawImage(damage, 0, 130);
        gc.drawImage(speed, 0, 200);
        gc.drawImage(hp, 0, 270);
        gc.drawImage(turr, 0, 340);
        Powerup dmg = new Powerup(1000, "damage", "fireball.png"); 
        Powerup spd = new Powerup(1000, "speed");
        Powerup health = new Powerup(1000, "health");
        Powerup turret = new Powerup(2000, "turret");
        ArrayList<Powerup> currentPows = new ArrayList<Powerup>();
        currentPows.add(dmg);
        currentPows.add(spd);
        currentPows.add(health);
        currentPows.add(turret);
        ship.createShop(currentPows);
        ship2.createShop(currentPows);
        pList.add(new Bomb(15, 15, 10, 10, "explosion.png", ship2));
        AnimationTimer loop = new AnimationTimer()
        {
            public void handle(long now)
            {
                moveProj(gc);
                runCollisions(gc);
                if (checkDeath())
                {
                    ship2.drawHealth(gc);
                    ship.drawHealth(gc);
                    primaryStage.close();
                    end(ship, primaryStage);
                    stop();
                }
                tick(gc);
                inputs(scene, gc);
            }
        };
        loop.start();
        primaryStage.show();
    }
    public void tick(GraphicsContext gc)
    {
        if (ship.getUp())
        {
            ship.moveUp();
        }
        if (ship.getDown())
        {
            ship.moveDown();
        }
        if (ship.getRight())
        {
            ship.moveRight();
        }
        if (ship.getLeft())
        {
            ship.moveLeft();
        }
        if (ship2.getUp())
        {
            ship2.moveUp();
        }
        if (ship2.getDown())
        {
            ship2.moveDown();
        }
        if (ship2.getLeft())
        {
            ship2.moveLeft();
        }
        if (ship2.getRight())
        {
            ship2.moveRight();
        }
        ship.addGold();
        ship2.addGold();
        gc.drawImage(background, 0, 0);
        ship.draw(gc);
        ship2.draw(gc);
        ship.drawHealth(gc);
        ship2.drawHealth(gc);
        for (Projectile p: pList)
        {
            p.draw(gc);
        }
        for (Projectile p: pList2)
        {
            p.draw(gc);
        }
        for (Turret t: ship.getTurrets())
        {
            t.draw(gc);
        }
        for (Turret t: ship2.getTurrets())
        {
            t.draw(gc);
        }
        ship2.displayGold(gc);
        ship.displayGold(gc);
    }
    
    public void moveProj(GraphicsContext gc)
    {
        for (Projectile proj: pList)
        {
            proj.move(gc);
            //proj.draw(gc);
        }
        for (Projectile proj2: pList2)
        {
            proj2.move(gc);
            //proj.draw(gc);
        }
    }

    public void inputs(Scene scene, GraphicsContext gc)
    {
        scene.setOnKeyPressed(e -> {
            switch(e.getCode())
            {
                case A:
                    ship.setLeft(true);
                    break;
                case W:
                    ship.setUp(true);
                    break;
                case D:
                    ship.setRight(true);
                    break;
                case S:
                    ship.setDown(true);
                    break;
                case ALT: 
                    // String sound = "Cannon_Sound_Effect.mp3";
                    // Media shootSound = new Media(new File(sound).toURI().toString());
                    // MediaPlayer mediaPlayer = new MediaPlayer(shootSound);
                    // mediaPlayer.play();
                    Projectile p = ship.shoot();
                    pList.add(p);
                    if (!ship.getTurrets().isEmpty())
                    {
                        for (Turret t : ship.getTurrets())
                        {
                            if (t.getCount() % 5 == 0)
                            {
                                Projectile s = t.shoot();
                                pList.add(s);
                            }
                        }
                    }
                    
                    break;
                case SHIFT:
                    Projectile p2 = ship2.shoot();
                    pList2.add(p2);
                    if (!ship2.getTurrets().isEmpty())
                    {
                        for (Turret t : ship2.getTurrets())
                        {
                            if (t.getCount() % 5 == 0)
                            {
                                Projectile s = t.shoot();
                                pList2.add(s);
                            }
                        }
                    }
                    
                    break;
                case LEFT:
                    ship2.setLeft(true);
                    break;
                case UP:
                    ship2.setUp(true);
                    break;
                case RIGHT:
                    ship2.setRight(true);
                    break;
                case DOWN:
                    ship2.setDown(true);
                    break;
                case DIGIT1:
                    ship.buyItem(0);
                    break;
                case DIGIT2:
                    ship.buyItem(1);
                    break;
                case DIGIT3:
                    if (ship.getHealth() < ship.getMax())
                    {
                        ship.buyItem(2);
                    }
                    break;
                case DIGIT4:
                    if (ship.getTurrets().size() < 4)
                    {
                        ship.buyItem(3);
                    }
                    break;
                case DIGIT6:
                    ship2.buyItem(0);
                    break;
                case DIGIT7:
                    ship2.buyItem(1);
                    break;
                case DIGIT8:
                    if (ship2.getHealth() < ship.getMax())
                    {
                        ship2.buyItem(2);
                    }
                    break;
                case DIGIT9:
                    if (ship2.getTurrets().size() < 4)
                    {
                        ship2.buyItem(3);
                    }
                    break;
                case DIGIT0:
                    ship2.buyItem(4);
                    break;
                
            }
        });
        scene.setOnKeyReleased(e -> {
            switch(e.getCode())
            {
                case A:
                    // ship.setVelX(0);
                    ship.setLeft(false);
                    //ship.draw(gc);
                    break;
                case W:
                    // ship.setVelY(0);
                    ship.setUp(false);
                    //ship.draw(gc);
                    break;
                case D:
                    // ship.setVelX(0);
                    ship.setRight(false);
                    //ship.draw(gc);
                    break;
                case S:
                    // ship.setVelY(0);
                    ship.setDown(false);
                    //ship.draw(gc);
                    break;
                case UP:
                    ship2.setUp(false);
                    //ship2.draw(gc);
                    break;
                case DOWN:                      
                    ship2.setDown(false);
                    //ship2.draw(gc);
                    break;
                case RIGHT:
                    ship2.setRight(false);
                    //ship2.draw(gc);
                    break;
                case LEFT:
                    ship2.setLeft(false);
                    //ship2.draw(gc);
                    break;
            }
        });
    }
    public boolean checkDeath()
    {
        if (ship2.checkHealth() || ship.checkHealth())
        {
            return true;
        }
        return false;
    }

    public void runCollisions(GraphicsContext gc)
    {
        if (!pList2.isEmpty())
        {
            for (int i = 0; i < pList2.size();)
        {
            if (ship.checkCollision(pList2.get(i), gc))
            {
                pList2.remove(i);
                ship.drawHealth(gc);
            }
            else
            {
                i++;
            }
        }
        }
        // for (int i = 0; i < pList2.size();)
        // {
        //     if (ship.checkCollision(pList2.get(i), gc))
        //     {
        //         pList2.remove(i);
        //         ship.drawHealth(gc);
        //     }
        //     else if (pList2.get(i).getX() < 0 || pList2.get(i).getX() > 1500 || pList2.get(i).getY() < 0 || pList2.get(i).getY() > 750 && pList2.get(i).getX() != -25)
        //     {
        //         pList2.remove(i);
        //     }
        //     else
        //     {
        //         i++;
        //     }
        // }
        if (!pList.isEmpty())
        {
            for (int i = 0; i < pList.size();)
        {
            if (ship2.checkCollision(pList.get(i), gc))
            {
                pList.remove(i);
                ship2.drawHealth(gc);
            }
            else
            {
                i++;
            }
        }
        }
        // for (int i = 0; i < pList.size();)
        // {
        //     if (ship2.checkCollision(pList.get(i), gc))
        //     {
        //         pList.remove(i);
        //         ship2.drawHealth(gc);
        //     }
        //     else if (pList.get(i).getX() < 0 || pList.get(i).getX() > 1500 || pList.get(i).getY() < 0 || pList.get(i).getY() > 750 && pList2.get(i).getX() != -25)
        //     {
        //         pList.remove(i);
        //     }
        //     else
        //     {
        //         i++;
        //     }
        // }
    }
    public void playAgain(Stage primaryStage)
    {
        ship.resetShip();
        ship2.resetShip();
        ship.clearTurrets();
        ship2.clearTurrets();
        pList.clear();
        pList2.clear();
        createGame(primaryStage);
    }
}
    