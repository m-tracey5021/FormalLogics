import java.util.ArrayList;

public class PropositionContainer {
	private Proposition proposition;
	private ArrayList<CompoundProposition> allConstituentCompoundProps;
	private ArrayList<AtomicProposition> allConstituentAtomicProps;
	private ArrayList<Proposition> allConstituentProps;
	
	public PropositionContainer(Proposition prop) {
		this.proposition = prop;
		breakdownProp(prop);
	}
	
	public void breakdownProp(Proposition prop) {
		allConstituentProps.add(prop);
		if (prop instanceof CompoundProposition) {
			breakdownProp(((CompoundProposition)prop).getFirstOperand());
			breakdownProp(((CompoundProposition)prop).getSecondOperand());
		}else {
			return;
		}
		
		
	}
	
	/*
	public void matchIds() {
		for (Proposition currentProp : allConstituentProps) {
			for (CompoundProposition comparedProp : allConstituentProps) {
				if (currentProp.isMatch(comparedProp)) {
					comparedProp.setId(currentProp.getId());
				}
			}
		}
	}
	*/
}
