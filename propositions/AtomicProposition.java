package propositions;
import java.util.ArrayList;
import java.util.UUID;

import enums.AuxillaryOperatorType;

public class AtomicProposition extends Proposition {
	private PropositionalVariable variable;

	public AtomicProposition() {
		super("atomic");
		super.setIsExpanded(true);
	}
	
	public AtomicProposition(ArrayList<AuxillaryOperator> auxOps, PropositionalVariable variable) {
		super("atomic", auxOps);
		super.setIsExpanded(isNotExpandable(auxOps));
		super.setPriority();
		this.variable = variable;
		
	}
	
	// ======= GET
	
	
	public PropositionalVariable getVariable() {
		return this.variable;
	}
	
	// ======= SET

	
	public void setVariable(PropositionalVariable variable) {
		this.variable = variable;
	}
	
	// ===================
	
	public boolean isNotExpandable(ArrayList<AuxillaryOperator> auxOps) {
		if (auxOps == null) {
			return true;
		}else if (auxOps.get(0).getAuxOpType() == AuxillaryOperatorType.NEGATION) {
			return true;
		}else {
			return false;
		}
	}
	
	// ============= OVERRIDEN METHODS
	
	@Override
	public void getPredicateProps(ArrayList<Proposition> propositions) {
		return;
	}
	
	@Override 
	public void assignVariables(PredicatedProposition predicate) {
		return;
	}
	
	@Override
	public void assignVariables(RelationalProposition relational) {
		return;
	}
	
	@Override
	public AtomicProposition copy() {
		ArrayList<AuxillaryOperator> copiedAuxOps = new ArrayList<AuxillaryOperator>();
		for(AuxillaryOperator auxOp : super.getAuxOps()) {
			AuxillaryOperator copiedAuxOp = auxOp.copy();
			copiedAuxOps.add(copiedAuxOp);
		}
		AtomicProposition copiedAtomicProp = new AtomicProposition(copiedAuxOps, variable.copy());
		copiedAtomicProp.assignVariablesForAll();
		return copiedAtomicProp;
	}

	
	@Override
	public String toString() {
		String auxOpsStr = "";
		if (this.getAuxOps() != null) {
			for (AuxillaryOperator auxOp : this.getAuxOps()) {
				auxOpsStr += auxOp;
			}
		}
		return auxOpsStr + this.variable.toString();
	}


}
