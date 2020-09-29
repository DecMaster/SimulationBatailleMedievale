package grounds;

import java.awt.image.BufferedImage;
import data.Constantes;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.portrayal.simple.ImagePortrayal2D;
import state.battleState;

public abstract class Tile implements Steppable {
	public int x, y;
	private int tourAvantAbsorption;
	
	protected battleState state;
	public Stoppable stoppable;
	
	// Pour gérer le sang :
	public int quantiteSang = 0;
	
	public Tile(battleState battle) {
		this.state = battle;
		this.tourAvantAbsorption = Constantes.VITESSE_ABSORPTION_SANG;
	}
	
	public void faireCoulerLeSang() {
		if (Constantes.MONTRER_SANG) {
			++quantiteSang;
			//System.out.println("Sang : " + quantiteSang);
			this.SetupPortrayals();
		}
	}
	
	private void enleveUnPeuDeSang() {
		if (quantiteSang > 0) {
			--quantiteSang;
			this.SetupPortrayals();
		}
	}
	
	@Override
	public void step(SimState arg0)	{
		--this.tourAvantAbsorption;
		if (tourAvantAbsorption <= 0) {
			enleveUnPeuDeSang();
			//System.out.println("Enleve : reste " + this.quantiteSang);
			this.tourAvantAbsorption = Constantes.VITESSE_ABSORPTION_SANG;
		}
	}
	
	public abstract BufferedImage getImage(); // To be overriden -->
	
	public void SetupPortrayals() {
		BufferedImage image = getImage();
		image = souillerDeSang(image);
		state.GUI.yardPortrayal.setPortrayalForObject(this, new ImagePortrayal2D(image));
	}
	
	private BufferedImage souillerDeSang(BufferedImage img) {
		int bloodColor = 200<<16; // Met ta meilleure couleur de sang ici !!
		for (int h = 0 ; h < img.getHeight() ; ++h) {
			for (int w = 0 ; w < img.getWidth() ; ++w) {
				int color = img.getRGB(w, h);
				int newColor = blendRGB(color, bloodColor, intensiteSang());
				img.setRGB(w, h, newColor);		// Pixel par pixel, calcul de couleur. Et oui.
			}
		}
		return img;
	}
	
	private double intensiteSang() {
		return (1. - Math.pow(70./100., this.quantiteSang))*(2./3.); // Pour pas que ça remplisse complètement l'opacité (ie = 1), serious geometric progression here
	}
	
	private int blend1color(int color1, int color2, double map) { 
		double blend = (color2 * map + color1 * (1 - map));  // Mapping linéaire, la base
		return (int) blend;
	}
	
	private int blendRGB(int color1, int color2, double map) {
			// Extraction des channels de couleur
		int R1 = (color1>>16) & 0x0ff;
		int G1 = (color1>>8)  & 0x0ff;
		int B1 = (color1)     & 0x0ff;
		
		int R2 = (color2>>16) & 0x0ff;
		int G2 = (color2>>8)  & 0x0ff;
		int B2 = (color2)     & 0x0ff;
		
			// Mélangeage adapté
		int R = blend1color(R1, R2, map);
		int G = blend1color(G1, G2, map);
		int B = blend1color(B1, B2, map);
		
			// Reconstitution de l'info
		return ( (R&0x0ff)<<16) | ((G&0x0ff)<<8) | (B&0x0ff);
	}
}
