package universeComponents;
import java.util.ArrayList;

import javafx.geometry.Point2D;
import propositions.InstanceVariable;
import visualModels.RelationModel;
import visualModels.TernaryTreeModel;
import visualModels.UniverseModel;
import visualModels.WorldModel;

public class Universe {
	private ArrayList<World> worlds;
	private ArrayList<Relation> relations;
	private ArrayList<InstanceVariable> instantiatedVariables;
	private boolean reflex;
	private boolean trans;
	private boolean symm;
	private boolean hereditary;
	private UniverseModel universeModel;
	private boolean popupUniverse;
	//private Point2D points;
	//private ArrayList<WorldModel> worldModels;
	//private ArrayList<RelationModel> relationModels;
	
	public Universe() {
		this.worlds = new ArrayList<World>();
	}
	public Universe(boolean reflex, boolean trans, boolean symm, boolean hereditary, boolean popupUniverse) {
		this.worlds = new ArrayList<World>();
		this.relations = new ArrayList<Relation>();
		this.instantiatedVariables = new ArrayList<InstanceVariable>();
		this.reflex = reflex;
		this.trans = trans;
		this.symm = symm;
		this.hereditary = hereditary;
		this.popupUniverse = popupUniverse;
	}
	
	// ================ GET
	
	public ArrayList<World> getWorlds(){
		return this.worlds;
	}
	
	public ArrayList<Relation> getRelations(){
		return this.relations;
	}
	
	public ArrayList<InstanceVariable> getInstantiatedVariables(){
		return this.instantiatedVariables;
	}
	
	public boolean[] getUniverseProperties() {
		boolean[] universeProperties = new boolean[] {reflex, trans, symm, hereditary};
		return universeProperties;
	}
	
	public UniverseModel getUniverseModel() {
		return this.universeModel;
	}
	
	// ================= SET
	
	public void setWorlds(ArrayList<World> worlds) {
		this.worlds = worlds;
	}
	public void setRelations(ArrayList<Relation> relations) {
		this.relations = relations;
	}
	public void addWorld(World world) {
		this.worlds.add(world);
	}
	public void addRelation(Relation relation) {
		this.relations.add(relation);
	}
	
	public void adjustRelationsForUniverseProperties() {
		if (reflex) {
			adjustRelationsForReflexivity();
		}
		if (trans) {
			adjustRelationsForTransitivity();
		}
		if (symm) {
			adjustRelationsForSymmetry();
		}
		
		
		
	}
	
	public void adjustRelationsForReflexivity() {
		
	}
	
	public void adjustRelationsForTransitivity() {
		
	}
	
	public void adjustRelationsForSymmetry() {
		
	}
	
	public ArrayList<World> getRelatedWorlds(World chosenWorld){ // returns worlds that are pointed towards by the chosen world
		ArrayList<World> relatedWorlds = new ArrayList<World>();
		for (Relation relation : relations) {
			if (relation.getFirstWorld() == chosenWorld) {
				relatedWorlds.add(relation.getSecondWorld());
			}
		}
		return relatedWorlds;
	}
	
	public ArrayList<World> getRelatingWorlds(World chosenWorld){ // returns all worlds that point toward the chosen world
		ArrayList<World> relatingWorlds = new ArrayList<World>();
		for (Relation relation : relations) {
			if (relation.getSecondWorld() == chosenWorld) {
				relatingWorlds.add(relation.getFirstWorld());
			}
		}
		return relatingWorlds;
	}
	
	
	public void generateUniverseModel() {
		
		int numberOfWorlds = worlds.size();
		double sumOfInteriorAngles = (numberOfWorlds - 2) * 180;
		double insideAngle = sumOfInteriorAngles / numberOfWorlds;
		double edgeLength = 50;
		Point2D initialEdgeStart = new Point2D(0, 0); // initialEdge will be bottom
		Point2D initialEdgeEnd = new Point2D(edgeLength, 20);
		
		double radius = 150;
		double xOffset = 0;
		double yOffset = 0;
		
		ArrayList<WorldModel> worldModels = new ArrayList<WorldModel>();
		ArrayList<RelationModel> relationModels = new ArrayList<RelationModel>();
		
		
		for (int i = 0; i < worlds.size(); i ++) {
			
			double duplicateI = (double) i;
			double x = (Math.sin(duplicateI / numberOfWorlds * 2 * Math.PI) * radius) + xOffset;
			double y = (Math.cos(duplicateI / numberOfWorlds * 2 * Math.PI) * radius) + yOffset;
			worlds.get(i).generateWorldModel();
			worlds.get(i).getWorldModel().translateWorld(x, y, popupUniverse);
			worldModels.add(worlds.get(i).getWorldModel());
	

			
		}
		
		for (int i = 0; i < relations.size(); i ++) {
			relations.get(i).generateRelationModel();
			relationModels.add(relations.get(i).getRelationModel());
		}
		
		universeModel = new UniverseModel(worldModels, relationModels, popupUniverse);
		
		
		
	}
	
	
}
