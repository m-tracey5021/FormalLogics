package propositions;

import enums.InstanceVariableState;
import enums.InstanceVariableType;
import enums.PlaceholderVariableType;

public class InstanceVariable {
	private PlaceholderVariableType placeholder;
	private InstanceVariableType variable;
	private InstanceVariableState state;
	
	public InstanceVariable() {
		
	}
	
	public InstanceVariable(PlaceholderVariableType placeholder, 
			InstanceVariableType variable, 
			InstanceVariableState state) {
		this.placeholder = placeholder;
		this.variable = variable;
		this.state = state;
	}
	
	public InstanceVariable(PlaceholderVariableType placeholder) {
		this.placeholder = placeholder;
		this.variable = null;
		this.state = InstanceVariableState.NOTINSTANTIATED;
	}
	
	// ======= GET
	
	public PlaceholderVariableType getPlaceholder() {
		return this.placeholder;
	}
	
	public InstanceVariableType getVariable() {
		return this.variable;
	}
	
	public InstanceVariableState getState() {
		return this.state;
	}
	
	// =========== SET
	
	public void setState(InstanceVariableState state) {
		this.state = state;
	}
	
	public void setVariable(InstanceVariableType variable) {
		this.variable = variable;
		this.state = InstanceVariableState.INSTANTIATED;
	}
	
	@Override
	public String toString() {

		if (state == InstanceVariableState.INSTANTIATED) {
			return variable.toString().toLowerCase();
		}else {
			return placeholder.toString().toLowerCase();
		}
	}
	
	public InstanceVariable copy() {
		return new InstanceVariable(placeholder, variable, state);
	}
	
}
