package grounds;

import java.awt.image.BufferedImage;

import state.battleState;

public class TilePlain extends Tile {
	public TilePlain(battleState battle) {
		super(battle);
	}

	@Override
	public BufferedImage getImage() {
		return state.GUI.groundPort.getPlain();
	}
}
