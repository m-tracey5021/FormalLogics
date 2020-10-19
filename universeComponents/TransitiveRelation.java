package universeComponents;

public class TransitiveRelation extends Relation {
	private World intermediaryWorld;
	
	public TransitiveRelation(World firstWorld, World intermediaryWorld, World thirdWorld) {
		super(firstWorld, thirdWorld);
		this.intermediaryWorld = intermediaryWorld;
	}
	
	// ============= GET
	
	public World getIntermediaryWorldWorld() {
		return this.intermediaryWorld;
	}
	
	// ============ 
}
