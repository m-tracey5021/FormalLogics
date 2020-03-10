package universeComponents;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import visualModels.RelationModel;

public class Relation {
	private World firstWorld, secondWorld;
	private boolean firstWorldReceives, secondWorldReceives;
	private RelationModel relationModel;
	
	public Relation(World firstWorld, World secondWorld, boolean firstWorldReceives, boolean secondWorldReceives) {
		this.firstWorld = firstWorld;
		this.secondWorld = secondWorld;
		this.firstWorldReceives = firstWorldReceives;
		this.secondWorldReceives = secondWorldReceives;
	}
	
	public Relation(World firstWorld, World secondWorld) {
		this.firstWorld = firstWorld;
		this.secondWorld = secondWorld;

	}
	
	// =========== GET
	
	public World[] getRelatedWorlds() {
		World[] relatedWorlds = new World[] {firstWorld, secondWorld};
		return relatedWorlds;
	}
	
	public World getFirstWorld() {
		return this.firstWorld;
	}
	
	public World getSecondWorld() {
		return this.secondWorld;
	}
	
	public RelationModel getRelationModel() {
		return this.relationModel;
	}
	
	public boolean isFirstWorldReceiving() {
		return this.firstWorldReceives;
	}
	
	public boolean isSecondWorldReceiving() {
		return this.secondWorldReceives;
	}
	
	//public void generateModel() {
	//	relationModel = new RelationModel(this); 
	//}
	
	public void generateRelationModel() {
		Point2D tmp1 = firstWorld.getWorldModel().getLocation();
		Point2D tmp2 = secondWorld.getWorldModel().getLocation();
		//if (!(tmp1.x == tmp2.x) && !(tmp1.y == tmp2.y)) { // if the line is not straight
		double xDiff = tmp2.getX() - tmp1.getX();
		double yDiff = tmp2.getY() - tmp1.getY();
		double totalDistance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
		double distanceRatio = 30 / totalDistance; // i want the line to start 20 px down the line
		double newX1 = (1 - distanceRatio) * tmp1.getX() + distanceRatio * tmp2.getX();
		double newY1 = (1 - distanceRatio) * tmp1.getY() + distanceRatio * tmp2.getY();
		Point2D newWorld = new Point2D((int) newX1, (int) newY1);
		
		double newX2 = (1 - distanceRatio) * tmp2.getX() + distanceRatio * tmp1.getX();
		double newY2 = (1 - distanceRatio) * tmp2.getY() + distanceRatio * tmp1.getY();
		Point2D newRelatedWorld = new Point2D((int) newX2, (int) newY2);
		
		relationModel = new RelationModel(newWorld, newRelatedWorld);
		

	}
	
	// ========= SET
}

