/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.Image;
import java.util.*;

import tankgame.TankGame.PlayerTank;

/**
 * This class is for Things which cannot cause collisions
 * @author Lowell
 */
abstract public class NotUnit extends Thing
{
    private int source;
    
    //targets contains everything that can cause collisions
    protected ArrayList<ArrayList> targets;
    
    public NotUnit(int x, int y, double direction, int speed, Image[] img,
            GameEvents events, int source, ArrayList ev)
    {
        super(x, y, direction, speed, img, events);
        this.source = source;
        this.targets = ev;
    
    }
    
    @Override
    public void move()
    {
        Unit temp;
        int i = 1;
     if(getSource()!=2){
        changeY((int) (getSpeed()*Math.sin(Math.toRadians(getDirection()))));
        changeX((int) (getSpeed()*Math.cos(Math.toRadians(getDirection()))));
     }
    	  
     
        Iterator<ArrayList> it = targets.listIterator(3);
        
        while (it.hasNext())
        {
            Iterator<Unit> it2 = it.next().listIterator();
  
            if(i != source)
            {
                while (it2.hasNext())
                {
                    temp = it2.next();
                    changeY((int) (getSpeed()*Math.sin(Math.toRadians(temp.getDirection()))));
                    changeX((int) (getSpeed()*Math.cos(Math.toRadians(temp.getDirection()))));
                    
                    if(temp.collision(getX(), getY(), getWidth(), getHeight()))
                    {
                    	this.itHit(temp);
                        getEvents().setCollision(this, temp);
                     
                    }
                }
            }
            i++;
        }   
    }
    
    @Override
    public void dead()
    {
    	
        setRDone(true);
    }
            
    public int getSource()
    {
        return source;
    }
}
