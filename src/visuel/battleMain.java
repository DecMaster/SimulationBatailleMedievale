package visuel;

import sim.display.Console;
import state.battleState;

public class battleMain {
	public static void main(String[] args) {
		runUI();
	}

	public static void runUI() {
		battleState model = new battleState(System.currentTimeMillis());
		battleGUI gui = new battleGUI(model);
		Console console = new Console(gui);
		console.setVisible(true);
	}
}