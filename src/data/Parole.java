package data;

import units.UnitsAgent;

// Classe contenant les informations de ce qui est dis par un personnage

public class Parole {

	// ENUM
	public enum TypeParole{
		DonneOrdre,
		DemandeOrdre,
		information,
		demandeSoin,
		confirmation,
		remerciement,
		impossible
	}
	
	// PROPRIETES
	public UnitsAgent agentDemandeur;
	public TypeParole type;
	public String texte;
	
	// CONSTRUCTEUR
	public Parole() {}
	public Parole(UnitsAgent a, TypeParole t){
		
		agentDemandeur = a;
		type = t;
		
		// DECISION DU TEXTE SELON LE TYPE
		switch(t) {
			case DonneOrdre:
				texte = "Order!";
				break;
			case DemandeOrdre:
				texte = "Need order!";
				break;
			case information:
				texte = "Info";
				break;
			case demandeSoin:
				texte = "Help";
				break;
			case confirmation:
				texte = "Roger";
				break;
			case remerciement:
				texte = "Thanks";
				break;
			case impossible:
				texte = "I can't";
				break;
			default:
				break;
		}
	}
}
