import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.simple.*;

import enums.AuxillaryOperatorType;
import enums.OperatorType;
import enums.Priority;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;


public class TernaryNode {
	private TernaryNode leftNode, rightNode, centerNode, parentNode; 
	private Proposition proposition;
	//private Point2D location, pointAbove, pointBelow;
	private TernaryNodeModel nodeModel;

	
	public TernaryNode() {
		
	}

	public TernaryNode(Proposition proposition) {
		this.proposition = proposition;
	}
	
	// ===== GET
	
	public TernaryNode getLeftNode() {
		return this.leftNode;
	}
	public TernaryNode getRightNode() {
		return this.rightNode;
	}
	public TernaryNode getCenterNode() {
		return this.centerNode;
	}
	public TernaryNode getParentNode() {
		return this.parentNode;
	}
	public Proposition getProposition(){
		return this.proposition;
	}
	//public Point2D[] getLocation() {
	//	Point2D[] points = new Point2D[] {this.location, this.pointAbove, this.pointBelow};
	//	return points;
	//}
	
	public TernaryNodeModel getNodeModel() {
		return this.nodeModel;
	}
	
	// ===== SET
	
	public void setLeftNode(TernaryNode left) {
		this.leftNode = left;
	}
	public void setRightNode(TernaryNode right) {
		this.rightNode = right;
	}
	public void setCenterNode(TernaryNode center) {
		this.centerNode = center;
	}
	public void setParentNode(TernaryNode parent) {
		this.parentNode = parent;
	}
	//public void setLocation(Point2D location) {
	//	this.location = location;
	//}
	
	
	
	
	// ================= NODE CREATION FUNCTIONS
	
	
	
	public void branchNode(Proposition firstProp, Proposition secondProp) {
		TernaryNode leftNode = new TernaryNode(firstProp);
		TernaryNode rightNode = new TernaryNode(secondProp);
		
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		leftNode.parentNode = this;
		rightNode.parentNode = this;

		
	}

	public void laneNode(Proposition proposition) {
		TernaryNode centerNode = new TernaryNode(proposition);
		
		this.centerNode = centerNode;
		centerNode.parentNode = this;

		
	}
	

	
	// =============== RECURSIVE GET FUNCTIONS
	
	public void getAllNodes(ArrayList<TernaryNode> allNodes) {
		allNodes.add(this);
		if (this.leftNode == null & this.rightNode == null & this.centerNode == null) {
			return;
		}else {
			if (this.leftNode != null) {
				this.leftNode.getAllNodes(allNodes);
			}
			if (this.rightNode != null){
				this.rightNode.getAllNodes(allNodes);
			}
			if (this.centerNode != null) {
				this.centerNode.getAllNodes(allNodes);
			}
			
			
		}
	}
	
	public void getEmptyNodes(ArrayList<TernaryNode> emptyNodes) {
		if (this.leftNode == null & this.rightNode == null & this.centerNode == null) {
			emptyNodes.add(this);
			return;
		}else {
			if (this.leftNode != null) {
				this.leftNode.getEmptyNodes(emptyNodes);
			}
			if (this.rightNode != null){
				this.rightNode.getEmptyNodes(emptyNodes);
			}
			if (this.centerNode != null) {
				this.centerNode.getEmptyNodes(emptyNodes);
			}
			
			
		}
	}
	
