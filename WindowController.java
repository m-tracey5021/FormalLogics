import java.util.ArrayList;

public class WindowController {
	private boolean storedBool;
	private int storedInt;
	private Proposition storedProp;
	private ArrayList<AuxillaryOperator> storedAuxOps;
	
	public WindowController() {
		
	}
	
	// ======= GET
	
	public boolean getBoolRet() {
		return this.storedBool;
	}
	
	public int getIntRet() {
		return this.storedInt;
	}
	
	public Proposition getStoredProp() {
		return this.storedProp;
	}
	
	public ArrayList<AuxillaryOperator> getStoredAuxOps(){
		return this.storedAuxOps;
	}
	
	// ======= SET
	
	public void setBoolRet(boolean val) {
		this.storedBool = val;
	}
	
	public void setIntRet(int num) {
		this.storedInt = num;
	}
	
	public void setStoredProp(Proposition prop) {
		this.storedProp = prop;
	}
	
	public void setStoredAuxOps(ArrayList<AuxillaryOperator> auxOps) {
		this.storedAuxOps = auxOps;
	}
	
}
