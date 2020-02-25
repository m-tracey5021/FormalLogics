
public class Relation {
	private World firstWorld, secondWorld;
	private boolean firstWorldReceives, secondWorldReceives;
	
	public Relation(World firstWorld, World secondWorld, boolean firstWorldReceives, boolean secondWorldReceives) {
		this.firstWorld = firstWorld;
		this.secondWorld = secondWorld;
		this.firstWorldReceives = firstWorldReceives;
		this.secondWorldReceives = secondWorldReceives;
	}
	
	// =========== GET
	
	public World[] getRelatedWorlds() {
		World[] relatedWorlds = new World[] {firstWorld, secondWorld};
		return relatedWorlds;
	}
	
	public boolean isFirstWorldReceiving() {
		return this.firstWorldReceives;
	}
	
	public boolean isSecondWorldReceiving() {
		return this.secondWorldReceives;
	}
	
	// ========= SET
}

