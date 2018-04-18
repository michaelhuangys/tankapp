/*
 * This is Tank project. It's a game which player can rotate the tank and move tank.
 * Tank can also shoot bullets and eat powerup to upgrade the power.
 */


package tankgame;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.net.URL;
import java.text.AttributedString;
import java.util.*;

import javax.imageio.ImageIO;
import javax.sound.midi.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import myGames.*;
import tankgame.TankGame.TankEvents;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.*;

/**
 *  The goal of the game is to destroy enemy boss.
 * For one player the controls are: arrow keys control movement, enter is fire
 * For the second player: wasd for movement and space to fire
 * You get 2 extra lives shared between the players
 * Player 1 spawns on the right and Player 2 on the left
 * Player 1 has the bottom life bar in the lower left corner and Player 2 has
 * the top life bar.
 * I got an error trying to run this in Chrome but it runs in Internet Explorer
 * and as an independent program.
 * @author Lowell Milliken
 */
public class TankGame extends Game
{
	private boolean collision_with_tank=false;
    private GameSpace screen;
    private Random random = new Random();
    private int player1_part1_X=0;
    private int player1_part2_X=0;
    private int player1_part1_Y=0;
    private int player1_part2_Y=495;
    
    private int player2_part1_X=640;
    private int player2_part2_X=768;
    private int player2_part1_Y=528;
    private int player2_part2_Y=1024;
    private Image mini;
    private Image weapon;
    private PlayerTank Player1;
    private PlayerTank Player2;
    private ArrayList<ArrayList> everything;
    private ArrayList<Thing> things;
    static ArrayList<Bullet> Bullets;
    static ArrayList<Wall> walls;
    private ArrayList<PlayerTank> players;
    private int score;
    final private int startPoint = -30;
    Image[][] Blue_wall;
   private Image player1_part1;
   private Image player1_part2;
   private Image player2_part1;
   private Image player2_part2;
	private Image[] playerimg[],Shell_basic,Shell_heavy,Shell_light,largeexpl,
	basic[],heavy[],light[],powerup[],bouncing[],shield[];
	private ArrayList<Image[]> heavy_bullet;
	private ArrayList<Image[]> light_bullet;
	private ArrayList<Image[]> basic_bullet;
	private ArrayList<Image[]> weapon_list;
    private GameController gcontroller;
    private TankEvents events;
    private ArrayList<ScoreType> sTable;
    private ScoreTable scoreTable; 
    private boolean gameover;
    private boolean destroy = false;
    private boolean twoplayers = false;
    private boolean isBoss = false;
    private int lives;
    private Image lifeImg;
    private URL[] explsoundurl;

    //creates and adds all the game panel to the applet
    public void makeImageArray(ArrayList<Image[]> list, Image image){
    	BufferedImage bimg;
    	Image img = null;
    	int numImages,w,h;
    	list=new ArrayList<>();
    	try{
    		img =image;
    	}catch (Exception e){
    		System.out.println("Error reading file: "+image);
    	}
    	bimg=convertToBuffered(img);
    	w=img.getWidth(this);
    	h=img.getHeight(this);
    	numImages=w/h;
    	
    	Image temp[]=new Image[1];
    	for(int i=0;i<numImages;i++){
    		temp[0]=bimg.getSubimage(i*h,0,h,h);
    		try{
    		 File imagefile = new File("bullet_light_"+i+ ".png");
             ImageIO.write((RenderedImage) temp[0], "png", imagefile);
    		}catch(Exception e){
    			
    		}
    		list.add(temp);
    	}
    
    }
    
