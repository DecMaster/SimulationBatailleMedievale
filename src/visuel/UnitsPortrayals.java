package visuel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import sim.portrayal.simple.CircledPortrayal2D;
import sim.portrayal.simple.ImagePortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;

// Classe regroupant toutes les méthodes de portrayals

public class UnitsPortrayals {
	
	// ENUM
	public enum team{
		blue,
		red
	}
	public enum orientation{
		left,
		right
	}
	
	private String teamPath;
	
	// CONSTRUCTEURS
	
	public UnitsPortrayals(team equipe) {
		switch(equipe) {
		case blue :
			teamPath = "/blueSprites";
			break;
		case red :
			teamPath = "/redSprites";
			break;
		}
	}
	
	// METHODES UTILES
    private BufferedImage createTransformed(
            BufferedImage image, AffineTransform at)
        {
            BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newImage.createGraphics();
            g.transform(at);
            g.drawImage(image, 0, 0, null);
            g.dispose();
            return newImage;
        }

	// PORTRAYALS UNITES
	
	public CircledPortrayal2D getSwordMan(orientation o){

		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res" + teamPath + "/sword.png");
			BufferedImage image = ImageIO.read(file);
			
			// FLIP IF LEFT
			if(o == orientation.left) {
		        AffineTransform at = new AffineTransform();
		        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
		        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
		        image = createTransformed(image, at);
			}
		
			// SETUP PORTRAYAL
			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
			return new CircledPortrayal2D(imagePort, Color.red, true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	public CircledPortrayal2D getArcher(orientation o){
		
		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res" + teamPath + "/archer.png");
			BufferedImage image = ImageIO.read(file);
		
			// FLIP IF LEFT
			if(o == orientation.left) {
		        AffineTransform at = new AffineTransform();
		        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
		        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
		        image = createTransformed(image, at);
			}
			
			// SETUP PORTRAYAL
			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
			return new CircledPortrayal2D(imagePort, Color.red, true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	public CircledPortrayal2D getCaptain(orientation o){
		
		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res" + teamPath + "/captain.png");
			BufferedImage image = ImageIO.read(file);
		
			// FLIP IF LEFT
			if(o == orientation.left) {
		        AffineTransform at = new AffineTransform();
		        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
		        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
		        image = createTransformed(image, at);
			}
			
			// SETUP PORTRAYAL
			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
			return new CircledPortrayal2D(imagePort, Color.red, true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	public CircledPortrayal2D getHealer(orientation o){
		
		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res" + teamPath + "/healer.png");
			BufferedImage image = ImageIO.read(file);
		
			// FLIP IF LEFT
			if(o == orientation.left) {
		        AffineTransform at = new AffineTransform();
		        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
		        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
		        image = createTransformed(image, at);
			}
			
			// SETUP PORTRAYAL
			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
			return new CircledPortrayal2D(imagePort, Color.red, true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	public CircledPortrayal2D getKnight(orientation o){
		
		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res" + teamPath + "/knight.png");
			BufferedImage image = ImageIO.read(file);
		
			// FLIP IF LEFT
			if(o == orientation.left) {
		        AffineTransform at = new AffineTransform();
		        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
		        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
		        image = createTransformed(image, at);
			}
			
			// SETUP PORTRAYAL
			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
			return new CircledPortrayal2D(imagePort, Color.red, true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	public CircledPortrayal2D getMage(orientation o){
		
		try {
			
			// CHARGEMENT IMAGE
			InputStream file = getClass().getResourceAsStream("/res" + teamPath + "/mage.png");
			BufferedImage image = ImageIO.read(file);
		
			// FLIP IF LEFT
			if(o == orientation.left) {
		        AffineTransform at = new AffineTransform();
		        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
		        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
		        image = createTransformed(image, at);
			}
			
			// SETUP PORTRAYAL
			ImagePortrayal2D imagePort = new ImagePortrayal2D(image);
			return new CircledPortrayal2D(imagePort, Color.red, true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
}
