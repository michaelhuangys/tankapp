package tankgame;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import myGames.Bullet;
import myGames.GameEvents;
import tankgame.*;
import tankgame.TankGame.PlayerTank;
import tankgame.TankGame.Wall;

public class BouncingBullet extends Bullet {
	private PlayerTank playerTank;
	public BouncingBullet(int x, int y, double direction, int speed, Image[] img, GameEvents events, int source,
			ArrayList ev, int damageto) {
		super(x, y, direction, speed, img, events, source, ev, damageto);
	}
	
	public void move(){

    //check collide with wall
     Wall temp_wall;
     int   dx=(int) (getSpeed()*Math.cos(Math.toRadians(getDirection())));
      int  dy=(int) (getSpeed()*Math.sin(Math.toRadians(getDirection())));
       
     changeY((int) (getSpeed()*Math.sin(Math.toRadians(getDirection()))));
     changeX((int) (getSpeed()*Math.cos(Math.toRadians(getDirection()))));
        Iterator<Wall> it_wall = TankGame.walls.listIterator();
        while (it_wall.hasNext())
        {
            temp_wall = it_wall.next();
           
            Rectangle r3 = new Rectangle(temp_wall.getX(),temp_wall.getY(),temp_wall.getWidth(),temp_wall.getHeight());
  
            Rectangle r4 = new Rectangle(getX(),getY(),getWidth(),getHeight());
               //when bouncing bullets collides with wall then change direction.  
            if (r3.intersects(r4)) {     
            	  setY(getY()-dy);
                  setX(getX()-dx);
            	 if(temp_wall.getX()-temp_wall.getWidth()/2>=getX()+getWidth()/2||temp_wall.getX()+temp_wall.getWidth()/2<=getX()-getWidth()/2){
            		setDirection(180-getDirection());	
            	 }else if(temp_wall.getY()-temp_wall.getWidth()/2>=getY()+getWidth()/2||temp_wall.getY()+temp_wall.getWidth()/2<=getY()-getWidth()/2){
             		setDirection(360-getDirection());	
            	 }
                break;
                 }   
   
        }
        
		
		//check collide with bullet
		PlayerTank temp;

        Iterator<ArrayList> it = super.targets.listIterator(3);
   
        while (it.hasNext())
        {
            Iterator<PlayerTank> it2 = it.next().listIterator();
           
           
                while (it2.hasNext())
                {
                        temp = it2.next();
       
                    	
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
