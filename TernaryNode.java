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
import priorityLists.ClassicalOperatorPriority;
import priorityLists.ModalAuxOpPriority;

public class TernaryNode {
	private TernaryNode leftNode, rightNode, centerNode, parentNode; // rootNode;
	private Proposition proposition;
	private Point2D location, pointAbove, pointBelow;

	
	public TernaryNode() {
		
	}

	public TernaryNode(Proposition proposition) {
		this.proposition = proposition;

		//this.rootNode = getRoot();
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
	//public TernaryNode getRootNode() {
	//	return this.rootNode;
	//}
	public Proposition getProposition(){
		return this.proposition;
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
		ArrayList<TernaryNode> nodesToExpand = sortNodesToExpand();
		
		for (TernaryNode nodeToExpand : nodesToExpand) {
			if (proposition instanceof CompoundProposition) {
				

				// ========= setup copied props
				CompoundProposition compoundProp = ((CompoundProposition) proposition);
				
				Operator operator = compoundProp.getOperator();
				AuxillaryOperator auxOp = compoundProp.getAuxOps().get(0);
				
				OperatorType operatorType = operator.getOperatorType();
				AuxillaryOperatorType auxOpType = auxOp.getAuxOpType();
				
				ArrayList<TernaryNode> emptyNodes = initEmptyNodesDownStream();
				
				if (compoundProp.getIsExpanded() == false & auxOpType != AuxillaryOperatorType.NONE) {
					
					if (auxOpType == AuxillaryOperatorType.NEGATION) {
						
						compoundProp.setIsExpanded(true);
						
						if (operatorType == OperatorType.AND) {
							for (TernaryNode node : emptyNodes) {
								CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
								copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								copiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								node.branch(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand(), true);
							}
						}else if (operatorType == OperatorType.OR) {
							for (TernaryNode node : emptyNodes) {
								CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
								copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								copiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								node.lane(copiedCompoundProp.getFirstOperand(), true);
								node.getCenterNode().lane(copiedCompoundProp.getSecondOperand(), true);
							}
						}else if (operatorType == OperatorType.IF) {
							for (TernaryNode node : emptyNodes) {
								CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
								copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								node.lane(copiedCompoundProp.getFirstOperand(), true);
								node.getCenterNode().lane(copiedCompoundProp.getSecondOperand(), true);
							}
						}else {
							for (TernaryNode node : emptyNodes) {
								CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
								CompoundProposition duplicateCopiedCompoundProp = (CompoundProposition)compoundProp.copy();
								copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
								duplicateCopiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));    
								node.branch(copiedCompoundProp.getFirstOperand(), duplicateCopiedCompoundProp.getFirstOperand(), true);
								node.getLeftNode().lane(copiedCompoundProp.getSecondOperand(), true);
								node.getRightNode().lane(duplicateCopiedCompoundProp.getSecondOperand(), true);
							}
						}
					}else {
						return; // if has auxOp and isnt expanded but auxOp is modal
					}
				}else if (compoundProp.getIsExpanded() == false & auxOpType == AuxillaryOperatorType.NONE) {

					compoundProp.setIsExpanded(true);
					
					if (operatorType == OperatorType.AND) {
						for (TernaryNode node : emptyNodes) {
							CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
							node.lane(copiedCompoundProp.getFirstOperand(), true);
							node.getCenterNode().lane(copiedCompoundProp.getSecondOperand(), true);
						}
					}else if (operatorType == OperatorType.OR) {
						for (TernaryNode node : emptyNodes) {
							CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
							node.branch(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand(), true);
						}

						
					}else if (operatorType == OperatorType.IF) {
						
						for (TernaryNode node : emptyNodes) {
							CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
							copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							node.branch(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand(), true);
						}


					}else {

						for (TernaryNode node : emptyNodes) {
							CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
							CompoundProposition duplicateCopiedCompoundProp = (CompoundProposition)compoundProp.copy();
							copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							copiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							node.branch(copiedCompoundProp.getFirstOperand(), duplicateCopiedCompoundProp.getFirstOperand(), true);
							node.getLeftNode().lane(copiedCompoundProp.getSecondOperand(), true);
							node.getRightNode().lane(duplicateCopiedCompoundProp.getSecondOperand(), true);
						}
					}
				} else {
					skipExpandedNode(); // just make sure that there arent any more unexpanded nodes down the chain
					return; // if node is already expanded
				}
				
				
				
				
				
			}else {
				skipExpandedNode();
				return; // if node is not compound
			}
		}
		
	}
	
	/*
	public void expandWithinWorld(World parentWorld) {
		ArrayList<TernaryNode> nodesToExpand = sortNodesToExpand();
		//ArrayList<TernaryNode> emptyNodesDownStream = initEmptyNodesDownStream();
		if (nodesToExpand.size() == 0) {
			return;
		}else {
			
		}
		TernaryNode highestPriorityNode = nodesToExpand.get(0);
		ArrayList<TernaryNode> emptyNodesDownStream = highestPriorityNode.initEmptyNodesDownStream();
		if (highestPriorityNode.getProposition() instanceof CompoundProposition) {
			

			// ========= setup copied props
			CompoundProposition compoundProp = ((CompoundProposition) highestPriorityNode.getProposition());
			compoundProp.setIsExpanded(true);
			
			CompoundProposition copiedCompoundProp = (CompoundProposition)compoundProp.copy();
			CompoundProposition duplicateCopiedCompoundProp = (CompoundProposition)compoundProp.copy();
			
			Operator operator = copiedCompoundProp.getOperator();
			AuxillaryOperator auxOp = copiedCompoundProp.getAuxOps().get(0);
			
			OperatorType operatorType = operator.getOperatorType();
			AuxillaryOperatorType auxOpType = auxOp.getAuxOpType();
			
			
			
			
			if (auxOp.isClassical()) {
				if (auxOpType == AuxillaryOperatorType.NEGATION) { // if classically negated
					
					
					if (operatorType == OperatorType.AND) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							copiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}
					}else if (operatorType == OperatorType.OR) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							copiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							emptyNode.laneNode(copiedCompoundProp.getFirstOperand());
							emptyNode.getCenterNode().laneNode(copiedCompoundProp.getSecondOperand());
						}
					}else if (operatorType == OperatorType.IF) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							emptyNode.laneNode(copiedCompoundProp.getFirstOperand());
							emptyNode.getCenterNode().laneNode(copiedCompoundProp.getSecondOperand());
						}
					}else {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							duplicateCopiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));    
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), duplicateCopiedCompoundProp.getFirstOperand());
							emptyNode.getLeftNode().laneNode(copiedCompoundProp.getSecondOperand());
							emptyNode.getRightNode().laneNode(duplicateCopiedCompoundProp.getSecondOperand());
						}
					}
				}else { // if classical and no auxOp
					
					if (operatorType == OperatorType.AND) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							emptyNode.laneNode(copiedCompoundProp.getFirstOperand());
							emptyNode.getCenterNode().laneNode(copiedCompoundProp.getSecondOperand());
						}
						//expandWithinWorld(parentWorld);
					}else if (operatorType == OperatorType.OR) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}
						//expandWithinWorld(parentWorld);

						
					}else if (operatorType == OperatorType.IF) {
						
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}
						//expandWithinWorld(parentWorld);


					}else {

						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							copiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							emptyNode.branch(copiedCompoundProp.getFirstOperand(), duplicateCopiedCompoundProp.getFirstOperand(), true);
							emptyNode.getLeftNode().laneNode(copiedCompoundProp.getSecondOperand());
							emptyNode.getRightNode().laneNode(duplicateCopiedCompoundProp.getSecondOperand());
						}
						//expandWithinWorld(parentWorld);
					}
				}
			}else if (auxOp.isModal()) {
				expandForModal(parentWorld, copiedCompoundProp, auxOpType);
				//expandWithinWorld(parentWorld);

			}else if (auxOp.isPredicate()) {
				
			}else {
				// something went wrong with auxOp
			}
			
			
			
			
			
		}else {
			AtomicProposition atomicProp = ((AtomicProposition) highestPriorityNode.getProposition());
			atomicProp.setIsExpanded(true);
			
			AtomicProposition copiedAtomicProp = (AtomicProposition)atomicProp.copy();

			AuxillaryOperator auxOp = copiedAtomicProp.getAuxOps().get(0);
			
			AuxillaryOperatorType auxOpType = auxOp.getAuxOpType();
			
			if (auxOp.isClassical()) {
				
				//expandWithinWorld(parentWorld);
				//skipExpandedNodeWithinUniverse(parentWorld);
				//return;
			}else if (auxOp.isModal()) {
				
				expandForModal(parentWorld, copiedAtomicProp, auxOpType);
				//expandWithinWorld(parentWorld);
				//return;
			}else if (auxOp.isPredicate()) {
				
				
				//return;
			}else {
				// something went wrong with auxOp
			}
		}
		expandWithinWorld(parentWorld);
		
	}
	
	*/
	
	/*
	public void expandForModal(World parentWorld, Proposition copiedProp, AuxillaryOperatorType auxOpType) {
		Universe parentUniverse = parentWorld.getParentUniverse();
		ArrayList<World> relatedWorlds = parentWorld.getRelatedWorlds();
		if (auxOpType == AuxillaryOperatorType.POSSIBLE || auxOpType == AuxillaryOperatorType.NOTNECESSARY) {
			copiedProp.removeRelevantAuxOp();
			World newWorld = new World(parentUniverse, new TernaryNode(copiedProp));
			newWorld.getRelatingWorlds().add(parentWorld);
			relatedWorlds.add(newWorld);
			parentUniverse.adjustRelationsForUniverseProperties();
			newWorld.expand2();
		}else if (auxOpType == AuxillaryOperatorType.NECESSARY || auxOpType == AuxillaryOperatorType.NOTPOSSIBLE) {
			for (World relatedWorld : relatedWorlds) {
				ArrayList<TernaryNode> emptyNodesAtRelatedWorld = relatedWorld.getNode().initEmptyNodesDownStream();
				for (TernaryNode emptyNode : emptyNodesAtRelatedWorld) {
					copiedProp.removeRelevantAuxOp();
					emptyNode.lane(copiedProp, false);
					
				}
				relatedWorld.expand2();
			}
		}
	}
	*/
	
	public void skipExpandedNode() {
		if (this.leftNode == null & this.rightNode == null & this.centerNode == null) {
			return;
		}else {
			if (leftNode != null) {
				leftNode.expand();
			}
			if (rightNode != null) {
				rightNode.expand();
			}
			if (centerNode != null) {
				centerNode.expand();
			}
		}
		
	}
	
	/*
	public void skipExpandedNodeWithinUniverse(World parentWorld) {
		if (this.leftNode == null & this.rightNode == null & this.centerNode == null) {
			return;
		}else {
			if (leftNode != null) {
				leftNode.expandWithinWorld(parentWorld);
			}
			if (rightNode != null) {
				rightNode.expandWithinWorld(parentWorld);
			}
			if (centerNode != null) {
				centerNode.expandWithinWorld(parentWorld);
			}
		}
		
	}
	*/
	
	// ================= NODE CREATION FUNCTIONS
	
	public void branch(Proposition firstProp, Proposition secondProp, boolean recurse) {
		TernaryNode leftNode = new TernaryNode(firstProp);
		TernaryNode rightNode = new TernaryNode(secondProp);
		
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		leftNode.parentNode = this;
		rightNode.parentNode = this;
		if (recurse) {
			leftNode.expand();
			rightNode.expand();
		}
		
	}

	public void lane(Proposition proposition, boolean recurse) {
		TernaryNode centerNode = new TernaryNode(proposition);
		
		this.centerNode = centerNode;
		centerNode.parentNode = this;
		if (recurse) {
			centerNode.expand();
		}
		
	}
	
	
	/*
	public void branch(World parentWorld, Proposition firstProp, Proposition secondProp, boolean recurse) {
		TernaryNode leftNode = new TernaryNode(firstProp);
		TernaryNode rightNode = new TernaryNode(secondProp);
		
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		leftNode.parentNode = this;
		rightNode.parentNode = this;
		if (recurse) {
			leftNode.expandWithinWorld(parentWorld);
			rightNode.expandWithinWorld(parentWorld);
		}
		
	}

	public void lane(World parentWorld, Proposition proposition, boolean recurse) {
		TernaryNode centerNode = new TernaryNode(proposition);
		
		this.centerNode = centerNode;
		centerNode.parentNode = this;
		if (recurse) {
			centerNode.expandWithinWorld(parentWorld);
		}
		
	}
	*/
	
	
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

	private void getEmptyNode(ArrayList<TernaryNode> emptyNodes) {
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
	
	private void getNodeToExpand(ArrayList<TernaryNode> nodesToExpand) {
		if (proposition.getIsExpanded() == false) {
			nodesToExpand.add(this);
		}
		
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		
		}else {
			if (this.leftNode != null) {
				this.leftNode.getNodeToExpand(nodesToExpand);
			}
			if (this.rightNode != null){
				this.rightNode.getNodeToExpand(nodesToExpand);
			}
			if (this.centerNode != null) {
				this.centerNode.getNodeToExpand(nodesToExpand);
			}
		}
	}
	

	
	private void getAllPropositions(ArrayList<Proposition> allPropositions) {
		allPropositions.add(proposition);
		
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
	
	public TernaryNode getRoot() {
		if (this.parentNode != null) {
			return this.parentNode.getRoot();
		}else {
			return this;
		}

	}
	
	// =============== INIT NODE LIST FUNCTIONS
	
	public ArrayList<TernaryNode> initAllEmptyNodes(){
		ArrayList<TernaryNode> emptyNodes = new ArrayList<TernaryNode>();
		this.getRoot().getEmptyNode(emptyNodes);
		return emptyNodes;
	}
	
	public ArrayList<TernaryNode> initEmptyNodesDownStream(){
		ArrayList<TernaryNode> emptyNodesDownStream = new ArrayList<TernaryNode>();
		this.getEmptyNode(emptyNodesDownStream);
		return emptyNodesDownStream;
	}
	
	
	
	public ArrayList<TernaryNode> initNodesToExpand(){
		ArrayList<TernaryNode> nodesToExpand = new ArrayList<TernaryNode>();
		this.getRoot().getNodeToExpand(nodesToExpand);
		return nodesToExpand;
	}
	

	
	
	
	public ArrayList<Proposition> initAllPropositions(){
		ArrayList<Proposition> allPropositions = new ArrayList<Proposition>();
		this.getRoot().getAllPropositions(allPropositions);
		return allPropositions;
	}
	
	
	// ========= SORTING FUNCTIONS
	
	public ArrayList<TernaryNode> sortNodesToExpand(){
		ArrayList<TernaryNode> toSort = initNodesToExpand();
		ArrayList<TernaryNode> sorted = new ArrayList<TernaryNode>();
		//TernaryNode highestPriority;
		
		while (toSort.size() != 0) {
			TernaryNode highestPriority = toSort.get(0);
			
			for(int j = 0; j < toSort.size(); j ++) {
				TernaryNode compared = toSort.get(j);
				if (compared.getRelativePriority(highestPriority) == Priority.HIGHER || compared.getRelativePriority(highestPriority) == Priority.EQUAL) {
					highestPriority = compared;
				}
			}
			
			sorted.add(highestPriority);
			toSort.remove(toSort.indexOf(highestPriority));
		}
		return sorted;
	}
	
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
	
	
	// =========== DRAWING FUNCTIONS
	
	public void drawTernaryTree(double rootX, double rootY, double horizontalSpacing, double verticalSpacing, Pane modelPane) {
		this.getRoot().setupGeometry(rootX, rootY, horizontalSpacing, verticalSpacing, modelPane);
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
			
			// ==== below for checking whether left or right
			
			if (parentNode.getLocation()[0].getX() > tmpX) {
				
			}else if (parentNode.getLocation()[0].getX() < tmpX) {
				
			}else {
				
			}
			
			// ==== end checking
			
			
			Line connection = new Line(pointAbove.getX(), pointAbove.getY(), parentNode.getLocation()[2].getX(), parentNode.getLocation()[2].getY());
			
			propLabel = new Label(proposition.toString());
			
			modelPane.getChildren().add(propLabel);
			modelPane.applyCss();
			modelPane.layout();
			
			propLabel.relocate(duplicateX - propLabel.getWidth() / 2, duplicateY - propLabel.getHeight() / 2);
			duplicateY += verticalSpacing;
			
			modelPane.getChildren().add(connection);
		}else {
			
			propLabel = new Label(proposition.toString());
			
			modelPane.getChildren().add(propLabel);
			modelPane.applyCss();
			modelPane.layout();
			
			propLabel.relocate(duplicateX - propLabel.getWidth() / 2, duplicateY - propLabel.getHeight() / 2);
			duplicateY += verticalSpacing;
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
			centerNode.setupGeometry(newX3, newY3, horizontalSpacing, verticalSpacing, modelPane); // dont need to make horizontal spacing less if center node
		}
		if (leftNode == null & rightNode == null & centerNode == null) {
			return;
		}
	}
	
	
	
	public String toString() {
		return proposition.toString();
	}
	

}
