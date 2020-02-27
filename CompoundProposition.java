import java.util.ArrayList;
import java.util.UUID;

import enums.AuxillaryOperatorType;
import enums.OperatorType;

public class CompoundProposition extends Proposition {
	

	private Proposition firstOperand, secondOperand, parentProp;
	private Operator operator;
	//private boolean isAtomic;
	//private ArrayList<AuxillaryOperator> auxOps;
	

	public CompoundProposition() {
		super("compound");
	}
	/*
	public CompoundProposition(String rep, ArrayList<AuxillaryOperator> auxOps) {
		
		this.id = UUID.randomUUID();
		this.representation = rep;
		this.isAtomic = true;
		this.auxOps = auxOps;
		
	}
	*/
	
	public CompoundProposition(Proposition firstOperand, Proposition secondOperand, Operator operator, ArrayList<AuxillaryOperator> auxOps) {
		super("compound", auxOps);
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
		//firstOperand.setParentProp(this);
		//secondOperand.setParentProp(this);
		this.parentProp = null;
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
	public Proposition getParentProp() {
		return this.parentProp;
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
	public void setParentProp(CompoundProposition prop) {
		this.parentProp = prop;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
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
	
	public String toStringWithoutAuxOps() {
		return "(" + firstOperand.toString() + " " + operator.toString() + " " + secondOperand.toString() + ")";
	}

}







