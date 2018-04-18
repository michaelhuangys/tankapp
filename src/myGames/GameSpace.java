/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myGames;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 * This is the space in which the game object live.
 * It draws the background and all objects.
 * This exact way in which the objects are drawn is determined by the DrawType
 * class which
 * @author Lowell
 */
public class GameSpace extends JPanel
{

    private int backspeed;
    private int movex = 0, movey = 0;
    private double backdirection;
    private Image tile;
    private Image[] wall;
    private DrawType drawer;
    Graphics2D g2;

    //initialize tile
    public GameSpace(Image tile, Image[] blue_wall, DrawType drawer)
    {
        super();
        this.tile = tile;
        this.wall=blue_wall;
        this.drawer = drawer;
    }
    public void drawWall(Image[] blue_wall, DrawAbs drawAbs) {
		
		
	}
    public void drawBackground(Graphics2D g2)
    {
        int h = this.getHeight();
        int w = this.getWidth();
        int TileWidth = tile.getWidth(this);
        int TileHeight = tile.getHeight(this);
        //number of tile
        int NumberX = (int) (w / TileWidth);
        int NumberY = (int) (h / TileHeight);

        for (int i = -1; i <= NumberY + 3; i++)
        {
            for (int j = -1; j <= NumberX + 1; j++)
            {
                g2.drawImage(tile, j * TileWidth,
                        i* TileHeight, TileWidth,
                        TileHeight, this);
            }
        }
    }

    //goes through and draws everything
    public void drawHere(ArrayList<ArrayList> everything, Graphics2D g2)
    {
    	this.g2=g2;
        Thing temp;
        Iterator<ArrayList> it = everything.listIterator();
        while (it.hasNext())
        {
            Iterator<Thing> it2 = it.next().listIterator();
            while (it2.hasNext())
            {
                temp = it2.next();
                drawer.drawThis(temp, g2, this);
            }
        }
    }

    public void setTile(Image tile)
    {
        this.tile = tile;
    }
    
    public Image getTile()
    {
        return tile;
    }

    public void setBackDirection(double direction)
    {
        backdirection = direction;
    }
    
    public double getBackDirection()
    {
        return backdirection;
    }

    public void setBackSpeed(int speed)
    {
        backspeed = speed;
    }
    
    public int getBackSpeed()
    {
        return backspeed;
    }
    
    public void setDrawType(DrawType drawer)
    {
        this.drawer = drawer;
    }
    
    public DrawType getDrawType()
    {
        return drawer;
    }
}
