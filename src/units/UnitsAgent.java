package units;

import java.util.ArrayList;
import java.util.Random;

import data.Constantes;
import data.Parole;
import data.Terrain;
import data.Toolbox;
import grounds.TileHouse;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.portrayal.Portrayal;
import sim.util.Bag;
import sim.util.Int2D;
import state.battleState;
import units.UnitsAgent.Priorites;

public abstract class UnitsAgent implements Steppable {
	
	// ENUM
	public enum Priorites{
		soigner,			// Seulement Healer
		chercherSoin,
		attaquer,
		donnerOrdre,		// Seulement Captain
		rejoindreCapitaine,
		getToCible,
		errer,
	}
	
	public enum TypeAttaque { // Enum pour le triangle des armes
		epeiste,
		archer,
		mage,
	}
	
	// REFERENCES
	public battleState state;
	public Stoppable stoppable;
	public Portrayal port;
	
	// CARACTERISTIQUES FIXES
	protected ArrayList<Priorites> priorites = new ArrayList<Priorites>();	// Contient les priorités de l'agent
	protected Priorites	prioriteActuelle;
	protected String faction;												// "red" ou "blue"
	protected int vitesse;
	protected int perception = Constantes.PERCEPTION_GENERALE;
	protected int armure;
	protected int degats;
	protected int porteeInitiale = -1;
	protected int PVMax;
	protected String name;
	protected boolean eviteEnnemis;										// Avec booleen, l'unite evitera les ennemis en prennant les cases les moins proches de ces derniers
	protected boolean debutBataille = false;
	protected boolean destinationInitiale = false;
	protected boolean attenteHeal = false;
	protected TypeAttaque typeAttaque;
	
	// CARACTERISTIQUES DYNAMIQUES
	public int PV;
	protected Int2D position;
	protected Int2D target = null;
	protected int portee = -1;
	protected ArrayList<UnitsAgent> listeUnitesCiblees = new ArrayList<UnitsAgent>();
	protected Terrain.Type terrainActuel;
	protected int modifVisibilite; 	// Pour voir plus loin ce qui est plus visible
	protected int modifPerception;	// 
	protected String texteADire = null;

	// ENVIRONNEMENT
	protected ArrayList<UnitsAgent> listeUnitesAPortees = new ArrayList<UnitsAgent>(); 		// Garde en memoire les unites proches (reinit a chaque perception)
	protected ArrayList<Int2D> listeEnnemis = new ArrayList<Int2D>(); 						// Garde en memoire la position d'ennemis detectes
	protected ArrayList<Int2D> listeMaisons = new ArrayList<Int2D>(); 						// Garde en memoire la position de maisons (soins) detectes
	protected ArrayList<Healer> listeMedecinsProches = new ArrayList<Healer>(); 			// Garde en memoire la position des medecins proches (reinit a chaque perception)
	
	// ATTRIBUTS
	protected int NbTourAttenteAvantDeplacement = Constantes.VITESSE_GLOBALE;
	protected int NbTourAvantDisparitionMessage = Constantes.TEMPS_AFFICHAGE_MESSAGES;
	
	// STATISTIQUES POST-MORTEM
	public boolean montrerStats = true;
	public ArrayList<Priorites> listeDecisionPrise = new ArrayList<Priorites>();
	public int degatInflige = 0;
	public int degatRecu = 0;
	public int nbCasesParcourues = 0;
	
	// RECHERCHE SOIN
	private Healer MedecinARejoindre = null;
	private Int2D MaisonARejoindre = null;
	private TileHouse MaisonARejoindre_obj = null;

