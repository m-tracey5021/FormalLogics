package visualModels;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import universeComponents.Relation;

public class RelationModel {
	private boolean reflexive;
	private Point2D firstWorldPoint, secondWorldPoint;
	private Line relationLine, firstHead, secondHead, arcExtension;
	private Arc reflexiveArc;
	private Group arrowGroup;
	
	
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
	
	//public RelationModel(Group arrowGroup) {
	//	this.arrowGroup = arrowGroup;
		
	//}
	
	public RelationModel(Arc arc, Line firstHead, Line secondHead, Line arcExtension) {
		this.reflexive = true;
		this.reflexiveArc = arc;
		this.firstHead = firstHead;
		this.secondHead = secondHead;
		this.arcExtension = arcExtension;
	}
	
	public RelationModel(Line relationLine, Line firstHead, Line secondHead) {
		this.reflexive = false;
		this.relationLine = relationLine;
		this.firstHead = firstHead;
		this.secondHead = secondHead;
		//Line line1 = new Line(0, 0, 10, 10);
		//Line line2 = new Line(0, 0, 10, 10);
		//line1.getTransforms().add(new Rotate(45));
		//line2.getTransforms().add(new Rotate(315));
		//this.arrowGroup = new Group(line1, line2);

		
		
	}
		 
	
	public void drawRelation(Pane modelPane) {
		if (reflexive) {
			modelPane.getChildren().addAll(reflexiveArc, firstHead, secondHead, arcExtension);
		}else {
			modelPane.getChildren().addAll(relationLine, firstHead, secondHead);
		}
		
		//modelPane.getChildren().addAll(relationLine, arrowGroup);
		//modelPane.getChildren().add(arrowGroup);
	}
	
	/*
	public void relocateRelation(double translateX, double translateY) {
		firstWorldPoint = new Point2D(firstWorldPoint.getX() + translateX, firstWorldPoint.getY() + translateY);
		secondWorldPoint = new Point2D(secondWorldPoint.getX() + translateX, secondWorldPoint.getY() + translateY);
		relationLine = new Line(firstWorldPoint.getX(), firstWorldPoint.getY(), secondWorldPoint.getX(), secondWorldPoint.getY());
	}
	*/
	
	
	
	public void relocateRelation(double translateX, double translateY) {
		
		if (reflexive) {
			reflexiveArc = new Arc(reflexiveArc.getCenterX() + translateX, reflexiveArc.getCenterY() + translateY, 10, 10, 0, 270);
			
			reflexiveArc.setFill(Color.TRANSPARENT);
			reflexiveArc.setStroke(Color.BLACK);
			
			firstHead = new Line(firstHead.getStartX() + translateX, 
					firstHead.getStartY() + translateY, 
					firstHead.getEndX() + translateX, 
					firstHead.getEndY() + translateY);
			
			secondHead = new Line(secondHead.getStartX() + translateX, 
					secondHead.getStartY() + translateY, 
					secondHead.getEndX() + translateX, 
					secondHead.getEndY() + translateY);
			
			arcExtension = new Line(arcExtension.getStartX() + translateX, 
					arcExtension.getStartY() + translateY, 
					arcExtension.getEndX() + translateX, 
					arcExtension.getEndY() + translateY);
		}else {
			relationLine = new Line(relationLine.getStartX() + translateX, 
					relationLine.getStartY() + translateY, 
					relationLine.getEndX() + translateX, 
					relationLine.getEndY() + translateY);

			
			firstHead = new Line(firstHead.getStartX() + translateX, 
					firstHead.getStartY() + translateY, 
					firstHead.getEndX() + translateX, 
					firstHead.getEndY() + translateY);
			
			secondHead = new Line(secondHead.getStartX() + translateX, 
					secondHead.getStartY() + translateY, 
					secondHead.getEndX() + translateX, 
					secondHead.getEndY() + translateY);
			firstHead.getTransforms().add(new Rotate(30, relationLine.getEndX(), relationLine.getEndY()));
			secondHead.getTransforms().add(new Rotate(330, relationLine.getEndX(), relationLine.getEndY()));
		}
		

		
				
	}
	
	/*
	public void relocateRelation(double translateX, double translateY) {
		arrowGroup.relocate(arrowGroup.getLayoutX() + translateX, arrowGroup.getLayoutY() + translateY);
	}
	*/
}
