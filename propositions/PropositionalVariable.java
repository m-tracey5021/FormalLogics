package propositions;

import enums.PropositionalVariableType;

public class PropositionalVariable {
	private PropositionalVariableType variable;
	
	public PropositionalVariable() {
		
	}
	
	public PropositionalVariable(PropositionalVariableType variable) {
		this.variable = variable;
	}
	
	public PropositionalVariableType getVariable() {
		return this.variable;
	}
	
	public PropositionalVariable copy() {
		return new PropositionalVariable(variable);
	}
	
	@Override 
	public String toString() {
		return variable.toString().toLowerCase();
	}
}
