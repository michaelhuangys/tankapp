/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 *
 * @author Lowell
 */
abstract public class PlayerParent extends Unit implements Observer
{
    //these contain the Key codes for the controls
    int left, right, up, down, fire, spfire;
    private boolean collision_with_wall=false;
    boolean RotateLeft, RotateRight, mvUp, mvDown, isFiring, isSp;
    int storeX,storeY;
    int power;
    int shotDelay;
    int shotTime;
    int fastShotTime;
    int deadTimer;
    int deadTime;
    int eps;
    int amount;
    public PlayerParent(int x, int y, double direction, int speed, Image[] img,
            GameEvents events, int maxdamage, int damageto, int eps,
            int left, int right, int up, int down, int fire, int spfire,
            int shotTime, int fastShotTime, int deadTime)
    {
        super(x, y, direction, speed, img, events, maxdamage, damageto, eps);
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.fire = fire;
        this.spfire = spfire;
        this.shotTime = shotTime;
        this.fastShotTime = fastShotTime;
        this.deadTime = deadTime;
        shotDelay = 0;
        deadTimer = 0;
        power = 0;
        RotateLeft = false;
        RotateRight = false;
        mvUp = false;
        mvDown = false;
        isFiring = false;
        isSp = false;
        this.eps=eps;
    }
    
    //checks event type and calls the method for dealing with that
    @Override
    public void update(Observable o, Object arg)
    {

        GameEvents events = (GameEvents) arg;
        if(events.getType() == 1)
        {        
            if(events.getTarget() == this)
            {
                hitMe((Thing)events.getCaller());
            }
        }
        else if(events.getType() == 2)
        {
            controls((KeyEvent)events.getTarget());
        }
    }
    
    //dealing with key events
    public void controls(KeyEvent e)
    {
        int key = e.getKeyCode();
        if(key == left || key == right || key == up || key == down)
        {
            keyMove(e);
        }
        
        else if(key == fire || key == spfire)
        {
            keyFire(e);
          
        }
       
    }
    
    public void update(int w, int h)
    {
        if (done)
        {
            dead();
        } else
        {
            action();
          
            move();
        }
    }
    //setting up key booleans based on the event. These are used in move() to
    //move the player
    public void keyMove(KeyEvent key)
        {
            //this eliminates the delay for the second key press when holding
            //down a key
            if (key.getID() == KeyEvent.KEY_PRESSED)
            {
                if (key.getKeyCode() == getLeft() && !RotateLeft)
                {
                    RotateLeft = true;
                } else if (key.getKeyCode() == getRight() && !RotateRight)
                {
                    RotateRight = true;
                } else if (key.getKeyCode() == getUp() && !mvUp)
                {
                    mvUp = true;
                } else if (key.getKeyCode() == getDown() && !mvDown)
                {
                    mvDown = true;
                }
            }
            
            if (key.getID() == KeyEvent.KEY_RELEASED)
            {
                if (key.getKeyCode() == getLeft() && RotateLeft)
                {
                    RotateLeft = false;
                } else if (key.getKeyCode() == getRight() && RotateRight)
                {
                    RotateRight = false;
                } else if (key.getKeyCode() == getUp() && mvUp)
                {
                    mvUp = false;
                } else if (key.getKeyCode() == getDown() && mvDown)
                {
                    mvDown = false;
                }
            }
        }
    
    //setting up firing booleans. These are used in action() to to actions.
    public void keyFire(KeyEvent key)
        {
            if(key.getID() == KeyEvent.KEY_PRESSED)
            {
                if(key.getKeyCode() == getFire() && !isFiring)
                {
                    isFiring = true;
                }
                
                if(key.getKeyCode() == getSpFire() && !isSp)
                {
                    isSp = true;
                }
            }
            
            if(key.getID() == KeyEvent.KEY_RELEASED)
            {
                if(key.getKeyCode() == getFire() && isFiring)
                {
                    isFiring = false;
                }
                
                if(key.getKeyCode() == getSpFire() && isSp)
                {
                    isSp = false;
                }
            }

        }
    
    //get and set methods follow
    public int getPower()
    {
        return power;
    }
    
    public void setPower(int power)
    {
    
        this.power = power;
        if(power==1){
        	setAmount(15);
        }
        //set tracking bullet amount
         if(power==2){
        	setAmount(10);
        }
         
         System.out.println(power);
         if(power==3){
        	 setAmount(400);
         }
         if(power==4){
         	setAmount(20);
         }
         
    }
    
    protected int getAmount(){
    	return amount;
    }
     protected void decreaseAmount(){
    	amount--;
    }
    private void setAmount(int i) {
		amount=i;
		
	}

	public int getShotDelay()
    {
        return shotDelay;
    }
    
    public void setShotDelay(int delay)
    {
        this.shotDelay = delay;
    }
    
    public void changeShotDelay(int change)
    {
        this.shotDelay += change;
    }
    
    public int getShotTime()
    {
        return shotTime;
    }
    
    public int getFastShotTime()
    {
        return fastShotTime;
    }
    
    public int getLeft()
    {
        return left;
    }
    
    public int getRight()
    {
        return right;
    }
    
    public int getUp()
    {
        return up;
    }
    
    public int getDown()
    {
        return down;
    }
    
    public int getFire()
    {
        return fire;
    }
    
    public int getSpFire()
    {
        return spfire;
    }
    
    public int getDeadTimer()
    {
        return deadTimer;
    }
    
    public void setDeadTimer(int deadTimer)
    {
        this.deadTimer = deadTimer;
    }
    
    public void decDeadTimer()
    {
        deadTimer--;
    }
    
    public int getDeadTime()
    {
        return deadTime;
    }
    
    public boolean getRotateLeft()
    {
        return RotateLeft;
    }
    
    public boolean getRotateRight()
    {
        return RotateRight;
    }
    
    public boolean getMvUp()
    {
        return mvUp;
    }
    
    public boolean getMvDown()
    {
        return mvDown;
    }
    
    public boolean getIsFiring()
    {
        return isFiring;
    }
    
    public boolean getIsSp()
    {
        return isSp;
    }

	public int geteps() {
		
		return eps;
	}

	public void setCollisionWithWall(boolean b) {
		collision_with_wall=b;
		
	}

    public boolean getCollisionWithWall(){
    	return collision_with_wall;
    }



}