	// ACCESSEURS
	public Priorites getPrioriteActuelle() {return prioriteActuelle;}
	public int getPVMax() {return PVMax;}
	public int getPV() {return PV;}
	public void setPV(int value) { PV = value ;}
	public String getFaction() {return faction;}
	public Int2D getPosition() {return position;}
	public void setPosition(Int2D position) {this.position = position;}
	public int getVitesse() {return vitesse;}
	public int getTourAvantDep() {return NbTourAttenteAvantDeplacement;}
	public Int2D getTarget() {return target;}
	public Terrain.Type getTerrain() {return terrainActuel;}
	public ArrayList<Int2D> getListeEnnemis() {return listeEnnemis;}
	public ArrayList<Int2D> getListeMaisons() {return listeMaisons;}
	public ArrayList<UnitsAgent> getListeUnitesCiblees() {return listeUnitesCiblees;}
	public int getMedecinsCount() {return listeMedecinsProches.size();}
	public int getModifVisibilite() {return modifVisibilite;}
	public int getModifPerception() {return modifPerception;}
	public String getName() { return name;}
	public void setName(String name) { this.name = name; }
	public TypeAttaque getTypeAttaque() { return typeAttaque; }
	public void setTypeAttaque(TypeAttaque ta) { typeAttaque = ta; }
	
