package universeComponents;
import java.util.ArrayList;

import enums.InstanceVariableType;
import javafx.geometry.Point2D;
import propositions.InstanceVariable;
import visualModels.RelationModel;
import visualModels.TernaryTreeModel;
import visualModels.UniverseModel;
import visualModels.WorldModel;

public class Universe {
	private ArrayList<World> worlds;
	private ArrayList<Relation> relations;
	private ArrayList<InstanceVariableType> instantiatedVariableTypes;
	private boolean reflex;
	private boolean trans;
	private boolean symm;
	private boolean hereditary;
	private UniverseModel universeModel;
	private boolean popupUniverse;

	
	public Universe() {
		this.worlds = new ArrayList<World>();
	}
	public Universe(boolean reflex, boolean trans, boolean symm, boolean hereditary, boolean popupUniverse) {
		this.worlds = new ArrayList<World>();
		this.relations = new ArrayList<Relation>();
		this.instantiatedVariableTypes = new ArrayList<InstanceVariableType>();
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
	
	public ArrayList<InstanceVariableType> getInstantiatedVariables(){
		return this.instantiatedVariableTypes;
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
	
	public void updateRelationsForNewWorld(World oldWorld, World newWorld) {
		if (reflex) {
			addReflexiveRelation(newWorld);
		}
		if (trans) {
			addTransitiveRelation(oldWorld, newWorld);
		}
		if (symm) {
			addSymmetricRelation(oldWorld, newWorld);
		}else {
			addSimpleRelation(oldWorld, newWorld);
		}

		
	}
	
	// ============= ADD RELATION TYPES
	
	public void addReflexiveRelation(World newWorld) {
		relations.add(new Relation(newWorld, newWorld));
	}
	
	
	/*
	 * this adds the same world multiple times which is a bug VVV
	 */
	
	public void addTransitiveRelation(World oldWorld, World newWorld) {
		/*
		ArrayList<Relation> relationsForOldWorld = getRelationsByWorld(oldWorld);
		for (Relation relation : relationsForOldWorld) {
			if (relation.getSecondWorld() == oldWorld) {
				relations.add(new Relation(relation.getFirstWorld(), newWorld));
				if (symm) {
					relations.add(new Relation(newWorld, relation.getFirstWorld()));
				}
			}
		}
		
		*/
		for (World relatesToOld : getRelatingWorlds(oldWorld)) {
			relations.add(new Relation(relatesToOld, newWorld));
			if (symm) {
				relations.add(new Relation(newWorld, relatesToOld));
			}
		}
	}
	
	public void addSymmetricRelation(World oldWorld, World newWorld) {
		relations.add(new Relation(oldWorld, newWorld));
		relations.add(new Relation(newWorld, oldWorld));
	}
	
	public void addSimpleRelation(World oldWorld, World newWorld) {
		relations.add(new Relation(oldWorld, newWorld));
	}
	
	
	// ========== ADJUST UNIVERSE FOR PROPERTIES
	
	
	public void adjustRelationsForReflexivity() {
		ArrayList<Relation> newRelationsToAdd = new ArrayList<Relation>();
		for (World world : worlds) {
			
			boolean reflexiveRelationExists = false;
			ArrayList<Relation> relationsForWorld = getRelationsByWorld(world);

			for (Relation relation : relationsForWorld) {
				if(relation instanceof ReflexiveRelation) {
					reflexiveRelationExists = true;
				}
			}
			
			if (!reflexiveRelationExists) {
				newRelationsToAdd.add(new ReflexiveRelation(world));
			}
			
		}
		relations.addAll(newRelationsToAdd);
	}
	
	/*
	 * transitivity method needs to be updated
	 * to apply to all worlds down the chain? do
	 * it recursively? if no related worlds return
	 */
	
	public void adjustRelationsForTransitivity() { 
		
		
		/*
		ArrayList<Relation> newRelationsToAdd = new ArrayList<Relation>();
		for (Relation relation : relations) {
			World firstWorld = relation.getFirstWorld();
			ArrayList<World> worldsRelatedToSecond = getRelatedWorlds(relation.getSecondWorld());
			for (World thirdWorld : worldsRelatedToSecond) {
				newRelationsToAdd.add(new Relation(firstWorld, thirdWorld));
			}
		}
		relations.addAll(newRelationsToAdd);
		*/
		
		ArrayList<Relation> newRelationsToAdd = new ArrayList<Relation>();
		for (Relation firstRelation : relations) {
			if (!(firstRelation instanceof TransitiveRelation)) {
				World relatedWorld = firstRelation.getSecondWorld();
				for (Relation comparedRelation : relations) {
					if (comparedRelation.getFirstWorld() == relatedWorld) {
						newRelationsToAdd.add(new TransitiveRelation(firstRelation.getFirstWorld(), comparedRelation.getSecondWorld(), comparedRelation.getFirstWorld()));
					}
				}
			}
			
		}
		relations.addAll(newRelationsToAdd);
	}
	
	public void adjustRelationsForSymmetry() {
		/*
		ArrayList<Relation> newRelationsToAdd = new ArrayList<Relation>();
		for(Relation relation : relations) {
			World firstWorld = relation.getFirstWorld();
			World secondWorld = relation.getSecondWorld();
			newRelationsToAdd.add(new Relation(secondWorld, firstWorld));
		}
		*/
		

		
		for (int i = 0; i < relations.size(); i ++) {
			Relation relation = relations.get(i);
			if (!(relation instanceof ReflexiveRelation) && !(relation instanceof SymmetricRelation)) {
				relations.remove(relation);
				relations.add(i, new SymmetricRelation(relation.getFirstWorld(), relation.getSecondWorld()));
			}
		}
		
		
		/*
		
		
		for (World world : worlds) {
			boolean symmetricRelationExists = false;
			ArrayList<Relation> relationsForWorld = getRelationsByWorld(world);
			
			for (int i = 0; i < relationsForWorld.size(); i ++) {
				Relation relationForWorld = relationsForWorld.get(i);
				if (!(relationForWorld instanceof ReflexiveRelation) && !(relationForWorld instanceof SymmetricRelation)) {
					relations.remove(relationForWorld);
					relations.add(new SymmetricRelation(relationForWorld.getFirstWorld(), relationForWorld.getSecondWorld()));
				}
				
				
			}
			
			if (!symmetricRelationExists) {
				
			}
		}
		
		
		relations.addAll(newRelationsToAdd);
		*/
	}
	
	
	// =============== GET RELATIONS AND WORLDS
	
	
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
	
	public ArrayList<Relation> getRelationsByWorld(World chosenWorld){ // returns all relations in which chosen world is involved
		ArrayList<Relation> relationsForWorld = new ArrayList<Relation>();
		for (Relation relation : relations) {
			if (relation.getFirstWorld() == chosenWorld || relation.getSecondWorld() == chosenWorld) {
				relationsForWorld.add(relation);
			}
		}
		return relationsForWorld;
	}
	
	
	// ==================
	
	
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
			Relation relation = relations.get(i);
			if (relation.getFirstWorld() == relation.getSecondWorld()) {
				relation.generateRelationModel(true);
			}else {
				relation.generateRelationModel(false);
			}
			relationModels.add(relations.get(i).getRelationModel());
		}
		
		universeModel = new UniverseModel(worldModels, relationModels, popupUniverse);
		
		
		
	}
	
	
}
