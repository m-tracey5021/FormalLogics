package propositions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class QuantifierContainer {
	private Quantifier quantifier;
	private HashMap<InstanceVariable, UUID> applicableInstanceVariables;
	
	public QuantifierContainer() {
		this.applicableInstanceVariables = new HashMap<InstanceVariable, UUID>();
	}
	
	// ========== GET
	
	public Quantifier getQuantifier() {
		return this.quantifier;
	}
	
	public HashMap<InstanceVariable, UUID> getApplicableInstanceVariables(){
		return this.applicableInstanceVariables;
	}
	
	// ========= SET
	
	public void setQuantifier(Quantifier quantifier) {
		this.quantifier = quantifier;
	}
}