	// Methode effectuee a chaque tour
	@Override
	public void step(SimState arg0) {
		// -- Si c'est fini c'est fini. --
		if (state.batailleTerminee)
			return;
		
		// -- CODE PRE ACTION --
		// PERCEPTION : Est effectuee a chaque tour
		// Pour l'instant on met du +5 au cas ou des ennemis plus loins aient une modif de visibilite les rendant plus visibles.
		percevoir(perception + 5);
		
		// Decompte de l'affichage du message en cours
		// Permet de faire disparaître un message après un certains nombre de tour
		if(texteADire != null) {
			NbTourAvantDisparitionMessage--;
			if(NbTourAvantDisparitionMessage <= 0) {
				texteADire = null;
				NbTourAvantDisparitionMessage = Constantes.TEMPS_AFFICHAGE_MESSAGES;
			}
		}
		
		// DESTINATION INITIALE : Avant que la bataille commence
		if(!destinationInitiale) {
			destinationInitiale = true;
			target = new Int2D(position.x, Constantes.GRID_SIZE_Y - position.y);
		}
		
		// -- CODE ACTION --
		// SI AUCUNE PRIORITES : Deplacement aleatoire, comportement par defaut
		if(priorites.size() == 0) {
			if(position.equals(target) || target == null) {
				int rand1 = new Random().nextInt(Constantes.GRID_SIZE_X);
				int rand2 = new Random().nextInt(Constantes.GRID_SIZE_Y);
				target = new Int2D(rand1, rand2);
			}
			seDeplacer();
		}
		else {
			
			// ATTENTE HEAL : Permet d'attendre sur une case (ex: Maison ou proche d'un medecin) en attendant d'être complètement rétabli
			if(attenteHeal) {
				if(getPV() < getPVMax()) {
					if(getPVMax() - 20 < getPV()) {
						setPV(getPVMax());
					}
					else setPV(getPV() + 20);
				}
				if(getPV() >= getPVMax()) {
					attenteHeal = false;
					texteADire ="Sacked!";
					if(MaisonARejoindre_obj != null) {
						MaisonARejoindre_obj.Utiliser();
					}
				}
			}
			else {
				
				// PRIORITES : On parcourt les priorites de l'unite selon leur ordre dans la liste (ordre de priorites)
				// Des qu'une action est decidee, on l'effectue et on break la boucle
				boolean decisionPrise = false;
				for(Priorites prio : priorites) {
					switch(prio) {
						
						// 1 - SOIGNER
						case soigner :
							
							// Verification que l'unite est un medecin et a des unites a soigner
							if(this.getClass() == Healer.class && listeUnitesCiblees.size() != 0) {
								
								// Decision prise
								decisionPrise = true;
								listeDecisionPrise.add(Priorites.soigner);
								
								// Si allie a portee : soin
								if(target != null && aDistanceDe(target) <= portee) {
									Soigner();
								}
								
								// Si alliee pas a portee : se deplace vers lui
								else {
									target = listeUnitesCiblees.get(0).getPosition();	// L'unitï¿½ ï¿½ soigner est toujours la premiï¿½re ï¿½ avoir demander du soin
									seDeplacer();
								}
							}
							
							break;
					
						// 2 - CHERCHER DES SOINS
						case chercherSoin :
							
							// Si PV sont faibles
							if(PV <= PVMax * Constantes.SEUIL_PV_FAIBLE && (listeMedecinsProches.size() != 0 || listeMaisons.size() != 0)) {
								
								// DETERMINATION SOURCE DE SOIN LA PLUS PROCHE
								if(prioriteActuelle != Priorites.chercherSoin) {
									
									// REINIT
									MedecinARejoindre = null;
									MaisonARejoindre = null;
										
									// MAJ PRIORITES
									decisionPrise = true;
									prioriteActuelle = Priorites.chercherSoin;
									listeDecisionPrise.add(prioriteActuelle);
									
									// Determination de la cible
									// Si un medecin est proche : Appel et le rejoins
									if(listeMedecinsProches.size() != 0) {
										
										MedecinARejoindre = listeMedecinsProches.get(0);
										// Appeller a l'aide
										Parler(Parole.TypeParole.demandeSoin);
										// Target = ce medecin
										target = MedecinARejoindre.getPosition();
									}
									else {
										// Sinon, cherche une maison
										if(listeMaisons.size() != 0) {
											
											// Determination de la maison le plus proche
											texteADire ="House!";
											MaisonARejoindre = determinerPlusProche(listeMaisons);
										}
									}
								}
								
								// Mise a jour cible + deplacement
								if(MedecinARejoindre != null) {
									decisionPrise = true;
									target = MedecinARejoindre.getPosition();
									seDeplacer();
								}
								else {
									if(MaisonARejoindre != null) {
										
										decisionPrise = true;
										target = MaisonARejoindre;
										
										// Si pas a distance de perception, s'y deplace
										if(aDistanceDe(target) > perception) {
											seDeplacer();
										}
										else {
											// Si a distance de perception et existe encore, s'y deplace
											if(state.getTile(target).getClass() == TileHouse.class) {
												
												MaisonARejoindre_obj = (TileHouse) state.getTile(target);
												
												// Si n'existe plus, autre maison (prochain step)
												if(MaisonARejoindre_obj.ASec) {
													listeMaisons.remove(target);
													if(listeMaisons.size() != 0) {
														texteADire ="Another house!";
														MaisonARejoindre = determinerPlusProche(listeMaisons);
													}
												}
												else {
													// Si maison atteinte, on se soigne progressivement
													if(getPosition().equals(MaisonARejoindre)) {
														attenteHeal = true;
													}
													// Sinon on s'y dï¿½place
													else {
														seDeplacer();
													}
												}
											}
										}
									}
								}

							}
							
							break;
							
						// 3 - DONNER UN ORDRE
						case donnerOrdre :
							// Verification que l'unite est un Capitaine et a des unites a qui donner des ordres
							if(this.getClass() == Captain.class && listeUnitesCiblees.size() != 0) {
							
								// Decision prise
								decisionPrise = true;
								
								// Si allie a portee de commandement : donne ordre
								if(target != null && aDistanceDe(target) <= portee) {
									Ordre();
								}
								
								else {
									target = listeUnitesCiblees.get(0).getPosition();
									seDeplacer();
								}
							}
							break;
							
						// 4 - SE RAPPROCHER D'UNE POSITION
						case getToCible :
							if (target != null) {
					
								if (listeUnitesCiblees.size() != 0 && listeEnnemis.size() == 0) {
									decisionPrise = true;
									seDeplacer();
									
								}
								else {
									listeUnitesCiblees.clear();
								}
							}
							else {
								listeUnitesCiblees.clear();
							}
							break;
							
						// 5 - ATTAQUER
						case attaquer :
							
							// Si des ennemis connus
							if(listeEnnemis.size() != 0) {
								listeUnitesCiblees.clear();
								
								// Debut de la bataille
								debutBataille = true;
								
								// Decision prise
								decisionPrise = true;
								
								// DETERMINATION ENNEMI LE PLUS PROCHE
								if(prioriteActuelle != Priorites.attaquer) {
									prioriteActuelle = Priorites.attaquer;
									listeDecisionPrise.add(prioriteActuelle);
								}
								
								// DETERMINATION TARGET
								//target = listeEnnemis.get(0);
								target = DeterminerTarget();
															
								// SI ENNEMI A PORTEE : ATTAQUE
								if(aDistanceDe(target) <= portee) {
									Attaquer();
								}
								// SINON : DEPLACEMENT VERS CIBLE
								else {
									seDeplacer();
								}
								
							}
							else {
								// Si la bataille n'a pas encore commencÃ©
								if(this.debutBataille == false) {
									decisionPrise = true;
									seDeplacer();
								}
								else {
									// Si aucun ennemi connu : Se dÃ©place vers le capitaine pour obtenir une cible
									if(faction == "blue" && Toolbox.capitaineBleuEnVie && listeUnitesCiblees.size() == 0) {
										prioriteActuelle = Priorites.rejoindreCapitaine;
										listeDecisionPrise.add(prioriteActuelle);
									}
									else {
										if(faction == "red" && Toolbox.capitaineRougeEnVie && listeUnitesCiblees.size() == 0) {
											prioriteActuelle = Priorites.rejoindreCapitaine;
											listeDecisionPrise.add(prioriteActuelle);
										}
										
										// Si le capitaine est mort, ils ï¿½rent sur la map de faï¿½on alï¿½atoire
										else {
											if(listeUnitesCiblees.size() == 0) {
												prioriteActuelle = Priorites.errer;
												listeDecisionPrise.add(prioriteActuelle);
											}
										}
									}
								}
							}
							

						
							break;
						
						// 6 - REJOINDRE SON CAPITAINE
						case rejoindreCapitaine :
							
							// Si le capitaine est en vie
							boolean capitainOK = false;
							Int2D posCapitain;
							if(faction == "red") {
								capitainOK = Toolbox.capitaineRougeEnVie;
								posCapitain = Toolbox.positionCapitainRouge;
							}
							else {
								capitainOK = Toolbox.capitaineBleuEnVie;
								posCapitain = Toolbox.positionCapitainBleu;
							}
							
							if(capitainOK && Constantes.NB_CAPTAIN > 0) {
								
								decisionPrise = true;
								if(prioriteActuelle != Priorites.rejoindreCapitaine) {
									prioriteActuelle = Priorites.rejoindreCapitaine;
									listeDecisionPrise.add(prioriteActuelle);
								}
								
								Parler(Parole.TypeParole.DemandeOrdre);
								target = posCapitain;
								seDeplacer();
							}
							
							break;

						// ACTION PAR DEFAUT : Deplacement vers une case aleatoire
						default :
							
							decisionPrise = true;
							if(prioriteActuelle != Priorites.errer) {
								prioriteActuelle = Priorites.errer;
								listeDecisionPrise.add(prioriteActuelle);
							}
							
							if(position.equals(target) || target == null) {
								int rand1 = new Random().nextInt(Constantes.GRID_SIZE_X);
								int rand2 = new Random().nextInt(Constantes.GRID_SIZE_Y);
								target = new Int2D(rand1, rand2);
							}
							seDeplacer();
							break;
					}
					
					// Si decision prise : On break la boucle, une priorité a été réalisée (pas besoin d'étudier la suivante)
					if(decisionPrise) {
						break;
					}
				}
			}
		}
		
		// -- CODE POST ACTION --
		SetupPortrayals(); 		// Mise a jour visuelle
	}
	
