package state;

import sim.util.Bag;
import units.UnitsAgent;

public class ArmyPool{
	private static int armyCount = 0;
	private String name;
	//Classe permettant de manipuler une armée
	private Bag pool;
	//Nombre d'unités
	private int count;
	//Index actuel de l'itérateur
	private int index;
	
	public ArmyPool() {
		armyCount++;
		name = "Unnamed " + armyCount;
		pool = new Bag();
		count = 0;
		index = 0;
	}
	
	public ArmyPool(String n) {
		armyCount++;
		name = n;
		pool = new Bag();
		count = 0;
		index = 0;
	}
	
	public int addUnit(UnitsAgent a) {
		pool.add(a);
		++count;
		return count;
	}
	
	//Fonction qui itere et renvoie l'unité suivante dans la pool
	//Toujours vérifier qu'il y a une unité avant de l'appeler
	//(count et index)
	public UnitsAgent next(){
		UnitsAgent agent = (UnitsAgent) pool.get(index);
		++index;
		return agent;
	}
	
	public static int getArmyCount() {
		return armyCount;
	}

	public static void setArmyCount(int armyCount) {
		ArmyPool.armyCount = armyCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bag getPool() {
		return pool;
	}

	public void setPool(Bag pool) {
		this.pool = pool;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	
}
