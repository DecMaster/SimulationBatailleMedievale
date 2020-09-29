package units;

import java.awt.Color;
import java.util.ArrayList;

import data.Constantes;
import data.Parole;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.util.Int2D;
import state.battleState;
import units.UnitsAgent.Priorites;
import units.UnitsAgent.TypeAttaque;
import visuel.UnitsPortrayals;

public class Healer extends UnitsAgent {
	
	// PROPRIETES
	private int nombreChargeSoin = Constantes.NB_CHARGE_SOINS_HEALER;
	
	// METHODES
	public Healer(int x, int y, battleState bS, String f) {
		
		position = new Int2D(x,y);
		state = bS;
		faction = f;
		distribuerStats();
		name = "Healer";
	}
	
	public Healer(battleState bS, String f) {
		state = bS;
		faction = f;
		typeAttaque = TypeAttaque.mage;
		distribuerStats();
		name = "Healer";
	}
	
	private void distribuerStats() {
		
		// DETERMINATION PV
		PV = Constantes.PV_HEALER;
		PVMax = Constantes.PV_HEALER;
		vitesse = Constantes.VITESSE_HEALER;
		degats = Constantes.DEGATS_HEALER;
		porteeInitiale = Constantes.PORTEE_HEALER;
		portee = porteeInitiale;
		
		// DETERMINATION PRIORITES
		priorites.add(Priorites.soigner);
		priorites.add(Priorites.chercherSoin);
		priorites.add(Priorites.attaquer);
		priorites.add(Priorites.getToCible);
		priorites.add(Priorites.errer);
	}
	
	@Override
	public void Soigner() {
		if(listeUnitesCiblees.size() != 0) {
			
			// SOIN DE L'UNITE
			// Si l'unité possède encore des charges de soin
			if(this.nombreChargeSoin > 0) {
				texteADire = "Heal!";
				nombreChargeSoin--;
				UnitsAgent unite = listeUnitesCiblees.get(0);
				unite.setPV(unite.getPVMax());
				listeUnitesCiblees.remove(0);
				unite.texteADire = "Thanks";
			}
			else {
				texteADire = "I can't";
				prioriteActuelle = Priorites.errer;
				listeDecisionPrise.add(Priorites.errer);
				listeUnitesCiblees.clear();
			}
		}
	}
	
	@Override
	public void Attaquer() {
		
		// APPEL GENERIQUE
		super.Attaquer();
	}
	
	@Override
	public void Entendre(Parole parole) {
		
		// APPEL GENERIQUE
		super.Entendre(parole);

		// SI APPEL AUX SOINS D'UNE UNITE ALLIE : Va l'aider
		if(parole.agentDemandeur.getFaction() == faction && parole.type.equals(Parole.TypeParole.demandeSoin)) {
			
			// Si l'unité possède encore des charges de soin
			if(this.nombreChargeSoin > 0) {
				
				// L'unité ajoute l'unité en difficulté dans sa liste d'unités à soigner
				prioriteActuelle = Priorites.soigner;
				target = parole.agentDemandeur.getPosition();
				if (!listeUnitesCiblees.contains(parole.agentDemandeur)) {
					listeUnitesCiblees.add(parole.agentDemandeur);
				}
				
				// Affichage d'un "bien reçu !"
				texteADire = "Roger";
			}
			else {
				// Affichage d'un "Je ne peux pas !"
				texteADire = "I can't";
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
			port = state.GUI.redPort.getHealer(orient);
		}
		else {
			port = state.GUI.bluePort.getHealer(orient);
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
	public void Ordre () {
		// Do nothing
	}
}
