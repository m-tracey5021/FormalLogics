package propositions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import enums.AuxillaryOperatorType;

public class AuxillaryOperator {
	//@JsonIgnore
	//private Proposition parentProposition;
	private AuxillaryOperatorType auxillaryOperatorType;
	
	public AuxillaryOperator() {
		
	}

	public AuxillaryOperator (AuxillaryOperatorType auxillaryOperator) {
		this.auxillaryOperatorType = auxillaryOperator;
	}
	
	// ============ GET
	
	//public Proposition getParentProposition() {
	//	return this.parentProposition;
	//}
	
	public AuxillaryOperatorType getAuxOpType() {
		return this.auxillaryOperatorType;
	}
	
	// ========== SET
	
	//public void setParentProposition(Proposition parentProposition) {
	//	this.parentProposition = parentProposition;
	//}
	
	public void setAuxOp(AuxillaryOperatorType auxillaryOperator) {
		this.auxillaryOperatorType = auxillaryOperator;
	}
	
	// =================
	
	@JsonIgnore
	public boolean isClassical() {
		if (this.auxillaryOperatorType == AuxillaryOperatorType.NONE || 
				this.auxillaryOperatorType == AuxillaryOperatorType.NEGATION) {
			return true;
		}else {
			return false;
		}
	}
	
	@JsonIgnore
	public boolean isModal() {
		if (this.auxillaryOperatorType == AuxillaryOperatorType.POSSIBLE || 
				this.auxillaryOperatorType == AuxillaryOperatorType.NECESSARY || 
				this.auxillaryOperatorType == AuxillaryOperatorType.NOTPOSSIBLE || 
				this.auxillaryOperatorType == AuxillaryOperatorType.NOTNECESSARY) {
			return true;
		}else {
			return false;
		}
	}
	
	@JsonIgnore
	public boolean isPredicate() {
		if (this.auxillaryOperatorType == AuxillaryOperatorType.UNIVERSAL || 
				this.auxillaryOperatorType == AuxillaryOperatorType.EXISTENTIAL || 
				this.auxillaryOperatorType == AuxillaryOperatorType.NOTUNIVERSAL || 
				this.auxillaryOperatorType == AuxillaryOperatorType.NOTEXISTENTIAL) {
			return true;
		}else {
			return false;
		}
	}
	
	
	// ==================
	
	
	public AuxillaryOperator copy() {
		AuxillaryOperator copiedAuxOp = new AuxillaryOperator(auxillaryOperatorType);
		return copiedAuxOp;
	}
	
	// =========== METHODS TO OVERRIDE

	@Override
	public String toString() {
		if (auxillaryOperatorType == AuxillaryOperatorType.NEGATION) {
			return "~";
		}else if (auxillaryOperatorType == AuxillaryOperatorType.POSSIBLE) {
			return "\u25C7 ";
		}else if (auxillaryOperatorType == AuxillaryOperatorType.NECESSARY) {
			return "\u2610 ";
		}else if (auxillaryOperatorType == AuxillaryOperatorType.NOTPOSSIBLE) {
			return "~\u25C7 ";
		}else if (auxillaryOperatorType == AuxillaryOperatorType.NOTNECESSARY) {
			return "~\u2610 ";
		}else {
			return ""; // if is NONE
		}
	}
	
	
}
