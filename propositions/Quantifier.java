package propositions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import enums.AuxillaryOperatorType;

public class Quantifier extends AuxillaryOperator {
	private InstanceVariable instanceVariable;
	@JsonIgnore
	private HashMap<UUID, InstanceVariable> appliesTo;
	
	
	public Quantifier(AuxillaryOperatorType auxOpType, InstanceVariable instanceVariable) {
		super(auxOpType);
		this.instanceVariable = instanceVariable;
		this.appliesTo = new HashMap<UUID, InstanceVariable>();
	}
	
	public Quantifier(AuxillaryOperatorType auxOpType, InstanceVariable instanceVariable, HashMap<UUID, InstanceVariable> appliesTo) {
		super(auxOpType);
		this.instanceVariable = instanceVariable;
		this.appliesTo = appliesTo;
	}
	
	// ============ GET
	
	
	
	public InstanceVariable getInstanceVariable() {
		return this.instanceVariable;
	}
	
	public HashMap<UUID, InstanceVariable> getAppliesTo(){
		return this.appliesTo;
	}
	
	// ============= SET
	
	
	
	public void setInstanceVariable(InstanceVariable instanceVariable) {
		this.instanceVariable = instanceVariable;
	}
	
	public void setAppliesTo(HashMap<UUID, InstanceVariable> appliesTo) {
		this.appliesTo = appliesTo;
	}
	

	
	// ============ OVERRIDEN METHODS
	
	
	
	@Override 
	public boolean isPredicate() {
		return true;
	}
	
	@Override 
	public String toString() {
		String varStr = "";
		if (instanceVariable != null) {
			varStr = instanceVariable.toString();
		}
		if (super.getAuxOpType() == AuxillaryOperatorType.UNIVERSAL) {
			return "\u2200" + varStr;
		}else if (super.getAuxOpType() == AuxillaryOperatorType.EXISTENTIAL) {
			return "\u2203" + varStr;
		}else if (super.getAuxOpType() == AuxillaryOperatorType.NOTUNIVERSAL) {
			return "~\u2200" + varStr;
		}else if (super.getAuxOpType() == AuxillaryOperatorType.NOTEXISTENTIAL) {
			return "~\u2203" + varStr;
		}else {
			return "";
		}
	}
	
	
	@Override
	public Quantifier copy() {
		HashMap<UUID, InstanceVariable> copiedAppliesTo = new HashMap<UUID, InstanceVariable>();
		
		for(Map.Entry<UUID, InstanceVariable> entry : appliesTo.entrySet()) {
			UUID predicateId = entry.getKey();
			InstanceVariable instanceVar = entry.getValue();
			InstanceVariable copiedInstanceVar = instanceVar.copy();
			
			copiedAppliesTo.put(predicateId, copiedInstanceVar);
		}
		
		
		if (instanceVariable != null) {
			return new Quantifier(super.getAuxOpType(), instanceVariable.copy(), copiedAppliesTo);
		}else {
			return new Quantifier(super.getAuxOpType(), null, copiedAppliesTo);
		}
		
	}
	
}
