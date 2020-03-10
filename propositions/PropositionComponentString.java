package propositions;
import java.util.ArrayList;

public class PropositionComponentString {
	private String mainAuxOps, openingBracket, firstOperandAuxOps, firstOperand, operator, secondOperandAuxOps, secondOperand, closingBracket;
	
	public PropositionComponentString() {
		this.openingBracket = "(";
		this.closingBracket = ")";
		
		this.mainAuxOps = "";
		this.firstOperandAuxOps ="";
		this.firstOperand = "";
		this.operator = "";
		this.secondOperandAuxOps = "";
		this.secondOperand = "";
		
	}
	
	
	
	public void setMainAuxOps(ArrayList<AuxillaryOperator> auxOps) {
		this.mainAuxOps = buildAuxOpsStr(auxOps);
	}
	
	public void setFirstOperandAuxOps(ArrayList<AuxillaryOperator> auxOps) {
		this.firstOperandAuxOps = buildAuxOpsStr(auxOps);
	}
	
	public void setFirstOperand(Proposition first) {
		this.firstOperand = first.toString();
	}
	
	public void setSecondOperandAuxOps(ArrayList<AuxillaryOperator> auxOps) {
		this.secondOperandAuxOps = buildAuxOpsStr(auxOps);
	}
	
	public void setOperator(Operator operator) {
		this.operator = operator.toString();
	}
	
	public void setSecondOperand(Proposition second) {
		this.secondOperand = second.toString();
	}
	
	
	
	
	
	
	
	public String buildAuxOpsStr(ArrayList<AuxillaryOperator> auxOps) {
		String str = "";
		for (int i = auxOps.size() - 1; i >= 0; i -= 1) {
			AuxillaryOperator auxOp = auxOps.get(i);
			str = auxOp + str;
		}
		return str;
	}
	
	@Override
	public String toString() {
		return mainAuxOps + openingBracket + " " + firstOperandAuxOps + firstOperand + " " + operator + " " + secondOperandAuxOps + secondOperand + " " + closingBracket;
	}
	
	
	
	
	
	
}