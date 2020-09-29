package units;

import java.awt.Color;
import java.util.Random;

import data.Constantes;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.util.Bag;
import sim.util.Int2D;
import state.battleState;
import units.UnitsAgent.Priorites;
import visuel.UnitsPortrayals;

public class Archer extends UnitsAgent {
	
	// PROPRIETES
	private int nbFleches;
	private boolean isReloading = false;
	
	// METHODES
	public Archer(int x, int y, battleState bS, String f) {
		
		position = new Int2D(x,y);
		state = bS;
		faction = f;
		distribuerStats();
		name = "Archer";
	}
	
	public Archer(battleState bS, String f) {
		state = bS;
		faction = f;
		typeAttaque = TypeAttaque.archer;
		distribuerStats();
		name = "Archer";
	}
	
	private void distribuerStats() {
		
		// DETERMINATION PV
		PV = Constantes.PV_ARCHER;
		PVMax = Constantes.PV_ARCHER;
		vitesse = Constantes.VITESSE_ARCHER;
		degats = Constantes.DEGATS_ARCHER;
		porteeInitiale = Constantes.PORTEE_ARCHER;
		nbFleches = Constantes.NB_FLECHES_MAX;
		portee = porteeInitiale;
		
		// DETERMINATION PRIORITES
		priorites.add(Priorites.chercherSoin);
		priorites.add(Priorites.attaquer);
		priorites.add(Priorites.getToCible);
		priorites.add(Priorites.rejoindreCapitaine);
		priorites.add(Priorites.errer);
	}
	
	@Override
	public void Attaquer() {
		if (isReloading) {
			int randomNum = (new Random()).nextInt(6) + 1; // reloads between 1 and 5 arrows in one turn
			nbFleches += randomNum;
			texteADire = "Reloading!";
			if (nbFleches >= Constantes.NB_FLECHES_MAX)
				isReloading = false;
			return;
		}
		
		if (nbFleches > 0) {
			// APPEL GENERIQUE
			texteADire = "Shooting!";
			super.Attaquer();
			nbFleches--;
		} else {
			isReloading = true;
		}
	}

	@Override
	public void SetupPortrayals() {
		
		UnitsPortrayals.orientation orient = UnitsPortrayals.orientation.right;
		if(target != null) {
			if(target.x < position.x) {
				orient = UnitsPortrayals.orientation.left;
			}
		}
		
		// RECUPERATION DU BON PORTRAYAL
		if(faction.equals("red")) {
			port = state.GUI.redPort.getArcher(orient);
		}
		else {
			port = state.GUI.bluePort.getArcher(orient);
		}
		
		// AJOUT TEXTE A DIRE
		if(texteADire != null) {
			LabelledPortrayal2D label = new LabelledPortrayal2D((SimplePortrayal2D) port, 0, texteADire, Color.white, false);
			label.font = Constantes.police;
			label.offsety = Constantes.offsety;
			label.offsetx = Constantes.offsetx;
			port = label;
		}
		
		// MISE A JOUR VISUELLE
		state.GUI.yardPortrayal.setPortrayalForObject(this, port);
	}

	@Override
	public void Soigner() {
		// Do nothing
	}
	
	@Override
	public void Ordre () {
		// Do nothing
	}
}
