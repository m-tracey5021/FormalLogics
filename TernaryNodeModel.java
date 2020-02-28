import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class TernaryNodeModel {
	private Point2D location, pointAbove, pointBelow;
	private Label propositionLabel;
	private double nodeWidth, nodeHeight;
	
	public TernaryNodeModel(Label propositionLabel) {
		this.propositionLabel = propositionLabel;
		calculateNodeSize();
	}
	
	public TernaryNodeModel(Point2D location, Point2D pointAbove, Point2D pointBelow, Label propositionLabel) {
		this.location = location;
		this.pointAbove = pointAbove;
		this.pointBelow = pointBelow;
		this.propositionLabel = propositionLabel;
		calculateNodeSize();
		relocateNodeLabel();
		
	}
	
	
	
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
		//dummyPane.setPrefSize(1000, 1000);
		
		
		
		Scene dummyScene = new Scene(dummyPane);
		dummyPane.getChildren().add(propositionLabel);
		dummyPane.applyCss();
		dummyPane.layout();

		nodeWidth = propositionLabel.getWidth();
		nodeHeight = propositionLabel.getHeight();
	}
	
	public void relocateNodeLabel() {
		propositionLabel.relocate(location.getX() - nodeWidth / 2, location.getY() - nodeHeight / 2);
	}
}	
