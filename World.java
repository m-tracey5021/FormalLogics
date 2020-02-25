import java.util.ArrayList;

import enums.AuxillaryOperatorType;

public class World {
	
	private Universe parentUniverse;
	private RootNode rootNode;
	private ArrayList<World> relatedWorlds; // if this world relates to another, it points towards the other
	private ArrayList<World> relatingWorlds;
	
	public World(Universe parentUniverse, RootNode rootNode) {
		this.parentUniverse = parentUniverse;
		this.parentUniverse.addWorld(this);
		this.rootNode = rootNode;
		this.relatedWorlds = new ArrayList<World>();
		this.relatingWorlds = new ArrayList<World>();
	}
	
	// ======== GET
	
	public RootNode getRootNode() {
		return this.rootNode;
	}
	public Universe getParentUniverse() {
		return this.parentUniverse;
	}
	
	public ArrayList<World> getRelatedWorlds(){
		return relatedWorlds;
	}
	
	public ArrayList<World> getRelatingWorlds(){
		return relatingWorlds;
	}
	
	// ======== SET
	
	/*
	public void expand() {
		ternaryNode.expand();
		ArrayList<TernaryNode> nodesToExpand = ternaryNode.sortNodesToExpand();
		for (TernaryNode node : nodesToExpand) {
			Proposition prop = node.getProposition();
			//ArrayList<AuxillaryOperator> auxOps = prop.getAuxOps();
			AuxillaryOperator auxOp = prop.getAuxOps().get(0);
			AuxillaryOperatorType auxOpType = auxOp.getAuxOpType();
			if (auxOp.isModal()) {
				prop.setIsExpanded(true);
				if (prop.getAuxOps().get(0).getAuxOpType() == AuxillaryOperatorType.POSSIBLE || 
						prop.getAuxOps().get(0).getAuxOpType() == AuxillaryOperatorType.NOTNECESSARY) {
					// CREATE NEW WORLD, EXPAND IN THAT WORLD 
					Proposition copiedProp = prop.copy();
					copiedProp.removeRelevantAuxOp();
					World newWorld = new World(parentUniverse, new TernaryNode(copiedProp));
					newWorld.getRelatingWorlds().add(this);
					relatedWorlds.add(newWorld);
					parentUniverse.adjustRelationsForUniverseProperties();
					newWorld.expand();
				}else if (prop.getAuxOps().get(0).getAuxOpType() == AuxillaryOperatorType.NECESSARY || 
						prop.getAuxOps().get(0).getAuxOpType() == AuxillaryOperatorType.NOTPOSSIBLE) {
					// EXPAND TO ALL WORLDS WHICH ARE RELATED
					for (World relatedWorld : relatedWorlds) {
						ArrayList<TernaryNode> emptyNodesDownStream = relatedWorld.getNode().initEmptyNodesDownStream();
						for (TernaryNode emptyNode : emptyNodesDownStream) {
							Proposition copiedProp = prop.copy();
							copiedProp.removeRelevantAuxOp();
							emptyNode.lane(copiedProp, false);
							
						}
						relatedWorld.expand();
					}
				}
			}
		}
	}
	*/
	
	public void expandWithinUniverse() {
		rootNode.expandWithinWorld(this);
	}
}
