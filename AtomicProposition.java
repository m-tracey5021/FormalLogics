import java.util.ArrayList;
import java.util.UUID;

import enums.AuxillaryOperatorType;

public class AtomicProposition extends Proposition {
	private String variableName;

	public AtomicProposition() {
		super("atomic");
		super.setIsExpanded(true);
	}
	
	public AtomicProposition(ArrayList<AuxillaryOperator> auxOps, String variableName) {
		super("atomic", auxOps);
		super.setIsExpanded(isNotExpandable(auxOps));
		super.setPriority();
		this.variableName = variableName;
		
	}
	
	// ======= GET
	

	public String getVariableName() {
		return this.variableName;
	}
	
	// ======= SET
	

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	
	public boolean isNotExpandable(ArrayList<AuxillaryOperator> auxOps) {
		if (auxOps == null) {
			return true;
		}else if (auxOps.get(0).getAuxOpType() == AuxillaryOperatorType.NEGATION) {
			return true;
		}else {
			return false;
		}
	}

	
	@Override
	public String toString() {
		String auxOpsStr = "";
		if (this.getAuxOps() != null) {
			for (AuxillaryOperator auxOp : this.getAuxOps()) {
				auxOpsStr += auxOp;
			}
		}
		return auxOpsStr + this.variableName;
	}
	
	public String toStringWithoutAuxOps() {
		return this.variableName;
	}
	

}
