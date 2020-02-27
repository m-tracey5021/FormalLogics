import enums.AuxillaryOperatorType;

public class AuxillaryOperator {
	
	private AuxillaryOperatorType auxillaryOperatorType;
	
	public AuxillaryOperator() {
		
	}
	
	public AuxillaryOperator (AuxillaryOperatorType auxillaryOperator) {
		this.auxillaryOperatorType = auxillaryOperator;
	}
	
	public AuxillaryOperatorType getAuxOpType() {
		return this.auxillaryOperatorType;
	}
	
	public void setAuxOp(AuxillaryOperatorType auxillaryOperator) {
		this.auxillaryOperatorType = auxillaryOperator;
	}
	
	public boolean isClassical() {
		if (this.auxillaryOperatorType == AuxillaryOperatorType.NONE || 
				this.auxillaryOperatorType == AuxillaryOperatorType.NEGATION) {
			return true;
		}else {
			return false;
		}
	}
	
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
	
	
	public AuxillaryOperator copy() {
		AuxillaryOperator copiedAuxOp = new AuxillaryOperator(auxillaryOperatorType);
		System.out.println("original auxOp: " + this.hashCode());
		System.out.println("copied auxOp: " + copiedAuxOp.hashCode());
		return copiedAuxOp;
	}


	
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
		}else if (auxillaryOperatorType == AuxillaryOperatorType.UNIVERSAL) {
			return "\u2200 ";
		}else if (auxillaryOperatorType == AuxillaryOperatorType.EXISTENTIAL) {
			return "\u2203 ";
		}else if (auxillaryOperatorType == AuxillaryOperatorType.NOTUNIVERSAL) {
			return "~\u2200 ";
		}else if (auxillaryOperatorType == AuxillaryOperatorType.NOTEXISTENTIAL) {
			return "~\u2203 ";
		}else {
			return ""; // if is NONE
		}
	}
	
	
}
