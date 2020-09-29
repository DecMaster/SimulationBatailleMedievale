package units;

import java.awt.Color;

import data.Constantes;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.util.Int2D;
import state.battleState;
import units.UnitsAgent.Priorites;
import units.UnitsAgent.TypeAttaque;
import visuel.UnitsPortrayals;

public class Knight extends UnitsAgent {
	
	// METHODES
	public Knight(int x, int y, battleState bS, String f) {
		
		position = new Int2D(x,y);
		state = bS;
		faction = f;
		distribuerStats();
		name = "Knight";
	}
	
	public Knight(battleState bS, String f) {
		state = bS;
		faction = f;
		typeAttaque = TypeAttaque.epeiste;
		distribuerStats();
		name = "Knight";
	}
	
	private void distribuerStats() {
		
		// DETERMINATION PV
		PV = Constantes.PV_KNIGHT;
		PVMax = Constantes.PV_KNIGHT;
		vitesse = Constantes.VITESSE_KNIGHT;
		degats = Constantes.DEGATS_KNIGHT;
		porteeInitiale = Constantes.PORTEE_KNIGHT;
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
		
		// APPEL GENERIQUE
		super.Attaquer();
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
			port = state.GUI.redPort.getKnight(orient);
		}
		else {
			port = state.GUI.bluePort.getKnight(orient);
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
