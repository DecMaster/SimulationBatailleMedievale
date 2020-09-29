package units;
import java.awt.Color;
import java.awt.Font;

import data.Constantes;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.util.Int2D;
import state.battleState;
import units.UnitsAgent.Priorites;
import units.UnitsAgent.TypeAttaque;
import visuel.UnitsPortrayals;

public class SwordMan extends UnitsAgent {
	
	// METHODES
	public SwordMan(int x, int y, battleState bS, String f) {
		
		position = new Int2D(x,y);
		state = bS;
		faction = f;
		distribuerStats();
		name = "Swordman";
	}
	
	public SwordMan(battleState bS, String f) {
		state = bS;
		faction = f;
		typeAttaque = TypeAttaque.epeiste;
		distribuerStats();
		name = "Swordman";
	}
	private void distribuerStats() {
		
		// DETERMINATION PV
		PV = Constantes.PV_SWORDMAN;
		PVMax = Constantes.PV_SWORDMAN;
		vitesse = Constantes.VITESSE_SWORDMAN;
		degats = Constantes.DEGATS_SWORDMAN;
		porteeInitiale = Constantes.PORTEE_SWORDMAN;
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
			port = state.GUI.redPort.getSwordMan(orient);
		}
		else {
			port = state.GUI.bluePort.getSwordMan(orient);
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
