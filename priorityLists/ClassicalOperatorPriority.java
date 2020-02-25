package priorityLists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import enums.AuxillaryOperatorType;
import enums.OperatorType;

public class ClassicalOperatorPriority {

	private static HashMap<AuxillaryOperatorType, ArrayList<OperatorType>> priorityMap;
	
	
	public static HashMap<AuxillaryOperatorType, ArrayList<OperatorType>> getPriorityMap(){
		
		priorityMap = new LinkedHashMap<AuxillaryOperatorType, ArrayList<OperatorType>>();
		
		ArrayList<OperatorType> ifNegated = new ArrayList<OperatorType>(Arrays.asList(OperatorType.OR, OperatorType.IF, OperatorType.AND, OperatorType.EQUALS));
		ArrayList<OperatorType> ifNotNegated = new ArrayList<OperatorType>(Arrays.asList(OperatorType.AND, OperatorType.OR, OperatorType.IF, OperatorType.EQUALS));

		priorityMap.put(AuxillaryOperatorType.NEGATION, ifNegated);
		priorityMap.put(null, ifNotNegated);
		
		return priorityMap;
	}
}
