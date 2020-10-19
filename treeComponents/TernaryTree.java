package treeComponents;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import enums.AuxillaryOperatorType;
import enums.InstanceVariableState;
import enums.InstanceVariableType;
import enums.LogicType;
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
import propositions.PredicatedProposition;
import propositions.Proposition;
import propositions.Quantifier;
import propositions.RelationalProposition;
import universeComponents.Relation;
import universeComponents.Universe;
import universeComponents.World;
import visualModels.TernaryNodeModel;
import visualModels.TernaryTreeModel;

public class TernaryTree {
	private TernaryNode treeStart, highestPriorityNode;
	private ArrayList<TernaryNode> allNodes, nodesToExpand, sortedNodesToExpand, emptyNodes, emptyNodesDownStream;
	private ArrayList<ArrayList<TernaryNode>> branches;
	private ArrayList<InstanceVariableType> existingInstantiatedVars;
	private ArrayList<double[]> nodeSizes;
	private double horizontalSpacing, verticalSpacing;
	private TernaryTreeModel treeModel;
	
	public TernaryTree() {
		
	}
	
	public TernaryTree(TernaryNode treeStart) {
		this.treeStart = treeStart;
		this.existingInstantiatedVars = new ArrayList<InstanceVariableType>();
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
		initBranches();
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
	
	private void initBranches() {
		branches = new ArrayList<ArrayList<TernaryNode>>();
		for (TernaryNode emptyNode : emptyNodes) {
			ArrayList<TernaryNode> newBranch = new ArrayList<TernaryNode>();
			emptyNode.getNodesOnBranch(newBranch);
			branches.add(newBranch);
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
			
			CompoundProposition compoundProp = (CompoundProposition) highestPriorityNode.getProposition();
			compoundProp.setIsExpanded(true);
			
			CompoundProposition copiedCompoundProp = (CompoundProposition) compoundProp.copy();
			CompoundProposition duplicateCopiedCompoundProp = (CompoundProposition) compoundProp.copy();
			
			
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
		}else if (highestPriorityNode.getProposition() instanceof PredicatedProposition) {
			PredicatedProposition predicatedProp = (PredicatedProposition) highestPriorityNode.getProposition();
			predicatedProp.setIsExpanded(true);
			
			PredicatedProposition copiedpredicatedProp = predicatedProp.copy();
			//copiedpredicatedProp.assignVariablesForAll();
			
			AuxillaryOperator auxOp = copiedpredicatedProp.getAuxOps().get(0);
			
			AuxillaryOperatorType auxOpType = auxOp.getAuxOpType();
			if (auxOp.isClassical()) {
				
			}else if (auxOp.isModal()) {
				expandForModal(parentWorld, copiedpredicatedProp, auxOpType);
			}else if (auxOp.isPredicate()) {
				expandForPredicate(parentWorld, copiedpredicatedProp, auxOp);
			}else {
				// something went wrong with auxOp
			}
			
			
		}else if (highestPriorityNode.getProposition() instanceof RelationalProposition) {
			RelationalProposition relationalProp = (RelationalProposition) highestPriorityNode.getProposition();
			relationalProp.setIsExpanded(true);
			
			RelationalProposition copiedRelationalProp = relationalProp.copy();

			
			AuxillaryOperator auxOp = copiedRelationalProp.getAuxOps().get(0);
			
			AuxillaryOperatorType auxOpType = auxOp.getAuxOpType();
			if (auxOp.isClassical()) {
				
			}else if (auxOp.isModal()) {
				expandForModal(parentWorld, copiedRelationalProp, auxOpType);
			}else if (auxOp.isPredicate()) {
				expandForPredicate(parentWorld, copiedRelationalProp, auxOp);
			}else {
				// something went wrong with auxOp
			}
			
		}else {
			AtomicProposition atomicProp = ((AtomicProposition) highestPriorityNode.getProposition());
			atomicProp.setIsExpanded(true);
			
			AtomicProposition copiedAtomicProp = atomicProp.copy();

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

		ArrayList<World> relatedWorlds = parentUniverse.getRelatedWorlds(parentWorld);
		if (auxOpType == AuxillaryOperatorType.POSSIBLE || auxOpType == AuxillaryOperatorType.NOTNECESSARY) {
			copiedProp.removeRelevantAuxOp();
			World newWorld = new World(parentUniverse, new TernaryTree(new TernaryNode(copiedProp)));
			parentUniverse.updateRelationsForNewWorld(parentWorld, newWorld);
			//Relation newRelation = new Relation(parentWorld, newWorld);
			//parentUniverse.getRelations().add(newRelation);

			//parentUniverse.adjustRelationsForUniverseProperties();
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

		Quantifier quantifier = (Quantifier) copiedAuxOp;
		
		/*
		ArrayList<InstanceVariableType> existingInstantiatedVars = parentWorld.getParentUniverse().getInstantiatedVariables();
		boolean instantiatedExists = false;

		if (existingInstantiatedVars.size() != 0) {
			instantiatedExists = true;
		}
		*/
		
		boolean instantiatedExists = false;

		if (existingInstantiatedVars.size() != 0) {
			instantiatedExists = true;
		}
		
		AuxillaryOperatorType auxOpType = quantifier.getAuxOpType();
		
		if (auxOpType == AuxillaryOperatorType.EXISTENTIAL || auxOpType == AuxillaryOperatorType.NOTUNIVERSAL) {
			
			//instantiateNewVariable(existingInstantiatedVars, quantifier, parentWorld.getParentUniverse());
			instantiateNewVariable(quantifier);
			copiedProp.removeRelevantAuxOp();
			for (TernaryNode emptyNode : emptyNodesDownStream) {
				emptyNode.laneNode(copiedProp);
			}
			
		}else if (auxOpType == AuxillaryOperatorType.UNIVERSAL || auxOpType == AuxillaryOperatorType.NOTEXISTENTIAL) {
			if (instantiatedExists) {
				for (TernaryNode emptyNode : emptyNodesDownStream) {
					for (int i = 0; i < existingInstantiatedVars.size(); i ++) {
						if (copiedProp instanceof PredicatedProposition) {
							PredicatedProposition copyForInstantiation = (PredicatedProposition) copiedProp.copy();
							instantiateForExistingVariable(existingInstantiatedVars.get(i), copyForInstantiation.getQuantifier());
							addInstantiatedCopy(copyForInstantiation, emptyNode);

						}else if (copiedProp instanceof RelationalProposition) {
							RelationalProposition copyForInstantiation = (RelationalProposition) copiedProp.copy();
							
							if (copyForInstantiation.getFirstQuantifier() != null) {
								instantiateForExistingVariable(existingInstantiatedVars.get(i), copyForInstantiation.getFirstQuantifier());
							}
							
							if (copyForInstantiation.getSecondQuantifier() != null) {
								instantiateForExistingVariable(existingInstantiatedVars.get(i), copyForInstantiation.getSecondQuantifier());
							}
							addInstantiatedCopy(copyForInstantiation, emptyNode);

						}else if (copiedProp instanceof CompoundProposition){
							CompoundProposition copyForInstantiation = (CompoundProposition) copiedProp.copy();
							
							instantiateForExistingVariable(existingInstantiatedVars.get(i), (Quantifier) copyForInstantiation.getAuxOps().get(0));
							addInstantiatedCopy(copyForInstantiation, emptyNode);

						}
						
					}
				}
			}else {

				//instantiateNewVariable(existingInstantiatedVars, quantifier, parentWorld.getParentUniverse());
				instantiateNewVariable(quantifier);
				copiedProp.removeRelevantAuxOp();
				for (TernaryNode emptyNode : emptyNodesDownStream) {
					emptyNode.laneNode(copiedProp);
				}
			}
		}	
	}
	/*
	public void instantiateNewVariable(ArrayList<InstanceVariableType> existingInstantiatedVars, Quantifier quantifier, Universe universe) {
		int rotation = existingInstantiatedVars.size();
		for (Map.Entry<UUID, ArrayList<InstanceVariable>> entry : quantifier.getAppliesTo().entrySet()) {
			UUID predicateId = entry.getKey();
			ArrayList<InstanceVariable> instanceVars = entry.getValue();
			for (InstanceVariable instanceVar : instanceVars) {
				instanceVar.setVariable(InstanceVariableType.values()[rotation]);
				if (!universe.getInstantiatedVariables().contains(instanceVar.getVariable())) {
					universe.getInstantiatedVariables().add(instanceVar.getVariable());
				}
				
			}
			
			
		}
	}
	*/
	
	public void instantiateNewVariable(Quantifier quantifier) {
		int rotation = existingInstantiatedVars.size();
		for (Map.Entry<UUID, ArrayList<InstanceVariable>> entry : quantifier.getAppliesTo().entrySet()) {
			UUID predicateId = entry.getKey();
			ArrayList<InstanceVariable> instanceVars = entry.getValue();
			for (InstanceVariable instanceVar : instanceVars) {
				instanceVar.setVariable(InstanceVariableType.values()[rotation]);
				if (!existingInstantiatedVars.contains(instanceVar.getVariable())) {
					existingInstantiatedVars.add(instanceVar.getVariable());
				}
				
			}
			
			
		}
	}
	
	public void instantiateForExistingVariable(InstanceVariableType existingInstantiatedVar, Quantifier quantifier) {
		for (Map.Entry<UUID, ArrayList<InstanceVariable>> entry : quantifier.getAppliesTo().entrySet()) {
			UUID predicateId = entry.getKey();
			ArrayList<InstanceVariable> instanceVars = entry.getValue();
			for (InstanceVariable instanceVar : instanceVars) {
				instanceVar.setVariable(existingInstantiatedVar);
				
			}
			
		}
	}
	
	public void addInstantiatedCopy(Proposition copyForInstantiation, TernaryNode emptyNode) {
		copyForInstantiation.removeRelevantAuxOp();
		TernaryNode nodeToLane = emptyNode.getEmptyCenterNode();
		nodeToLane.laneNode(copyForInstantiation);
	}
	
	public boolean isValid() {
		initTreeInfo();
		int branchCount = branches.size();
		int validBranchCount = 0;
		for (ArrayList<TernaryNode> branch : branches) {
			boolean validBranch = true;
			for (int i = 0; i < branch.size(); i ++) {
				TernaryNode node = branch.get(i);
				for (int j = 0; j < branch.size(); j ++) {
					TernaryNode comparedNode = branch.get(j);
					if (node.getProposition().isContradictory(comparedNode.getProposition())) {
						validBranch = false;
					}
				}
			}
			if (validBranch) {
				validBranchCount ++;
			}
		}
		if (branchCount == validBranchCount) {
			return true;
		}else {
			return false;
		}
	}
}





















