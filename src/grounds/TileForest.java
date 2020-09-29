package grounds;

import java.awt.image.BufferedImage;

import state.battleState;

public class TileForest extends Tile {

	public TileForest(battleState battle) {
		super(battle);
	}
	
	@Override
	public BufferedImage getImage() {
		return state.GUI.groundPort.getForest();
	}
	
}
