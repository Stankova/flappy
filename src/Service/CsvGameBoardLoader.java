/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import flappybird3.game.GameBoard;
import flappybird3.game.Tile;
import flappybird3.game.tiles.EmptyTile;
import flappybird3.game.tiles.WallTile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 *
 * @author enrico
 */
public class CsvGameBoardLoader implements GameBoardLoader {

    InputStream is;

    public CsvGameBoardLoader(InputStream is) {
        this.is = is;
    }

    @Override
    public GameBoard loadLevel() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            // String line = br.readLine();
            String[] line = br.readLine().split(";");
            int typeCount = Integer.parseInt(line[0]);
            //radky s definicemi typu dlazdic
            
            Map<String, Tile> tileTypes = new HashMap<>();
            
            for (int i = 0; i < typeCount; i++) {
                line = br.readLine().split(";");
                String tileType = line[0];
                String clazz = line[1];
                int x = Integer.parseInt(line[2]);
                int y = Integer.parseInt(line[3]);
                int w = Integer.parseInt(line[4]);
                int h = Integer.parseInt(line[5]);
                String url = line[6];
                Tile tile = createTile(clazz,x,y,w,h,url);
                tileTypes.put(tileType,tile);
            }

            line = br.readLine().split(";");
            int rows = Integer.parseInt(line[0]);
            int colums = Integer.parseInt(line[1]);
            System.out.println(rows + "   " + colums);
           Tile[][] tiles = new Tile[rows][colums];
           
            for (int i = 0; i < rows; i++) {
                line = br.readLine().split(";");

               
                
                for (int j = 0; j < colums; j++) {
                    String cell;
                    if (j < line.length) {
                        cell = line[j];
                    } //bunka existuje v csv
                    else {
                        cell = "";
                    } //povazujeme ji prazdnou
                   
                   tiles[i][j]= tileTypes.get(cell);				//ziskame odpovidajici typ dlazdice hashmapy
                    
                }
            }
            GameBoard gb = new GameBoard(tiles);
            return gb;
        } catch (IOException e) {
            throw new RuntimeException("Chyba pri cteni souboru", e);
        }

    }

	private Tile createTile(String clazz, int x, int y, int w, int h, String url) {
		try {
			//stahnout obrazek z url a ulozit do promenne
			BufferedImage originalImage = ImageIO.read(new URL(url));
			//vyriznout odpovidajici sprite z velkoho obrazku s mnoha sprity
			BufferedImage croppedImage = originalImage.getSubimage(x, y, w, h);
			//zvetseni/zmenseni obrazku tak, aby sedel na velikost dlazdice
			BufferedImage resizedImage = new BufferedImage(Tile.size, Tile.size, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(croppedImage, 0, 0, Tile.size, Tile.size, null);
			//vytvorime odpovidajici typ dlazdice
			switch (clazz){
			case "Wall": 
				return new WallTile(resizedImage);
			case "Empty":
				return new EmptyTile(resizedImage);
			case "Bonus":
				return new EmptyTile(resizedImage);		//TODO
			
			}
			//ani jedna vetev switch-case nevyhovovala
			throw new RuntimeException ("Neznamy typ dlazdice"+clazz);
		} catch (MalformedURLException e) {
			throw new RuntimeException("�patn� URL pro obr�zek"+clazz+":"+url,e);
		} catch (IOException e) {
			throw new RuntimeException("Chyba p�i �ten� obr�zku"+clazz+"z URL"+url,e);
		}
	}

}
