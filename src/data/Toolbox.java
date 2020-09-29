package data;

import sim.util.Int2D;
import units.UnitsAgent;

public class Toolbox {

	// Cette classe stocke de façon statiques des données qui pourront être accessibles par n'importe quel agent
	
	// INFOS CAPITAINES
	public static Int2D positionCapitainRouge;
	public static Int2D positionCapitainBleu;
	public static boolean capitaineRougeEnVie = true;
	public static boolean capitaineBleuEnVie = true;
	
	public static void MontrerStats(UnitsAgent ua) {
		System.out.println("-------- STATISTIQUES UNITES --------");
		System.out.println(" " + ua.getName() + " de l'armée " + ua.getFaction());
		System.out.println("-- 1 : Decisions prises --");
		System.out.println(" " +ua.listeDecisionPrise);
		System.out.println("-- 2 : Performance --");
		System.out.println(" Degats infliges : " + ua.degatInflige);
		System.out.println(" Degats recus : " + ua.degatRecu);
		System.out.println(" Cases parcourues : " + ua.nbCasesParcourues);
		System.out.println("-------------------------------------");
	}
}
