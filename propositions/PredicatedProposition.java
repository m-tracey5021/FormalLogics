package propositions;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PredicatedProposition extends Proposition {
	@JsonIgnore
	private Quantifier quantifier;
	private String predicate;
	private InstanceVariable instanceVariable;
	
	public PredicatedProposition() {
		super("predicated");

	}
	
	public PredicatedProposition(ArrayList<AuxillaryOperator> auxOps, Quantifier quantifier, String predicate, InstanceVariable instanceVariable) {
		super("predicated", auxOps);
		super.setPriority();
		this.quantifier = quantifier;
		this.predicate = predicate;
		this.instanceVariable = instanceVariable;

	}
	
	// ======== GET
	

	public Quantifier getQuantifier() {
		return this.quantifier;
	}
	
	public String getPredicate() {
		return this.predicate;
	}
	
	public InstanceVariable getInstanceVariable() {
		return this.instanceVariable;
	}
	
	// =========== SET
	
	public void setQuantifier(Quantifier quantifier) {
		this.quantifier = quantifier;
	}
	
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	
	public void setInstanceVariable(InstanceVariable instanceVariable) {
		this.instanceVariable = instanceVariable;
	}
	
	// ==============
	
	
	public boolean isVariableMatch(Quantifier comparedQuantifier) {
		InstanceVariable externalVariable = comparedQuantifier.getInstanceVariable();
		if (externalVariable.getPlaceholder() == instanceVariable.getPlaceholder()) {
			
			
			
			comparedQuantifier.getAppliesTo().put(this.getId(), instanceVariable);
			quantifier = comparedQuantifier;

			return true;
		}else {
			return false;
		}
	}
	
	
	// ========= OVERRIDEN METHODS
	
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
	public void assignVariables(PredicatedProposition predicate) {
		for (int i = 0; i < super.getAuxOps().size(); i ++) {
			if (super.getAuxOps().get(i).isPredicate()) {
				Quantifier parentQuantifier = (Quantifier) super.getAuxOps().get(i);
				
				if (isVariableMatch(parentQuantifier)) {
					
					return;
				}
			}
		}
		if (super.getParentProp() != null) {
			super.getParentProp().assignVariables(predicate);
		}else {
			return;
		}
	}
	
	
	
	
	@Override
	public PredicatedProposition copy() {
		ArrayList<AuxillaryOperator> copiedAuxOps = new ArrayList<AuxillaryOperator>();
		for(AuxillaryOperator auxOp : super.getAuxOps()) {
			AuxillaryOperator copiedAuxOp = auxOp.copy();
			copiedAuxOps.add(copiedAuxOp);
		}
		
		PredicatedProposition copiedPredicatedProp;
		if (quantifier == null) {
			copiedPredicatedProp = new PredicatedProposition(copiedAuxOps, null, predicate, instanceVariable.copy());
		}else {
			//InstanceVariable newVar = instanceVariable.copy();
			//Quantifier newQuantifier = quantifier.copy();
			//newQuantifier.updateApplicableVariables(instanceVariable, newVar);
			//quantifier.updateApplicableVariables(instanceVariable, newVar);
			//copiedPredicatedProp = new PredicatedProposition(copiedAuxOps, quantifier, predicate, newVar);
			
			Quantifier copiedQuantifier = quantifier.copy();
			//quantifier = copiedQuantifier;
			quantifier.getParentProposition().replaceAuxOp(quantifier, copiedQuantifier);
			
			InstanceVariable instanceVarToAssign = copiedQuantifier.getAppliesTo().get(this.getId());
			
			
			
			
			copiedPredicatedProp = new PredicatedProposition(copiedAuxOps, copiedQuantifier, predicate, instanceVarToAssign);
			copiedQuantifier.getAppliesTo().remove(this.getId());
			copiedQuantifier.getAppliesTo().put(copiedPredicatedProp.getId(), instanceVarToAssign);
			
			//copiedPredicatedProp.getQuantifier().getAppliesTo().add(copiedPredicatedProp.getInstanceVariable());
		}
		
		
		return copiedPredicatedProp;
	}
	
	@Override
	public String toString() {
		String auxOpsStr = "";
		if (this.getAuxOps() != null) {
			for (AuxillaryOperator auxOp : this.getAuxOps()) {
				auxOpsStr += auxOp;
			}
		}
		return auxOpsStr + predicate + instanceVariable;
	}
}






