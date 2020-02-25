package priorityLists;
import java.util.ArrayList;
import java.util.Arrays;

import enums.AuxillaryOperatorType;

public class ModalAuxOpPriority {
	
	private static ArrayList<AuxillaryOperatorType> priorityList;

	public static ArrayList<AuxillaryOperatorType> getPriorityList(){
		
		priorityList = new ArrayList<AuxillaryOperatorType>(Arrays.asList(AuxillaryOperatorType.NEGATION, 
				AuxillaryOperatorType.POSSIBLE, 
				AuxillaryOperatorType.NOTNECESSARY, 
				AuxillaryOperatorType.NOTPOSSIBLE, 
				AuxillaryOperatorType.NECESSARY 
				));
		
		return priorityList;
	}
}
