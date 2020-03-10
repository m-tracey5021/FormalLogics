package propositions;

import java.util.ArrayList;

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
			comparedQuantifier.getAppliesTo().put(this.getId(), firstInternalVariable);
			firstQuantifier = comparedQuantifier;
			/*
			if (comparedQuantifier.getParentContainer() != null) {
				comparedQuantifier.getParentContainer().getApplicableInstanceVariables().add(firstInternalVariable);
			}else {
				QuantifierContainer newContainer = new QuantifierContainer();
				newContainer.getApplicableInstanceVariables().add(firstInternalVariable);
				comparedQuantifier.setParentContainer(newContainer);
			}
			*/
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isSecondVariableMatch(Quantifier comparedQuantifier) {

		InstanceVariable externalVariable = comparedQuantifier.getInstanceVariable();
		InstanceVariable secondInternalVariable = secondInstanceVariable;
		if (externalVariable.getPlaceholder() == secondInternalVariable.getPlaceholder()) {
			comparedQuantifier.getAppliesTo().put(this.getId(), secondInternalVariable);
			//comparedQuantifier.getRelationalPropositions().add(this);
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
		
		/*
		Quantifier firstCopiedQuantifier;
		InstanceVariable firstVarToAssign;
		
		Quantifier secondCopiedQuantifier;
		InstanceVariable secondVarToAssign;
		*/
		
		/*
		if (firstQuantifier == null & secondQuantifier != null) {
			secondCopiedQuantifier = secondQuantifier.copy();
			secondVarToAssign = secondCopiedQuantifier.getAppliesTo().get(this.getId());
			copiedRelational = new RelationalProposition(copiedAuxOps, null, secondCopiedQuantifier, firstInstanceVariable.copy(), secondVarToAssign);
			
			secondCopiedQuantifier.getParentProposition().replaceAuxOp(secondCopiedQuantifier, secondQuantifier);
			secondCopiedQuantifier.getAppliesTo().remove(this.getId());
			secondCopiedQuantifier.getAppliesTo().put(copiedRelational.getId(), secondVarToAssign);
		}else if (firstQuantifier != null & secondQuantifier == null) {
			firstCopiedQuantifier = firstQuantifier.copy();
			firstVarToAssign = firstCopiedQuantifier.getAppliesTo().get(this.getId());
			copiedRelational = new RelationalProposition(copiedAuxOps, firstCopiedQuantifier, null, firstVarToAssign, secondInstanceVariable.copy());
			
			firstCopiedQuantifier.getParentProposition().replaceAuxOp(firstCopiedQuantifier, firstQuantifier);
			firstCopiedQuantifier.getAppliesTo().remove(this.getId());
			firstCopiedQuantifier.getAppliesTo().put(copiedRelational.getId(), firstVarToAssign);
		}else if (firstQuantifier == null & secondQuantifier == null){
			copiedRelational = new RelationalProposition(copiedAuxOps, null, null, firstInstanceVariable.copy(), secondInstanceVariable.copy());
		}else {
			firstCopiedQuantifier = firstQuantifier.copy();
			firstVarToAssign = firstCopiedQuantifier.getAppliesTo().get(this.getId());
			secondCopiedQuantifier = secondQuantifier.copy();
			secondVarToAssign = secondCopiedQuantifier.getAppliesTo().get(this.getId());
			copiedRelational = new RelationalProposition(copiedAuxOps, firstCopiedQuantifier, secondCopiedQuantifier, firstVarToAssign, secondVarToAssign);
			
			firstCopiedQuantifier.getParentProposition().replaceAuxOp(firstQuantifier, firstCopiedQuantifier);
			firstCopiedQuantifier.getAppliesTo().remove(this.getId());
			firstCopiedQuantifier.getAppliesTo().put(copiedRelational.getId(), firstVarToAssign);
			
			secondCopiedQuantifier.getParentProposition().replaceAuxOp(secondCopiedQuantifier, secondQuantifier);
			secondCopiedQuantifier.getAppliesTo().remove(this.getId());
			secondCopiedQuantifier.getAppliesTo().put(copiedRelational.getId(), secondVarToAssign);
		}
		*/
		
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


















