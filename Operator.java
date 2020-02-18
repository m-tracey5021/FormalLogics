import enums.OperatorType;

public class Operator {
	
	private OperatorType operator;
	
	public Operator() {
		
	}
	
	public Operator (OperatorType operator) {
		this.operator = operator;
	}
	
	public OperatorType getOperator() {
		return this.operator;
	}
	
	public void setOperator(OperatorType type) {
		this.operator = type;
	}
	
	@Override
	public String toString() {
		if (operator == OperatorType.OR) {
			return "V";
		}else if (operator == OperatorType.AND) {
			return "&";
		}else if (operator == OperatorType.IF) {
			return "\u2283";
		}else if (operator == OperatorType.EQUALS) {
			return "=";
		}else {
			return "";
		}
		
	}
	
	public Operator copy() {
		Operator copiedOperator = new Operator(operator);
		System.out.println("original op: " + this.hashCode());
		System.out.println("copied op: " + copiedOperator.hashCode());
		return copiedOperator;
	}

}
