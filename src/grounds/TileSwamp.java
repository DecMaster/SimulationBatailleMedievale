package grounds;

import java.awt.image.BufferedImage;

import state.battleState;

public class TileSwamp extends Tile {
	public TileSwamp(battleState battle) {
		super(battle);
	}

	@Override
	public BufferedImage getImage() {
		return state.GUI.groundPort.getSwamp();
	}
}
