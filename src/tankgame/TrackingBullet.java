/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import myGames.Bullet;
import myGames.GameEvents;
import myGames.NotUnit;
import myGames.Unit;
import tankgame.TankGame.PlayerTank;
import tankgame.TankGame.TankEvents;

/**
 *
 * @author Lowell
 */
 public class TrackingBullet extends Bullet
{     
    private int damageto;
     private PlayerTank playerTank;
     private boolean NotTracking=false;
    public TrackingBullet(int x, int y, double direction, int speed, Image[] img,
            GameEvents events, int source, ArrayList ev, int damageto)
    {
        super(x, y, direction, speed, img, events, source, ev, damageto);
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
        int dx=(int) (getSpeed()*Math.cos(Math.toRadians(getDirection())));
        int dy=(int) (getSpeed()*Math.sin(Math.toRadians(getDirection())));
       	 changeY((int) (getSpeed()*Math.sin(Math.toRadians(getDirection()))));
         changeX((int) (getSpeed()*Math.cos(Math.toRadians(getDirection()))));
      
        while (it.hasNext())
        {
            Iterator<PlayerTank> it2 = it.next().listIterator();
           
           
                while (it2.hasNext())
                {
                    temp = it2.next();
                       
                   
                    //if enemy is alive then tracking it
			        if(notOwner(temp)&&NotTracking==false &&temp.getDone()==false){
                     
                    	int deltaY=getY()-temp.getY();
                    	int deltaX=getX()-temp.getX();
                    	//get angle between tracking bullet and enemy
                       double angle= Math.toDegrees(Math.atan2(deltaY, deltaX));
                    	changeY((int)(getSpeed()*Math.sin(Math.toRadians(angle))));
  	                    changeX((int) (getSpeed()*Math.cos(Math.toRadians(angle))));
  	                    setDirection(angle);
  	                     dx=(int) (getSpeed()*Math.cos(Math.toRadians(angle)));
  	                     dy=(int) (getSpeed()*Math.sin(Math.toRadians(angle)));
   
                    }else{
                    	//if enemy die then don't track enemy
                    	changeY((int) (getSpeed()*Math.sin(Math.toRadians(getDirection()))));
                        changeX((int) (getSpeed()*Math.cos(Math.toRadians(getDirection()))));
                       
                    }
                    
                
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
	
	
	
}
