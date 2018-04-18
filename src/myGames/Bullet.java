/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import tankgame.Shield;
import tankgame.TankGame.PlayerTank;
import tankgame.TankGame.TankEvents;

/**
 *
 * @author Lowell
 */
 public class Bullet extends NotUnit
{
    private int damageto;
     private PlayerTank playerTank;
     private boolean NotTracking=false;
    public Bullet(int x, int y, double direction, int speed, Image[] img,
            GameEvents events, int source, ArrayList ev, int damageto)
    {
        super(x, y, direction, speed, img, events, source, ev);
        this.damageto = damageto;
    }
	@Override
    public void itHit(Unit u)
    {     	
        u.changeDamage(damageto);
        setDone(true);
    }
    
    public int getDamageTo()
    {
        return damageto;
    }     
    
    public void move()
    {
    	PlayerTank temp;
  
        Iterator<ArrayList> it = super.targets.listIterator(3);
       	 changeY((int) (getSpeed()*Math.sin(Math.toRadians(getDirection()))));
         changeX((int) (getSpeed()*Math.cos(Math.toRadians(getDirection()))));
      
        while (it.hasNext())
        {
            Iterator<PlayerTank> it2 = it.next().listIterator();
           
           
                while (it2.hasNext())
                {
                    temp = it2.next();
       
                    	 changeY((int) (getSpeed()*Math.sin(Math.toRadians(getDirection()))));
                         changeX((int) (getSpeed()*Math.cos(Math.toRadians(getDirection()))));
				
                    if(temp.collision(getX(),getY(),getImage().getWidth(null),getImage().getHeight(null)))
                    {
                    	if(notOwner(temp)){
	                    	this.itHit(temp);
	                        getEvents().setCollision(this, temp);
                    	}
                    }
                
            }
           
        }   
    }
	private void setNoTracking(boolean b) {
		NotTracking=b;	
	}
	
	public boolean notOwner(PlayerTank temp) {
		if(temp==this.playerTank){
			return false;
		}
		return true;
	}
	public void add(PlayerTank playerTank) {
	  this.playerTank=playerTank;
	}
	
	public PlayerTank getOwner(){
		return playerTank;
	}
	public void itHit(Shield shield) {
		shield.changeDamage(damageto);
		setDone(true);
		
	}
   
	
}
