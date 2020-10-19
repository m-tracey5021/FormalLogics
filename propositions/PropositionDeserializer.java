package propositions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import enums.AuxillaryOperatorType;
import enums.InstanceVariableType;
import enums.OperatorType;
import enums.PlaceholderVariableType;
import enums.PropositionalVariableType;

public class PropositionDeserializer {
	private ObjectMapper mapper;
	
	public PropositionDeserializer() {
		this.mapper = new ObjectMapper();
	}
	
	public Proposition deserialize(String jsonStr) {
		try {
			JsonNode rootNode = mapper.readTree(jsonStr);
			
			UUID id = UUID.fromString(rootNode.get("id").asText());
			String type = rootNode.get("type").asText();
			ArrayList<AuxillaryOperator> auxOps = new ArrayList<AuxillaryOperator>();
			JsonNode auxOpsNode = rootNode.get("auxOps");
			if (auxOpsNode.isArray()) {
				ArrayNode auxOpsArray = (ArrayNode) auxOpsNode;
				for (int i = 0; i < auxOpsArray.size(); i ++) {
					JsonNode auxOpObject = auxOpsArray.get(i);
					String auxOpStr = auxOpObject.get("auxOpType").toString();
					String trimmedAuxOpStr = auxOpStr.substring(1, auxOpStr.length() - 1);
					AuxillaryOperator auxOp = getAuxOpValue(trimmedAuxOpStr);
					if (auxOp.isPredicate()) {
						JsonNode instanceVarObject = auxOpObject.get("instanceVariable");
						String instanceVarStr = instanceVarObject.get("placeholder").toString();
						String trimmedVarStr = instanceVarStr.substring(1, instanceVarStr.length() - 1);
						InstanceVariable instanceVar = getInstanceVariableValue(trimmedVarStr);
						Quantifier quantifier = new Quantifier(auxOp.getAuxOpType(), instanceVar);
						auxOps.add(quantifier);
					}else {
						auxOps.add(auxOp);
					}
					
				}
			}
			//boolean isExpanded = false;
			
			
			if (type.equals("compound")) {
				Proposition firstOperand = deserialize(rootNode.get("firstOperand").toString());
				Proposition secondOperand = deserialize(rootNode.get("secondOperand").toString());
				JsonNode operatorObject = rootNode.get("operator");
				String operatorStr = operatorObject.get("operatorType").toString();
				String trimmedStr = operatorStr.substring(1, operatorStr.length() - 1);
				Operator operator = getOperatorValue(trimmedStr);
				
				
				CompoundProposition compoundProp = new CompoundProposition(firstOperand, secondOperand, operator, auxOps);
				//compoundProp.assignAuxOps();
				firstOperand.setParentProp(compoundProp);
				secondOperand.setParentProp(compoundProp);
				 return compoundProp;
			}else if(type.equals("atomic")) {
				
				//String variableName = rootNode.get("variableName").asText();
				//AtomicProposition atomicProp = new AtomicProposition(auxOps, variableName);  this is for old variable structure with string
				
				String varStr = rootNode.get("variable").get("variable").asText();
				//String trimmedStr = varStr.substring(1, varStr.length() - 1);
				PropositionalVariable variable = getPropositionalVariableValue(varStr);
				AtomicProposition atomicProp = new AtomicProposition(auxOps, variable);
				//atomicProp.assignAuxOps();
				return atomicProp;
			}else if (type.equals("predicated")){
				
				//JsonNode quantifierObject = rootNode.get("quantifier");
				String predicate = rootNode.get("predicate").toString();
				String trimmedPredicate = predicate.substring(1, predicate.length() - 1);
				JsonNode instanceVarObject = rootNode.get("instanceVariable");
				String instanceVarStr = instanceVarObject.get("placeholder").toString();
				String trimmedVarStr = instanceVarStr.substring(1, instanceVarStr.length() - 1);
				InstanceVariable instanceVar = getInstanceVariableValue(trimmedVarStr);

				PredicatedProposition predicatedprop = new PredicatedProposition(auxOps, null, trimmedPredicate, instanceVar);
				//predicatedprop.assignAuxOps();
				return predicatedprop;
				// throw exception
			}else if (type.equals("relational")) {
				JsonNode firstInstanceVarObject = rootNode.get("firstInstanceVariable");
				String firstInstanceVarStr = firstInstanceVarObject.get("placeholder").toString();
				String firstTrimmedVarStr = firstInstanceVarStr.substring(1, firstInstanceVarStr.length() - 1);
				InstanceVariable firstInstanceVar = getInstanceVariableValue(firstTrimmedVarStr);
				
				JsonNode secondInstanceVarObject = rootNode.get("secondInstanceVariable");
				String secondInstanceVarStr = secondInstanceVarObject.get("placeholder").toString();
				String secondTrimmedVarStr = secondInstanceVarStr.substring(1, secondInstanceVarStr.length() - 1);
				InstanceVariable secondInstanceVar = getInstanceVariableValue(secondTrimmedVarStr);
				
				RelationalProposition relationalProp = new RelationalProposition(auxOps, null, null, firstInstanceVar, secondInstanceVar);
				//relationalProp.assignAuxOps();
				return relationalProp;
			}else {
				return new Proposition();
			}
			

		}catch (IOException ex) {
			ex.printStackTrace();
			return new Proposition();

		}
		
	}
	
