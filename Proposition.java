import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import enums.AuxillaryOperatorType;
import enums.OperatorType;

public class Proposition {
	private UUID id;
	private String type;
	private ArrayList<AuxillaryOperator> auxOps;
	private boolean isExpanded;
	private int priority;
	
	public Proposition() {
		
	}
	
	public Proposition(String type) {
		this.type = type;
		this.isExpanded = false;
		this.priority = -1;
	}
	
	public Proposition(String type, ArrayList<AuxillaryOperator> auxOps) {
		this.id = UUID.randomUUID();
		this.type = type;
		this.auxOps = auxOps;
		this.isExpanded = false;
		this.priority = -1;
		checkAuxOpsNonEmpty();
		
	}
	
	// ======= GET
	
	public UUID getId() {
		return this.id;
	}
	public String getType() {
		return this.type;
	}
	public ArrayList<AuxillaryOperator> getAuxOps(){
		return this.auxOps;
	}
	
	public boolean getIsExpanded() {
		return this.isExpanded;
	}
	
	public int getPriority() {
		return this.priority;
	}
	
	// ====== SET
	
	public void setId(UUID id) {
		this.id = id;
	}
	public void setAuxOps(ArrayList<AuxillaryOperator> auxOps) {
		this.auxOps = auxOps;
		checkAuxOpsNonEmpty();
	}
	public void setIsExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}
	
	public void setPriority() {
		AuxillaryOperatorType auxOpType = auxOps.get(0).getAuxOpType();
		if (auxOpType == AuxillaryOperatorType.POSSIBLE || auxOpType == AuxillaryOperatorType.NOTNECESSARY) {
			priority = 4;
		}else if (auxOpType == AuxillaryOperatorType.NECESSARY || auxOpType == AuxillaryOperatorType.NOTPOSSIBLE){
			priority = 5;
		}else {
			if (this instanceof CompoundProposition) {
				
				CompoundProposition thisPropAsCompound = ((CompoundProposition)this);
				OperatorType operatorType = thisPropAsCompound.getOperator().getOperatorType();
				if (auxOpType == AuxillaryOperatorType.NEGATION) {
					if (operatorType == OperatorType.OR || operatorType == OperatorType.IF) {
						priority = 1;
					}else if (operatorType == OperatorType.AND) {
						priority = 2;
					}else {
						priority = 3; // if is negated and operator is equals
					}
				}else { // has no auxOp but does have operator
					if (operatorType == OperatorType.AND) {
						priority = 1;
					}else if (operatorType == OperatorType.OR || operatorType == OperatorType.IF) {
						priority = 2;
					}else {
						priority = 3; // if no auxOp and operator is equals
					}
				}

				
			}else {
				
				priority = 0; // proposition is atomic and has either no auxOp or is negated

				
			}
		}

		
	}
	
	public void addNegation() {
		if (auxOps.get(0).getAuxOpType() == AuxillaryOperatorType.NONE) {
			auxOps.remove(0);
			auxOps.add(new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
		}else {
			auxOps.add(0, new AuxillaryOperator(AuxillaryOperatorType.NEGATION));
			removeDoubleNegation();
		}
		
		
		checkAuxOpsNonEmpty();
	}
	
	private void removeDoubleNegation() {
		AuxillaryOperator first = auxOps.get(0);
		AuxillaryOperator second = null;
		if (auxOps.size() >= 2) {
			second = auxOps.get(1);
		}
		
		if (first.getAuxOpType() == AuxillaryOperatorType.NEGATION & second.getAuxOpType() == AuxillaryOperatorType.NEGATION) {
			auxOps.remove(0);
			auxOps.remove(0);
		}else if (first.getAuxOpType() == AuxillaryOperatorType.NEGATION & second.getAuxOpType() == AuxillaryOperatorType.NOTPOSSIBLE) {
			auxOps.remove(0);
			auxOps.remove(0);
			auxOps.add(0, new AuxillaryOperator(AuxillaryOperatorType.POSSIBLE));
		}else if (first.getAuxOpType() == AuxillaryOperatorType.NEGATION & second.getAuxOpType() == AuxillaryOperatorType.NOTNECESSARY) {
			auxOps.remove(0);
			auxOps.remove(0);
			auxOps.add(0, new AuxillaryOperator(AuxillaryOperatorType.NECESSARY));
		}
	}
	
	public void removeRelevantAuxOp() {
		auxOps.remove(0);
		checkAuxOpsNonEmpty();
	}
	
	private void checkAuxOpsNonEmpty() {
		if (auxOps != null) {
			if (auxOps.size() == 0) {
				this.auxOps = new ArrayList<AuxillaryOperator>(Arrays.asList(new AuxillaryOperator(AuxillaryOperatorType.NONE)));
			}
		}else {
			this.auxOps = new ArrayList<AuxillaryOperator>(Arrays.asList(new AuxillaryOperator(AuxillaryOperatorType.NONE)));
		}
		
		
	}
	
	public Proposition copy() {
		ArrayList<AuxillaryOperator> copiedAuxOps = new ArrayList<AuxillaryOperator>();
		for(AuxillaryOperator auxOp : auxOps) {
			AuxillaryOperator copiedAuxOp = auxOp.copy();
			copiedAuxOps.add(copiedAuxOp);
		}
		if (this instanceof CompoundProposition) {
			
			CompoundProposition thisPropAsCompound = ((CompoundProposition)this);
			Proposition copiedFirst;
			Proposition copiedSecond;
			
			if (thisPropAsCompound.getFirstOperand() instanceof AtomicProposition) {
				copiedFirst = ((AtomicProposition) thisPropAsCompound.getFirstOperand()).copy();
			}else {
				copiedFirst = ((CompoundProposition) thisPropAsCompound.getFirstOperand()).copy();
			}
			
			if (thisPropAsCompound.getSecondOperand() instanceof AtomicProposition) {
				copiedSecond = ((AtomicProposition) thisPropAsCompound.getSecondOperand()).copy();
			}else {
				copiedSecond = ((CompoundProposition) thisPropAsCompound.getSecondOperand()).copy();
			} 
			
			Operator copiedOp = thisPropAsCompound.getOperator().copy();
			
			CompoundProposition copiedCompoundProp = new CompoundProposition(copiedFirst, copiedSecond, copiedOp, copiedAuxOps);
			System.out.println("original compound prop: " + this.hashCode() + " rep: " + this.toString());
			System.out.println("copied compound prop: " + copiedCompoundProp.hashCode() + " rep: " + this.toString());
			return copiedCompoundProp;
		}else {
			AtomicProposition thisPropAsAtomic = ((AtomicProposition)this);
			AtomicProposition copiedAtomicProp = new AtomicProposition(copiedAuxOps, thisPropAsAtomic.getVariableName());
			System.out.println("original atomic prop: " + this.hashCode() + " rep: " + this.toString());
			System.out.println("copied atomic prop: " + copiedAtomicProp.hashCode() + " rep: " + this.toString());
			return copiedAtomicProp;
		}

	}
}
