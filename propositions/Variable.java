package propositions;

import enums.InstanceVariableType;

public class Variable {
	private InstanceVariableType instantiatedVariable;
	
	public Variable() {
		this.instantiatedVariable = null;
	}
	
	public Variable(InstanceVariableType instantiatedVariable) {
		this.instantiatedVariable = instantiatedVariable;
	}
	
	// ======= GET
	
	public InstanceVariableType getVariable() {
		return this.instantiatedVariable;
	}
	
	// ========= SET
	
	public void setVariable(InstanceVariableType instantiatedVariable) {
		this.instantiatedVariable = instantiatedVariable;
	}
	
	
	@Override
	public String toString() {
		return this.instantiatedVariable.toString().toLowerCase();
	}
}
