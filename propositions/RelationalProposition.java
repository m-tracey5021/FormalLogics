package propositions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RelationalProposition extends Proposition{
	private String relation = "R";
	@JsonIgnore
	private Quantifier firstQuantifier, secondQuantifier;
	private InstanceVariable firstInstanceVariable, secondInstanceVariable;
	
	public RelationalProposition() {
		super("relational");
	}
	

	
	public RelationalProposition(ArrayList<AuxillaryOperator> auxOps, 
			Quantifier firstQuantifier, Quantifier secondQuantifier, 
			InstanceVariable firstInstanceVariable, InstanceVariable secondInstanceVariable) {
		super("relational", auxOps);
		super.setPriority();
		this.firstQuantifier = firstQuantifier;
		this.secondQuantifier = secondQuantifier;
		this.firstInstanceVariable = firstInstanceVariable;
		this.secondInstanceVariable = secondInstanceVariable;

	}
	
	// ============= GET
	
	public Quantifier getFirstQuantifier() {
		return this.firstQuantifier;
	}
	
	public Quantifier getSecondQuantifier() {
		return this.secondQuantifier;
	}
	
	public InstanceVariable getFirstInstanceVariable() {
		return this.firstInstanceVariable;
	}
	
	public InstanceVariable getSecondInstanceVariable() {
		return this.secondInstanceVariable;
	}

	
	// ================ SET
	
	public void setFirstQuantifier(Quantifier quantifier) {
		this.firstQuantifier = quantifier;
	}
	
	public void setSecondQuantifier(Quantifier quantifier) {
		this.secondQuantifier = quantifier;
	}
	
	
	// =================
	
	public boolean isFirstVariableMatch(Quantifier comparedQuantifier) {
		InstanceVariable externalVariable = comparedQuantifier.getInstanceVariable();
		InstanceVariable firstInternalVariable = firstInstanceVariable;
		if (externalVariable.getPlaceholder() == firstInternalVariable.getPlaceholder()) {
			ArrayList<InstanceVariable> entry = comparedQuantifier.getAppliesTo().get(this.getId());
			if (entry == null) {
				comparedQuantifier.getAppliesTo().put(this.getId(), new ArrayList<InstanceVariable>(Arrays.asList(firstInternalVariable)));
			}else {
				entry.add(firstInternalVariable);
			}
			
			firstQuantifier = comparedQuantifier;
	
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isSecondVariableMatch(Quantifier comparedQuantifier) {

		InstanceVariable externalVariable = comparedQuantifier.getInstanceVariable();
		InstanceVariable secondInternalVariable = secondInstanceVariable;
		if (externalVariable.getPlaceholder() == secondInternalVariable.getPlaceholder()) {
			ArrayList<InstanceVariable> entry = comparedQuantifier.getAppliesTo().get(this.getId());
			if (entry == null) {
				comparedQuantifier.getAppliesTo().put(this.getId(), new ArrayList<InstanceVariable>(Arrays.asList(secondInternalVariable)));
			}else {
				entry.add(secondInternalVariable);
			}
		
			secondQuantifier = comparedQuantifier;
			
			
			
			return true;
		}else {
			return false;
		}
	}
	
	// ============== OVERRIDEN METHODS
	
	
	@Override
	public void getPredicateProps(ArrayList<Proposition> propositions) {
		propositions.add(this);
		return;
	}
	
	@Override
	public void startAssignment() {

		assignVariables(this);
	}
	
	@Override
	public void assignVariables(RelationalProposition relational) {
		for (int i = 0; i < super.getAuxOps().size(); i ++) {
			if (super.getAuxOps().get(i).isPredicate()) {
				Quantifier parentQuantifier = (Quantifier) super.getAuxOps().get(i);
				if (isFirstVariableMatch(parentQuantifier) & isSecondVariableMatch(parentQuantifier)) {
					return;
				}
			}
		}
		if (super.getParentProp() != null) {
			super.getParentProp().assignVariables(relational);
		}else {
			return;
		}
	}
	

	@Override 
	public RelationalProposition copy() {
		ArrayList<AuxillaryOperator> copiedAuxOps = new ArrayList<AuxillaryOperator>();
		for(AuxillaryOperator auxOp : super.getAuxOps()) {
			AuxillaryOperator copiedAuxOp = auxOp.copy();
			copiedAuxOps.add(copiedAuxOp);
		}
		RelationalProposition copiedRelational = new RelationalProposition(copiedAuxOps, null, null, firstInstanceVariable.copy(), secondInstanceVariable.copy());
		
	
		copiedRelational.assignVariablesForAll();
		return copiedRelational;

	}
	
	@Override
	public String toString() {
		String auxOpsStr = "";
		if (this.getAuxOps() != null) {
			for (AuxillaryOperator auxOp : this.getAuxOps()) {
				auxOpsStr += auxOp;
			}
		}
		return auxOpsStr + relation + firstInstanceVariable + secondInstanceVariable;
	}
}


















