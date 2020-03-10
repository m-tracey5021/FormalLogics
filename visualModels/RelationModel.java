package visualModels;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import universeComponents.Relation;

public class RelationModel {
	private Point2D firstWorldPoint, secondWorldPoint;
	private Line relationLine;
	
	
	//public RelationModel(Relation relation) {
	//	this.firstWorldModel = relation.getFirstWorld().getWorldModel();
	//	this.secondWorldModel = relation.getSecondWorld().getWorldModel();
	//	setupGeometry();
	//}
	
	public RelationModel(Point2D p1, Point2D p2) {
		this.firstWorldPoint = p1;
		this.secondWorldPoint = p2;
		relationLine = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		
	}
		
	
	public void drawRelation(Pane modelPane) {
		modelPane.getChildren().add(relationLine);
	}
	
	public void relocateRelation(double translateX, double translateY) {
		firstWorldPoint = new Point2D(firstWorldPoint.getX() + translateX, firstWorldPoint.getY() + translateY);
		secondWorldPoint = new Point2D(secondWorldPoint.getX() + translateX, secondWorldPoint.getY() + translateY);
		relationLine = new Line(firstWorldPoint.getX(), firstWorldPoint.getY(), secondWorldPoint.getX(), secondWorldPoint.getY());
	}
}