	public void getNodesToExpand(ArrayList<TernaryNode> nodesToExpand) {
		if (proposition.getIsExpanded() == false) {
			nodesToExpand.add(this);
		}
		
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		
		}else {
			if (this.leftNode != null) {
				this.leftNode.getNodesToExpand(nodesToExpand);
			}
			if (this.rightNode != null){
				this.rightNode.getNodesToExpand(nodesToExpand);
			}
			if (this.centerNode != null) {
				this.centerNode.getNodesToExpand(nodesToExpand);
			}
		}
	}
	
	public void getTreeLayers(int layerCount) {
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		
		}else {
			
			if (this.leftNode != null) {
				this.leftNode.getTreeLayers(layerCount);
			}
			if (this.rightNode != null){
				this.rightNode.getTreeLayers(layerCount);
			}
			if (this.centerNode != null) {
				this.centerNode.getTreeLayers(layerCount);
			}
		}
		layerCount += 1;
	}
	
	
	// ============= NODE SELECTION FUNCTIONS
	
	public TernaryNode getTreeStart() {
		if (this.parentNode != null) {
			return this.parentNode.getTreeStart();
		}else {
			return this;
		}

	}
	
	public TernaryNode getLeftMostNode() {
		if (this.leftNode != null) {
			return this.leftNode.getLeftMostNode();
		}else {
			return null;
		}
	}
	
	public TernaryNode getRightMostNode() {
		if (this.rightNode != null) {
			return this.rightNode.getRightMostNode();
		}else {
			return null;
		}
	}
	
	
	
	
	
	// ========= SORTING FUNCTIONS
	

	
	public Priority getRelativePriority(TernaryNode other) { 
		
		int thisPriority = proposition.getPriority();
		int otherPriority = other.getProposition().getPriority();
		
		if (thisPriority < otherPriority) {
			return Priority.HIGHER;
		}else if (thisPriority > otherPriority) {
			return Priority.LOWER;
		}else {
			return Priority.EQUAL;
		}

	}
	
	
	// =========== GEOMETRY FUNCTIONS
	
	
	
	
	public void setupNodeModels() {
		nodeModel = new TernaryNodeModel(new Label(proposition.toString()));
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		
		}else {
			if (this.leftNode != null) {
				this.leftNode.setupNodeModels();
			}
			if (this.rightNode != null){
				this.rightNode.setupNodeModels();
			}
			if (this.centerNode != null) {
				this.centerNode.setupNodeModels();
			}
		}
	}
	
	
	public void setupGeometry(double tmpX, double tmpY, double horizontalSpacing, double verticalSpacing, ArrayList<Line> lines) {
		
		nodeModel.setLocation(new Point2D(tmpX, tmpY));
		nodeModel.setPointAbove(new Point2D(tmpX, tmpY - verticalSpacing));
		nodeModel.setPointBelow(new Point2D(tmpX, tmpY + verticalSpacing));
		nodeModel.relocateNodeLabel();

		if (parentNode != null) {
			lines.add(new Line(parentNode.getNodeModel().getPointBelow().getX(), 
					parentNode.getNodeModel().getPointBelow().getY(), 
					parentNode.getNodeModel().getPointBelow().getX(), 
					parentNode.getNodeModel().getPointBelow().getY() - (verticalSpacing / 4)));
			lines.add(new Line(nodeModel.getPointAbove().getX(), 
					nodeModel.getPointAbove().getY(), 
					parentNode.getNodeModel().getPointBelow().getX(), 
					parentNode.getNodeModel().getPointBelow().getY()));
			lines.add(new Line(nodeModel.getPointAbove().getX(), 
					nodeModel.getPointAbove().getY(), 
					nodeModel.getPointAbove().getX(), 
					nodeModel.getPointAbove().getY() + (verticalSpacing / 4)));
		}

		double newX1, newX2, newX3;
		double newY1, newY2, newY3;

		newX1 = getNodeModel().getPointBelow().getX() - horizontalSpacing;
		newX2 = getNodeModel().getPointBelow().getX() + horizontalSpacing;
		newX3 = getNodeModel().getPointBelow().getX();
		
		newY1 = getNodeModel().getPointBelow().getY() + verticalSpacing * 2;
		newY2 = getNodeModel().getPointBelow().getY() + verticalSpacing * 2;
		newY3 = getNodeModel().getPointBelow().getY() + verticalSpacing * 2;
		
		if (leftNode != null) {
			leftNode.setupGeometry(newX1, newY1, horizontalSpacing / 2, verticalSpacing, lines);
		}
		if (rightNode != null) {
			rightNode.setupGeometry(newX2, newY2, horizontalSpacing / 2, verticalSpacing, lines);
		}
		if (centerNode != null) {
			centerNode.setupGeometry(newX3, newY3, horizontalSpacing, verticalSpacing, lines); // dont need to make horizontal spacing less if center node
		}
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		}
	}
	
	
	
	
	public String toString() {
		return proposition.toString();
	}
	

}
