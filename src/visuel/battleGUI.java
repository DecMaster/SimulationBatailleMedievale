package visuel;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;

import data.Constantes;
import grounds.TileForest;
import grounds.TileHill;
import grounds.TileHouse;
import grounds.TilePlain;
import grounds.TileSwamp;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.ImagePortrayal2D;
import state.battleState;
import units.UnitsAgent;

public class battleGUI extends GUIState {
	
	// SINGLETON
	public static battleGUI instance = null;
	
	// PORTRAYALS GIVER
	public UnitsPortrayals bluePort = new UnitsPortrayals(UnitsPortrayals.team.blue);
	public UnitsPortrayals redPort = new UnitsPortrayals(UnitsPortrayals.team.red);
	public GroundsPortrayals groundPort = new GroundsPortrayals();
	
	// REFERENCES
	public Display2D display;
	public JFrame displayFrame;
	public JFrame inspectorFrame;
	
	public ArrayList<UnitsAgent> listeUnites = new ArrayList<UnitsAgent>();
	public SparseGridPortrayal2D yardPortrayal = new SparseGridPortrayal2D();

	public battleGUI(SimState state) {
		super(state);
		
		// SINGLETON
		if(instance == null) {
			instance = this;
		}
	}

	public static String getName() {
		return "Simulation de bataille medievale - IA04";
	}

	@Override
	public void start() {
		super.start();
		setupPortrayals();
		getInspector();
	}

	public void setupPortrayals() {
		// BACKGROUND
		battleState battlefield = (battleState) state;
		yardPortrayal.setField(battlefield.yard);
		display.reset();
		Color c = new Color(175,255,129);
		display.setBackdrop(c);
		display.repaint();
		
		// ATTRIBUTION PORTRAYALS TERRRAIN
		yardPortrayal.setPortrayalForClass(TilePlain.class, new ImagePortrayal2D(groundPort.getPlain()));
		yardPortrayal.setPortrayalForClass(TileForest.class, new ImagePortrayal2D(groundPort.getForest()));
		yardPortrayal.setPortrayalForClass(TileSwamp.class, new ImagePortrayal2D(groundPort.getSwamp()));
		yardPortrayal.setPortrayalForClass(TileHill.class, new ImagePortrayal2D(groundPort.getHill()));	
		yardPortrayal.setPortrayalForClass(TileHouse.class, new ImagePortrayal2D(groundPort.getHouse()));	
		
		// ATTRIBUTION PORTRAYALS UNITES (Envoi signal)
		for(UnitsAgent agent : listeUnites) {
			agent.SetupPortrayals();
		}
	}

	@Override
	public void init(Controller c) {
		super.init(c);
		System.out.println("GUI init");
		display = new Display2D(Constantes.FRAME_SIZE_X, Constantes.FRAME_SIZE_X, this);
		display.setClipping(true);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Visuel : Bataille");
		inspectorFrame = getInspector().createFrame(null);
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach(yardPortrayal, "Yard");
	}
	
	// METHODES INSPECTIONS
	@Override
	public Object getSimulationInspectedObject() { return state; } 
	@Override
	public Inspector getInspector() {
		Inspector i = super.getInspector();
		i.setVolatile(true);
		return i;
	}
}
