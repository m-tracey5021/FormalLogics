package propositions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import enums.AuxillaryOperatorType;
import enums.OperatorType;

public class Proposition {
	private UUID id;
	private String type;
	private ArrayList<AuxillaryOperator> auxOps;
	@JsonIgnore
	private Proposition parentProp;
	@JsonIgnore
	private boolean isExpanded;
	@JsonIgnore
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
	
	public Proposition getParentProp() {
		return this.parentProp;
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
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setAuxOps(ArrayList<AuxillaryOperator> auxOps) {
		this.auxOps = auxOps;
		checkAuxOpsNonEmpty();
	}
	
	public void setParentProp(Proposition parentProp) {
		this.parentProp = parentProp;
	}
	
	public void setIsExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}
	
	
	// ===========================
	
	
	public void setPriority() {
		AuxillaryOperatorType auxOpType = auxOps.get(0).getAuxOpType();
		if (auxOpType == AuxillaryOperatorType.EXISTENTIAL || auxOpType == AuxillaryOperatorType.NOTUNIVERSAL) {
			priority = 4;
		}else if (auxOpType == AuxillaryOperatorType.UNIVERSAL || auxOpType == AuxillaryOperatorType.NOTEXISTENTIAL) {
			priority = 5;
		}else if (auxOpType == AuxillaryOperatorType.POSSIBLE || auxOpType == AuxillaryOperatorType.NOTNECESSARY) {
			priority = 6;
		}else if (auxOpType == AuxillaryOperatorType.NECESSARY || auxOpType == AuxillaryOperatorType.NOTPOSSIBLE){
			priority = 7;
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
	

	public ArrayList<Proposition>  initAllPredicateProps() {
		ArrayList<Proposition> allPredicateProps = new ArrayList<Proposition>();
		this.getPredicateProps(allPredicateProps);
		return allPredicateProps;
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
		setPriority();
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
		}else if (first.getAuxOpType() == AuxillaryOperatorType.NEGATION & second.getAuxOpType() == AuxillaryOperatorType.NOTUNIVERSAL) {
			auxOps.remove(0);
			auxOps.remove(0);
			auxOps.add(0, new AuxillaryOperator(AuxillaryOperatorType.UNIVERSAL));
		}else if (first.getAuxOpType() == AuxillaryOperatorType.NEGATION & second.getAuxOpType() == AuxillaryOperatorType.NOTEXISTENTIAL) {
			auxOps.remove(0);
			auxOps.remove(0);
			auxOps.add(0, new AuxillaryOperator(AuxillaryOperatorType.EXISTENTIAL));
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
	
	public void assignVariablesForAll() {
		ArrayList<Proposition> allPredicates = initAllPredicateProps();
		for (Proposition pred : allPredicates) {
			pred.startAssignment();
		}
	}
	

	// ================ OVERRIDEN METHODS
	
	public void getPredicateProps(ArrayList<Proposition> propositions){
		return;
	}

	public void assignVariables(PredicatedProposition predicate) {
		return;
	}
	
	public void assignVariables(RelationalProposition relational) {
		return;
	}

	public void startAssignment() {
		return;
	}
	
	//public void assignAuxOps() {
	//	for (AuxillaryOperator auxOp : auxOps) {
	//		auxOp.setParentProposition(this);
	//	}
	//}
	
	public void replaceAuxOp(AuxillaryOperator oldAuxOp, AuxillaryOperator newAuxOp) {
		int index = auxOps.indexOf(oldAuxOp);
		auxOps.remove(oldAuxOp);
		auxOps.add(index, newAuxOp);
	}
	
	public Proposition copy() {
		return null;
	}
	
	public boolean auxOpsAreContradictory(ArrayList<AuxillaryOperator> otherAuxOps) {
		
		
		int thisSize = auxOps.size();
		int otherSize = otherAuxOps.size();
		
		if (thisSize == 1 & otherSize == 1) {
			if (auxOps.get(0).getAuxOpType() == AuxillaryOperatorType.NEGATION) {
				if (otherAuxOps.get(0).getAuxOpType() == AuxillaryOperatorType.NONE) {
					return true;
				}
			}
			
			if (otherAuxOps.get(0).getAuxOpType() == AuxillaryOperatorType.NEGATION) {
				if (auxOps.get(0).getAuxOpType() == AuxillaryOperatorType.NONE) {
					return true;
				}
			}
		}
		
		if (thisSize > otherSize & thisSize - otherSize == 1) {
			if (auxOps.get(0).getAuxOpType() == AuxillaryOperatorType.NEGATION) {
				for (int i = 0; i < thisSize; i ++) {
					AuxillaryOperator auxOp = auxOps.get(i + 1);
					AuxillaryOperator otherAuxOp = otherAuxOps.get(i);
					if (auxOp.getAuxOpType() != otherAuxOp.getAuxOpType()) {
						return false;
					}
				}
				return true;
			}else {
				return false;
			}
		}else if (thisSize < otherSize & otherSize - thisSize == 1){
			if (otherAuxOps.get(0).getAuxOpType() == AuxillaryOperatorType.NEGATION) {
				for (int i = 0; i < otherSize; i ++) {
					AuxillaryOperator auxOp = auxOps.get(i);
					AuxillaryOperator otherAuxOp = otherAuxOps.get(i + 1);
					if (auxOp.getAuxOpType() != otherAuxOp.getAuxOpType()) {
						return false;
					}
				}
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
		
		
		
		/*
		int diff = thisSize - otherSize;
		if (diff == 1 | diff == -1) {
			
		}else {
			return false;
		}
		*/
	}
	
	public boolean auxOpsAreEqual(ArrayList<AuxillaryOperator> otherAuxOps) {
		if (auxOps.size() != otherAuxOps.size()) {
			return false;
		}else {
			for (int i = 0; i < auxOps.size(); i ++) {
				AuxillaryOperator auxOp = auxOps.get(i);
				AuxillaryOperator otherAuxOp = otherAuxOps.get(i);
				if (auxOp.getAuxOpType() != otherAuxOp.getAuxOpType()) {
					return false;
				}
			}
			return true;
		}
		
	}
	
	public boolean isEqualTo(Proposition other) {
		if (this instanceof CompoundProposition & other instanceof CompoundProposition) {
			CompoundProposition thisProp = (CompoundProposition) this;
			CompoundProposition otherProp = (CompoundProposition) other;
			if (thisProp.getFirstOperand().isEqualTo(otherProp.getFirstOperand()) & 
					thisProp.getSecondOperand().isEqualTo(otherProp.getSecondOperand())) {
				if (auxOpsAreEqual(otherProp.getAuxOps())) {
					return true;
				}else {
					return false; 
				}
			}else {
				return false;
			}
		}else if (this instanceof PredicatedProposition & other instanceof PredicatedProposition) {
			PredicatedProposition thisProp = (PredicatedProposition) this;
			PredicatedProposition otherProp = (PredicatedProposition) other;
			if (thisProp.getInstanceVariable().getVariable() == otherProp.getInstanceVariable().getVariable() & 
					thisProp.getPredicate().equals(otherProp.getPredicate())) {
				if (auxOpsAreEqual(otherProp.getAuxOps())) {
					return true;
				}else {
					return false; 
				}
			}else {
				return false;
			}
		}else if (this instanceof RelationalProposition & other instanceof RelationalProposition) {
			RelationalProposition thisProp = (RelationalProposition) this;
			RelationalProposition otherProp = (RelationalProposition) other;
			if (thisProp.getFirstInstanceVariable().getVariable() == otherProp.getFirstInstanceVariable().getVariable() & 
					thisProp.getSecondInstanceVariable().getVariable() == otherProp.getSecondInstanceVariable().getVariable()) {
				if (auxOpsAreEqual(otherProp.getAuxOps())) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
				
			
		}else if (this instanceof AtomicProposition & other instanceof AtomicProposition) {
			AtomicProposition thisProp = (AtomicProposition) this;
			AtomicProposition otherProp = (AtomicProposition) other;
			if (thisProp.getVariable().getVariable() == otherProp.getVariable().getVariable()) {
				if (auxOpsAreEqual(otherProp.getAuxOps())) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	
	public boolean isContradictory(Proposition other) {
		if (this instanceof CompoundProposition & other instanceof CompoundProposition) {
			CompoundProposition thisProp = (CompoundProposition) this;
			CompoundProposition otherProp = (CompoundProposition) other;
			if (thisProp.getFirstOperand().isContradictory(otherProp.getFirstOperand()) & 
					thisProp.getSecondOperand().isContradictory(otherProp.getSecondOperand())) {
				if (auxOpsAreContradictory(otherProp.getAuxOps())) {
					return true;
				}else {
					return false; 
				}
			}else {
				return false;
			}
		}else if (this instanceof PredicatedProposition & other instanceof PredicatedProposition) {
			PredicatedProposition thisProp = (PredicatedProposition) this;
			PredicatedProposition otherProp = (PredicatedProposition) other;
			if (thisProp.getInstanceVariable().getVariable() == otherProp.getInstanceVariable().getVariable() & 
					thisProp.getPredicate().equals(otherProp.getPredicate())) {
				if (auxOpsAreContradictory(otherProp.getAuxOps())) {
					return true;
				}else {
					return false; 
				}
			}else {
				return false;
			}
		}else if (this instanceof RelationalProposition & other instanceof RelationalProposition) {
			RelationalProposition thisProp = (RelationalProposition) this;
			RelationalProposition otherProp = (RelationalProposition) other;
			if (thisProp.getFirstInstanceVariable().getVariable() == otherProp.getFirstInstanceVariable().getVariable() & 
					thisProp.getSecondInstanceVariable().getVariable() == otherProp.getSecondInstanceVariable().getVariable()) {
				if (auxOpsAreContradictory(otherProp.getAuxOps())) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
				
			
		}else if (this instanceof AtomicProposition & other instanceof AtomicProposition) {
			AtomicProposition thisProp = (AtomicProposition) this;
			AtomicProposition otherProp = (AtomicProposition) other;
			if (thisProp.getVariable().getVariable() == otherProp.getVariable().getVariable()) {
				if (auxOpsAreContradictory(otherProp.getAuxOps())) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
}
