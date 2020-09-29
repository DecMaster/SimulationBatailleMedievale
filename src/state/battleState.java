package state;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import data.Arbitre;
import data.Constantes;
import data.Terrain;
import data.Toolbox;
import grounds.Tile;
import grounds.TileForest;
import grounds.TileHill;
import grounds.TileHouse;
import grounds.TilePlain;
import grounds.TileSwamp;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;
import units.Archer;
import units.Captain;
import units.Healer;
import units.Knight;
import units.Mage;
import units.SwordMan;
import units.UnitsAgent;
import visuel.UnitsPortrayals;
import visuel.battleGUI;

public class battleState extends SimState {
	
	// SINGLETON
	public static battleState instance = null;
	private static ArmyPool redPool = null;
	private static ArmyPool bluePool = null;
	public static ArmyPool getPool1() { return redPool; }
	public static ArmyPool getPool2() {	return bluePool; }
	public Terrain terrain = null;
	private ArrayList<Tile> listeTiles = new ArrayList<Tile>();
	public SparseGrid2D yard = null;
	public battleGUI GUI;
	
	// PROPRIETES
	public boolean BatailleACommencee = false;		// Mis � true lors de la premi�re attaque d'un des agents
	public boolean batailleTerminee = false; //Sera modifié par le ArbitreAgent
	
	public battleState(long seed) {
		super(seed);
		
		// SINGLETON
		if(instance == null) {
			instance = this;
		}
	}
	
	private void loadTerrain() {
		//Chargement du terrain
		int num = Constantes.TERRAIN_NB;
		if (Constantes.TERRAIN_NB == 0)
			num = ThreadLocalRandom.current().nextInt(1, Constantes.TERRAIN_MAX + 1);
		terrain = new Terrain("res/Terrain0" + num + ".txt");
	}

	
	@Override
	public void start() {
		super.start();
		
		loadTerrain();
		yard = new SparseGrid2D(Constantes.GRID_SIZE_X, Constantes.GRID_SIZE_Y);
		
		GUI = battleGUI.instance;
		BatailleACommencee = false;
		batailleTerminee = false;
		yard.clear();
		
		// AJOUT TERRAINS : A faire avant les unit�s pour que ces derni�res soient au premier plan
		AddGrounds();
		
		// AJOUT AGENTS
		preparePools();
		AddUnits();
		
		// Création de l'arbitre
		Toolbox.capitaineBleuEnVie = true;
		Toolbox.capitaineRougeEnVie = true;
		Arbitre arbitre = new Arbitre(this);
		Stoppable stoppable = schedule.scheduleRepeating(arbitre);
		arbitre.stoppable = stoppable;
	}

	public Tile getTile(Int2D location)  {
		Bag bag = yard.getObjectsAtLocation(location);
		for (int i = 0 ; i<bag.size() ; ++i) {
			Object o = bag.get(i);
			if (Tile.class.isAssignableFrom(o.getClass())) {
				return (Tile) o;
			}
		}
		return null; // Should not happen
	}
	
	public ArrayList<Tile> getListeTiles() { return listeTiles; }
	
	private void AddGrounds() {
		for (int l=0 ; l<Constantes.GRID_SIZE_X ; ++l) {
			for (int c=0 ; c<Constantes.GRID_SIZE_Y ; ++c) {
				Terrain.Type type = Terrain.getTerrain(l, c);
				Tile tile = null;
				switch (type) {
				case SWAMP:
					tile = new TileSwamp(this);
					break;
				case FOREST:
					tile = new TileForest(this);
					break;
				case NORMAL:
					tile = new TilePlain(this);
					break;
				case HILL:
					tile = new TileHill(this);
					break;
				case HOUSE:
					tile = new TileHouse(this);
					break;
				default:
					System.out.println("Should not happen");
					System.exit(0);
					break;
				}
				yard.setObjectLocation(tile, l, c);
				tile.x = l;
				tile.y = c;
				Stoppable stoppable = schedule.scheduleRepeating(tile);
				tile.stoppable = stoppable;
				listeTiles.add(tile);
			}
		}
	}
	
	
	private Int2D getFreeLocation(int x1, int x2, int y1, int y2) {
		Int2D location;
		do {
			do {
				location = new Int2D(random.nextInt(x2), random.nextInt(y2));
				//System.out.println("New try : " + location.toString());
			} while(location.getX() < x1 || location.getY() < y1);
		} while (!free(location.getX(), location.getY()));
		return location;
	}

