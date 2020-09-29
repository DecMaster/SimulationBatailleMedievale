package grounds;

import java.awt.image.BufferedImage;

import state.battleState;

public class TileHill extends Tile {
	public TileHill(battleState battle) {
		super(battle);
	}

	@Override
	public BufferedImage getImage() {
		return state.GUI.groundPort.getHill();
	}
}
