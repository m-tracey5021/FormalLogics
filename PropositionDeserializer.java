import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import enums.AuxillaryOperatorType;
import enums.OperatorType;

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
					String trimmedStr = auxOpStr.substring(1, auxOpStr.length() - 1);
					AuxillaryOperator auxOp = getAuxOpValue(trimmedStr);
					auxOps.add(auxOp);
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
				 return compoundProp;
			}else if(type.equals("atomic")) {
				
				String variableName = rootNode.get("variableName").asText();

				
				
				
				AtomicProposition atomicProp = new AtomicProposition(auxOps, variableName);
				return atomicProp;
			}else {
				return new Proposition();
				// throw exception
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
			return new Operator();
		}
	}
}