	public boolean free(int x, int y) {
		if (yard.getObjectsAtLocation(yard.stx(x), yard.sty(y)).contains(UnitsAgent.class) == false)
			return true;
		else
			return false;
	}
	// METHODES D'INIT DES POSITIONS
	//Methode de positionnement des arm�es dans un camp pr�d�fini, mais r�partition al�atoire dans ce camp
	private Int2D getPositionRouge() {
		int i = Constantes.GRID_SIZE_X;
		int j = 3;
		Int2D location = getFreeLocation(0, i, 0, j);
		/*
		 * int i = new Random().nextInt(Constantes.GRID_SIZE); int j = new
		 * Random().nextInt(Constantes.GRID_SIZE);
		 */
		return location;
	}
	private Int2D getPositionBleue() {
		int i = Constantes.GRID_SIZE_X;
		int j = Constantes.GRID_SIZE_Y;
		Int2D location = getFreeLocation(0, i, j - 3, j);
		/*
		 * int i = new Random().nextInt(Constantes.GRID_SIZE); int j = new
		 * Random().nextInt(Constantes.GRID_SIZE);
		 */
		return location;
	}
	
	private void preparePools() {
		redPool = new ArmyPool("armee 1");
		bluePool= new ArmyPool("armee 2");
		
		// RED POOL
		String[] poolData = Constantes.REDARMYCONST.split(",");
		for(int x = 0; x < poolData.length;++x) {
			System.out.println("poolData :" + poolData[x]);
		}
		for(int i = 0; i < poolData.length; ++i) {
			String currentUnitType = poolData[i];
			
			switch(currentUnitType) {
			case "ARCHER":
				for(int j = 0; j < Constantes.NB_ARCHER ; ++j) {
					redPool.addUnit(new Archer(this, UnitsPortrayals.team.red.toString()));
				}
				break;
			case "SWORDMAN":
				for(int j = 0; j < Constantes.NB_SWORDMAN ; ++j) {
					redPool.addUnit(new SwordMan(this, UnitsPortrayals.team.red.toString()));
				}
				break;
			case "KNIGHT":
				for(int j = 0; j < Constantes.NB_KNIGHT ; ++j) {
					redPool.addUnit(new Knight(this, UnitsPortrayals.team.red.toString()));
				}
				break;
			case "HEALER":
				for(int j = 0; j < Constantes.NB_HEALER ; ++j) {
					redPool.addUnit(new Healer(this, UnitsPortrayals.team.red.toString()));
				}
				break;
			case "MAGE":
				for(int j = 0; j < Constantes.NB_MAGE ; ++j) {
					redPool.addUnit(new Mage(this, UnitsPortrayals.team.red.toString()));
				}
				break;
			}
		}
		for(int j = 0; j < Constantes.NB_CAPTAIN ; ++j) {
			redPool.addUnit(new Captain(this, UnitsPortrayals.team.red.toString()));
		}
		
		// BLUE POOL
		
		poolData = Constantes.BLUEARMYCONST.split(",");
		for(int i = 0; i < poolData.length; ++i) {
			String currentUnitType = poolData[i];
			switch(currentUnitType) {
			case "ARCHER":
				for(int j = 0; j < Constantes.NB_ARCHER ; ++j) {
					bluePool.addUnit(new Archer(this, UnitsPortrayals.team.blue.toString()));
				}
				break;
			case "SWORDMAN":
				for(int j = 0; j < Constantes.NB_SWORDMAN ; ++j) {
					bluePool.addUnit(new SwordMan(this, UnitsPortrayals.team.blue.toString()));
				}
				break;
			case "KNIGHT":
				for(int j = 0; j < Constantes.NB_KNIGHT ; ++j) {
					bluePool.addUnit(new Knight(this, UnitsPortrayals.team.blue.toString()));
				}
				break;
			case "HEALER":
				for(int j = 0; j < Constantes.NB_HEALER ; ++j) {
					bluePool.addUnit(new Healer(this, UnitsPortrayals.team.blue.toString()));
				}
				break;
			case "MAGE":
				for(int j = 0; j < Constantes.NB_MAGE ; ++j) {
					bluePool.addUnit(new Mage(this, UnitsPortrayals.team.blue.toString()));
				}
				break;
			}
		}

		for(int j = 0; j < Constantes.NB_CAPTAIN ; ++j) {
			bluePool.addUnit(new Captain(this, UnitsPortrayals.team.blue.toString()));
		}
		System.out.println("Effectif Armee Rouge : "+ redPool.getCount());
		System.out.println("Effectif Armee Bleue : "+ bluePool.getCount());
	}
	
