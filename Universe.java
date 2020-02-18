import java.util.ArrayList;

public class Universe {
	private ArrayList<World> worlds;
	private ArrayList<Relation> relations;
	private boolean reflex;
	private boolean trans;
	private boolean symm;
	private boolean hereditary;
	
	public Universe() {
		
	}
	public Universe(ArrayList<World> worlds) {
		this.worlds = worlds;
	}
}
