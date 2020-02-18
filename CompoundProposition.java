import java.util.ArrayList;
import java.util.UUID;

import enums.AuxillaryOperatorType;

public class CompoundProposition extends Proposition {
	

	private Proposition firstOperand, secondOperand, parentProp;
	private Operator operator;
	//private boolean isAtomic;
	//private ArrayList<AuxillaryOperator> auxOps;
	

	public CompoundProposition() {
		super();
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
		super(auxOps);
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
		//firstOperand.setParentProp(this);
		//secondOperand.setParentProp(this);
		this.parentProp = null;
		this.operator = operator;

		
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
	public Operator getOp() {
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
	public void setOp(Operator op) {
		this.operator = op;
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
	
	
	/*
	public CompoundProposition copy() {
		CompoundProposition copiedProp;

		ArrayList<AuxillaryOperator> copiedAuxOps = new ArrayList<AuxillaryOperator>();
		for(AuxillaryOperator auxOp : this.getAuxOps()) {
			AuxillaryOperator copiedAuxOp = auxOp.copy();
			copiedAuxOps.add(copiedAuxOp);
		}

		
		Proposition copiedFirst;
		Proposition copiedSecond;
		
		if (firstOperand instanceof AtomicProposition) {
			copiedFirst = ((AtomicProposition) firstOperand).copy();
		}else {
			copiedFirst = ((CompoundProposition) firstOperand).copy();
		}
		
		if (secondOperand instanceof AtomicProposition) {
			copiedSecond = ((AtomicProposition) secondOperand).copy();
		}else {
			copiedSecond = ((CompoundProposition) secondOperand).copy();
		}

		Operator copiedOperator = operator.copy();
		
		copiedProp = new CompoundProposition(copiedFirst, copiedSecond, copiedOperator, copiedAuxOps);
		System.out.println("original prop: " + this.hashCode() + " rep: " + this.toString());
		System.out.println("copied prop: " + copiedProp.hashCode() + " rep: " + this.toString());
		return copiedProp;
			

	}
	
	public boolean isMatch(CompoundProposition otherProp) {
		if (this.toString().equals(otherProp.toString())) {
			return true;
		}else {
			return false;
		}
	}
	
	*/
}