	// METHODES DE SPAWN GENERALES
	private void AddUnits() {
		
		for(int i = 0; i < redPool.getCount(); ++i) {
			try {
				UnitsAgent agent = redPool.next();
				addUnitToField(agent, getPositionRouge());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for(int i = 0; i < bluePool.getCount(); ++i) {
			try {
				UnitsAgent agent = bluePool.next();
				addUnitToField(agent, getPositionBleue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*
		 * // SWORDMAN for(int i = 0; i < Constantes.NB_SWORDMAN ; i++) { //
		 * DETERMINATION POSITIONS Int2D pos1 = getPositionRouge(); Int2D pos2 =
		 * getPositionBleue(); AddSwordMan(UnitsPortrayals.team.red, pos1);
		 * AddSwordMan(UnitsPortrayals.team.blue, pos2); }
		 * 
		 * // KNIGHT for(int i = 0; i < Constantes.NB_KNIGHT ; i++) { // DETERMINATION
		 * POSITIONS Int2D pos1 = getPositionRouge(); Int2D pos2 = getPositionBleue();
		 * AddKnight(UnitsPortrayals.team.red, pos1);
		 * AddKnight(UnitsPortrayals.team.blue, pos2); }
		 * 
		 * // ARCHER for(int i = 0; i < Constantes.NB_ARCHER ; i++) { // DETERMINATION
		 * POSITIONS Int2D pos1 = getPositionRouge(); Int2D pos2 = getPositionBleue();
		 * AddArcher(UnitsPortrayals.team.red, pos1);
		 * AddArcher(UnitsPortrayals.team.blue, pos2); }
		 * 
		 * // MAGE for(int i = 0; i < Constantes.NB_MAGE ; i++) { // DETERMINATION
		 * POSITIONS Int2D pos1 = getPositionRouge(); Int2D pos2 = getPositionBleue();
		 * AddMage(UnitsPortrayals.team.red, pos1); AddMage(UnitsPortrayals.team.blue,
		 * pos2); } // CAPTAIN for(int i = 0; i < Constantes.NB_CAPTAIN ; i++) { //
		 * DETERMINATION POSITIONS Int2D pos1 = getPositionRouge(); Int2D pos2 =
		 * getPositionBleue(); AddCaptain(UnitsPortrayals.team.red, pos1);
		 * AddCaptain(UnitsPortrayals.team.blue, pos2); }
		 */
	}
	
	//private void addUnits(armyPool a1, armyPool a2, String rule1, String rule2) {}
	// METHODES DE SPAWN INDIVIDUELLES
	private void addUnitToField(UnitsAgent agent, Int2D position) {
		agent.setPosition(position);
		yard.setObjectLocation(agent, position);
		Stoppable stoppable = schedule.scheduleRepeating(agent);
		agent.stoppable = stoppable;
		GUI.listeUnites.add(agent);
	}
	
	private void AddSwordMan(UnitsPortrayals.team team, Int2D position) {
		SwordMan agent = new SwordMan(position.x,position.y, this, team.toString());
		yard.setObjectLocation(agent, position);
		Stoppable stoppable = schedule.scheduleRepeating(agent);
		agent.stoppable = stoppable;
		GUI.listeUnites.add(agent);
	}
	private void AddArcher(UnitsPortrayals.team team, Int2D position) {
		Archer agent = new Archer(position.x,position.y, this, team.toString());
		yard.setObjectLocation(agent, position);
		Stoppable stoppable = schedule.scheduleRepeating(agent);
		agent.stoppable = stoppable;
		GUI.listeUnites.add(agent);
	}
	private void AddCaptain(UnitsPortrayals.team team, Int2D position) {
		Captain agent = new Captain(position.x,position.y, this, team.toString());
		yard.setObjectLocation(agent, position);
		Stoppable stoppable = schedule.scheduleRepeating(agent);
		agent.stoppable = stoppable;
		GUI.listeUnites.add(agent);
	}
	private void AddKnight(UnitsPortrayals.team team, Int2D position) {
		Knight agent = new Knight(position.x,position.y, this, team.toString());
		yard.setObjectLocation(agent, position);
		Stoppable stoppable = schedule.scheduleRepeating(agent);
		agent.stoppable = stoppable;
		GUI.listeUnites.add(agent);
	}
	private void AddMage(UnitsPortrayals.team team, Int2D position) {
		Mage agent = new Mage(position.x,position.y, this, team.toString());
		yard.setObjectLocation(agent, position);
		Stoppable stoppable = schedule.scheduleRepeating(agent);
		agent.stoppable = stoppable;
		GUI.listeUnites.add(agent);
	}
	
	public static int getRedArmyCount() {if(redPool != null) {return redPool.getCount();} else return -1;}
	public static int getBlueArmyCount() {if(bluePool != null) {return bluePool.getCount();} else return -1;}
	public void decreasePool(String f) {
		switch (f) {
		case "red":
			redPool.setCount(redPool.getCount() - 1);
			break;
		case "blue":
			bluePool.setCount(bluePool.getCount() - 1);
			break;
		default:
			break;
		}
		/*
		if(pool1.getCount() <= 0) {
			System.out.println("Blue army wins");
		} else if (pool2.getCount() <= 0) {
			System.out.println("Red army wins");
		}
		*/
	}
}
