package flappybird3.game.tiles;

import java.awt.Graphics;
import java.awt.Image;

import flappybird3.game.Tile;

public abstract class AbstractTile implements Tile{


	Image image;
		
	public AbstractTile(Image image){
		this.image=image;
	}
	
	
	@Override
	public void draw(Graphics g,int x, int y ) {
		//g.drawRect(x, y, Tile.size, Tile.size);
		g.drawImage(image, x, y, null);
	}

}
