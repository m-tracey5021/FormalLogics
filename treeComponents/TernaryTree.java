package treeComponents;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import enums.AuxillaryOperatorType;
import enums.InstanceVariableState;
import enums.InstanceVariableType;
import enums.OperatorType;
import enums.PlaceholderVariableType;
import enums.Priority;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import propositions.AtomicProposition;
import propositions.AuxillaryOperator;
import propositions.CompoundProposition;
import propositions.InstanceVariable;
import propositions.Operator;
import propositions.Proposition;
import propositions.Quantifier;
import universeComponents.Relation;
import universeComponents.Universe;
import universeComponents.World;
import visualModels.TernaryNodeModel;
import visualModels.TernaryTreeModel;

public class TernaryTree {
	private TernaryNode treeStart, highestPriorityNode;
	private ArrayList<TernaryNode> allNodes, nodesToExpand, sortedNodesToExpand, emptyNodes, emptyNodesDownStream;
	private ArrayList<double[]> nodeSizes;
	private double largestNode, horizontalSpacing, verticalSpacing;
	private TernaryTreeModel treeModel;
	
	public TernaryTree() {
		
	}
	
	public TernaryTree(TernaryNode treeStart) {
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
	
	public TernaryTreeModel getTreeModel() {
		return this.treeModel;
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
	
	public void initNodeSizes() {
		nodeSizes = new ArrayList<double[]>();
		treeStart.getNodeSizes(nodeSizes);
		
	}
	
	public TernaryNode getLeftMostNode() {
		return treeStart.getLeftMostNode();
	}
	public TernaryNode getRightMostNode() {
		return treeStart.getRightMostNode();
	}
	
	
	
	
	// ================= GEOMETRY FUNCTIONS
	

	
	public void initSpacing() {
		double maxWidth = 0.0;
		for (double[] size : nodeSizes) {
			if (size[0] >= maxWidth) {
				maxWidth = size[0];
			}
		}

		horizontalSpacing = (maxWidth / 2) + 10;
		verticalSpacing = 20.0;
	}
	

	
	
	public void generateTreeModel() {
		initNodeSizes();
		initSpacing();
		ArrayList<Line> lines = new ArrayList<Line>();
		ArrayList<TernaryNodeModel> nodeModels = new ArrayList<TernaryNodeModel>();
		treeStart.generateNodeModels(0, 0, horizontalSpacing, verticalSpacing, lines);
		for (TernaryNode node : allNodes) {
			nodeModels.add(node.getNodeModel());
		}
		treeModel = new TernaryTreeModel(nodeModels, lines);
	}
	
	
	
	// ================ EXPANSION FUNCTIONS
	
	
	
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
				expandForPredicate(parentWorld, copiedCompoundProp, auxOp);
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
				expandForPredicate(parentWorld, copiedAtomicProp, auxOp);
			}else {
				// something went wrong with auxOp
			}
		}
		expandWithinWorld(parentWorld);

	}
	
	public void expandForModal(World parentWorld, Proposition copiedProp, AuxillaryOperatorType auxOpType) {
		Universe parentUniverse = parentWorld.getParentUniverse();
		//ArrayList<World> relatedWorlds = parentWorld.getRelatedWorlds();
		ArrayList<World> relatedWorlds = parentUniverse.getRelatedWorlds(parentWorld);
		if (auxOpType == AuxillaryOperatorType.POSSIBLE || auxOpType == AuxillaryOperatorType.NOTNECESSARY) {
			copiedProp.removeRelevantAuxOp();
			World newWorld = new World(parentUniverse, new TernaryTree(new TernaryNode(copiedProp)));
			Relation newRelation = new Relation(parentWorld, newWorld);
			parentUniverse.getRelations().add(newRelation);
			//newWorld.getRelatingWorlds().add(parentWorld);
			//relatedWorlds.add(newWorld);
			parentUniverse.adjustRelationsForUniverseProperties();
			newWorld.expandWithinUniverse();
		}else if (auxOpType == AuxillaryOperatorType.NECESSARY || auxOpType == AuxillaryOperatorType.NOTPOSSIBLE) {
			for (World relatedWorld : relatedWorlds) {
				relatedWorld.getTernaryTree().initTreeInfo();
				ArrayList<TernaryNode> emptyNodesAtRelatedWorld = relatedWorld.getTernaryTree().getEmptyNodes();
				for (TernaryNode emptyNode : emptyNodesAtRelatedWorld) {
					Proposition duplicate = copiedProp.copy();
					duplicate.removeRelevantAuxOp();
					emptyNode.laneNode(duplicate);
					
				}
				relatedWorld.expandWithinUniverse();
			}
		}
	}
	
	public void expandForPredicate(World parentWorld, Proposition copiedProp, AuxillaryOperator copiedAuxOp) {
		if (copiedProp.getType().equals("predicated")) {
			
		}else if (copiedProp.getType().equals("relational")) {
			
		}
		Quantifier quantifier = (Quantifier) copiedAuxOp;
		AuxillaryOperatorType auxOpType = quantifier.getAuxOpType();
		ArrayList<InstanceVariable> existingInstanceVars = parentWorld.getParentUniverse().getInstantiatedVariables();
		ArrayList<InstanceVariableType> existingInstantiatedVars = new ArrayList<InstanceVariableType>();
		boolean instantiatedExists = false;
		for (InstanceVariable var : existingInstanceVars) {
			existingInstantiatedVars.add(var.getVariable());
		}
		
		if (existingInstantiatedVars.size() != 0) {
			instantiatedExists = true;
		}
		
		if (auxOpType == AuxillaryOperatorType.EXISTENTIAL || auxOpType == AuxillaryOperatorType.NOTUNIVERSAL) {
			
			instantiateNewVariable(existingInstantiatedVars, quantifier, parentWorld.getParentUniverse());
			for (TernaryNode emptyNode : emptyNodesDownStream) {
				emptyNode.laneNode(copiedProp);
			}
			
		}else if (auxOpType == AuxillaryOperatorType.UNIVERSAL || auxOpType == AuxillaryOperatorType.NOTEXISTENTIAL) {
			if (instantiatedExists) {
				for (TernaryNode emptyNode : emptyNodesDownStream) {
					//for(InstanceVariable var : instanceVars) {
						//emptyNode.laneNode(proposition);
					//}
				}
			}else {

				instantiateNewVariable(existingInstantiatedVars, quantifier, parentWorld.getParentUniverse());
				for (TernaryNode emptyNode : emptyNodesDownStream) {
					//copiedProp.set
					copiedProp.removeRelevantAuxOp();

					emptyNode.laneNode(copiedProp);
				}
			}
			
		}
		
		
	}
	
	public void instantiateNewVariable(ArrayList<InstanceVariableType> existingInstantiatedVars, Quantifier quantifier, Universe universe) {
		int rotation = existingInstantiatedVars.size();
		for (Map.Entry<UUID, InstanceVariable> entry : quantifier.getAppliesTo().entrySet()) {
			UUID predicateId = entry.getKey();
			InstanceVariable instanceVar = entry.getValue();
			instanceVar.setVariable(InstanceVariableType.values()[rotation]);
			universe.getInstantiatedVariables().add(instanceVar);
		}
		

	}
}





















