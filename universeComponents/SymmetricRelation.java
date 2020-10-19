package universeComponents;

public class SymmetricRelation extends Relation{
	
	private World reverseFirstWorld, reverseSecondWorld;
	
	public SymmetricRelation(World firstWorld, World secondWorld) {
		super(firstWorld, secondWorld);
		this.reverseFirstWorld = secondWorld;
		this.reverseSecondWorld = firstWorld;
	}
	
	// ========= GET
	
	public World getReverseFirstWorld() {
		return this.reverseFirstWorld;
	}
	
	public World getReverseSecondWorld() {
		return this.reverseSecondWorld;
	}
	
	// ========= SET
}
