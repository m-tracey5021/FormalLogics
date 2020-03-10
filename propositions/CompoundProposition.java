package propositions;
import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import enums.AuxillaryOperatorType;
import enums.OperatorType;

public class CompoundProposition extends Proposition {
	

	private Proposition firstOperand, secondOperand;
	private Operator operator;
	

	public CompoundProposition() {
		super("compound");
	}


	
	public CompoundProposition(Proposition firstOperand, Proposition secondOperand, Operator operator, ArrayList<AuxillaryOperator> auxOps) {
		super("compound", auxOps);
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
		this.operator = operator;
		super.setPriority();

		
	}
	

	// ====== GET
	
	public Proposition getFirstOperand() {
		return this.firstOperand;
	}

	public Proposition getSecondOperand() {
		return this.secondOperand;
	}
	public Operator getOperator() {
		return this.operator;
	}

	
	// ====== SET
	

	public void setFirstOperand(Proposition prop) {
		this.firstOperand = prop;
	}
	public void setSecondOperand(Proposition prop) {
		this.secondOperand = prop;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
	// =======================
	
	// ============ OVERRIDEN METHODS
		
	
	@Override
	public void getPredicateProps(ArrayList<Proposition> propositions){
		if (this.getFirstOperand() != null) {
			//if (this.getFirstOperand().getType().equals("relational") || this.getFirstOperand().getType().equals("predicated")) {
			//	propositions.add(this.getFirstOperand());
			//}
			this.getFirstOperand().getPredicateProps(propositions);
		}
		if (this.getSecondOperand() != null) {
			//if (this.getSecondOperand().getType().equals("relational") || this.getSecondOperand().getType().equals("predicated")) {
			//	propositions.add(this.getSecondOperand());
			//}
			this.getSecondOperand().getPredicateProps(propositions);
		}
	}
	
	
	@Override
	public void assignVariables(PredicatedProposition predicate) {
		for (int i = 0; i < super.getAuxOps().size(); i ++) {
			if (super.getAuxOps().get(i).isPredicate()) {
				Quantifier parentQuantifier = (Quantifier) super.getAuxOps().get(i);
				if (predicate.isVariableMatch(parentQuantifier)) {
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
	public void assignVariables(RelationalProposition relational) {

		for (int i = 0; i < super.getAuxOps().size(); i ++) {
			if (super.getAuxOps().get(i).isPredicate()) {
				Quantifier parentQuantifier = (Quantifier) super.getAuxOps().get(i);
				if (relational.getFirstQuantifier() == null & relational.getSecondQuantifier() != null) {
					if (relational.isFirstVariableMatch(parentQuantifier)) {
						return;
					}
				}else if (relational.getFirstQuantifier() != null & relational.getSecondQuantifier() == null) {
					if (relational.isSecondVariableMatch(parentQuantifier)) {
						return;
					}
				}else if (relational.getFirstQuantifier() == null & relational.getSecondQuantifier() == null) { // if neither has a quantifier match
					if (relational.isFirstVariableMatch(parentQuantifier) & relational.isSecondVariableMatch(parentQuantifier)) {
						return;
					}
				}else {
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
	public CompoundProposition copy() {
		
		ArrayList<AuxillaryOperator> copiedAuxOps = new ArrayList<AuxillaryOperator>();
		for(AuxillaryOperator auxOp : super.getAuxOps()) {
			AuxillaryOperator copiedAuxOp = auxOp.copy();
			copiedAuxOps.add(copiedAuxOp);
		}
		Proposition copiedFirst;
		Proposition copiedSecond;
		
	
		copiedFirst = firstOperand.copy();

		copiedSecond = secondOperand.copy();


		Operator copiedOp = operator.copy();
		
		CompoundProposition copiedCompoundProp = new CompoundProposition(copiedFirst, copiedSecond, copiedOp, copiedAuxOps);
		copiedFirst.setParentProp(copiedCompoundProp);
		copiedSecond.setParentProp(copiedCompoundProp);
		/*
		ArrayList<Proposition> allPredicates = copiedCompoundProp.getAllPredicateProps();
		for (Proposition pred : allPredicates) {
			pred.startAssignment();
		}
		*/
		copiedCompoundProp.assignVariablesForAll();
		return copiedCompoundProp;
	}

	
	@Override
	public String toString() {
		String auxOpsStr = "";
		if (this.getAuxOps() != null) {
			for (AuxillaryOperator auxOp : this.getAuxOps()) {
				auxOpsStr += auxOp;
			}
		}
		return auxOpsStr + "(" + firstOperand.toString() + " " + operator.toString() + " " + secondOperand.toString() + ")";
	}
}







