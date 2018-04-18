package tankgame;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import tankgame.TankGame.PlayerTank;
import tankgame.TankGame.Wall;
import myGames.Bullet;
import myGames.GameEvents;
import myGames.NotUnit;
import myGames.Unit;

public class Shield extends NotUnit{
   
	private int health;
	 private PlayerTank player=null;
	public Shield(int x, int y, double direction, int speed, Image[] img,
			GameEvents events, int source, ArrayList ev) {
		super(x, y, direction, speed, img, events, source, ev);
		health=100;
	}

	public void move(){
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
                    	setX(temp.getX());
                    	setY(temp.getY());
                    	setOwner(temp);
                    }
            }   
        }  
        
    	Bullet temp_bullet;

    	 Iterator<Bullet> it2 = TankGame.Bullets.listIterator();
          while (it2.hasNext())
          {
                    temp_bullet = it2.next();
                       
                      Rectangle r3 = new Rectangle(getX(),getY(),getWidth(),getHeight());
                      int dy=0;
                        int dx=0;
                       
                         dx=(int) ( temp_bullet.getSpeed()*Math.cos(Math.toRadians( temp_bullet.getDirection())));
                         dy=(int) ( temp_bullet.getSpeed()*Math.sin(Math.toRadians( temp_bullet.getDirection())));
                        
               
                        Rectangle r4 = new Rectangle( temp_bullet.getX(), temp_bullet.getY(), temp_bullet.getWidth(),temp_bullet.getHeight());
                      
                        if (r3.intersects(r4)) {
                        
                        	if(temp_bullet.notOwner(this.player)&&this.player!=null){
                        		
                        	  temp_bullet.itHit(this);
                        	}
                        	if(health<=0){
                        		setDone(true);
                        	}
                        }
            }   
        
	}
	private void setOwner(PlayerTank temp) {
		player=temp;
	}

	public void itHit(Unit u) {
      
	}

	public void changeDamage(int damageto) {
		health-=damageto;
		
	}

}
