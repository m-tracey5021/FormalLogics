package visualModels;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import treeComponents.TernaryNode;

public class TernaryNodeModel {
	private Point2D location, pointAbove, pointBelow;
	private Label propositionLabel;
	private double nodeWidth, nodeHeight;
	
	public TernaryNodeModel(Point2D location, Point2D pointAbove, Point2D pointBelow, Label propositionLabel) {
		this.location = location;
		this.pointAbove = pointAbove;
		this.pointBelow = pointBelow;
		this.propositionLabel = propositionLabel;
		calculateNodeSize();
		centerNodeLabel();
		
	}
	
	// ========== GET
	
	public Point2D getLocation() {
		return this.location;
	}
	
	public Point2D getPointAbove() {
		return this.pointAbove;
	}
	
	public Point2D getPointBelow() {
		return this.pointBelow;
	}
	
	public Label getPropositionLabel() {
		return this.propositionLabel;
	}
	
	public double getNodeWidth() {
		return this.nodeWidth;
	}
	
	public double getNodeHeight() {
		return this.nodeHeight;
	}
	
	
	// ====== SET
	
	public void setLocation(Point2D point) {
		this.location = point;
	}
	
	public void setPointAbove(Point2D point) {
		this.pointAbove = point;
	}
	
	public void setPointBelow(Point2D point) {
		this.pointBelow = point; 
	}
	
	public void calculateNodeSize() {
		Pane dummyPane = new Pane();
		Scene dummyScene = new Scene(dummyPane);
		dummyPane.getChildren().add(propositionLabel);
		dummyPane.applyCss();
		dummyPane.layout();

		nodeWidth = propositionLabel.getWidth();
		nodeHeight = propositionLabel.getHeight();
	}
	
	public void centerNodeLabel() {
		propositionLabel.relocate(location.getX() - nodeWidth / 2, location.getY() - nodeHeight / 2);
		//propositionLabel.relocate(location.getX() - nodeWidth / 2, location.getY());
	}
	
	
	
	public void translateNodeModel(double translateX, double translateY) {
		
		location = new Point2D(location.getX() + translateX, location.getY() + translateY);
		pointAbove = new Point2D(pointAbove.getX() + translateX, pointAbove.getY() + translateY);
		pointBelow = new Point2D(pointBelow.getX() + translateX, pointBelow.getY() + translateY);
		propositionLabel.relocate(propositionLabel.getLayoutX() + translateX, propositionLabel.getLayoutY() + translateY);
		
		
		
	}
	
	
	
}	
