package treeComponents;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.simple.*;


import propositions.*;
import visualModels.TernaryNodeModel;
import enums.AuxillaryOperatorType;
import enums.OperatorType;
import enums.Priority;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
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
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		}else {
			if (leftNode != null) {
				leftNode.getAllNodes(allNodes);
			}
			if (rightNode != null){
				rightNode.getAllNodes(allNodes);
			}
			if (centerNode != null) {
				centerNode.getAllNodes(allNodes);
			}
			
			
		}
	}
	
	public void getEmptyNodes(ArrayList<TernaryNode> emptyNodes) {
		if (leftNode == null & rightNode == null & centerNode == null) {
			emptyNodes.add(this);
			return;
		}else {
			if (leftNode != null) {
				leftNode.getEmptyNodes(emptyNodes);
			}
			if (rightNode != null){
				rightNode.getEmptyNodes(emptyNodes);
			}
			if (centerNode != null) {
				centerNode.getEmptyNodes(emptyNodes);
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
			if (leftNode != null) {
				leftNode.getNodesToExpand(nodesToExpand);
			}
			if (rightNode != null){
				rightNode.getNodesToExpand(nodesToExpand);
			}
			if (centerNode != null) {
				centerNode.getNodesToExpand(nodesToExpand);
			}
		}
	}
	
	public void getNodesOnBranch(ArrayList<TernaryNode> nodesOnBranch) {
		nodesOnBranch.add(this);
		if (parentNode != null) {
			parentNode.getNodesOnBranch(nodesOnBranch);
		}else {
			return;
		}
	}
	
	public void getTreeLayers(int layerCount) {
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		
		}else {
			
			if (leftNode != null) {
				leftNode.getTreeLayers(layerCount);
			}
			if (rightNode != null){
				rightNode.getTreeLayers(layerCount);
			}
			if (centerNode != null) {
				centerNode.getTreeLayers(layerCount);
			}
		}
		layerCount += 1;
	}
	
	public void getNodeSizes(ArrayList<double[]> sizes) {
		Label label = new Label(proposition.toString());
		double sizesAtThisNode[] = calculateNodeSize(label);
		sizes.add(sizesAtThisNode);
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		
		}else {
			if (leftNode != null) {
				leftNode.getNodeSizes(sizes);
			}
			if (rightNode != null){
				rightNode.getNodeSizes(sizes);
			}
			if (centerNode != null) {
				centerNode.getNodeSizes(sizes);
			}
		}
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
	
	public TernaryNode getEmptyCenterNode() {
		if (this.centerNode != null) {
			return this.centerNode.getEmptyCenterNode();
		}else {
			return this;
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
	
	
	public double[] calculateNodeSize(Label label) {
		Pane dummyPane = new Pane();
		Scene dummyScene = new Scene(dummyPane);
		dummyPane.getChildren().add(label);
		dummyPane.applyCss();
		dummyPane.layout();

		double nodeWidth = label.getWidth();
		double nodeHeight = label.getHeight();
		double[] sizes = new double[] {nodeWidth, nodeHeight};
		return sizes;
	}
	
	
	/*
	public void generateNodeModels() {
		nodeModel = new TernaryNodeModel(this);
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		
		}else {
			if (this.leftNode != null) {
				this.leftNode.generateNodeModels();
			}
			if (this.rightNode != null){
				this.rightNode.generateNodeModels();
			}
			if (this.centerNode != null) {
				this.centerNode.generateNodeModels();
			}
		}
	}
	
	*/
	/*
	public void setupGeometry(double tmpX, double tmpY, double horizontalSpacing, double verticalSpacing, ArrayList<Line> lines) {
		
		nodeModel = new TernaryNodeModel(new Point2D(tmpX, tmpY), 
				new Point2D(tmpX, tmpY - verticalSpacing), 
				new Point2D(tmpX, tmpY + verticalSpacing), 
				new Label(proposition.toString()));
		//nodeModels.add(nodeModel);
		//nodeModel.setLocation(new Point2D(tmpX, tmpY));
		//nodeModel.setPointAbove(new Point2D(tmpX, tmpY - verticalSpacing));
		//nodeModel.setPointBelow(new Point2D(tmpX, tmpY + verticalSpacing));
		//nodeModel.centerNodeLabel();

		if (pointBelowParent != null) {
			lines.add(new Line(pointBelowParent.getX(), 
					pointBelowParent.getY(), 
					pointBelowParent.getX(), 
					pointBelowParent.getY() - (verticalSpacing / 4)));
			lines.add(new Line(nodeModel.getPointAbove().getX(), 
					nodeModel.getPointAbove().getY(), 
					pointBelowParent.getX(), 
					pointBelowParent.getY()));
			lines.add(new Line(nodeModel.getPointAbove().getX(), 
					nodeModel.getPointAbove().getY(), 
					nodeModel.getPointAbove().getX(), 
					nodeModel.getPointAbove().getY() + (verticalSpacing / 4)));
		}

		double newX1, newX2, newX3;
		double newY1, newY2, newY3;

		newX1 = nodeModel.getPointBelow().getX() - horizontalSpacing;
		newX2 = nodeModel.getPointBelow().getX() + horizontalSpacing;
		newX3 = nodeModel.getPointBelow().getX();
		
		newY1 = nodeModel.getPointBelow().getY() + verticalSpacing * 2;
		newY2 = nodeModel.getPointBelow().getY() + verticalSpacing * 2;
		newY3 = nodeModel.getPointBelow().getY() + verticalSpacing * 2;
		
		if (leftNode != null) {
			leftNode.setupGeometry(newX1, newY1, horizontalSpacing / 2, verticalSpacing, nodeModel.getPointBelow(), nodeModels, lines);
		}
		if (rightNode != null) {
			rightNode.setupGeometry(newX2, newY2, horizontalSpacing / 2, verticalSpacing, nodeModel.getPointBelow(), nodeModels, lines);
		}
		if (centerNode != null) {
			centerNode.setupGeometry(newX3, newY3, horizontalSpacing, verticalSpacing, nodeModel.getPointBelow(), nodeModels, lines); // dont need to make horizontal spacing less if center node
		}
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		}
	}
	*/
	
	
	public void generateNodeModels(double tmpX, double tmpY, double horizontalSpacing, double verticalSpacing, ArrayList<Line> lines) {
		//Label nodeLabel = new Label(proposition.toString());
		nodeModel = new TernaryNodeModel(new Point2D(tmpX, tmpY), 
				new Point2D(tmpX, tmpY - verticalSpacing), 
				new Point2D(tmpX, tmpY + verticalSpacing), 
				new Label(proposition.toString()));
		//nodeModels.add(nodeModel);
		//nodeModel.setLocation(new Point2D(tmpX, tmpY));
		//nodeModel.setPointAbove(new Point2D(tmpX, tmpY - verticalSpacing));
		//nodeModel.setPointBelow(new Point2D(tmpX, tmpY + verticalSpacing));
		//nodeModel.centerNodeLabel();

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
			leftNode.generateNodeModels(newX1, newY1, horizontalSpacing / 2, verticalSpacing, lines);
		}
		if (rightNode != null) {
			rightNode.generateNodeModels(newX2, newY2, horizontalSpacing / 2, verticalSpacing, lines);
		}
		if (centerNode != null) {
			centerNode.generateNodeModels(newX3, newY3, horizontalSpacing, verticalSpacing, lines); // dont need to make horizontal spacing less if center node
		}
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		}
	}
	
	
	
	
	public String toString() {
		return proposition.toString();
	}
	

}
