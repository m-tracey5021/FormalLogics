import java.util.ArrayList;

import enums.AuxillaryOperatorType;
import enums.OperatorType;

public class RootNode {
	private TernaryNode treeStart;
	
	public RootNode() {
		
	}
	
	public RootNode(TernaryNode treeStart) {
		this.treeStart = treeStart;
	}
	
	public TernaryNode getTreeStart() {
		return this.treeStart;
	}
	
	public void expandWithinWorld(World parentWorld) {
		ArrayList<TernaryNode> nodesToExpand = treeStart.sortNodesToExpand();
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
							copiedCompoundProp.getFirstOperand().addNegation();
							copiedCompoundProp.getSecondOperand().addNegation();
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}
					}else if (operatorType == OperatorType.OR) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							//copiedCompoundProp.getFirstOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
							//copiedCompoundProp.getSecondOperand().getAuxOps().add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
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
						//expandWithinWorld(parentWorld);
					}else if (operatorType == OperatorType.OR) {
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}
						//expandWithinWorld(parentWorld);

						
					}else if (operatorType == OperatorType.IF) {
						
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().addNegation();
							emptyNode.branchNode(copiedCompoundProp.getFirstOperand(), copiedCompoundProp.getSecondOperand());
						}
						//expandWithinWorld(parentWorld);


					}else {

						for (TernaryNode emptyNode : emptyNodesDownStream) {
							copiedCompoundProp.getFirstOperand().addNegation();
							copiedCompoundProp.getSecondOperand().addNegation();
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
					emptyNode.lane(copiedProp, false);
					
				}
				relatedWorld.expandWithinUniverse();
			}
		}
	}
}