	public void Attaquer() {
		Bag bag = state.yard.getObjectsAtLocation(target.x, target.y);
		if (bag != null && bag.numObjs != 0) {
			for(Object obj : bag.objs) {
				if (obj != null)
				if (obj != null && UnitsAgent.class.isAssignableFrom(obj.getClass())) {
					UnitsAgent ua = (UnitsAgent)obj;
					int degatTotaux = ua.recevoirDegats(degats, typeAttaque);
					degatInflige += degatTotaux;
				}
			}
		}
	};
	
	public int recevoirDegats(int degats, TypeAttaque ta) {
		state.getTile(this.position).faireCoulerLeSang();
		
		// TRIANGLE DES ARMES
		float multiplicateursDegats = 1.0f;
		if (
				(ta == TypeAttaque.mage && typeAttaque == TypeAttaque.epeiste) ||
				(ta == TypeAttaque.epeiste && typeAttaque == TypeAttaque.archer) ||
				(ta == TypeAttaque.archer && typeAttaque == TypeAttaque.mage)
				)
			multiplicateursDegats = 2.0f;
		else if (
				(ta == TypeAttaque.mage && typeAttaque == TypeAttaque.archer) ||
				(ta == TypeAttaque.epeiste && typeAttaque == TypeAttaque.mage) ||
				(ta == TypeAttaque.archer && typeAttaque == TypeAttaque.epeiste)
				)
			multiplicateursDegats = 0.5f;
		
		int degatsTotaux = (int) ( degats * multiplicateursDegats);
		degatRecu += degatsTotaux;
		PV -= degatsTotaux;
		
		if (PV <= 0)
			mourir();
		
		return degatsTotaux;
	}
	
