package units;

import java.awt.Color;

import data.Constantes;
import data.Parole;
import data.Toolbox;
import sim.engine.SimState;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.util.Bag;
import sim.util.Int2D;
import state.battleState;
import units.UnitsAgent.Priorites;
import units.UnitsAgent.TypeAttaque;
import visuel.UnitsPortrayals;

public class Captain extends UnitsAgent {
	
	// METHODES
	public Captain(int x, int y, battleState bS, String f) {
		
		position = new Int2D(x,y);
		state = bS;
		faction = f;
		distribuerStats();
		name = "Captain";
	}
	
	public Captain(battleState bS, String f) {
		state = bS;
		faction = f;
		typeAttaque = TypeAttaque.epeiste;
		distribuerStats();
		name = "Captain";
	}
	
	@Override
	protected void mourir() {
		super.mourir();
		if (this.faction == "blue") {
			Toolbox.capitaineBleuEnVie = false;
		} else {
				Toolbox.capitaineRougeEnVie = false;
		}
	}

	private void distribuerStats() {
		
		// DETERMINATION PV
		PV = Constantes.PV_CAPTAIN;
		PVMax = Constantes.PV_CAPTAIN;
		vitesse = Constantes.VITESSE_CAPTAIN;
		degats = Constantes.DEGATS_CAPTAIN;
		porteeInitiale = Constantes.PORTEE_CAPTAIN;
		perception = Constantes.GRID_SIZE_X;
		portee = porteeInitiale;
		
		// DETERMINATION PRIORITES
		priorites.add(Priorites.donnerOrdre);
		priorites.add(Priorites.chercherSoin);
		priorites.add(Priorites.attaquer);
		priorites.add(Priorites.errer);
		
		
	}
	
	@Override
	public void step(SimState arg0) {
		
		// APPEL STEP GENERIQUE
		super.step(arg0);
		
		// ACTIONS SPECIFIQUES
		// 1 - Mise a jour des infos du capitaine dans la Toolbox
		if(faction == "blue") {
			Toolbox.positionCapitainBleu = position;
			if(PV > 0) {
				Toolbox.capitaineBleuEnVie = true;
			}
			else {
				Toolbox.capitaineBleuEnVie = false;
			}
		}
		else if(faction == "red") {
			Toolbox.positionCapitainRouge = position;
			if(PV > 0) {
				Toolbox.capitaineRougeEnVie = true;
			}
			else {
				Toolbox.capitaineRougeEnVie = false;
			}
		}
		
	}
	
	
	@Override
	public void Ordre() {
		if(listeUnitesCiblees.size() != 0) {
			UnitsAgent unite = listeUnitesCiblees.get(0);
			texteADire = "Order!";
			unite.setOrdre(listeEnnemis.get(0), listeUnitesCiblees.get(0));
			listeUnitesCiblees.remove(0);
		}
	};
	
	@Override
	public void Attaquer() {
		
		// APPEL GENERIQUE
		super.Attaquer();
	}
	

	
	
	@Override
	public void Entendre(Parole parole) {
		
		// APPEL GENERIQUE
		super.Entendre(parole);
		
		// SI APPEL AUX ORDRES D'UNE UNITE ALLIE : Va lui donner un ordre
		if(parole.agentDemandeur.getFaction() == faction && parole.type.equals(Parole.TypeParole.DemandeOrdre)) {
				
				// L'unité ajoute l'unité en difficulté dans sa liste d'unités à soigner
				prioriteActuelle = Priorites.donnerOrdre;
				target = parole.agentDemandeur.getPosition();
				if (!listeUnitesCiblees.contains(parole.agentDemandeur)) {
					listeUnitesCiblees.add(parole.agentDemandeur);
				}
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
			port = state.GUI.redPort.getCaptain(orient);
		}
		else {
			port = state.GUI.bluePort.getCaptain(orient);
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
}
