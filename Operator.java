import enums.OperatorType;

public class Operator {
	
	private OperatorType operatorType;
	
	public Operator() {
		
	}
	
	public Operator (OperatorType operator) {
		this.operatorType = operator;
	}
	
	public OperatorType getOperatorType() {
		return this.operatorType;
	}
	
	public void setOperatorType(OperatorType type) {
		this.operatorType = type;
	}
	
	@Override
	public String toString() {
		if (operatorType == OperatorType.OR) {
			return "V";
		}else if (operatorType == OperatorType.AND) {
			return "&";
		}else if (operatorType == OperatorType.IF) {
			return "\u2283";
		}else if (operatorType == OperatorType.EQUALS) {
			return "=";
		}else {
			return "";
		}
		
	}
	
	public Operator copy() {
		Operator copiedOperator = new Operator(operatorType);
		System.out.println("original op: " + this.hashCode());
		System.out.println("copied op: " + copiedOperator.hashCode());
		return copiedOperator;
	}

}