	public AuxillaryOperator getAuxOpValue(String auxOpStr) {
		if (auxOpStr.equals("NEGATION")) {
			return new AuxillaryOperator(AuxillaryOperatorType.NEGATION);
		}else if (auxOpStr.equals("POSSIBLE")) {
			return new AuxillaryOperator(AuxillaryOperatorType.POSSIBLE);
		}else if (auxOpStr.equals("NECESSARY")) {
			return new AuxillaryOperator(AuxillaryOperatorType.NECESSARY);
		}else if (auxOpStr.equals("NOTPOSSIBLE")) {
			return new AuxillaryOperator(AuxillaryOperatorType.NOTPOSSIBLE);
		}else if (auxOpStr.equals("NOTNECESSARY")) {
			return new AuxillaryOperator(AuxillaryOperatorType.NOTNECESSARY);
		}else if (auxOpStr.equals("UNIVERSAL")) {
			return new AuxillaryOperator(AuxillaryOperatorType.UNIVERSAL);
		}else if (auxOpStr.equals("EXISTENTIAL")) {
			return new AuxillaryOperator(AuxillaryOperatorType.EXISTENTIAL);
		}else if (auxOpStr.equals("NOTUNIVERSAL")) {
			return new AuxillaryOperator(AuxillaryOperatorType.NOTUNIVERSAL);
		}else if (auxOpStr.equals("NOTEXISTENTIAL")) {
			return new AuxillaryOperator(AuxillaryOperatorType.NOTEXISTENTIAL);
		}else {
			return new AuxillaryOperator(AuxillaryOperatorType.NONE);
		}
	}
	
	public Operator getOperatorValue(String operatorStr) {
		if (operatorStr.equals("OR")) {
			return new Operator(OperatorType.OR);
		}else if (operatorStr.equals("AND")) {
			return new Operator(OperatorType.AND);
		}else if (operatorStr.equals("IF")) {
			return new Operator(OperatorType.IF);
		}else if (operatorStr.equals("EQUALS")) {
			return new Operator(OperatorType.EQUALS);
		}else {
			return null;
		}
	}
	
	public PropositionalVariable getPropositionalVariableValue(String varStr) {
		if (varStr.equals("P")) {
			return new PropositionalVariable(PropositionalVariableType.P);
		}else if (varStr.equals("Q")) {
			return new PropositionalVariable(PropositionalVariableType.Q);
		}else if (varStr.equals("R")) {
			return new PropositionalVariable(PropositionalVariableType.R);
		}else if (varStr.equals("S")) {
			return new PropositionalVariable(PropositionalVariableType.S);
		}else if (varStr.equals("T")){
			return new PropositionalVariable(PropositionalVariableType.T);
		}else if (varStr.equals("U")){
			return new PropositionalVariable(PropositionalVariableType.U);
		}else if (varStr.equals("V")){
			return new PropositionalVariable(PropositionalVariableType.V);
		}else {
			return null;
		}
	}
	
	public InstanceVariable getInstanceVariableValue(String varStr) {
		if (varStr.equals("W")) {
			return new InstanceVariable(PlaceholderVariableType.W);
		}else if (varStr.equals("X")){
			return new InstanceVariable(PlaceholderVariableType.X);
		}else if (varStr.equals("Y")) {
			return new InstanceVariable(PlaceholderVariableType.Y);
		}else if (varStr.equals("Z")) {
			return new InstanceVariable(PlaceholderVariableType.Z);
		}else {
			return null;
		}
	}
}
