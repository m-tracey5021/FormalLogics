import java.util.ArrayList;
import java.util.UUID;

public class AtomicProposition extends Proposition {
	private String variableName;

	public AtomicProposition() {
		super();
	}
	
	public AtomicProposition(ArrayList<AuxillaryOperator> auxOps, String variableName) {
		super(auxOps);
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
	
	
	/*
	public AtomicProposition copy() {
		AtomicProposition copiedProp;
		
		ArrayList<AuxillaryOperator> copiedAuxOps = new ArrayList<AuxillaryOperator>();
		for(AuxillaryOperator auxOp : this.getAuxOps()) {
			AuxillaryOperator copiedAuxOp = auxOp.copy();
			copiedAuxOps.add(copiedAuxOp);
		}
		
		copiedProp = new AtomicProposition(copiedAuxOps, variableName);
		return copiedProp;
	}
	*/
}