	// --- METHODES UTILES ---
	
	// Calcul de distance
	protected int aDistanceDe(Int2D pos) {
		return Math.max(
			Math.abs(this.position.x - pos.x),
			Math.abs(this.position.y - pos.y)
		);
	}
	
	// Calcul de la position la plus proche parmi une liste de Int2D
	protected Int2D determinerPlusProche(ArrayList<Int2D> listePos) {
		if(listePos.size() != 0) {
			Int2D pos = listePos.get(0);
			int distance = aDistanceDe(pos);
			for(Int2D p : listePos) {
				int newDistance = aDistanceDe(p);
				if(newDistance < distance) {
					pos = p;
					distance = newDistance;
				}
			}
			return pos;
		}
		else {
			return null;
		}
	}
	
	// --- METHODES UNITES ---
	
	// Mort de l'unité
	protected void mourir() {
		
		// REMOVE UNIT
		System.out.println("Mort : " + this);
		state.decreasePool(getFaction());
		stoppable.stop();
		state.yard.remove(this);
		
		// DISPLAY STATS IF SELECTED
		if(montrerStats) {
			Toolbox.MontrerStats(this);
		}
		
	}
	
	// --- METHODES COMMUNICATION ---
	
	// PARLER : Envoie un message (Parole) aux unités proches
	protected void Parler(Parole.TypeParole type) {
		
		// CREATION OBJET PAROLE
		Parole parole = new Parole(this, type);
		
		// AFFICHAGE TEXTE
		texteADire = parole.texte;
		
		// TRANSMISSION DES UNITES A PORTEES
		for(UnitsAgent ua : listeUnitesAPortees) {
			ua.Entendre(parole);
		}
	}
	
	// ENTENDRE : Traite un message envoyé par une unité proche
	protected void Entendre(Parole parole) {
		// Comportement général, si besoin
		// La méthode est pour le moment hérité puis définie dans les classes filles traitant des messages (Healer, Captain)
	}
	
	// --- METHODES PERCEPTION ---
	
	// Tient compte des modificateurs pour savoir si un agent peut en voir un autre
	protected boolean peutVoir(UnitsAgent other) {
		int distance = this.aDistanceDe(other.getPosition());
		if (distance + this.modifPerception - other.getModifVisibilite() < this.perception)
			return true;
		return false;
	}
	
