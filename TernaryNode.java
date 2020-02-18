import java.util.ArrayList;
import java.util.Arrays;

import enums.AuxillaryOperatorType;
import enums.OperatorType;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class TernaryNode {
	private TernaryNode leftNode, rightNode, centerNode, parentNode, rootNode;
	private ArrayList<Proposition> propositions;
	private Point2D location, pointAbove, pointBelow;
	
	public TernaryNode() {
		
	}
	public TernaryNode(ArrayList<Proposition> propositions) {
		this.propositions = propositions;
		this.rootNode = getRoot();
	}
	public TernaryNode(Proposition proposition) {
		this.propositions = new ArrayList<Proposition>();
		this.propositions.add(proposition);
		this.rootNode = getRoot();
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
	public TernaryNode getRootNode() {
		return this.rootNode;
	}
	public ArrayList<Proposition> getPropositions(){
		return this.propositions;
	}
	public Point2D[] getLocation() {
		Point2D[] points = new Point2D[] {this.location, this.pointAbove, this.pointBelow};
		return points;
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
	public void setLocation(Point2D location) {
		this.location = location;
	}
	
	public void expand() {
		for (Proposition prop : propositions) {
			if (prop instanceof CompoundProposition) {

				// ========= setup copied props
				CompoundProposition compoundProp = ((CompoundProposition) prop);
				//CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
				//CompoundProposition duplicateCopiedCompoundProp = (CompoundProposition)compoundProp.copy();
				
				OperatorType operatorType = compoundProp.getOp().getOperator();
				
				/*
				ArrayList<Proposition> propsCopyForLeft = new ArrayList<Proposition>();
				ArrayList<Proposition> propsCopyForRight = new ArrayList<Proposition>();
				for (Proposition propToCopy : propositions) {
					Proposition copiedProp = propToCopy.copy();
					propsCopyForLeft.add(copiedProp);
					propsCopyForRight.add(copiedProp);
				}
				*/
				
				ArrayList<TernaryNode> emptyNodes = initEmptyNodes();
				
				if (compoundProp.getAuxOps().size() != 0) {
					if (compoundProp.getAuxOps().get(0).getAuxOp() == AuxillaryOperatorType.NEGATION) {
						if (operatorType == OperatorType.AND) {
							
							for (TernaryNode node : emptyNodes) {
								CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
								copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								copiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								node.branch(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
							}
							
							
							//propsCopyForLeft.add(copiedCompoundProp.getFirstOperand());
							//propsCopyForRight.add(copiedCompoundProp.getSecondOperand());
							
							
						}else if (operatorType == OperatorType.OR) {
							
							for (TernaryNode node : emptyNodes) {
								CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
								copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								copiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								node.lane(copiedCompoundProp.getFirstOperand());
								node.getCenterNode().lane(copiedCompoundProp.getSecondOperand());
							}
							
							
							
						}else if (operatorType == OperatorType.IF) {
							for (TernaryNode node : emptyNodes) {
								CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
								copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								node.lane(copiedCompoundProp.getFirstOperand());
								node.getCenterNode().lane(copiedCompoundProp.getSecondOperand());
							}
							
							

						}else {
							// TO DO
							
							
							//propsCopyForLeft.add(copiedCompoundProp.getFirstOperand());
							//propsCopyForLeft.add(copiedCompoundProp.getSecondOperand());
							
							//propsCopyForRight.add(duplicateCopiedCompoundProp.getFirstOperand());
							//propsCopyForRight.add(duplicateCopiedCompoundProp.getSecondOperand());
							
							
							for (TernaryNode node : emptyNodes) {
								CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
								CompoundProposition duplicateCopiedCompoundProp = (CompoundProposition)compoundProp.copy();
								copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								duplicateCopiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								
								
								//ArrayList<Proposition> firstProps = new ArrayList<Proposition>(Arrays.asList(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand()));
								//ArrayList<Proposition> secondProps = new ArrayList<Proposition>(Arrays.asList(duplicateCopiedCompoundProp.getFirstOperand(), duplicateCopiedCompoundProp.getSecondOperand()));      
								node.branch(copiedCompoundProp.getFirstOperand(), duplicateCopiedCompoundProp.getFirstOperand());
								node.getLeftNode().lane(copiedCompoundProp.getSecondOperand());
								node.getRightNode().lane(duplicateCopiedCompoundProp.getSecondOperand());
								
							}
							

							
						}
					}
				
				}else {
					if (operatorType == OperatorType.AND) {
						for (TernaryNode node : emptyNodes) {
							CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
							node.lane(copiedCompoundProp.getFirstOperand());
							node.getCenterNode().lane(copiedCompoundProp.getSecondOperand());
						}
					}else if (operatorType == OperatorType.OR) {
						for (TernaryNode node : emptyNodes) {
							CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
							node.branch(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}

						
					}else if (operatorType == OperatorType.IF) {
						
						for (TernaryNode node : emptyNodes) {
							CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
							copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							node.branch(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}


					}else {
						// TO DO
						for (TernaryNode node : emptyNodes) {
							CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
							CompoundProposition duplicateCopiedCompoundProp = (CompoundProposition)compoundProp.copy();
							copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							copiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							//ArrayList<Proposition> firstProps = new ArrayList<Proposition>(Arrays.asList(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand()));
							//ArrayList<Proposition> secondProps = new ArrayList<Proposition>(Arrays.asList(duplicateCopiedCompoundProp.getFirstOperand(), duplicateCopiedCompoundProp.getSecondOperand()));
							//node.branch(firstProps, secondProps);
							node.branch(copiedCompoundProp.getFirstOperand(), duplicateCopiedCompoundProp.getFirstOperand());
							node.getLeftNode().lane(copiedCompoundProp.getSecondOperand());
							node.getRightNode().lane(duplicateCopiedCompoundProp.getSecondOperand());
						}
					}
				}
				
				
				
				
			}else {
				return;
			}
		}
	}
	public void branch(Proposition firstProp, Proposition secondProp) {
		TernaryNode leftNode = new TernaryNode(firstProp);
		TernaryNode rightNode = new TernaryNode(secondProp);
		
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		leftNode.parentNode = this;
		rightNode.parentNode = this;
		leftNode.expand();
		rightNode.expand();
	}
	/*
	public void branch(ArrayList<Proposition> firstProps, ArrayList<Proposition> secondProps) {
		TernaryNode leftNode = new TernaryNode(firstProps);
		TernaryNode rightNode = new TernaryNode(secondProps);
		
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		leftNode.parentNode = this;
		rightNode.parentNode = this;
		leftNode.expand();
		rightNode.expand();
	}
	*/
	public void lane(Proposition proposition) {
		TernaryNode centerNode = new TernaryNode(proposition);
		
		this.centerNode = centerNode;
		centerNode.parentNode = this;
		centerNode.expand();
	}
	

	
	// ======== REMEMBER TO INIT LIST BEFORE ENTERING FUNCTION
	public void getEmptyNode(ArrayList<TernaryNode> emptyNodes) {
		if (this.leftNode == null & this.rightNode == null & this.centerNode == null) {
			emptyNodes.add(this);
			return;
		}else {
			if (this.leftNode != null) {
				this.leftNode.getEmptyNode(emptyNodes);
			}
			if (this.rightNode != null){
				this.rightNode.getEmptyNode(emptyNodes);
			}
			if (this.centerNode != null) {
				this.centerNode.getEmptyNode(emptyNodes);
			}
			
			
		}
	}
	
	public ArrayList<TernaryNode> initEmptyNodes(){
		ArrayList<TernaryNode> emptyNodes = new ArrayList<TernaryNode>();
		this.getEmptyNode(emptyNodes);
		return emptyNodes;
	}
	
	public void getAllPropositions(ArrayList<Proposition> allPropositions) {
		allPropositions.addAll(this.propositions);
		
		if (this.leftNode != null) {
			this.leftNode.getAllPropositions(allPropositions);
		}
		if (this.rightNode != null){
			this.rightNode.getAllPropositions(allPropositions);
		}
		if (this.centerNode != null) {
			this.centerNode.getAllPropositions(allPropositions);
		}
	}
	
	public ArrayList<Proposition> initAllPropositions(){
		ArrayList<Proposition> allPropositions = new ArrayList<Proposition>();
		this.getAllPropositions(allPropositions);
		return allPropositions;
	}
	
	public TernaryNode getRoot() {
		if (this.parentNode != null) {
			return this.parentNode.getRootNode();
		}else {
			return this;
		}

	}
	
	
	
	public void drawTernaryTree(double rootX, double rootY, double horizontalSpacing, double verticalSpacing, Pane modelPane) {
		rootNode.setupGeometry(rootX, rootY, horizontalSpacing, verticalSpacing, modelPane);
	}
	
	public void setupGeometry(double tmpX, double tmpY, double horizontalSpacing, double verticalSpacing, Pane modelPane) {
		this.location = new Point2D(tmpX, tmpY);
		this.pointAbove = new Point2D(tmpX, tmpY - verticalSpacing);
		

		
		double duplicateX, duplicateY;
		
		duplicateX = tmpX;
		duplicateY = tmpY;
		
		//double verticalSpacing = 20.0;
		Label propLabel;
		if (parentNode != null) {
			Line connection = new Line(pointAbove.getX(), pointAbove.getY(), parentNode.getLocation()[2].getX(), parentNode.getLocation()[2].getY());
			
			for (Proposition prop : propositions) {
				propLabel = new Label(prop.toString());
				
				modelPane.getChildren().add(propLabel);
				modelPane.applyCss();
				modelPane.layout();
				
				propLabel.relocate(duplicateX - propLabel.getWidth() / 2, duplicateY - propLabel.getHeight() / 2);
				duplicateY += verticalSpacing;
				

			}
			
			modelPane.getChildren().add(connection);
		}else {
			
			for (Proposition prop : propositions) {
				propLabel = new Label(prop.toString());
				
				modelPane.getChildren().add(propLabel);
				modelPane.applyCss();
				modelPane.layout();
				
				propLabel.relocate(duplicateX - propLabel.getWidth() / 2, duplicateY - propLabel.getHeight() / 2);
				duplicateY += verticalSpacing;
				

			}
		}
		
		// this will set the point below even if there are more than one label
		
		this.pointBelow = new Point2D(duplicateX, duplicateY);
		

		double newX1, newX2, newX3;
		double newY1, newY2, newY3;
		

		
		newX1 = pointBelow.getX() - horizontalSpacing;
		newX2 = pointBelow.getX() + horizontalSpacing;
		newX3 = pointBelow.getX();
		
		newY1 = pointBelow.getY() + verticalSpacing * 2;
		newY2 = pointBelow.getY() + verticalSpacing * 2;
		newY3 = pointBelow.getY() + verticalSpacing * 2;
		
		if (leftNode != null) {
			leftNode.setupGeometry(newX1, newY1, horizontalSpacing / 2, verticalSpacing, modelPane);
		}
		if (rightNode != null) {
			rightNode.setupGeometry(newX2, newY2, horizontalSpacing / 2, verticalSpacing, modelPane);
		}
		if (centerNode != null) {
			centerNode.setupGeometry(newX3, newY3, horizontalSpacing / 2, verticalSpacing, modelPane);
		}
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		}
	}
	
	
	public String toString() {
		String returnStr = "";
		for (Proposition prop : propositions) {
			returnStr += prop.toString();
		}
		return returnStr;
	}
	

}
