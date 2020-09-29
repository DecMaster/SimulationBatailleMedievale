package data;

import sim.engine.Stoppable;
import sim.engine.SimState;
import sim.engine.Steppable;
import state.battleState;

public class Arbitre implements Steppable {
	public static enum ModeDeJeu {
		KILLTHEMALL,
		KILLCAPTAIN;
	}
	private boolean whoWon = true; //(t = blue, f = red)
	protected battleState state;
	public Stoppable stoppable;
	
	public Arbitre(battleState battle) {
		//System.out.println("Création de l'Arbitre.");
		System.out.print("Mode de Jeu : ");
		switch (Constantes.MODE_DE_JEU) {
		case KILLTHEMALL:
			System.out.println("Kill Them All.");
			break;
		case KILLCAPTAIN:
			System.out.println("Kill the Captain.");
			break;
		default:
			System.out.println("Mode de Jeu invalide !!");
			System.exit(0);
			break;
		}
		this.state = battle;
	}

	@Override
	public void step(SimState arg0) {
		if (isFinished()) {
			displayWinner();
			displayVictory();
			System.out.println("\nSimulation terminee !");
			battleState.instance.finish();
			state.batailleTerminee = true;
		}
	}
	
	private boolean isFinished() {
		switch (Constantes.MODE_DE_JEU) {
		case KILLTHEMALL:
			return isKillThemAllFinished();
		case KILLCAPTAIN:
			return isKillCaptainFinished();
		default:
			System.out.println("Mode de Jeu invalide !!");
			System.exit(0);
			break;
		}
		return false;
	}

	private boolean isKillCaptainFinished() {
		if (!Toolbox.capitaineRougeEnVie) {
			whoWon = true;
			return true;
		} else if (!Toolbox.capitaineBleuEnVie) {
			whoWon = false;
			return true;
		}
		return false;
	}

	private boolean isKillThemAllFinished() {
		if (battleState.getPool1().getCount() ==  0) {
			whoWon = true;
			return true;
		} else if (battleState.getPool2().getCount() ==  0) {
			whoWon = false;
			return true;
		}
		return false;
	}
	
	private void displayWinner() {
		if (whoWon) {
			System.out.println("Equipe bleue victorieuse !");
		} else {
			System.out.println("Equipe rouge victorieuse !");
		}
	}
	
	private void displayVictory() {
		switch (Constantes.MODE_DE_JEU) {
		case KILLTHEMALL:
			System.out.println("L'autre equipe a ete entierement eliminee.");
			break;
		case KILLCAPTAIN:
			System.out.println("Le capitaine adverse a ete tue.");
			break;
		default:
			// Should not happen
			break;
		}
	}
}
