
public class WindowController {
	private boolean boolRet;
	private int intRet;
	private Proposition storedProp;
	
	public WindowController() {
		
	}
	
	// ======= GET
	
	public Proposition getStoredProp() {
		return this.storedProp;
	}
	public boolean getBoolRet() {
		return this.boolRet;
	}
	// ======= SET
	
	public void setStoredProp(Proposition prop) {
		this.storedProp = prop;
	}
	public void setBoolRet(boolean val) {
		this.boolRet = val;
	}
}
