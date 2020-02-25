import java.util.ArrayList;

public class Universe {
	private ArrayList<World> worlds;
	private ArrayList<Relation> relations;
	private boolean reflex;
	private boolean trans;
	private boolean symm;
	private boolean hereditary;
	
	public Universe() {
		this.worlds = new ArrayList<World>();
	}
	public Universe(boolean reflex, boolean trans, boolean symm, boolean hereditary) {
		this.worlds = new ArrayList<World>();
		this.reflex = reflex;
		this.trans = trans;
		this.symm = symm;
		this.hereditary = hereditary;
	}
	public ArrayList<World> getWorlds(){
		return this.worlds;
	}
	public ArrayList<Relation> getRelations(){
		return this.relations;
	}
	public boolean[] getUniverseProperties() {
		boolean[] universeProperties = new boolean[] {reflex, trans, symm, hereditary};
		return universeProperties;
	}
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
	
}
