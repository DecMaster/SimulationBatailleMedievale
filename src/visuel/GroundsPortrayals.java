package visuel;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import sim.portrayal.simple.CircledPortrayal2D;
import sim.portrayal.simple.ImagePortrayal2D;

public class GroundsPortrayals {
	
	// PORTRAYALS TERRAINS
	
	public BufferedImage getPlain(){
		
		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res/grounds/plain.png");
			BufferedImage image = ImageIO.read(file);
			return image;
			
			// SETUP PORTRAYAL
//			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
//			return imagePort;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	public BufferedImage getForest(){
		
		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res/grounds/forest.png");
			BufferedImage image = ImageIO.read(file);
			return image;
			
			// SETUP PORTRAYAL
//			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
//			return imagePort;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	public BufferedImage getSwamp(){
		
		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res/grounds/swamp.png");
			BufferedImage image = ImageIO.read(file);
			return image;
			// SETUP PORTRAYAL
//			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
//			return imagePort;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	
	public BufferedImage getWater(){
		
		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res/grounds/water.png");
			BufferedImage image = ImageIO.read(file);
			return image;
			// SETUP PORTRAYAL
//			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
//			return imagePort;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	public BufferedImage getHill(){
		
		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res/grounds/hill.png");
			BufferedImage image = ImageIO.read(file);
			return image;
			// SETUP PORTRAYAL
//			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
//			return imagePort;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	public BufferedImage getMountain(){
		
		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res/grounds/mountain.png");
			BufferedImage image = ImageIO.read(file);
			return image;
			// SETUP PORTRAYAL
//			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
//			return imagePort;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	public BufferedImage getHouse(){
		
		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res/grounds/house.png");
			BufferedImage image = ImageIO.read(file);
			return image;
			// SETUP PORTRAYAL
//			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
//			return imagePort;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
}
