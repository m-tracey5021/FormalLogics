import java.util.ArrayList;

import enums.LogicType;

public class PropositionContainer {
	//private Proposition proposition;
	//private ArrayList<CompoundProposition> allConstituentCompoundProps;
	//private ArrayList<AtomicProposition> allConstituentAtomicProps;
	private LogicType logicType;
	private ArrayList<Proposition> propositions;
	
	public PropositionContainer() {
		
	}
	
	public PropositionContainer(LogicType logicType, ArrayList<Proposition> propositions) {
		this.logicType = logicType;
		this.propositions = propositions;
	}
	public LogicType getLogicType() {
		return this.logicType;
	}
	public ArrayList<Proposition> getPropositions(){
		return this.propositions;
	}
	
	/*
	public void matchIds() {
		for (Proposition currentProp : allConstituentProps) {
			for (CompoundProposition comparedProp : allConstituentProps) {
				if (currentProp.isMatch(comparedProp)) {
					comparedProp.setId(currentProp.getId());
				}
			}
		}
	}
	*/
}
