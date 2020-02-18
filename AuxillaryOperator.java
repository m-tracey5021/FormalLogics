import enums.AuxillaryOperatorType;

public class AuxillaryOperator {
	
	private AuxillaryOperatorType auxillaryOperator;
	
	public AuxillaryOperator() {
		
	}
	
	public AuxillaryOperator (AuxillaryOperatorType auxillaryOperator) {
		this.auxillaryOperator = auxillaryOperator;
	}
	
	public AuxillaryOperatorType getAuxOp() {
		return this.auxillaryOperator;
	}
	
	public void setAuxOp(AuxillaryOperatorType auxillaryOperator) {
		this.auxillaryOperator = auxillaryOperator;
	}
	
	@Override
	public String toString() {
		if (auxillaryOperator == AuxillaryOperatorType.NEGATION) {
			return "~";
		}else if (auxillaryOperator == AuxillaryOperatorType.POSSIBILITY) {
			return "\u25C7 ";
		}else if (auxillaryOperator == AuxillaryOperatorType.NECESSITY) {
			return "\u2610 ";
		}else if (auxillaryOperator == AuxillaryOperatorType.UNIVERSAL) {
			return "\u2200 ";
		}else if (auxillaryOperator == AuxillaryOperatorType.EXISTENTIAL) {
			return "\u2203 ";
		}else {
			return "";
		}
	}
	
	public AuxillaryOperator copy() {
		
		
		AuxillaryOperator copiedAuxOp = new AuxillaryOperator(auxillaryOperator);
		System.out.println("original auxOp: " + this.hashCode());
		System.out.println("copied auxOp: " + copiedAuxOp.hashCode());
		return copiedAuxOp;
	}
}
