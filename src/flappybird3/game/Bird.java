package flappybird3.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

public class Bird implements TickAware {
		//fyzika
		static final double koefUp = -5.0;
		static final double koefDown = 2;
		static final int ticksFlyingUp = 4;
           
		// souradnice stredu ptaka
		int viewportX;
		double viewportY;
		// rychlost pad�n�(pozitivn�) nebo vzletu (negativn�)
		double velocityY = koefDown;
		//kolik ticku jeste zbyva, nez ptak po nakopnuti zacne padat
		int ticksToFall =0;
		public Bird(int initialX, int initialY) {
			this.viewportX = initialX;
			this.viewportY = initialY;			
		}
		public void kick(){
			velocityY = koefUp;
			
                        ticksToFall = ticksFlyingUp;
		}
		public void draw(Graphics g){
			g.setColor(Color.green);
			g.fillOval(viewportX-Tile.size/2, (int) (viewportY-Tile.size/2), Tile.size, Tile.size);
			g.setColor(Color.BLACK);
			g.drawString(viewportX+", "+viewportY, viewportX, (int) viewportY);
		}
		
		public boolean collidesWithRectangel(int x, int y, int w, int h){	
			//vytvoreni kruznice reprezentujici ptaka
			Ellipse2D.Float birdsBoundary = new Ellipse2D.Float(viewportX-Tile.size/2, (int) (viewportY-Tile.size/2), Tile.size, Tile.size);
			//overeni jestli kruznice ma neprazdny prunik s ctvercem zadanym x,y,w,h
			return birdsBoundary.intersects(x, y, w, h);
			
		}
		
	@Override
	public void tick(long ticksSinceStart) {
		// TODO Auto-generated method stub
            if(ticksToFall>0){ // ptak lezi nahoru "cekame"
            ticksToFall--;
            }else{
            //ptak pada nebo ma zacit padat
                velocityY=koefDown;
            
            }
            viewportY=(viewportY+velocityY);
		
	}
       

}