    private BufferedImage convertToBuffered(Image img)     {       
    	int w = img.getWidth(this);        
    	int h = img.getHeight(this);  
	    BufferedImage bimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);  
	    Graphics2D g2 = bimg.createGraphics();  
	    g2.drawImage(img, 0, 0, this);      
        g2.dispose();  
	    return bimg;     
    } 
    
	public void init() 
    {      
        super.init();
        
        //initialize bullets
//        makeImageArray(basic_bullet,Shell_basic[0]);
//        makeImageArray(light_bullet,Shell_light[0]);
//        makeImageArray(heavy_bullet,Shell_heavy[0]);
    //    makeImageArray(weapon_list,weapon);
        
        screen = new GameSpace(getSprite("Resource/Background.png"),Blue_wall[0], new DrawAbs());
        add(screen, BorderLayout.CENTER);
        setBackground(Color.white);
     
        
 
        everything = new ArrayList<ArrayList>();
        things = new ArrayList<Thing>();
        walls = new ArrayList<Wall>();
        Bullets= new ArrayList<Bullet>();
        players = new ArrayList<PlayerTank>();
   
        everything.add(walls);
        everything.add(Bullets);
        everything.add(things);
        everything.add(players);
      
     
        events = new TankEvents();

        KeyControl keys = new KeyControl(events);
        addKeyListener(keys);
        
        gcontroller = new GameController();
        score = 0;
        
        sTable = new ArrayList<ScoreType>(10);
        for(int i = 0; i < 10; i++)
        {
            sTable.add(new ScoreType("<No One>", 0));
        }
        
        gameover = false;
        
      
    }
    
  

	//getting all image files
    @Override
    public void initImages()
    {
        try
        {
            playerimg = new Image[3][1];
            Blue_wall =new Image[2][1];
            Shell_basic=new Image[1];
            Shell_light=new Image[1];
            Shell_heavy=new Image[1];
            Blue_wall[0][0]=getSprite("Resource/Blue_wall1.png");
            Blue_wall[1][0]=getSprite("Resource/Blue_wall2.png");
            for (int i = 0; i < 3; i++)
            {
                playerimg[i][0] = getSprite("Resource/Tank_grey_" + (i + 1) + ".png");
            }
            Shell_basic[0]=getSprite("Resource/Shell_basic_strip60.png");
            Shell_light[0]=getSprite("Resource/Shell_light_strip60.png");
            Shell_heavy[0]=getSprite("Resource/Shell_heavy_strip60.png");
            
            largeexpl = new Image[7];
            for (int i = 0; i < 7; i++)
            {
                largeexpl[i] = getSprite("Resource/explosion2_" + (i + 1) + ".png");
            }
        
            basic=new Image[1][1];
           
            basic[0][0] = getSprite("Resource/bullet_" + 29 + ".png");
           
           
           light=new Image[1][1];
           
            light[0][0] = getSprite("Resource/bullet_light_" + 29 + ".png");
           
           heavy=new Image[1][1];
         
            heavy[0][0] = getSprite("Resource/bullet_heavy_" + 29 + ".png");
           bouncing=new Image[1][1];
           bouncing[0][0]=getSprite("Resource/bullet.png");
        
           powerup=new Image[6][1];
           powerup[0][0]=getSprite("Resource/powerup.png");
          powerup[1][0]=getSprite("Resource/basic_bullet.png");
           powerup[2][0]=getSprite("Resource/light_bullet.png");
           powerup[3][0]=getSprite("Resource/heavy_bullet.png");
           powerup[4][0]=getSprite("Resource/bouncing_bullet.png");
           powerup[5][0]=getSprite("Resource/explosion2_1.png");
           weapon=getSprite("Resource/Weapon_strip4.png");
        } catch (Exception e)
        {
            System.out.println("Error in getting images: " + e.getMessage());
        }
    }
    
    //getting all sound files
    @Override
    public void initSound()
    {
        try
        {
        Sequence music;
        Sequencer seq;
        URL musicu = TankGame.class.getResource("Resource/tank.wav");
      
        AudioInputStream explSound;
        Clip clip;
        explSound = AudioSystem.getAudioInputStream(musicu);
        clip = AudioSystem.getClip();
        clip.open(explSound);
        clip.start();
       clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch(Exception e)
        {
            System.out.println("Error in midi: " + e.getMessage());
        }
    }

    //this stores scores paired with names
    public class ScoreType
    {

        private String name;
        private int score;

        public ScoreType(String name, int score)
        {
            this.name = name;
            this.score = score;
        }

        public int getScore()
        {
            return score;
        }

        public String getName()
        {
            return name;
        }
        
        public void setName(String name)
        {
            this.name = name;
        }
    }

    //this creates Things when needed to make the gameplay pattern
    public class GameController
    {

        private int timer;

        public GameController()
        {
            timer = 0;
        }

        public void timeline()
        {
         
        switch(timer){
        case 0:
        	
        	walls.add(new Wall(0,0,Blue_wall[0],events,30));
			ImageObserver observer = null;
			int tile_height=Blue_wall[0][0].getHeight(observer);
			int tile_width=Blue_wall[0][0].getWidth(observer);
			int numberX=1280/tile_height;
			int numberY=1280/tile_width;
		
			//side wall
			for(int i=0;i<numberX;i++){
				walls.add(new Wall(16,16+i*tile_height,Blue_wall[0],events,30));
				walls.add(new Wall(1264,16+i*tile_height,Blue_wall[0],events,30));
     			walls.add(new Wall(16+i*tile_height,16,Blue_wall[0],events,30));
				walls.add(new Wall(i*tile_height,1264,Blue_wall[0],events,30));
		    }
			
			//middle 2 walls
			for(int i=0;i<12;i++){
				walls.add(new Wall(16+20*tile_height,48+i*tile_height,Blue_wall[1],events,70));
				walls.add(new Wall(16+19*tile_height,48+i*tile_height,Blue_wall[1],events,70));
				walls.add(new Wall(16+20*tile_height,48+26*tile_height+i*tile_height,Blue_wall[1],events,70));
				walls.add(new Wall(16+19*tile_height,48+26*tile_height+i*tile_height,Blue_wall[1],events,70));
			}
			
			//hoeizontal wall
			for(int i=0;i<16;i++){
				walls.add(new Wall(12*tile_height+16+i*tile_height,16+13*tile_height,Blue_wall[0],events,30));
				walls.add(new Wall(12*tile_height+16+i*tile_height,16+26*tile_height,Blue_wall[0],events,30));
			}
			
			//middle side 4 walls
			for(int i=0;i<4;i++){
				walls.add(new Wall(16+27*tile_width,9*tile_width+16+i*tile_height,Blue_wall[0],events,30));
				walls.add(new Wall(16+12*tile_width,26*tile_width+16+i*tile_height,Blue_wall[0],events,30));
			}
			
			//initilize 6 blocks
			for(int i=0;i<4;i++){
				for(int j=0;j<6;j++){
				walls.add(new Wall(16+j*tile_width,18*tile_width+16+i*tile_width,Blue_wall[0],events,30));
				walls.add(new Wall(16+12*tile_width+j*tile_width,18*tile_width+16+i*tile_width,Blue_wall[0],events,30));
				walls.add(new Wall(16+22*tile_width+j*tile_width,18*tile_width+16+i*tile_width,Blue_wall[0],events,30));
				walls.add(new Wall(16+34*tile_width+j*tile_width,18*tile_width+16+i*tile_width,Blue_wall[0],events,30));
				}
			}
				
			//side horizontall wall
			for(int i=0;i<7;i++){
				walls.add(new Wall(16+i*tile_width+32*tile_height,16+5*tile_height,Blue_wall[1],events,70));
				walls.add(new Wall(16+i*tile_width,35*tile_height,Blue_wall[1],events,70));
			}
			
			//horizontal wall around tank
			for(int i=0;i<4;i++){
				walls.add(new Wall(5*tile_width+16+i*tile_height,16+4*tile_width,Blue_wall[0],events,30));
				walls.add(new Wall(5*tile_width+16+i*tile_height,16+8*tile_width,Blue_wall[0],events,30));
				walls.add(new Wall(30*tile_width+16+i*tile_height,16+35*tile_width,Blue_wall[0],events,30));
				walls.add(new Wall(30*tile_width+16+i*tile_height,16+31*tile_width,Blue_wall[0],events,30));
			}
			
			//vertical wall around tank
			for(int i=0;i<3;i++){
				walls.add(new Wall(5*tile_width+16,i*tile_width+16+5*tile_width,Blue_wall[0],events,30));
				walls.add(new Wall(33*tile_width+16,i*tile_width+48+31*tile_width,Blue_wall[0],events,30));
			}
			Player1=new PlayerTank(256,218, 0., 3, playerimg[0],
                    events,120, 20, 10, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                    KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER,
                    KeyEvent.VK_DELETE, 10, 5, 2);
			players.add(Player1);
			
			Player2=new PlayerTank(1024, 1062, 180, 3, playerimg[0],
                    events, 120, 20, 10, KeyEvent.VK_A, KeyEvent.VK_D,
                    KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_SPACE,
                    KeyEvent.VK_P, 10, 5, 2);
		
			players.add(Player2);
			
			lives=3;
			
			Shield shield=new Shield(300,300,0,0,powerup[5],events,2,everything);
			PowerUp tripleBullet=new PowerUp(100,200,0.,0,powerup[1],events,2,everything,1);
			PowerUp TrackingBullet=new PowerUp(400,200,0.,0,powerup[3],events,2,everything,2);
			PowerUp fast_Bullet=new PowerUp(500,200,0.,0,powerup[2],events,2,everything,3);
			PowerUp bouncing_Bullet=new PowerUp(200,200,0.,0,powerup[4],events,2,everything,4);
			PowerUp bouncing_Bullet_2=new PowerUp(900,1000,0.,0,powerup[4],events,2,everything,4);
			things.add(tripleBullet);
			things.add(TrackingBullet);
			things.add(fast_Bullet);
			things.add(bouncing_Bullet);
			things.add(bouncing_Bullet_2);
			things.add(shield);
			if(tripleBullet.getHit()){
				 tripleBullet=new PowerUp(randomX(),randomY(),0.,0,powerup[0],events,2,everything,1);
	        	things.add(tripleBullet);	
			}

		timer++;
		
        }
       }
        
        private int randomX()
        {
            return random.nextInt(screen.getWidth());
        }
        
        private int randomY()
        {
            return random.nextInt(screen.getHeight());
        }

        public void resetTimer()
        {
            timer = 0;
        }
    }

   
  
    //This is the player's plane
    public class PlayerTank extends PlayerParent
    {
        private int startx, starty, spawnDelay;
        private int mercyTimer;
       
       
        private boolean up;
        public PlayerTank(int x, int y, double direction, int speed, Image[] img,
                GameEvents events, int maxdamage, int damageto, int eps, 
                int left, int right, int up, int down, int fire, int spfire,
                int shotTime, int fastShotTime, int deadTime)
        {
            super(x, y, direction, speed, img, events, maxdamage, damageto, eps,
                    left, right, up, down, fire, spfire, shotTime, fastShotTime,
                    deadTime);
            
            startx = x;
            starty = y;
            spawnDelay = 70;
            mercyTimer = 20;
        }

        //moves based on the keys pressed, but only with the basic update
        @Override
        public void move()
        {
        
        	if(getDone()){
        		
        	}
            if(getRotateLeft())
            {
            	
              changeDirection(6);
            }
            
            if(getRotateRight())
            {
            	changeDirection(-6);
            	 
            }
            
            if(getMvUp())
            {
            	
                  
                if (getY() + getSpeed()*Math.sin(Math.toRadians(getDirection())) >15)
                {
                    changeY((int) (getSpeed()*Math.sin(Math.toRadians(getDirection()))));
                                                     
                } else
                {
                    setY(15);
                }
                
   
                if (getX() + getSpeed()*Math.cos(Math.toRadians(getDirection())) >15)
                {
                   changeX((int) (getSpeed()*Math.cos(Math.toRadians(getDirection()))));
                       
                } else
                {
                    setX(15);
                }
                

                if (getX() + getSpeed()*Math.cos(Math.toRadians(getDirection())) <screen.getWidth()-15)
                {
                   changeX((int) (getSpeed()*Math.cos(Math.toRadians(getDirection()))));
                       
                } else
                {
                    setX(screen.getWidth()-15);
                }
                
                if (getY() + getSpeed()*Math.sin(Math.toRadians(getDirection())) <1280-15)
                {
                    changeY((int) (getSpeed()*Math.sin(Math.toRadians(getDirection()))));
                                                     
                } else
                {
                    setY(1280-15);
                }
                
            }
            
            if(getMvDown())
            {
            	 if (getY() + getSpeed()*Math.sin(Math.toRadians(getDirection())) >15)
                 {
                     changeY(-(int)(getSpeed()*Math.sin(Math.toRadians(getDirection()))));
                                                      
                 } else
                 {
                     setY(15);
                 }
                 
    
                 if (getX() + getSpeed()*Math.cos(Math.toRadians(getDirection())) >15)
                 {
                    changeX(-(int) (getSpeed()*Math.cos(Math.toRadians(getDirection()))));
                        
                 } else
                 {
                     setX(15);
                 }
                 

                 if (getX() + getSpeed()*Math.cos(Math.toRadians(getDirection())) <screen.getWidth()-15)
                 {
                    changeX(-(int) (getSpeed()*Math.cos(Math.toRadians(getDirection()))));
                        
                 } else
                 {
                     setX(screen.getWidth()-15);
                 }
                 
                 if (getY() + getSpeed()*Math.sin(Math.toRadians(getDirection())) <1280-15)
                 {
                     changeY(-(int) (getSpeed()*Math.sin(Math.toRadians(getDirection()))));
                                                      
                 } else
                 {
                     setY(1280-15);
                 }
            }
            
            if (getDamage() >= getMax())
            {
                lives--;
                if(getPower() > 0)
                {
                    setPower(getPower() - 1);
                }
                
                setDone(true);
            }

            if(getShotDelay() > 0)
            {
                changeShotDelay(-1);
            }
            
            if(mercyTimer > 0)
            {
                mercyTimer--;
            }
            
            Wall temp;
   		 
            Iterator<Wall> it = walls.listIterator();
            while (it.hasNext())
            {
                temp = it.next();
               
                Rectangle r3 = new Rectangle(temp.getX()+16,temp.getY()+16,temp.getWidth(),temp.getHeight());
                int dy=0;
                int dx=0;
                if(getMvUp()){
                 dx=(int) (getSpeed()*Math.cos(Math.toRadians(getDirection())));
                 dy=(int) (getSpeed()*Math.sin(Math.toRadians(getDirection())));
                
                }
                else if(getMvDown()){
               	   dx=(int) -(getSpeed()*Math.cos(Math.toRadians(getDirection())));
                      dy=(int) -(getSpeed()*Math.sin(Math.toRadians(getDirection())));
                }
                
                Rectangle r4 = new Rectangle(getX()+dx,getY()+dy,getWidth(),getHeight());
                      
                if (r3.intersects(r4)) {             	
                            setY(getY()-2*dy);
                            setX(getX()-2*dx);
                            setCollisionWithWall(true);
                            break;
                       }   
                setCollisionWithWall(false);
            }
            
            PlayerTank temp_tank;
            
            Iterator<PlayerTank> player = players.listIterator();
            while(player.hasNext()){
            	temp_tank=player.next();
            	if(temp_tank!=this){
            		int  dx=(int) (getSpeed()*Math.cos(Math.toRadians(getDirection())));
                    int  dy=(int) (getSpeed()*Math.sin(Math.toRadians(getDirection())));
                     
            		  Rectangle r3 = new Rectangle(temp_tank.getX(),temp_tank.getY(),temp_tank.getWidth(),temp_tank.getHeight());
            		  Rectangle r4 = new Rectangle(getX(),getY(),getWidth(),getHeight());
            	   if (r4.intersects(r3)){
            		   setY(getY()-2*dy);
            		   setX(getX()-2*dx);
       	   }
            	}
            }
        }
        
     
		//shoots with button pressed, but only with the basic update
        @Override
        public void action()
        {
        	
            if(getIsFiring())
            {
                if(getShotDelay() == 0)
                {  
                	if(getPower()==0){
                	Bullet bullet=new Bullet(getX()-(int)(5*Math.cos(getDirection())),getY()-(int)(5*Math.sin(getDirection())), getDirection()+180, -10, basic[0], events,
                                1, everything, 10);
                		    bullet.add(this);
                		    Bullets.add(bullet);
                 	}
                
                	//triple bullet
                	
                	if(getPower()==1&&getAmount()>0){
	                	Bullet bullet1=new Bullet(getX()-(int)(5*Math.cos(getDirection())),getY()-(int)(5*Math.sin(getDirection())), getDirection()+180+30, -10, basic[0], events,
	                                1, everything, 10);
	                	Bullet bullet2=new Bullet(getX()-(int)(5*Math.cos(getDirection())),getY()-(int)(5*Math.sin(getDirection())), getDirection()+180, -10, basic[0], events,
	                            1, everything, 10);
	                	Bullet bullet3=new Bullet(getX()-(int)(5*Math.cos(getDirection())),getY()-(int)(5*Math.sin(getDirection())), getDirection()+180-30, -10, basic[0], events,
	                            1, everything, 10);
			                	bullet1.add(this);
			                	bullet2.add(this);
			                	bullet3.add(this);
		                		Bullets.add(bullet1);
		                		Bullets.add(bullet2);
		                		Bullets.add(bullet3);
		                		
		                		decreaseAmount();
            		}else if(getAmount()==0){
                		setPower(0);
                	}
                	
                	//tracking bullet
                	if(getPower()==2 && getAmount()>0){
                	    TrackingBullet Trackingbullet=new TrackingBullet(getX()-(int)(5*Math.cos(getDirection())),getY()-(int)(5*Math.sin(getDirection())), getDirection()+180, -10, heavy[0], events,
                                1, everything, 20);
                		   Trackingbullet.add(this);
                		Bullets.add(Trackingbullet);
                		decreaseAmount();
                	}else if(getAmount()==0){
                		setPower(0);
                	}
                	
                	//bouncing bullet
                	if(getPower()==4 && getAmount()>0){
                	    BouncingBullet BouncingBullet=new  BouncingBullet(getX()-(int)(5*Math.cos(getDirection())),getY()-(int)(5*Math.sin(getDirection())), getDirection(), 5, bouncing[0], events,
                                1, everything, 25);
                	    BouncingBullet.add(this);
                		Bullets.add(BouncingBullet);
                		decreaseAmount();
                	}else if(getAmount()==0){
                		setPower(0);
                	}
                	
                	setShotDelay(getShotTime());
                	//fast bullet
                	if(getPower()==3){
                		Bullet fast_bullet=new Bullet(getX()-(int)(5*Math.cos(getDirection())),getY()-(int)(5*Math.sin(getDirection())), getDirection()+180, -20, light[0], events,
                                1, everything, 10);
                		    fast_bullet.add(this);
                		    Bullets.add(fast_bullet);
                		    setShotDelay(4);
                	}
                	
                	
                }
            }
            else
            {
                if(getShotDelay() > getFastShotTime())
                {
                    setShotDelay(getFastShotTime());
                }
            }
        }

        

       
		//explodes, then set up the scoretable or respawns depending on lives
        @Override
        public void dead()
        {   
            if(getDeadTimer() == 0)
            {
               
            }
            
            setDeadTimer(getDeadTimer() +1);
            setPower(0);
            if(getDeadTimer() == getDeadTime() + spawnDelay)
            {
                if(lives < 0)
                {
                    isBoss = false;
                    gameover = true;
                    scoreTable = new ScoreTable("High Scores");
                    scoreTable.setVisible(true);
                }
                else
                {
                	
                    setX(startx);
                    setY(starty);
                    setDone(false);
                    setDeadTimer(0);
                    setDamage(0);
                    mercyTimer = 30;
                }
            }
        }
        
      
		//this makes the plane flash during mercy invincibility
        @Override
        public void draw(Graphics2D g2, ImageObserver obs)
        {
            if(mercyTimer%2 == 0)
            {
                super.draw(g2, obs);
              //draw life bar
                if((double)((double)(getDamage())/(double)(getMax()))>.9){
                g2.setColor(Color.red);
                }else if((double)((double)(getDamage())/(double)(getMax()))>.6){
                	g2.setColor(Color.yellow);
                }else {
                	g2.setColor(Color.green);
                }
                g2.fillRect(getX()-40, getY()-getHeight()/2-20, (int)(80*(1-(double)getDamage()/
                        (double)getMax())), 14);
            }
        }
        
        //cannot be hit for some time after being hit
        @Override
        public boolean collision(int x, int y, int w, int h)
        {
            if(mercyTimer == 0)
            {
                return super.collision(x, y, w, h);
            }
            
            return false;
        }

       
    
        //starting mercy timer
        @Override
        public void hitMe(Thing t)
        {
            if(!(t instanceof PowerUp))
            {
                mercyTimer = 20;
            }
            t.itHit(this);
        }
    }
    
    //adds an event type to GameEvents
    public class TankEvents extends GameEvents
    {

        public void setBoss(int type)
        {
            setType(type);
            setChanged();

            notifyObservers(this);
        }
    }

    public class Wall extends Unit {

    	
    	private boolean collision_with_tank=false;
    	public Wall(int x, int y, Image[] blue_wall, TankEvents events,int max_damage) {
    		
    		super(x, y, 0., 0, blue_wall, events,max_damage,0,10);
    	}

    	
    	@Override
    	public void move() {
    		 
    	
	        Bullet bullet;
    		 
             Iterator<Bullet> it1 = Bullets.listIterator();
           
             while (it1.hasNext())
             {
            	 bullet = it1.next();
            	 
                 Rectangle r3 = new Rectangle(getX(),getY(),getWidth(),getHeight());
                 int dy=0;
                 int dx=0;
                
                  dx=(int) ( bullet.getSpeed()*Math.cos(Math.toRadians( bullet.getDirection())));
                  dy=(int) ( bullet.getSpeed()*Math.sin(Math.toRadians( bullet.getDirection())));
                 
        
                 Rectangle r4 = new Rectangle( bullet.getX()+dx, bullet.getY()+dy, bullet.getWidth(),bullet.getHeight());
               
                 if (r3.intersects(r4)&&bullet.getDamageTo()!=25) {
                	 
                	   bullet.itHit(this);
                	   if (getDamage() >= getMax()&&getMax()!=70)
                       {
                	     setRDone(true);
                       }else{
                    	   setDamage(70);
                       }
                             
                        }
                  }
                 
          }

    	
    	public void dead() {
    		

    	}

    	
    	public void itHit(Unit u) {
    		
    	}



    	public boolean collision(int x, int y, int w, int h)
        {
               
                return super.collision(x, y, w, h);
        }
    	@Override
    	public void hitMe(Thing caller) {
    		
    		
    	}

		@Override
		public void update(Observable o, Object arg) {
			  GameEvents events = (GameEvents) arg;
		        if(events.getType() == 1)
		        {        
		            if(events.getTarget() == this)
		            {
		                hitMe((Thing)events.getCaller());
		            }
		        }
		      
		}

    }
    //Updates all Things and then draws everything
    //when the game is resetting, this method will also 
    @Override
    public void drawAll(int w, int h, Graphics2D g2)
    {
  
        Thing temp;
   
        screen.drawBackground(g2);
        Iterator<ArrayList> it = everything.listIterator();
        
        while (it.hasNext())
        {
            Iterator<Thing> it2 = it.next().listIterator();
            while (it2.hasNext())
            {
                if (gameover)
                {
                    break;
                }
                temp = it2.next();
                temp.update(w, h); //things are moving
            
            
                if (temp.getRDone())
                {
                    it2.remove();
                }
            }
            if (gameover)
            {
                break;
            }
        }

        screen.drawHere(everything, g2);

       

        if(destroy)
        {
            it = everything.listIterator();
            while(it.hasNext())
            {
                Iterator<Thing> it2 = it.next().listIterator();
                while(it2.hasNext())
                {
                    it2.next();
                    it2.remove();
                }
                
            }
            
            gcontroller = new GameController();
            destroy = false;
            gameover = false;
            score = 0;
            events.deleteObservers();
            this.requestFocus();
        }
        
       gcontroller.timeline();
    }
   
	//This is the high score table. It is created by Boss or PlayerPlane when
    //needed. It shows the high scores and asks for the users to enter their
    //name if needed. The high scores are not persistent after the game is
    //closed
    public class ScoreTable extends JFrame implements ActionListener
    {
        private JTextField enterName;
        private boolean high = false;
        private int thisIndex;
        
        public ScoreTable(String title)
        {
            super(title);
            this.setLocation(screen.getWidth()/2, screen.getHeight()/2);
            this.setLayout(new GridLayout(0,2));
            for(int i = 0; i < 10; i++)
            {
                if(score >= sTable.get(i).getScore() && !high)
                {
                    enterName = new JTextField(10);
                    enterName.setEditable(true);
                    enterName.setFocusable(true);
                    this.add(enterName);
                    
                    JTextField scoreT = new JTextField(6);
                    scoreT.setEditable(false);
                    scoreT.setText("" + score);
                    this.add(scoreT);
                    
                    sTable.add(i, new ScoreType("dummy", score));
                    
                    high = true;
                    thisIndex = i;
                }
                else
                {
                    JTextField name = new JTextField(20);
                    name.setEditable(false);
                    name.setText(sTable.get(i).getName());
                    this.add(name);
                    
                    JTextField scoreT = new JTextField(6);
                    scoreT.setEditable(false);
                    scoreT.setText("" + sTable.get(i).getScore());
                    this.add(scoreT);
                }
            }
            
            JButton one = new JButton("One Player");
            one.setActionCommand("one");
            this.add(one);
            JButton two = new JButton("Two Players");
            two.setActionCommand("two");
            this.add(two);
            one.addActionListener(this);
            two.addActionListener(this);
            this.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    actionPerformed(new ActionEvent(this,
                            ActionEvent.RESERVED_ID_MAX+1, "None"));
                }
            });
            this.pack();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if("one".equals(e.getActionCommand()))
            {
                twoplayers = false;
            }
            else if("two".equals(e.getActionCommand()))
            {
                twoplayers = true;
            }
            
            destroy = true;
            if(high)
            {
                sTable.get(thisIndex).setName(enterName.getText());
            }
            this.dispose();
        }
    }
    
   
    
    public void paint(Graphics g)
    {
    	 setSize(1286,1280);
        Dimension d = getSize();
         
        Graphics2D g2 = createGraphics2D(d.width, d.height);
        drawAll(d.width, d.height, g2);
        g2.dispose();
        BufferedImage image=super.getImage(); 
       
         mini = image.getScaledInstance(this.getWidth()/5,  
                this.getHeight() / 5, 
                BufferedImage.SCALE_FAST); 
          //reset player position
         if(Player1.getDone()){
        	 Player1.setY(218);
        	 Player1.setX(256);
         }
          
         //calculate the position of x of subimage
        if(Player1.getX()-256>=0&&Player1.getX()-256<=640){
         player1_part1_X=Player1.getX()-256;
         player1_part2_X=Player1.getX()-256;
        }
        //calculate the position of y of subimage
        if(Player1.getY()-218>=0 &&Player1.getY()-218<=495){
         player1_part1_Y=Player1.getY()-218;
         player1_part2_Y=Player1.getY()+277;
        }
          player1_part1= image.getSubimage(player1_part1_X, player1_part1_Y,640 ,screen.getHeight()-mini.getHeight(null));
          player1_part2= image.getSubimage(player1_part2_X, player1_part2_Y,640-mini.getWidth(null)/2+6 ,mini.getHeight(null)); 
         
        
         
          //calculate the position of x of subimage
          if(Player2.getX()-374>=0&&Player2.getX()-374<=640){
           player2_part1_X=Player2.getX()-374;
           player2_part2_X=Player2.getX()-247;
          }
          //calculate the position of y of subimage
          if(Player2.getY()-218>=0 &&Player2.getY()-218<=495){
           player2_part1_Y=Player2.getY()-218;
           player2_part2_Y=Player2.getY()+277;
          }
          
          //reset Player2 position if it dies.
          if(Player2.getDone()){
        	player2_part1_X=640;
         	player2_part1_Y=528;
         	player2_part2_X=768;
         	player2_part2_Y=1024;
           }
      
          player2_part1= image.getSubimage(player2_part1_X, player2_part1_Y,640 ,screen.getHeight()-mini.getHeight(null));
          player2_part2= image.getSubimage(player2_part2_X, player2_part2_Y,640-mini.getWidth(null)/2-5,mini.getHeight(null)); 
     
		//draw player1
        g.drawImage(player1_part1, 0, 0, this);
        g.drawImage(player1_part2, 0, screen.getHeight()-mini.getHeight(null), this);
        //draw player2
      g.drawImage(player2_part2, 640+mini.getHeight(null)/2+5, screen.getHeight()-mini.getHeight(null), this);
      g.drawImage(player2_part1, 645, 0, this);
      
        g.setColor(Color.BLACK);
        g.fillRect(640, 0, 5, screen.getHeight()-mini.getHeight(null));
       g.drawImage(mini,517, screen.getHeight()-mini.getHeight(null), this);
       
    }
    
    private void setWidth(int i) {

	}

	public static void main(String[] args)
    {
        final TankGame game = new TankGame();
        game.init();
        final JFrame f = new JFrame("Tank Game");
        f.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                f.dispose();
                System.exit(0);
            }
        });
      
        f.getContentPane().add("Center", game);
        f.pack();
        f.setSize(new Dimension(1300,1280));
          f.setResizable(false);
          game.start();
          f.setVisible(true);
    }
}

