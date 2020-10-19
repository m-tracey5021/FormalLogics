package universeComponents;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import visualModels.RelationModel;

public class Relation {
	private World firstWorld, secondWorld;
	private boolean firstWorldReceives, secondWorldReceives;
	private RelationModel relationModel;
	
	/*
	public Relation(World firstWorld, World secondWorld, boolean firstWorldReceives, boolean secondWorldReceives) {
		this.firstWorld = firstWorld;
		this.secondWorld = secondWorld;
		this.firstWorldReceives = firstWorldReceives;
		this.secondWorldReceives = secondWorldReceives;
	}
	*/
	
	public Relation(World firstWorld, World secondWorld) { // first points towards second
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

	public void generateRelationModel(boolean reflexive) {
		if (reflexive) {
			double centerX = firstWorld.getWorldModel().getLocation().getX();
			double centerY = firstWorld.getWorldModel().getLocation().getY() - 30;
			Arc arc = new Arc(centerX, centerY, 10, 10, 0, 270);
			arc.setFill(Color.TRANSPARENT);
			arc.setStroke(Color.BLACK);
			double extensionEnd = centerY + 10;
			
			Line firstHead = new Line(centerX + 10, extensionEnd, centerX + 5, extensionEnd - 5);
			Line secondHead = new Line(centerX + 10, extensionEnd, centerX + 15, extensionEnd - 5);
			Line arcExtension = new Line(centerX + 10, centerY, centerX + 10, extensionEnd);
			relationModel = new RelationModel(arc, firstHead, secondHead, arcExtension);
		}else {
			Point2D tmp1 = firstWorld.getWorldModel().getLocation();
			Point2D tmp2 = secondWorld.getWorldModel().getLocation();

			double xDiff = tmp2.getX() - tmp1.getX();
			double yDiff = tmp2.getY() - tmp1.getY();
			double totalDistance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
			double distanceRatio = 30 / totalDistance; // i want the line to start 30 px down the line
			double newX1 = (1 - distanceRatio) * tmp1.getX() + distanceRatio * tmp2.getX();
			double newY1 = (1 - distanceRatio) * tmp1.getY() + distanceRatio * tmp2.getY();
			Point2D newWorld = new Point2D((int) newX1, (int) newY1);
			
			double newX2 = (1 - distanceRatio) * tmp2.getX() + distanceRatio * tmp1.getX();
			double newY2 = (1 - distanceRatio) * tmp2.getY() + distanceRatio * tmp1.getY();
			Point2D newRelatedWorld = new Point2D((int) newX2, (int) newY2);
			
			Line relationLine = new Line(newWorld.getX(), newWorld.getY(), newRelatedWorld.getX(), newRelatedWorld.getY());
			Line[] arrowHeads = getArrowHeads(newWorld, newRelatedWorld);
			

			relationModel = new RelationModel(relationLine, arrowHeads[0], arrowHeads[1]);
		}
		

		

	}
	
	public Line[] getArrowHeads(Point2D newWorld, Point2D newRelatedWorld) {
		double xDiff = newWorld.getX() - newRelatedWorld.getX();
		double yDiff = newWorld.getY() - newRelatedWorld.getY();
		double totalDistance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
		double distanceRatio = 20 / totalDistance;
		double newX1 = (1 - distanceRatio) * newRelatedWorld.getX() + distanceRatio * newWorld.getX();
		double newY1 = (1 - distanceRatio) * newRelatedWorld.getY() + distanceRatio * newWorld.getY();
		Line firstHead = new Line(newX1, newY1, newRelatedWorld.getX(), newRelatedWorld.getY());
		
		//Line firstHead = new Line(0, 0, newRelatedWorld.getX(), newRelatedWorld.getY());
		
		Line secondHead = new Line(newX1, newY1, newRelatedWorld.getX(), newRelatedWorld.getY());
		
		//Line secondHead = new Line(0, 0, newRelatedWorld.getX(), newRelatedWorld.getY());
		
		//firstHead.getTransforms().add(new Rotate(60, newRelatedWorld.getX(), newRelatedWorld.getY()));
		//secondHead.getTransforms().add(new Rotate(85, newRelatedWorld.getX(), newRelatedWorld.getY()));
		
		return new Line[] {firstHead, secondHead};
		

		
		
	}
	
	// ========= SET
}

