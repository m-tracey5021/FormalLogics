import java.util.ArrayList;
import java.util.UUID;

public class Proposition {
	private UUID id;
	private ArrayList<AuxillaryOperator> auxOps;
	
	public Proposition() {
		
	}
	
	public Proposition(ArrayList<AuxillaryOperator> auxOps) {
		this.id = UUID.randomUUID();
		this.auxOps = auxOps;
	}
	
	// ======= GET
	
	public UUID getId() {
		return this.id;
	}
	public ArrayList<AuxillaryOperator> getAuxOps(){
		return this.auxOps;
	}
	
	// ====== SET
	
	public void setId(UUID id) {
		this.id = id;
	}
	public void setAuxOps(ArrayList<AuxillaryOperator> auxOps) {
		this.auxOps = auxOps;
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
			
			Operator copiedOp = thisPropAsCompound.getOp().copy();
			
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