	//Methode permettant a l'unite de voir autour de lui
	protected void percevoir(int distanceDePerception) {
		
		// VIDAGE LISTES (Celles mises a jours a chaque perception)
		listeEnnemis.clear();
		listeUnitesAPortees.clear();
		listeMedecinsProches.clear();
		
		for (int i=position.x-distanceDePerception; i < position.x+distanceDePerception; i++) {
			for (int j=position.y-distanceDePerception; j < position.y+distanceDePerception; j++) {
				
				// bag contient les objets presents sur la case de la grille
				Bag bag = state.yard.getObjectsAtLocation(i, j);
				boolean ennemiDetecte = false;
				if (bag != null && bag.numObjs != 0) {
					for (Object obj : bag.objs) {
						
						// DETECTION AUTRE UNITES
						if (obj != null) {
							
							if(obj.getClass().getSuperclass() == UnitsAgent.class) {
								UnitsAgent ua = (UnitsAgent) obj;
								
								// Remplissage listeUnitesAPortees
								listeUnitesAPortees.add(ua);
								
								// Remplissage ListeEnnemis
								if (ua.getFaction() != this.faction) {
									listeEnnemis.add(ua.getPosition());
									ennemiDetecte = true;
								}
								
								// Remplissage listeMedecinsProches
								if(ua.getFaction() == this.faction && obj.getClass().isAssignableFrom(Healer.class)) {
									listeMedecinsProches.add((Healer) obj);
								}
							}
							else {
								
								// Remplissage ListeMaison
								if(obj.getClass() == TileHouse.class) {
									Int2D pos = new Int2D(i,j);
									if(!listeMaisons.contains(pos)) {
										listeMaisons.add(new Int2D(i,j));
									}
								}
							}
						}
						
					}
				}
				
				//Si aucun ennemi n'est detecte a la position (i, j) et que listeEnnemi contient un ennemi a la position (i, j), le retirer
				if (!ennemiDetecte) {
					Int2D pos = new Int2D(i,j);
					if(listeEnnemis.contains(pos)) {
						listeEnnemis.remove(pos);
					}
				}
			}
		}
	}
	
	// --- METHODES DEPLACEMENTS ---
	
	protected void seDeplacer() {
		
		// SECURITE : Aucun deplacement si aucune target
		if(target == null) {
			int rand1 = new Random().nextInt(Constantes.GRID_SIZE_X);
			int rand2 = new Random().nextInt(Constantes.GRID_SIZE_Y);
			target = new Int2D(rand1, rand2);
		}
		
		// DECREMENTATION TOUR ATTENTE
		NbTourAttenteAvantDeplacement -= vitesse;
		
		// DEPLACEMENT POSSIBLE
		if(NbTourAttenteAvantDeplacement <= 0) {
			boolean deplacementPossible = false;
			Int2D prochainePosition = position;
			// On determine une position ou l'unie peut se deplacer
			int tentatives = 0;
			while(!deplacementPossible && tentatives < 4) {
				
				// Determination prochaine position
				prochainePosition = getProchainePosition();
				tentatives++;
				
				// Test si l'unite peut se deplacer a cette position
				deplacementPossible = peutSeDeplacerEn(prochainePosition);
				if(!deplacementPossible) {
					prochainePosition = position;
				}
			}
			
			// SI DEPLACEMENT OK
			if(deplacementPossible) {
				nbCasesParcourues++;
			}
			
			position = prochainePosition;
			
			// INFLUENCE TERRAIN
			NbTourAttenteAvantDeplacement = Constantes.VITESSE_GLOBALE;
			terrainActuel = Terrain.getTerrain(position.x, position.y);
			InfluenceTerrain();
			
			// Si target atteinte, on met target ï¿½ null
			if(position.equals(target)) {
				target = null;
			}
			
			// Mise a jour visuelle
			state.yard.setObjectLocation(this, position.x, position.y);
		}
		
		// DEPLACEMENT IMPOSSIBLE
		else {
			// On attend, aucun deplacement
		}
		
	}
	
