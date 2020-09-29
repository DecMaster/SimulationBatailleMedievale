package grounds;

import java.awt.image.BufferedImage;

import data.Constantes;
import state.battleState;

public class TileHouse extends Tile {
	
	public boolean ASec = false;
	private int DureeDeVie = Constantes.CHARGE_MAISON;
	
	public TileHouse(battleState battle) {
		super(battle);
	}
	
	public void Utiliser() {
		DureeDeVie--;
		
		// Si maison à sec, on la retransforme en case normale
		if(DureeDeVie <= 0) {
			ASec = true;
			this.SetupPortrayals();
		}
	}

	@Override
	public BufferedImage getImage() {
		if(ASec) {
			return state.GUI.groundPort.getPlain();
		}
		else {
			return state.GUI.groundPort.getHouse();
		}
	}
}
