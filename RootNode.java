import java.util.ArrayList;

import enums.AuxillaryOperatorType;
import enums.OperatorType;
import enums.Priority;

public class RootNode {
	private TernaryNode treeStart, highestPriorityNode;
	private ArrayList<TernaryNode> allNodes, nodesToExpand, sortedNodesToExpand, emptyNodes, emptyNodesDownStream;
	
	public RootNode() {
		
	}
	
	public RootNode(TernaryNode treeStart) {
		this.treeStart = treeStart;
	}
	
	// ======= GET
	
	public TernaryNode getTreeStart() {
		return this.treeStart;
	}
	
	public TernaryNode getHighestPriorityNode() {
		return this.highestPriorityNode;
	}
	
	public ArrayList<TernaryNode> getAllNodes(){
		return this.allNodes;
	}
	
	public ArrayList<TernaryNode> getNodesToExpand(){
		return this.nodesToExpand;
	}

	public ArrayList<TernaryNode> getSortedNodesToExpand(){
		return this.sortedNodesToExpand;
	}
	
	public ArrayList<TernaryNode> getEmptyNodes(){
		return this.emptyNodes;
	}

	public ArrayList<TernaryNode> getEmptyNodesDownStream(){
		return this.emptyNodesDownStream;
	}

	
	
	// ======= SET
	
	public void setTreeStart(TernaryNode treeStart) {
		this.treeStart = treeStart;
	}
	
	// =========== INIT TREE INFO
	
	public void initTreeInfo() {
		initAllNodes();
		initNodesToExpand();
		initSortedNodesToExpand();
		initEmptyNodes();
		initEmptyNodesDownStream();
	}
	
	// =========== GET NODE TYPE FUNCTIONS
	
	private void initAllNodes() {
		allNodes = new ArrayList<TernaryNode>();
		treeStart.getAllNodes(allNodes);
	}
	
	private void initNodesToExpand() {
		nodesToExpand = new ArrayList<TernaryNode>();
		treeStart.getNodesToExpand(nodesToExpand);
	}
	
	private void initSortedNodesToExpand(){
		if (nodesToExpand.size() != 0) {
			ArrayList<TernaryNode> toSort = nodesToExpand;
			sortedNodesToExpand = new ArrayList<TernaryNode>();
			while (toSort.size() != 0) {
				TernaryNode highestPriority = toSort.get(0);
				
				for(int j = 0; j < toSort.size(); j ++) {
					TernaryNode compared = toSort.get(j);
					if (compared.getRelativePriority(highestPriority) == Priority.HIGHER || compared.getRelativePriority(highestPriority) == Priority.EQUAL) {
						highestPriority = compared;
					}
				}
				
				sortedNodesToExpand.add(highestPriority);
				toSort.remove(toSort.indexOf(highestPriority));
			}
			highestPriorityNode = sortedNodesToExpand.get(0);
		}
		else {
			highestPriorityNode = null;
		}
		
		
		

		
	}
	
	private void initEmptyNodes(){
		emptyNodes = new ArrayList<TernaryNode>();
		treeStart.getEmptyNodes(emptyNodes);
	}
	
	private void initEmptyNodesDownStream() {
		emptyNodesDownStream = new ArrayList<TernaryNode>();
		if (highestPriorityNode != null) {
			highestPriorityNode.getEmptyNodes(emptyNodesDownStream);
		}
		
	}
	
	public TernaryNode getLeftMostNode() {
		return treeStart.getLeftMostNode();
	}
	public TernaryNode getRightMostNode() {
		return treeStart.getRightMostNode();
	}
	
	
	
	
	
	
	// ================ EXPANSION FUNCTIONS
	
	
	/*
	public void expandWithinWorld(World parentWorld) {
		
		ArrayList<TernaryNode> nodesToExpand = treeStart.sortNodesToExpand();
		if (nodesToExpand.size() == 0) {
			return;
		}else {
			
		}
		TernaryNode highestPriorityNode = nodesToExpand.get(0);
		ArrayList<TernaryNode> emptyNodesDownStream = highestPriorityNode.initEmptyNodesDownStream();
		if (highestPriorityNode.getProposition() instanceof CompoundProposition) {
			
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
							copiedCompoundProp.getFirstOperand().addNegation();
							copiedCompoundProp.getSecondOperand().addNegation();
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}
					}else if (operatorType == OperatorType.OR) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().addNegation();
							copiedCompoundProp.getSecondOperand().addNegation();
							emptyNode.laneNode(copiedCompoundProp.getFirstOperand());
							emptyNode.getCenterNode().laneNode(copiedCompoundProp.getSecondOperand());
						}
					}else if (operatorType == OperatorType.IF) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().addNegation();
							emptyNode.laneNode(copiedCompoundProp.getFirstOperand());
							emptyNode.getCenterNode().laneNode(copiedCompoundProp.getSecondOperand());
						}
					}else {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().addNegation();
							duplicateCopiedCompoundProp.getSecondOperand().addNegation();   
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
					}else if (operatorType == OperatorType.OR) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}
					}else if (operatorType == OperatorType.IF) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().addNegation();
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}
					}else {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().addNegation();
							copiedCompoundProp.getSecondOperand().addNegation();
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), duplicateCopiedCompoundProp.getFirstOperand());
							emptyNode.getLeftNode().laneNode(copiedCompoundProp.getSecondOperand());
							emptyNode.getRightNode().laneNode(duplicateCopiedCompoundProp.getSecondOperand());
						}
					}
				}
			}else if (auxOp.isModal()) {
				expandForModal(parentWorld, copiedCompoundProp, auxOpType);
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
				
			}else if (auxOp.isModal()) {
				expandForModal(parentWorld, copiedAtomicProp, auxOpType);
			}else if (auxOp.isPredicate()) {

			}else {
				// something went wrong with auxOp
			}
		}
		expandWithinWorld(parentWorld);
	}
	
	public void expandForModal(World parentWorld, Proposition copiedProp, AuxillaryOperatorType auxOpType) {
		Universe parentUniverse = parentWorld.getParentUniverse();
		ArrayList<World> relatedWorlds = parentWorld.getRelatedWorlds();
		if (auxOpType == AuxillaryOperatorType.POSSIBLE || auxOpType == AuxillaryOperatorType.NOTNECESSARY) {
			copiedProp.removeRelevantAuxOp();
			World newWorld = new World(parentUniverse, new RootNode(new TernaryNode(copiedProp)));
			newWorld.getRelatingWorlds().add(parentWorld);
			relatedWorlds.add(newWorld);
			parentUniverse.adjustRelationsForUniverseProperties();
			newWorld.expandWithinUniverse();
		}else if (auxOpType == AuxillaryOperatorType.NECESSARY || auxOpType == AuxillaryOperatorType.NOTPOSSIBLE) {
			for (World relatedWorld : relatedWorlds) {
				ArrayList<TernaryNode> emptyNodesAtRelatedWorld = relatedWorld.getRootNode().getTreeStart().initEmptyNodesDownStream();
				for (TernaryNode emptyNode : emptyNodesAtRelatedWorld) {
					copiedProp.removeRelevantAuxOp();
					emptyNode.laneNode(copiedProp);
					
				}
				relatedWorld.expandWithinUniverse();
			}
		}
	}
	*/
	
