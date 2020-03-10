package universeComponents;
import java.util.ArrayList;
import java.util.UUID;

import enums.AuxillaryOperatorType;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import treeComponents.TernaryTree;
import visualModels.TernaryTreeModel;
import visualModels.WorldModel;

public class World {
	
	private UUID id;
	private Universe parentUniverse;
	private int worldNum;
	private TernaryTree ternaryTree;
	private ArrayList<World> relatedWorlds; // if this world relates to another, it points towards the other
	private ArrayList<World> relatingWorlds;
	private WorldModel worldModel;
	
	public World(Universe parentUniverse, TernaryTree ternaryTree) {
		this.id = UUID.randomUUID();
		this.parentUniverse = parentUniverse;
		this.parentUniverse.addWorld(this);
		this.worldNum = parentUniverse.getWorlds().size();
		this.ternaryTree = ternaryTree;
		this.relatedWorlds = new ArrayList<World>();
		this.relatingWorlds = new ArrayList<World>();
	}
	
	// ======== GET
	
	public UUID getId() {
		return this.id;
	}
	
	public TernaryTree getTernaryTree() {
		return this.ternaryTree;
	}
	public Universe getParentUniverse() {
		return this.parentUniverse;
	}
	
	public ArrayList<World> getRelatedWorlds(){
		return relatedWorlds;
	}
	
	public ArrayList<World> getRelatingWorlds(){
		return relatingWorlds;
	}
	
	public WorldModel getWorldModel() {
		return this.worldModel;
	}
	
	// ============== SET
	
	public void setWorldModel(WorldModel worldModel) {
		this.worldModel = worldModel;
	}
	
	public void setWorldNum(int num) {
		this.worldNum = num;
	}
	
	// =============
	
	

	
	public void generateWorldModel() {
		ternaryTree.generateTreeModel();
		worldModel = new WorldModel(new Point2D(0, 0), new Label(this.toString()), ternaryTree.getTreeModel());
	}
	
	// ============== EXPAND
	
	public void expandWithinUniverse() {
		ternaryTree.expandWithinWorld(this);
	}
	
	@Override
	public String toString() {
		return "World: " + worldNum;
	}
}