	protected Int2D getProchainePosition() {

		// Permet d'effectuer un deplacement sur X ou Y en premier pour simuler les deplacements en diagonale
		int rand = new Random().nextInt(2);
		
		Int2D positionTemp = new Int2D(position.x, position.y);
		int x = positionTemp.x;
		int y = positionTemp.y;
		
		// X d'abord
		if(rand == 0) {
			if (x != target.x) { // Dans un premier temps, on aligne notre coordonnï¿½e x
				if (target.x > x)
					x += 1;
				else
					x -= 1;
			}
			else if (y != target.y) { // Dans un second temps, on aligne notre coordonnï¿½e y
				if (target.y > y)
					y += 1;
				else
					y -= 1;
			}
		}
		
		// Y d'abord
		else {
			if (y != target.y) { // Dans un premier temps, on aligne notre coordonnï¿½e y
				if (target.y > y)
					y += 1;
				else
					y -= 1;
			}
			else if (x != target.x) { // Dans un second temps, on aligne notre coordonnï¿½e x
				if (target.x > x)
					x += 1;
				else
					x -= 1;
			}
		}
		
		return new Int2D(x,y);
	}
	
	// DESCRIPTION : Permet de tester si l'unite peut se deplacer a cette position
	// Elle ne peut pas s'il y a une montagne ou une autre unite par exemple
	protected boolean peutSeDeplacerEn(Int2D pos) {
		
		boolean deplacementPossible = true;
		Bag bag = state.yard.getObjectsAtLocation(pos.x, pos.y);
		if (bag != null && bag.numObjs != 0) {
			for (Object obj : bag.objs) {
				
				// SI AUTRE UNITE DEJA SUR CASE : Deplacement impossible
				if (obj != null && obj.getClass().getSuperclass() == UnitsAgent.class) {
					deplacementPossible = false;
					break;
				}
			}
		}

		return deplacementPossible;
	}
	
	protected void InfluenceTerrain() {
		switch(terrainActuel) {
		case NORMAL :
			this.modifVisibilite = 0;
			portee = porteeInitiale;
			break;
		case FOREST :
			// PERCEPTION
			this.modifVisibilite = -2;
			// DEPLACEMENT
			NbTourAttenteAvantDeplacement += 5;		// Ajout d'une penalite de deplacement d'un tour
			// PORTEE
			portee -= Constantes.MALUS_PORTEE_FORET;
			if(portee <= 0) {
				portee = 1;
			}
			break;
		case SWAMP :
			// PERCEPTION
			this.modifVisibilite = 0;
			// DEPLACEMENT
			NbTourAttenteAvantDeplacement += 10;		// Ajout d'une penalite de deplacement de deux tours
			// PORTEE
			portee = porteeInitiale;
			break;
		case HILL :
			this.modifVisibilite = +2;
			// PORTEE
			portee = porteeInitiale;
			break;
		case HOUSE :
			// PORTEE
			portee = porteeInitiale;
			break;
		}
	}
	
	protected Int2D DeterminerTarget() {
		for (Int2D pos : listeEnnemis){
			Bag bag = state.yard.getObjectsAtLocation(pos.x, pos.y);
			if (bag != null && bag.numObjs != 0) {
				for (Object obj : bag.objs) {
					if (obj != null) {
						if (obj.getClass() == Captain.class) {
							return pos;
						}
						if (obj.getClass() == Healer.class) {
							return pos;
						}
					}
				}
			}
		}
		return listeEnnemis.get(0); //Si on arrive ici, c'est qu'il n'y avait aucun Captain/Healer dans la liste d'ennemis
	}
	
	
	public void setOrdre(Int2D ennemi, UnitsAgent ennemiAgent) {
		target = ennemi;
		listeUnitesCiblees.add(ennemiAgent);
		prioriteActuelle = Priorites.getToCible;
		}


	// METHODES A DEFINIR DANS CLASSES FILLES
	public abstract void SetupPortrayals();
	public abstract void Soigner();
	public abstract void Ordre();
	
}
