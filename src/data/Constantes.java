package data;

import java.awt.Font;

import data.Arbitre.ModeDeJeu;

public class Constantes {
	// BATTLEFIELD : La taille de la grille est mise à jour par le terrain
	public static int GRID_SIZE_X = 0;
	public static int GRID_SIZE_Y = 0;
	public static int FRAME_SIZE_X = 800;	// Largeur de la fenetre
	public static Arbitre.ModeDeJeu MODE_DE_JEU = ModeDeJeu.KILLTHEMALL;

	// SOLDATS PAR ARMEES
	// ajouter le nom de l'unite en majuscule avec une virgule, le nombre de chaque est facteur des nombres définies plus bas de la forme NB_X (ex : NB_SWORDMAN)
	// Pour le moment, chaque armee a forcement un ou 0 capitaine. (voir constante NB_CAPTAIN plus bas)
	// ex : Pour une unité avec seulement des epeiste et medecins : "SWORDMAN,HEALER" et pour en avoir encore plus "SWORDMAN,SWORDMAN,HEALER,HEALER".
	public static String REDARMYCONST = "SWORDMAN,ARCHER,HEALER,KNIGHT,MAGE";
	public static String BLUEARMYCONST = "SWORDMAN,ARCHER,HEALER,KNIGHT,MAGE";

	// TERRAIN
	public static boolean MONTRER_SANG = true;		// Activer / désactiver le sang
	public static int VITESSE_ABSORPTION_SANG = 8;	// Temps avant que le terrain commence à retrouver son apparence normale
	public static int CHARGE_MAISON = 1;			// Nombre de fois où une maison peut être pillée	
	public static int TERRAIN_NB = 0; 				// Choix du terrain, mettre 0 : aleatoire
	public static int TERRAIN_MAX = 6; 				// à modifier si ajout de terrain dans les ressources
	public static int MALUS_PORTEE_FORET = 2;		// Réduit la portée des unités en forêt

	// MESSAGES
	public static int TEMPS_AFFICHAGE_MESSAGES = 15;				// Nombre de steps ou le message reste affiche
	public static Font police = new Font("SansSerif", 0, 10); 		// Police utilisee pour l'affichage des messages
	public static double offsetx = -15;								// Placement X du message
	public static double offsety = 17;								// Placement Y du message

	// UNITES
	public static int VITESSE_GLOBALE = 3;
	public static float SEUIL_PV_FAIBLE = 0.2f;
	public static int PERCEPTION_GENERALE = 5;

	// CAPITAINE
	public static int NB_CAPTAIN = 1;
	public static int PV_CAPTAIN = 400;
	public static int VITESSE_CAPTAIN = 1;
	public static int DEGATS_CAPTAIN = 5;
	public static int PORTEE_CAPTAIN = 2;
	
	// EPEISTE
	public static int NB_SWORDMAN = 10;
	public static int PV_SWORDMAN = 150;
	public static int VITESSE_SWORDMAN = 2;
	public static int DEGATS_SWORDMAN = 4;
	public static int PORTEE_SWORDMAN = 1;

	// CHEVALIER
	public static int NB_KNIGHT = 5;
	public static int PV_KNIGHT = 200;
	public static int VITESSE_KNIGHT = 3;
	public static int DEGATS_KNIGHT = 10;
	public static int PORTEE_KNIGHT = 1;

	// ARCHER
	public static int NB_ARCHER = 10;
	public static int PV_ARCHER = 100;
	public static int VITESSE_ARCHER = 2;
	public static int NB_FLECHES_MAX = 20;
	public static int DEGATS_ARCHER = 5;
	public static int PORTEE_ARCHER = 5;

	// MAGE
	public static int NB_MAGE = 4;
	public static int PV_MAGE = 100;
	public static int VITESSE_MAGE = 2;
	public static int NB_MAGIE_MAX_MAGE = 20;
	public static int DEGATS_MAGE = 12;
	public static int PORTEE_MAGE = 5;

	// HEALER
	public static int NB_HEALER = 4;
	public static int PV_HEALER = 100;
	public static int NB_CHARGE_SOINS_HEALER = 10;
	public static int VITESSE_HEALER = 1;
	public static int NB_MAGIE_MAX_HEALER = 20;
	public static int DEGATS_HEALER = 3;
	public static int PORTEE_HEALER = 2;

}