	public void expandWithinWorld(World parentWorld) {
		initTreeInfo();

		if (highestPriorityNode == null) {
			return;
		}else {
			
		}
		
		if (highestPriorityNode.getProposition() instanceof CompoundProposition) {
			
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
							copiedCompoundProp.getFirstOperand().addNegation();
							copiedCompoundProp.getSecondOperand().addNegation();
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}
					}else if (operatorType == OperatorType.OR) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().addNegation();
							copiedCompoundProp.getSecondOperand().addNegation();
							emptyNode.laneNode(copiedCompoundProp.getFirstOperand());
							emptyNode.getCenterNode().laneNode(copiedCompoundProp.getSecondOperand());
						}
					}else if (operatorType == OperatorType.IF) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().addNegation();
							emptyNode.laneNode(copiedCompoundProp.getFirstOperand());
							emptyNode.getCenterNode().laneNode(copiedCompoundProp.getSecondOperand());
						}
					}else {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().addNegation();
							duplicateCopiedCompoundProp.getSecondOperand().addNegation();   
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
					}else if (operatorType == OperatorType.OR) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}
					}else if (operatorType == OperatorType.IF) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().addNegation();
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}
					}else {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().addNegation();
							copiedCompoundProp.getSecondOperand().addNegation();
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), duplicateCopiedCompoundProp.getFirstOperand());
							emptyNode.getLeftNode().laneNode(copiedCompoundProp.getSecondOperand());
							emptyNode.getRightNode().laneNode(duplicateCopiedCompoundProp.getSecondOperand());
						}
					}
				}
			}else if (auxOp.isModal()) {
				expandForModal(parentWorld, copiedCompoundProp, auxOpType);
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
				
			}else if (auxOp.isModal()) {
				expandForModal(parentWorld, copiedAtomicProp, auxOpType);
			}else if (auxOp.isPredicate()) {

			}else {
				// something went wrong with auxOp
			}
		}
		expandWithinWorld(parentWorld);

	}
	
	public void expandForModal(World parentWorld, Proposition copiedProp, AuxillaryOperatorType auxOpType) {
		Universe parentUniverse = parentWorld.getParentUniverse();
		ArrayList<World> relatedWorlds = parentWorld.getRelatedWorlds();
		if (auxOpType == AuxillaryOperatorType.POSSIBLE || auxOpType == AuxillaryOperatorType.NOTNECESSARY) {
			copiedProp.removeRelevantAuxOp();
			World newWorld = new World(parentUniverse, new RootNode(new TernaryNode(copiedProp)));
			newWorld.getRelatingWorlds().add(parentWorld);
			relatedWorlds.add(newWorld);
			parentUniverse.adjustRelationsForUniverseProperties();
			newWorld.expandWithinUniverse();
		}else if (auxOpType == AuxillaryOperatorType.NECESSARY || auxOpType == AuxillaryOperatorType.NOTPOSSIBLE) {
			for (World relatedWorld : relatedWorlds) {
				relatedWorld.getRootNode().initTreeInfo();
				ArrayList<TernaryNode> emptyNodesAtRelatedWorld = relatedWorld.getRootNode().getEmptyNodes();
				for (TernaryNode emptyNode : emptyNodesAtRelatedWorld) {
					Proposition duplicate = copiedProp.copy();
					duplicate.removeRelevantAuxOp();
					emptyNode.laneNode(duplicate);
					
				}
				relatedWorld.expandWithinUniverse();
			}
		}
	}
}
